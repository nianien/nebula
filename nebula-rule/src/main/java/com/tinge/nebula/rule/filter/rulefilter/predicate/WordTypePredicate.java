package com.tinge.nebula.rule.filter.rulefilter.predicate;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.WordRule;
import com.tinge.nebula.rule.define.WordType;

import java.util.BitSet;
import java.util.function.Predicate;

/**
 * 词表类型过滤器, 包含指定任意词表类型, 则规则生效
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class WordTypePredicate implements Predicate<WordRule> {

    /**
     * 词表类型集合
     */
    private BitSet typeSet = new BitSet();

    /**
     * 设置词表类型
     *
     * @param criterion
     */
    public WordTypePredicate(Criterion criterion) {
        this(criterion.getWordTypes());
    }


    /**
     * 设置词表类型
     *
     * @param wordTypes 支持的词表类型
     */
    public WordTypePredicate(WordType[] wordTypes) {
        if (wordTypes != null) {
            for (WordType wordType : wordTypes) {
                typeSet.set(wordType.value);
            }
        }
    }

    @Override
    public boolean test(WordRule rule) {
        return typeSet.isEmpty() || typeSet.get(rule.getWordType().value);
    }


}
