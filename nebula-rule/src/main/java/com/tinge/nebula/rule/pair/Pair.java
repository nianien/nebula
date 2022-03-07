package com.tinge.nebula.rule.pair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

import java.util.Set;

/**
 * 配对信息
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Pair {
    /**
     * 文本ID
     */
    private long id;
    /**
     * 文本
     */
    @Include
    private String text;
    /**
     * 配对词
     */
    @Include
    private String word;
    /**
     * 配对词对应原词列表
     */
    private Set<String> origins;

}