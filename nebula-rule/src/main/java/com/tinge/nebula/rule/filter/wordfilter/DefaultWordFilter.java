package com.tinge.nebula.rule.filter.wordfilter;

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * 基于Trie树的词表过滤器<br/>
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class DefaultWordFilter implements WordFilter {

    /**
     * 子串长度
     */
    private int width;
    /**
     * 连接处判断
     */
    private Predicate<String> predicate;


    /**
     * 指定k-shingle子串长度
     *
     * @param words      用于过滤的字符集合
     * @param biFunction 构建Predicate的函数
     */
    public DefaultWordFilter(Collection<String> words,
                             BiFunction<Collection<String>, Integer, Predicate<String>> biFunction) {
        this(words, 4, biFunction);
    }


    /**
     * 指定k-shingle子串长度
     *
     * @param words      用于过滤的字符集合
     * @param width      必须介于2和4之间
     * @param biFunction 构建Predicate的函数
     */
    public DefaultWordFilter(Collection<String> words, int width,
                             BiFunction<Collection<String>, Integer, Predicate<String>> biFunction) {
        Preconditions.checkArgument(width >= 2 && width <= 4, "width of shingle must be between 2 and 4.");
        this.width = width;
        this.predicate = biFunction.apply(words, width);
    }


    @Override
    public boolean filter(String text, int start, int end, String word) {
        int lw = word.length();
        int lt = text.length();
        if (start > 0) {
            boolean b1 = text.charAt(start - 1) != '}';
            boolean b2 = b1 && start > 1 && text.charAt(start - 2) != '}';
            boolean b3 = b2 && start > 2 && text.charAt(start - 3) != '}';
            if (b1) {
                if (width >= 4) {
                    if (b3) {
                        if (predicate.test(text.substring(start - 3, start) + word.substring(0, 1))) {
                            return true; //xxx{x}
                        }
                    }
                    if (b2 && lw > 1) {
                        if (predicate.test(text.substring(start - 2, start) + word.substring(0, 2))) {
                            return true;//xx{xx}
                        }
                    }
                    if (lw > 2) {
                        if (predicate.test(text.substring(start - 1, start) + word.substring(0, 3))) {
                            return true; //x{xxx}
                        }
                    }
                }
                if (width >= 3) {
                    if (b2) {
                        if (predicate.test(text.substring(start - 2, start) + word.substring(0, 1))) {
                            return true; //xx{x}
                        }
                    }
                    if (lw > 1) {
                        if (predicate.test(text.substring(start - 1, start) + word.substring(0, 2))) {
                            return true; //x{xx}
                        }
                    }
                }
                if (width >= 2) {
                    if (predicate.test(text.substring(start - 1, start) + word.substring(0, 1))) {
                        return true; //x{x}
                    }
                }
            } else {
                if (width >= 4) {
                    if (lw > 2) {
                        if (predicate.test(word.substring(lw - 3) + word.substring(0, 1))) {
                            return true; //{xxx}{x}
                        }
                        if (predicate.test(word.substring(lw - 1) + word.substring(0, 3))) {
                            return true; //{x}{xxx}
                        }
                    }
                    if (lw > 1) {
                        if (predicate.test(word.substring(lw - 2) + word.substring(0, 2))) {
                            return true;//{xx}{xx}
                        }
                    }
                }
                if (width >= 3) {
                    if (lw > 1) {
                        if (predicate.test(word.substring(lw - 2) + word.substring(0, 1))) {
                            return true;  //{xx}{x}
                        }
                        if (predicate.test(word.substring(lw - 1) + word.substring(0, 2))) {
                            return true;  //{x}{xx}
                        }
                    }
                }
                if (width >= 2) {
                    if (predicate.test(word.substring(lw - 1) + word.substring(0, 1))) {
                        return true;  //{x}{x}
                    }
                }
            }
        }
        boolean e1 = lt > end && text.charAt(end) != '{';
        boolean e2 = e1 && lt > end + 1 && text.charAt(end + 1) != '{';
        boolean e3 = e2 && lt > end + 2 && text.charAt(end + 2) != '{';
        if (e1) {
            if (width >= 4) {
                if (lw > 2) {
                    if (predicate.test(word.substring(lw - 3) + text.substring(end, end + 1))) {
                        return true; //{xxx}x
                    }
                }
                if (e2 & lw > 1) {
                    if (predicate.test(word.substring(lw - 2) + text.substring(end, end + 2))) {
                        return true;  //{xx}xx
                    }
                }
                if (e3) {
                    if (predicate.test(word.substring(lw - 1) + text.substring(end, end + 3))) {
                        return true;   //{x}xxx
                    }
                }
            }
            if (width >= 3) {
                if (lw > 1) {
                    if (predicate.test(word.substring(lw - 2) + text.substring(end, end + 1))) {
                        return true;  //{xx}x
                    }
                }
                if (e2) {
                    if (predicate.test(word.substring(lw - 1) + text.substring(end, end + 2))) {
                        return true;  //{x}xx
                    }
                }
            }
            if (width >= 2) {
                if (predicate.test(word.substring(lw - 1) + text.substring(end, end + 1))) {
                    return true;  //{x}x
                }
            }
        }
        return false;
    }


}
