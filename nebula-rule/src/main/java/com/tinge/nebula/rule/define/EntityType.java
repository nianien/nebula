package com.tinge.nebula.rule.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实体对象类型,支持按位组合<p/>
 *
 * @author scorpio
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum EntityType implements Valuable {
    ANY(0, "任意"),
    IDEA(1, "创意"),
    WORD(2, "关键词"),
    PAGE(4, "落地页");

    public final int value;
    public final String name;


    /**
     * 当前类型是否属于指定类型
     *
     * @param source
     * @return
     */
    public boolean oneOf(int source) {
        return (this.value & source) == this.value;
    }


}
