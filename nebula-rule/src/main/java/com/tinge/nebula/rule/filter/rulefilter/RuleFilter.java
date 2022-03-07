package com.tinge.nebula.rule.filter.rulefilter;


import com.tinge.nebula.rule.HitRule;

import java.util.List;

/**
 * 规则过滤器
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public interface RuleFilter {

    /**
     * 根据criterion过滤规则,返回过滤后的规则
     *
     * @param rules 待过滤的规则,过滤操作不应改变原始参数
     * @return 返回过滤后的规则
     */
    List<HitRule> filter(List<HitRule> rules);
}
