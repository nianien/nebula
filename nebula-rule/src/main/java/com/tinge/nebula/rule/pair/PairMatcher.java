package com.tinge.nebula.rule.pair;

import com.tinge.nebula.algorithm.HitResult;
import com.tinge.nebula.algorithm.HitWord;
import com.tinge.nebula.algorithm.Phrase;
import com.tinge.nebula.algorithm.Word;
import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.HitRule;
import com.tinge.nebula.rule.HitRuleSet;
import com.tinge.nebula.rule.RuleEngine;
import com.tinge.nebula.rule.filter.rulefilter.PredicateRuleFilter;
import com.tinge.nebula.rule.filter.rulefilter.predicate.CriterionPredicate;
import com.tinge.nebula.rule.filter.wordfilter.WordFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tinge.nebula.rule.filter.rulefilter.PredicateRuleFilter.create;
import static java.util.stream.Collectors.toMap;


/**
 * 配对审核<br/>
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Slf4j
public class PairMatcher {

    /**
     * 配对匹配模式
     */
    private final static String PATTERN = "\\{(.*?)\\}";

    /**
     * 规则引擎实例
     */
    private RuleEngine ruleEngine;
    /**
     * 配对过滤器
     */
    private WordFilter wordFilter;
    /**
     * 格式化函数
     */
    private Function<String[], String[]> textFormatter;
    /**
     * 配对表 &lt;text,word,pairedText>
     */
    private final Map<Pair, String> pairTable = new HashMap<>();
    /**
     * 文本部分命中结果
     */
    private final Map<String, Map<Phrase, Map<HitWord, Set<Word>>>> partialHits = new HashMap<>();
    /**
     * 配对组合结果 &lt;text,word,hitRule>
     */
    private final Map<Pair, List<HitRule>> combinedHits = new HashMap<>();


    /**
     * 构造方法
     *
     * @param ruleEngine 词表引擎
     */
    public PairMatcher(RuleEngine ruleEngine, Function<String[], String[]> textFormatter) {
        this.ruleEngine = ruleEngine;
        if (ruleEngine != null) {
            this.wordFilter = ruleEngine.getWordFilter();
        }
        this.textFormatter = textFormatter != null ? textFormatter : Function.identity();
    }


    /**
     * 配对匹配
     *
     * @param criteria
     * @param words
     * @return
     */
    public List<HitRuleSet> matchPairs(Collection<Criterion> criteria, String[] words) {
        //标准词表到原词的映射
        List<Pair> pairs = genPairs(criteria, words);
        Set<String> pairTexts = checkPairs(pairs);
        log.info("==>filter pairs before:{}, after:{}", pairs.size(), pairTexts.size());
        //标准词表到匹配结果的映射
        Map<String, List<HitRule>> hitRuleMap = matchParallel(pairTexts, textFormatter);
        Map<Long, PredicateRuleFilter> filterMap = criteria.stream()
                .collect(toMap(Criterion::getId, e -> create(new CriterionPredicate(e)), (v1, v2) -> v1));

        Map<Long, Map<String, List<HitRule>>> hitTable = new HashMap<>();
        //配对结果
        List<HitRuleSet> hitRuleSets = new ArrayList<>();
        //生成配对结果
        for (Pair pair : pairs) {
            String pairedText = pairTable.get(pair);
            List<HitRule> hitRules = isEmpty(pairedText) ? combinedHits.get(pair) :
                    hitRuleMap.get(pairedText);
            if (hitRules != null && !hitRules.isEmpty()) {
                hitRules = filterMap.get(pair.getId()).filter(hitRules);
                if (!hitRules.isEmpty()) {
                    for (String origin : pair.getOrigins()) {
                        hitTable.computeIfAbsent(pair.getId(), k -> new HashMap<>())
                                .computeIfAbsent(origin, k -> new ArrayList<>())
                                .addAll(hitRules);
                    }
                }
            }
        }
        hitTable.forEach(
                (id, map) ->
                        map.entrySet()
                                .stream()
                                .map(e -> new HitRuleSet(id, e.getValue(), e.getKey()))
                                .forEach(hitRuleSets::add)
        );
        return hitRuleSets;
    }

    /**
     * 构建配对
     *
     * @param criteria
     * @param words
     * @return
     */
    public List<Pair> genPairs(Collection<Criterion> criteria, String[] words) {
        Map<String, Set<String>> wordsMap = formatText(words);
        List<Pair> pairs = new ArrayList<>();
        for (Criterion criterion : criteria) {
            for (String text : formatText(criterion)) {
                wordsMap.forEach((k, v) -> pairs.add(new Pair(criterion.getId(), text, k, v)));
            }
        }
        return pairs;
    }


    /**
     * 检查配对,并生成中间结果
     *
     * @param pairs
     */
    private Set<String> checkPairs(List<Pair> pairs) {
        //检查配对
        Set<String> pairTexts = pairs
                .stream()
                .map(pair -> pairTable.computeIfAbsent(pair, this::checkPair))
                .filter(this::isNotEmpty)
                .collect(Collectors.toSet());
        return pairTexts;
    }

    /**
     * 检查配对,并生成中间结果
     *
     * @param pair
     */
    private String checkPair(Pair pair) {
        AtomicInteger counter = new AtomicInteger(0);
        String text = pair.getText();
        String word = pair.getWord();
        boolean hitNone = passByFilter(text, word, counter);
        if (hitNone) {//不配对
            //合成匹配结果,判断是否能命中规则
            List<HitRule> hitResults = matchCombined(text, word, counter.get());
            if (!hitResults.isEmpty()) {
                //缓存匹配结果
                combinedHits.put(pair, hitResults);
            }
        }
        //如果配对,生成配对文本
        return hitNone ? "" : replacePair(text, word);
    }


    /**
     * 判断是否会命中连接处<br/>
     * 如果为true,表示不会命中连接处
     *
     * @param text    待匹配文本
     * @param word    代入词
     * @param counter 通配符计数器
     * @return
     */
    private boolean passByFilter(String text, String word, AtomicInteger counter) {
        Matcher matcher = Pattern.compile(PATTERN).matcher(text);
        while (matcher.find()) {
            counter.incrementAndGet();
            if (wordFilter.filter(text, matcher.start(), matcher.end(), word)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 组合匹配结果
     *
     * @param text   待匹配文本
     * @param word   代入词
     * @param occurs 代入词出现次数
     * @return
     */
    private List<HitRule> matchCombined(String text, String word, int occurs) {
        Map<Phrase, Map<HitWord, Set<Word>>> first = matchPartial(text);
        Map<Phrase, Map<HitWord, Set<Word>>> second = matchPartial(word);
        if (first == null || second == null || first.isEmpty() || second.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<HitResult> hitResults = new ArrayList<>();
        for (Entry<Phrase, Map<HitWord, Set<Word>>> entry : first.entrySet()) {
            Phrase phrase = entry.getKey();
            Map<HitWord, Set<Word>> v1 = entry.getValue();
            Map<HitWord, Set<Word>> v2 = second.get(phrase);
            //不存在规则交集
            if (v2 == null || v2.isEmpty() || v1.isEmpty()) {
                continue;
            }
            Map<HitWord, Set<Word>> map = merge(v1, v2, occurs);
            //合成匹配结果
            HitResult hitResult = phrase.match(map);
            if (hitResult != null) {
                hitResults.add(hitResult);
            }
        }
        return ruleEngine.translate(hitResults);
    }


    /**
     * 合并命中词
     *
     * @param first  第一组命中词组
     * @param second 第二组命中词组
     * @param times  第二组命中词组的个数
     * @return
     */
    private Map<HitWord, Set<Word>> merge(Map<HitWord, Set<Word>> first, Map<HitWord, Set<Word>> second, int times) {
        Map<HitWord, Set<Word>> result = new HashMap<>();
        mergeTo(first, 0, result);
        IntStream.rangeClosed(1, times).forEach(i -> mergeTo(second, i, result));
        return result;
    }


    /**
     * 合并命中词
     *
     * @param source 原命中词组
     * @param id     命中词组标记
     * @param result 合并后结果
     */
    private void mergeTo(Map<HitWord, Set<Word>> source, long id, Map<HitWord, Set<Word>> result) {
        source.forEach((k, v) -> result.put(k.toBuilder().textId(id).build(), v));
    }


    /**
     * 部分匹配词表
     *
     * @param text
     * @return
     */
    private Map<Phrase, Map<HitWord, Set<Word>>> matchPartial(String text) {
        return partialHits.computeIfAbsent(text, s -> ruleEngine.matchPartial(s));
    }

    /**
     * 批量匹配词表
     *
     * @param texts
     * @return
     */
    private Map<String, List<HitRule>> matchParallel(Collection<String> texts, Function<String[], String[]> textFormatter) {
        Map<String, List<HitRule>> hitMap = new ConcurrentHashMap<>();
        texts.parallelStream()
                .forEach(text -> {
                    List<HitRule> hitRules = ruleEngine.match(textFormatter.apply(new String[]{text}));
                    if (!hitRules.isEmpty()) {
                        hitMap.put(text, hitRules);
                    }
                });
        return hitMap;
    }


    /**
     * 包含配对符
     *
     * @param text
     * @return
     */
    private boolean containsPair(String text) {
        return text != null && text.indexOf('{') != -1 && text.indexOf('}') != -1;
    }

    /**
     * 统一配对符
     *
     * @param text
     * @return
     */
    private String formatPair(String text) {
        return text.replaceAll(PATTERN, "{}");
    }

    /**
     * 代入配对词
     *
     * @param text 待匹配文本
     * @param word 代入词
     * @return
     */
    private String replacePair(String text, String word) {
        return text.replaceAll(PATTERN, word);
    }


    /**
     * 提取格式化文本
     *
     * @param criterion
     * @return
     */
    private String[] formatText(Criterion criterion) {
        return Arrays.stream(textFormatter.apply(criterion.getTexts()))
                .map(this::formatPair)
                .filter(this::containsPair)
                .toArray(n -> new String[n]);
    }


    /**
     * 格式化配对词
     *
     * @param origins
     * @return
     */
    private Map<String, Set<String>> formatText(String[] origins) {
        Map<String, Set<String>> formatMap = new HashMap<>();
        for (String origin : origins) {
            for (String format : textFormatter.apply(new String[]{origin})) {
                if (isNotEmpty(format)) {
                    formatMap.computeIfAbsent(format, s -> new HashSet<>()).add(origin);
                }
            }
        }
        return formatMap;
    }

    private boolean isNotEmpty(String src) {
        return !isEmpty(src);
    }

    private boolean isEmpty(String src) {
        return src == null || src.length() == 0;
    }
}

