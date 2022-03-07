package com.tinge.nebula.rule.filter.rulefilter.predicate;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.WordExtra;
import com.tinge.nebula.rule.WordRule;

import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.Arrays.binarySearch;

/**
 * 行业过滤器，包含任意行业,则规则生效
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class IndustryPredicate implements Predicate<WordRule> {

    /**
     * 支持的行业列表
     */
    private int[] industries = new int[0];

    /**
     * 设置支持的行业
     *
     * @param criterion
     */
    public IndustryPredicate(Criterion criterion) {
        this(criterion.getIndustries());
    }

    /**
     * 设置支持的行业
     *
     * @param industries
     */
    public IndustryPredicate(int[] industries) {
        if (industries != null) {
            this.industries = Arrays.stream(industries)
                    .filter(e -> e > 0).toArray();
        }
    }


    @Override
    public boolean test(WordRule rule) {
        if (industries.length == 0) {
            return true;
        }
        WordExtra extra = rule.getExtra();
        if (extra == null
                || extra.getIndustries() == null
                || extra.getIndustries().length == 0) {
            return true;
        }
        for (int industry : industries) {
            if (binarySearch(extra.getIndustries(), industry) >= 0) {
                return true;
            }
        }
        return false;
    }
}
