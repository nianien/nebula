package com.tinge.nebula.algorithm;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 一个{@link Phrase}匹配项由多个{@link Term}组成, 只要匹配Phrase包含的任意一个Term, 则当前Phrase就匹配成功<br/>
 *
 * @author scorpio
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Phrase implements Serializable {
    /**
     * 规则唯一标识
     */
    @Include
    private long id;
    /**
     * 匹配词组合,每个匹配存在一个或多个同义词<br/>
     */
    private List<Term> terms;
    /**
     * 非限词
     */
    private Set<String> exemptWords;
    /**
     * 词组原始文本
     */
    private String originText;


    /**
     * 创建词表
     *
     * @param id
     * @param text
     * @param exemptWords
     * @param fuzzy
     * @return
     */
    public static Phrase create(long id, String text, String[] exemptWords, boolean fuzzy) {
        String[] words = fuzzy ? text.split("\\s+") : new String[]{text};
        return new Phrase(id, Lists.newArrayList(new Term(words, id, 0)), Sets.newHashSet(exemptWords), text);
    }

    /**
     * 扩展同义词
     *
     * @param synonymMap
     * @return
     */
    public void synonyms(Map<String, Collection<String>> synonymMap) {
        if (synonymMap.isEmpty()) {
            return;
        }
        //原始非限词
        List<String> originExemptWords = new ArrayList<>(exemptWords);
        //原始匹配项
        Term originTerm = this.terms.get(0);
        synonymMap.forEach((key, values) -> {
            //变体词表包含" ",则需要原词项整体替换. 以原词“a b”为例:
            // 1)同义词表: "a->a1","b->b1"，则可替换为“a1 b”,"ab1","a1 b1"
            // 2)同义词表: "a b->a1 b1"，则只能替换为“a1 b1"
            if (key.contains(" ")) {
                if (originText.contains(key)) {
                    for (String value : values) {
                        terms.add(new Term(originText.replace(key, value).split(" "), id, terms.size()));
                    }
                }
            } else { //扩展词项同义词
                for (Word synonym : originTerm.getWords()) {
                    String word = synonym.getWord();
                    //扩展原词
                    if (word.contains(key)) {
                        for (String value : values) {
                            synonym.addSynonyms(word.replace(key, value));
                        }
                    }
                }
                //扩展非限同义词
                for (String exemptWord : originExemptWords) {
                    if (exemptWord.contains(key)) {
                        for (String value : values) {
                            exemptWords.add(exemptWord.replace(key, value));
                        }
                    }
                }
            }
        });
    }


    /**
     * 根据命中结果判断是否匹配当前{@link Phrase}<br/>
     *
     * @param hitWords 排除非限词后的命中词列表
     * @return
     */
    public boolean match(Set<Word> hitWords) {
        if (hitWords.isEmpty()) {
            return false;
        }
        //词表规则中的命中词,每个候选词选一个
        for (Term term : this.getTerms()) {
            if (term.match(hitWords)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据命中词判断是否匹配{@link Phrase}, 并返回匹配结果<br/>
     * 如果匹配{@link Phrase}中的任意一个{@link Term}，即认为匹配成功<br/>
     * <p>
     * 注: 原始命中结果需要通过{@link #hit(Map)}进行非限操作
     *
     * @param hitWords 排除非限词后的命中词列表
     * @return
     * @see #hit(Map)
     */
    public HitResult match(Map<HitWord, Set<Word>> hitWords) {
        if (hitWords.isEmpty()) {
            return null;
        }
        //按Term分组
        Map<Integer, Map<Word, Set<HitWord>>> termMap = groupByTerm(hitWords);
        //词表规则中的命中词,每个候选词选一个
        for (Term term : this.getTerms()) {
            Map<Word, Set<HitWord>> wordMap = termMap.get(term.getTermIndex());
            if (wordMap != null) {
                List<String> hits = term.match(wordMap);
                if (hits != null) {
                    return new HitResult(this, hits);
                }
            }
        }
        return null;
    }


    /**
     * 根据命中结果获取非限后的命中词表
     *
     * @param hitWords 命中词结果
     * @return
     */
    public Map<HitWord, Set<Word>> hit(Map<HitWord, Set<Word>> hitWords) {
        Map<String, Set<HitWord>> hitsMap = hitWords.keySet().stream().collect(groupingBy(HitWord::getWord, Collectors.toSet()));
        //命中非限词列表
        Set<HitWord> hitExemptWords = getHitWords(this.exemptWords, hitsMap);
        //命中词列表
        Iterator<Entry<HitWord, Set<Word>>> iterator = hitWords.entrySet().iterator();
        while (iterator.hasNext()) {
            HitWord hit = iterator.next().getKey();
            //移除非限词
            if (hitExemptWords.contains(hit)) {
                iterator.remove();
                continue;
            }
            //剔除非限词包含的命中词
            for (HitWord exclude : hitExemptWords) {
                if (hit.getBegin() >= exclude.getBegin() && hit.getEnd() <= exclude.getEnd()) {
                    iterator.remove();
                    break;
                }
            }
        }
        return hitWords;
    }


    /**
     * 根据词面获取命中词集合
     *
     * @param words
     * @param hitsMap
     * @return
     */
    private Set<HitWord> getHitWords(Collection<String> words, Map<String, Set<HitWord>> hitsMap) {
        Set<HitWord> hits = new LinkedHashSet<>();
        for (String word : words) {
            Set<HitWord> value = hitsMap.get(word);
            if (value != null) {
                hits.addAll(value);
            }
        }
        return hits;
    }


    /**
     * 按Term分组
     *
     * @param hitWords
     * @return
     */
    private Map<Integer, Map<Word, Set<HitWord>>> groupByTerm(Map<HitWord, Set<Word>> hitWords) {
        //按Term分组
        Map<Integer, Map<Word, Set<HitWord>>> termMap = new HashMap<>();
        hitWords.forEach(
                (hitWord, words) -> words.forEach(
                        word -> termMap
                                .computeIfAbsent(word.getTermIndex(), k -> new HashMap<>())
                                .computeIfAbsent(word, k -> new HashSet<>()).add(hitWord)
                )
        );
        return termMap;
    }

}