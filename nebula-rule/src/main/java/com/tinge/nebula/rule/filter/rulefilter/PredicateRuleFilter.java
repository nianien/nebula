package com.tinge.nebula.rule.filter.rulefilter;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.HitRule;
import com.tinge.nebula.rule.WordRule;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 基于{@link Criterion}的规则过滤器
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class PredicateRuleFilter implements RuleFilter {

    /**
     * 默认断言
     */
    private Predicate<HitRule> rulePredicate;


    /**
     * 使用{@link Predicate<HitRule>}进行规则过滤
     *
     * @param predicate
     */
    public PredicateRuleFilter(Predicate<HitRule> predicate) {
        this.rulePredicate = predicate;
    }

    /**
     * 使用{@link Predicate<HitRule>}进行规则过滤
     *
     * @param predicate
     */
    public static PredicateRuleFilter create(Predicate<WordRule> predicate) {
        return new PredicateRuleFilter(hitRule -> predicate.test(hitRule.getRule()));
    }


    /**
     * 根据criterion过滤规则,返回过滤后的规则
     *
     * @param rules 待过滤的规则,过滤操作保持规则列表不变
     * @return
     */
    @Override
    public List<HitRule> filter(List<HitRule> rules) {
        if (rulePredicate == null || rules == null) {
            return rules;
        }
        return rules.stream().filter(rulePredicate).collect(Collectors.toList());
    }
}

