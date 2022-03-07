package com.tinge.nebula.rule.filter.rulefilter.predicate;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.WordExtra;
import com.tinge.nebula.rule.WordRule;
import com.tinge.nebula.rule.define.SourceType;

import java.util.function.Predicate;

/**
 * 词表来源过滤器，包含任意指定来源，则规则生效
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class SourceTypePredicate implements Predicate<WordRule> {

    /**
     * 词表来源按位组合
     */
    private int sourceTypes;

    /**
     * 设置词表来源
     *
     * @param criterion
     */
    public SourceTypePredicate(Criterion criterion) {
        this(criterion.getSourceTypes());
    }

    /**
     * 设置词表来源
     *
     * @param sourceTypes 支持的词表来源
     */
    public SourceTypePredicate(SourceType[] sourceTypes) {
        if (sourceTypes != null) {
            for (SourceType sourceType : sourceTypes) {
                this.sourceTypes |= sourceType.value;
            }
        }
    }

    @Override
    public boolean test(WordRule rule) {
        if (sourceTypes == 0) {
            return true;
        }
        WordExtra extra = rule.getExtra();
        int ruleSourceTypes = extra.getSourceTypes();
        return ruleSourceTypes == 0 || (this.sourceTypes & ruleSourceTypes) > 0;
    }


}
