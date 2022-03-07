package com.tinge.nebula.rule.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 类型转换函数
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class TypeConverter {

    /**
     * 根据换行符转换成数组
     *
     * @param string
     * @return
     */
    public static String[] toLines(String string) {
        return Arrays.stream(Optional.ofNullable(string).orElse("").split("\n"))
                .map(String::trim)
                .filter(e -> !e.isEmpty())
                .toArray(String[]::new);
    }

    /**
     * 字符串转long型数组,忽略<=0的数
     *
     * @param string
     * @return
     */
    public static long[] toLongs(String string) {
        return toLongs(Arrays.stream(Optional.ofNullable(string).orElse("").split(","))
                .filter(e -> !e.trim().isEmpty())
                .map(Long::valueOf)
                .filter(e -> e > 0)
                .collect(Collectors.toSet()));
    }

    /**
     * 字符串转int型数组,忽略<=0的数
     *
     * @param string
     * @return
     */
    public static int[] toInts(String string) {
        return toInts(Arrays.stream(Optional.ofNullable(string).orElse("").split(","))
                .filter(e -> !e.trim().isEmpty())
                .map(Integer::valueOf)
                .filter(e -> e > 0)
                .collect(Collectors.toSet()));
    }

    /**
     * 转换为int数组
     *
     * @param list
     */
    public static int[] toInts(Collection<Integer> list) {
        int[] ints = new int[list.size()];
        int i = 0;
        for (int e : list) {
            ints[i++] = e;
        }
        return ints;
    }

    /**
     * 转换为long数组
     *
     * @param list
     */
    public static long[] toLongs(Collection<Long> list) {
        long[] longs = new long[list.size()];
        int i = 0;
        for (long e : list) {
            longs[i++] = e;
        }
        return longs;
    }


}
