package com.tinge.nebula.rule;

import java.util.function.Supplier;

/**
 * 规则加载类
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public interface RuleLoader extends Supplier<RuleEngine> {

    /**
     * 加载规则
     *
     * @return
     */
    RuleEngine load();

    /**
     * 加载规则
     *
     * @return
     */
    @Override
    default RuleEngine get() {
        return load();
    }
}
