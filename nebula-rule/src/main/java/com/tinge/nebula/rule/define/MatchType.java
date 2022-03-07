package com.tinge.nebula.rule.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 匹配类型<p/>
 * 0:分散,1:包含,2:精确
 *
 * @author scorpio
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum MatchType implements Valuable {
    DISPERSED(0, "分散"), CONTAINED(1, "包含"), EXACT(2, "精确");

    public final int value;
    public final String name;


    /**
     * 获取枚举类型
     *
     * @param value
     * @return
     */
    public static MatchType of(int value) {
        return Valuable.of(MatchType.class, value, DISPERSED);
    }
}
