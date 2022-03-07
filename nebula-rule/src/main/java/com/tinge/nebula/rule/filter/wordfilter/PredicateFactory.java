package com.tinge.nebula.rule.filter.wordfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.tinge.nebula.algorithm.trie.AhoCorasickDoubleArrayTrie;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Predicate工厂类
 */
public class PredicateFactory {

    /**
     * 构建BloomPredicate
     *
     * @param words
     * @param width
     * @return
     */
    public static Predicate<String> bloomPredicate(Collection<String> words, int width) {
        BloomFilter<String>[] filters = IntStream
                .range(0, width - 1)
                .mapToObj(i -> BloomFilter.create(Funnels.unencodedCharsFunnel(), 1 << 27))
                .toArray(n -> new BloomFilter[n]);
        words.stream().forEach(word -> accept(word, width, (s) -> filters[s.length() - 2].put(s)));
        return new BloomPredicate(filters);
    }


    /**
     * 构建TriePredicate
     *
     * @param words
     * @param width 子串最大长度
     * @return
     */
    public static Predicate<String> triePredicate(Collection<String> words, int width) {
        Map<String, Boolean>[] trieMaps = IntStream
                .range(0, width - 1)
                .mapToObj(i -> new HashMap())
                .toArray(n -> new HashMap[n]);
        words.stream().forEach(word -> accept(word, width, s -> trieMaps[s.length() - 2].put(s, true)));
        //构建datTrie树
        AhoCorasickDoubleArrayTrie<Boolean>[] tries = Arrays.stream(trieMaps)
                .map(trieMap -> {
                    AhoCorasickDoubleArrayTrie<Boolean> trie = new AhoCorasickDoubleArrayTrie<>();
                    trie.build(trieMap);
                    return trie;
                })
                .toArray(n -> new AhoCorasickDoubleArrayTrie[n]);
        return new TriePredicate(tries);
    }

    /**
     * 接受length≥2且length≤width的字符串,如果长度超过width,则按照k-shingle拆分子串
     *
     * @param text
     * @param width
     * @param consumer
     */
    protected static void accept(String text, int width, Consumer<String> consumer) {
        //字符长度
        int wl = text.length();
        if (wl < 2) {
            return;
        }
        if (wl <= width) {
            consumer.accept(text);
        } else {
            for (int i = 0; i <= wl - width; i++) {
                consumer.accept(text.substring(i, i + width));
            }
        }
    }


    /**
     * 基于Trie树的Predicate&lt;String&gt;实现
     */
    public static class TriePredicate implements Predicate<String>, Serializable {

        private AhoCorasickDoubleArrayTrie<Boolean>[] tries;

        public TriePredicate(AhoCorasickDoubleArrayTrie<Boolean>[] tries) {
            this.tries = tries;
        }

        @Override
        public boolean test(String word) {
            return word.length() >= 2 && word.length() <= tries.length + 1 ? tries[word.length() - 2].matches(word) : false;
        }
    }


    /**
     * 基于BloomFilter的Predicate&lt;String&gt;实现
     */
    public static class BloomPredicate implements Predicate<String>, Serializable {

        private BloomFilter<String>[] filters;

        public BloomPredicate(BloomFilter<String>[] filters) {
            this.filters = filters;
        }

        @Override
        public boolean test(String word) {
            return word.length() >= 2 && word.length() <= filters.length + 1 ? filters[word.length() - 2].mightContain(word) : false;
        }
    }

}
