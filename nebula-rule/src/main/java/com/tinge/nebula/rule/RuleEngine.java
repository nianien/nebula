package com.tinge.nebula.rule;

import com.tinge.nebula.algorithm.*;
import com.tinge.nebula.rule.define.MatchType;
import com.tinge.nebula.rule.filter.wordfilter.DefaultWordFilter;
import com.tinge.nebula.rule.filter.wordfilter.PredicateFactory;
import com.tinge.nebula.rule.filter.wordfilter.WordFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

/**
 * 词表规则引擎
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Slf4j
@AllArgsConstructor
public class RuleEngine implements Serializable {

    /**
     * 全部规则表
     */
    private Map<Long, WordRule> uuid2Rule;
    /**
     * 用于分散和包含匹配
     */
    @Getter
    private WordMatcher wordMatcher;
    /**
     * 字符串过滤器
     */
    @Getter
    private WordFilter wordFilter;


    /**
     * 创建规则引擎
     *
     * @param rules      规则列表
     * @param synonymMap 变体词列表
     */
    public static RuleEngine create(List<WordRule> rules, Map<String, Collection<String>> synonymMap) {
        Map<Long, WordRule> uuid2Rule = new HashMap<>(rules.size());
        WordMatcher wordMatcher = new WordMatcher(rules.size());
        Set<String> wordSet = new HashSet<>();
        for (WordRule rule : rules) {
            //规则映射
            uuid2Rule.put(rule.getUuid(), rule);
            //构建匹配对象
            wordMatcher.addItem(
                    rule.getUuid(),
                    rule.getWord(),
                    rule.getExtra().getExcludeWords(),
                    rule.getMatchType() == MatchType.DISPERSED);
        }
        //初审过滤器
        wordMatcher.build(synonymMap, wordSet::add);
        return new RuleEngine(uuid2Rule, wordMatcher, new DefaultWordFilter(wordSet, PredicateFactory::triePredicate));
    }


    /**
     * 匹配词表规则,返回命中的规则<br/>
     * 该方法不会进行规则过滤
     *
     * @param texts 待匹配文本
     * @return
     */
    public List<HitRule> match(String[] texts) {
        if (texts == null || texts.length == 0) {
            return Collections.emptyList();
        }
        List<HitRule> rules = new LinkedList<>();
        for (String text : texts) {
            //包含匹配
            List<HitResult> hitResults = wordMatcher.match(text);
            for (HitResult hitResult : hitResults) {
                WordRule wordRule = uuid2Rule.get(hitResult.getPhrase().getId());
                if (wordRule.getMatchType() != MatchType.EXACT ||
                        //精确匹配时,必须内容一致
                        hitResult.getHits().size() == 1 && hitResult.getHits().get(0).equals(text)
                ) {
                    rules.add(new HitRule(wordRule, hitResult.getHits()));
                }
            }
        }
        return rules;
    }


    /**
     * 获取全部规则，开放外部服务使用
     *
     * @return
     */
    public Collection<WordRule> getAllRules() {
        return new ArrayList<>(uuid2Rule.values());
    }


    /**
     * 将命中结果转换成命中规则
     *
     * @param hitResults
     * @return
     */
    public List<HitRule> translate(List<HitResult> hitResults) {
        List<HitRule> rules = new LinkedList<>();
        for (HitResult hitResult : hitResults) {
            WordRule wordRule = uuid2Rule.get(hitResult.getPhrase().getId());
            rules.add(new HitRule(wordRule, hitResult.getHits()));
        }
        return rules;
    }


    /**
     * 部分匹配词表
     *
     * @param text
     * @return
     */
    public Map<Phrase, Map<HitWord, Set<Word>>> matchPartial(String text) {
        return wordMatcher.matchPartial(text);
    }


}
