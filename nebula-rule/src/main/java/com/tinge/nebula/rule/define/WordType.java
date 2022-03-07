package com.tinge.nebula.rule.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 词表类型<p/>
 * 1:黑名单，2：商标词，3：竞品词，4：敏感词
 *
 * @author scorpio
 * @version 1.0.0
 */
@AllArgsConstructor
@Getter
public enum WordType implements Valuable {
    BLACK(1, "黑词"),
    BRAND(2, "商标词"),
    COMPETE(3, "竞品词"),
    SENSITIVE(4, "敏感词");

    public final int value;
    public final String name;


    /**
     * 获取枚举类型
     *
     * @param type
     * @return
     */
    public static WordType of(int type) {
        for (WordType wordType : values()) {
            if (wordType.value == type) {
                return wordType;
            }
        }
        return null;
    }

    public boolean is(int type) {
        return of(type) == this;
    }
}
