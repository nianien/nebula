package com.tinge.nebula.rule;

import com.tinge.nebula.rule.define.EntityType;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.ToString.Exclude;

import java.io.Serializable;

import static java.util.Arrays.sort;

/**
 * 词表附加信息<br/>
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Getter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class WordExtra implements Serializable {

    /**
     * 非限词
     */
    @Default
    @Exclude
    private String[] excludeWords = new String[0];
    /**
     * 支持的实体类型
     *
     * @see EntityType
     */
    private int entityTypes;
    /**
     * 支持的规则来源
     */
    private int sourceTypes;
    /**
     * 支持的行业列表
     */
    @Default
    @Exclude
    private int[] industries = new int[0];
    /**
     * 非限用户
     */
    @Default
    @Exclude
    private long[] excludeUsers = new long[0];
    /**
     * 非限客户
     */
    @Default
    @Exclude
    private long[] excludeCustomers = new long[0];
    /**
     * 风险标签,客户端使用
     */
    private String riskTag;

    /**
     * 词表等级
     */
    private int grade;

    /**
     * 构造方法
     *
     * @param excludeWords
     * @param entityTypes
     * @param sourceTypes
     * @param industries
     * @param excludeUsers
     * @param excludeCustomers
     * @param riskTag          风险标签
     */
    public WordExtra(String[] excludeWords, int entityTypes, int sourceTypes,
                     int[] industries, long[] excludeUsers, long[] excludeCustomers, String riskTag, int grade) {
        this.excludeWords = excludeWords;
        this.entityTypes = entityTypes;
        this.sourceTypes = sourceTypes;
        this.industries = industries;
        this.excludeUsers = excludeUsers;
        this.excludeCustomers = excludeCustomers;
        this.riskTag = riskTag;
        this.grade = grade;
        //数组排序,保证二分查找正确性
        sort(excludeUsers);
        sort(excludeCustomers);
        sort(industries);
    }

}
