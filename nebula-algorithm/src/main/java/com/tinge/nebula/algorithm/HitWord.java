package com.tinge.nebula.algorithm;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import java.io.Serializable;

/**
 * 命中词
 *
 * @author scorpio
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class HitWord implements Serializable, Cloneable {
    /**
     * 原文开始位置
     */
    @Include
    @NonNull
    private final int begin;
    /**
     * 原文结束位置
     */
    @Include
    @NonNull
    private final int end;
    /**
     * 匹配内容
     */
    @NonNull
    private final String word;

    /**
     * 文本识别号,用于多文本组合匹配
     */
    @Include
    private long textId;


    @Override
    public String toString() {
        return "HitWord{" +
                "begin=" + begin +
                ", end=" + end +
                ", word='" + word + '\'' +
                '}';
    }


}
