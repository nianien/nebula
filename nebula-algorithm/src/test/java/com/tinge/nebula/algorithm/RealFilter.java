package com.tinge.nebula.algorithm;

import com.tinge.nebula.algorithm.trie.AhoCorasickDoubleArrayTrie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 准确的字符过滤器
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
public class RealFilter implements Function<String, Boolean> {
    AhoCorasickDoubleArrayTrie<String> trie = new AhoCorasickDoubleArrayTrie<String>();


    public RealFilter(Collection<String> words) {
        Map<String, String> dualMap = new HashMap<>();
        for (String word : words) {
            put(word, dualMap);
        }
        trie.build(dualMap);
    }


    public boolean checkNeighbor(char c1, char c2) {
        return trie.matches(c1 + "" + c2);
    }


    public void put(String word, Map<String, String> dualMap) {
        int length = word.length();
        //空词不能作为过滤词
        if (length <= 1) {
            return;
        }
        //首字符与尾字符不同，其他相同
        for (int i = 0; i < length - 1; i++) {
            String key = word.substring(i, i + 2);
            dualMap.put(key, key);
        }
    }

    @Override
    public Boolean apply(String s) {
        return checkNeighbor(s.charAt(0), s.charAt(1));
    }
}
