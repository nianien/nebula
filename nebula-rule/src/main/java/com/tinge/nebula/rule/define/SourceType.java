package com.tinge.nebula.rule.define;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则来源,按照产品线划分,支持按位组合<br/>
 * NOTE: 由于采用PB序列化,枚举类型不可删除
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@AllArgsConstructor
@Getter
public enum SourceType implements Valuable {

    ANY(0, "任意"),
    SEARCH(1, "搜索"),
    FEED(2, "信息流"),
    APP(4, "APP"),
    VIDEO(8, "视频");

    public final int value;
    public final String desc;

}
