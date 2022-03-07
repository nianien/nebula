package com.tinge.nebula.rule.define;

import java.util.Arrays;

/**
 * 具有对应int值的接口定义, 用于区分枚举定义实例
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public interface Valuable {

    /**
     * 获取枚举实例对应的数值
     *
     * @return
     */
    int getValue();


    /**
     * 获取枚举类型
     *
     * @param value
     * @return
     */
    static <T extends Enum<T> & Valuable> T of(Class<T> type, int value, T defaultEnum) {
        return Arrays.stream(type.getEnumConstants())
                .filter(e -> e.getValue() == value)
                .findFirst().orElse(defaultEnum);

    }

}
