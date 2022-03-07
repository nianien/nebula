package com.tinge.nebula.rule.filter.rulefilter.predicate;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.WordExtra;
import com.tinge.nebula.rule.WordRule;
import lombok.AllArgsConstructor;

import java.util.function.Predicate;

import static java.util.Arrays.binarySearch;

/**
 * 指定客户不在规则的非限客户范围内, 则命中规则
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@AllArgsConstructor
public class ExcludeCustomerPredicate implements Predicate<WordRule> {

    /**
     * 客户ID
     */
    private long cid;

    public ExcludeCustomerPredicate(Criterion criterion) {
        this.cid = criterion.getCid();
    }

    /**
     * 未指定客户Id或者客户Id不是非限客户
     *
     * @param rule
     * @return
     */
    @Override
    public boolean test(WordRule rule) {
        if (cid <= 0) {
            return true;
        }
        WordExtra extra = rule.getExtra();
        if (extra.getExcludeCustomers() == null) {
            return true;
        }
        return binarySearch(extra.getExcludeCustomers(), cid) < 0;
    }
}
