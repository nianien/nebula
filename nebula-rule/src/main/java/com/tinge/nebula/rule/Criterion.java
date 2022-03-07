package com.tinge.nebula.rule;


import com.tinge.nebula.rule.define.EntityType;
import com.tinge.nebula.rule.define.SourceType;
import com.tinge.nebula.rule.define.WordType;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 查询范式,用于规则过滤
 *
 * @author scorpio
 * @version 1.0.0
 */
@Data
@Builder(toBuilder = true)
@ToString
@NoArgsConstructor/*for json*/
@AllArgsConstructor/*for builder*/
public class Criterion implements Serializable {

    /**
     * 对象ID
     */
    private long id;
    /**
     * 实体类型
     */
    private EntityType entityType;
    /**
     * 规则来源,默认全部来源
     */
    @Default
    private SourceType[] sourceTypes = new SourceType[0];
    /**
     * 用户ID
     */
    private long userId;
    /**
     * 客户ID
     */
    private long cid;

    @Default
    private int[] industries = new int[0];
    /**
     * 待匹配文本
     */
    @Default
    private String[] texts = new String[0];

    /**
     * 词表类型,默认匹配全部词表
     */
    @Default
    private WordType[] wordTypes = new WordType[0];


}
