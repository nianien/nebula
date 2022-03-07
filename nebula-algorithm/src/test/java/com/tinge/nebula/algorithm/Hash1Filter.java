/**
 * com.sm.audit.auto.words.RoughFilter.java
 * created by Tianxin(tianjige@163.com) on 2016年9月30日 上午10:49:19
 */
package com.tinge.nebula.algorithm;


import com.google.common.hash.Hashing;

import java.util.BitSet;
import java.util.Collection;
import java.util.function.Function;


/**
 * 基于BitSet的过滤器
 */
public class Hash1Filter implements Function<String, Boolean> {

    /**
     * java中一个char的最大int值
     */
    private int maxCharValue = 65535 << 15;

    /**
     * 词表第N个字符和第N-1个字符组合的掩码，所有词存储在一个BitSet上
     */
    private BitSet neighborCharSet;

    /**
     * 构造函数
     * roughWords就是用来做粗选的词。
     * 词与一个字符串匹配的方法就是在字符串中全包含，没有宽泛或精确等匹配方式。
     */
    public Hash1Filter(Collection<String> roughWords) {

        initCharBitSets();

        // 将词表信息加载到CharSet中
        loadWords2CharSetArrays(roughWords);
    }

    /**
     * 将词的内容加载到字符的bitset中
     */
    private void loadWords2CharSetArrays(Collection<String> words) {
        for (String word : words) {
            loadWord(word);
        }
    }

    /**
     * 将某一个词加载到字符的bitset中
     */
    private void loadWord(String word) {
        int length = word.length();

        //空词不能作为过滤词
        if (length == 0) {
            return;
        }

        //首字符与尾字符不同，其他相同
        for (int i = 0; i < length - 1; i++) {
            char c = word.charAt(i);
            char neighbor = word.charAt(i + 1);
            int neighborSign = sign(c, neighbor);
            neighborCharSet.set(neighborSign);
        }
    }

    /**
     * 做两个字符的签名
     *
     * @param first
     * @param second
     */
    private int sign(char first, char second) {
        boolean useHash = false;
        if (useHash) {
            long hash64 = Hashing.murmur3_128().hashUnencodedChars(new String(new char[]{first, second})).asLong();
            int hash1 = (int) hash64;
            int hash2 = (int) (hash64 >>> 32);
            int combinedHash = hash1 + hash2;
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            return combinedHash;
        }
//        return (int) ((((first << 16) + second) * 2654435769L) >> 28) & Integer.MAX_VALUE;
        return ((first << 15) + second) & Integer.MAX_VALUE;
    }

    /**
     * 将几个字符的数组做初始化
     */
    private void initCharBitSets() {
        neighborCharSet = new BitSet(maxCharValue);
    }

    /**
     * 检查字符组合是否在相邻bitmap中
     *
     * @param first
     * @param second
     * @return
     */
    public boolean checkNeighbor(char first, char second) {
        int sign = sign(first, second);
        return neighborCharSet.get(sign);
    }


    @Override
    public Boolean apply(String s) {
        return checkNeighbor(s.charAt(0), s.charAt(1));
    }
}
