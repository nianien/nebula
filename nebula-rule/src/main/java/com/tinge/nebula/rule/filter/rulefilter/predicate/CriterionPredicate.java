package com.tinge.nebula.rule.filter.rulefilter.predicate;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.HitRule;
import com.tinge.nebula.rule.WordRule;

import java.util.function.Predicate;

/**
 * 根据{@link Criterion}过滤{@link HitRule}
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class CriterionPredicate implements Predicate<WordRule> {

    private Predicate<WordRule> predicate;

    /**
     * 范式过滤器
     *
     * @param criterion
     */
    public CriterionPredicate(Criterion criterion) {
        predicate = new SourceTypePredicate(criterion)
                .and(new EntityTypePredicate(criterion))
                .and(new IndustryPredicate(criterion))
                .and(new ExcludeUserPredicate(criterion))
                .and(new ExcludeCustomerPredicate(criterion))
                .and(new WordTypePredicate(criterion))
        ;
    }


    /**
     * 是否命中该规则<br/>返回结果为true表示命中,否则未命中
     *
     * @param rule
     * @return
     */
    @Override
    public boolean test(WordRule rule) {
        return predicate.test(rule);
    }

}
