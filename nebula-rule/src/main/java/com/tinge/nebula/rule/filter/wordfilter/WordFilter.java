package com.tinge.nebula.rule.filter.wordfilter;

import java.io.Serializable;

/**
 * 词表过滤器<br/>
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public interface WordFilter extends Serializable {

    /**
     * 将text的start(含)与end(含)之间的内容替换为word, 判断替换后的字符串是否匹配过滤条件
     *
     * @param text  待匹配文本
     * @param start 文本的起始位置
     * @param end   文本的结束位置
     * @param word  代入词
     * @return
     */
    boolean filter(String text, int start, int end, String word);

}
