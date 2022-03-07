package com.tinge.nebula.algorithm;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 命中结果,包含词表项和命中词
 *
 * @author scorpio
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
@ToString
public class HitResult implements Serializable {
    /**
     * 命中词表
     */
    private Phrase phrase;
    /**
     * 命中词列表
     */
    private List<String> hits;

}
