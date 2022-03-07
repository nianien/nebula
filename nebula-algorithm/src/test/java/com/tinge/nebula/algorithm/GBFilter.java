package com.tinge.nebula.algorithm;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.Collection;
import java.util.function.Function;

/**
 * 基于googleBloomFilter
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class GBFilter implements Function<String, Boolean> {
    private static BloomFilter<String> bloomFilter = BloomFilter
            .create(Funnels.unencodedCharsFunnel(), 647702);


    public GBFilter(Collection<String> words) {
        for (String word : words) {
            put(word);
        }
    }

    public boolean checkNeighbor(char c1, char c2) {
        return bloomFilter.mightContain(new String(new char[]{c1, c2}));
    }


    public void put(String word) {
        int length = word.length();
        //空词不能作为过滤词
        if (length <= 1) {
            return;
        }
        //首字符与尾字符不同，其他相同
        for (int i = 0; i < length - 1; i++) {
            bloomFilter.put(word.substring(i, i + 2));
        }
    }

    @Override
    public Boolean apply(String s) {
        return checkNeighbor(s.charAt(0), s.charAt(1));
    }
}
