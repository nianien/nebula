package com.tinge.nebula.rule.filter.rulefilter.predicate;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.WordExtra;
import com.tinge.nebula.rule.WordRule;

import java.util.function.Predicate;

import lombok.AllArgsConstructor;

import static java.util.Arrays.binarySearch;

/**
 * 指定用户不在规则的非限客户范围内, 则命中规则
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@AllArgsConstructor
public class ExcludeUserPredicate implements Predicate<WordRule> {

    /**
     * 用户ID
     */
    private long userId;

    public ExcludeUserPredicate(Criterion criterion) {
        this.userId = criterion.getUserId();
    }

    /**
     * 未指定用户Id或者用户Id不属于非限用户
     *
     * @param rule
     * @return
     */
    @Override
    public boolean test(WordRule rule) {
        if (userId <= 0) {
            return true;
        }
        WordExtra extra = rule.getExtra();
        if (extra.getExcludeUsers() == null) {
            return true;
        }
        return binarySearch(extra.getExcludeUsers(), userId) < 0;
    }
}
