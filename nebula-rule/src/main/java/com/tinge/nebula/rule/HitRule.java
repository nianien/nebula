package com.tinge.nebula.rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

/**
 * 命中词表规则,包含命中词面<br/>
 * 注: 当对命中规则去重时, 仅计算{@link WordRule}对象
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HitRule implements Serializable {

    /**
     * 命中规则
     */
    @Include
    private WordRule rule;

    /**
     * 命中词面
     */
    private List<String> hits;


    /**
     * 防止pb序列化为null
     */
    public HitRule() {
        this.hits = new ArrayList<>();
    }


    /**
     * 命中词表规则,包含命中词面
     *
     * @param rule
     * @param hits
     */
    public HitRule(WordRule rule, String... hits) {
        this(rule, Arrays.asList(hits));
    }

}
