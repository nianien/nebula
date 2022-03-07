package com.tinge.test;


import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.io.Resources;
import com.google.common.primitives.Longs;
import com.tinge.nebula.algorithm.GBFilter;
import com.tinge.nebula.algorithm.Hash1Filter;
import com.tinge.nebula.algorithm.Hash2Filter;
import com.tinge.nebula.algorithm.RealFilter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author scorpio
 * @version 1.0.0
 */
public class TestFilter {

    /**
     * 测试性能和准确率
     */
    @Test
    @SneakyThrows
    public void testMemory() {
        Collection<String> wordlist = loadLines("wordlist.txt");
        int type = 3;
        switch (type) {
            case 1:
                new Hash1Filter(wordlist);
                break;
            case 2:
                new Hash2Filter(wordlist);
                break;
            case 3:
                new RealFilter(wordlist);
                break;
            case 4:
                new GBFilter(wordlist);
                break;
        }
        System.gc();
        Thread.sleep(1000);
        System.out.println("Memory used: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024.0 / 1024 + "MB");
    }

    /**
     * 测试性能和准确率
     */
    @Test
    public void testFilter() {
        Collection<String> words = new HashSet<>();
        {
            List<String> similars = loadLines("similar.txt");
            List<String> wordlist = loadLines("wordlist.txt");
            for (String word : wordlist) {
                String[] splits = word.toLowerCase().split("\\s+");
                for (String split : splits) {
                    words.add(split.trim());
                }
            }
            for (String similar : similars) {
                String[] arr = similar.split("\t");
                if (arr.length < 1) {
                    continue;
                }
                String origin = arr[0].trim();
                String[] sims = arr[1].trim().split(";;");
                for (String word : wordlist) {
                    if (word.indexOf(origin) != -1) {
                        for (String sim : sims) {
                            String replace = word.replace(origin, sim.trim());
                            if (!replace.equals(word)) {
                                String[] splits = replace.toLowerCase().split("\\s+");
                                for (String split : splits) {
                                    words.add(split.trim());
                                }
                            }
                        }
                    }
                }
            }
        }
        Hash1Filter hash1Filter = new Hash1Filter(words);
        Hash2Filter hash2Filter = new Hash2Filter(words);
        RealFilter realFilter = new RealFilter(words);
        GBFilter gbFilter = new GBFilter(words);
        //可能是规则词的一部分,但不是完整的规则词
        List<String> tests = loadLines("wordlist_test.txt");
        //命中数
        AtomicInteger num_hash1 = new AtomicInteger(0);
        AtomicInteger num_hash2 = new AtomicInteger(0);
        AtomicInteger num_real = new AtomicInteger(0);
        AtomicInteger num_gb = new AtomicInteger(0);
        long time_real = testFilter(tests, realFilter, num_real);
        long time_gb = testFilter(tests, gbFilter, num_gb);
        long time_hash1 = testFilter(tests, hash1Filter, num_hash1);
        long time_hash2 = testFilter(tests, hash2Filter, num_hash2);
        int total = num_real.get();
        System.out.println("RealFilter, 总数:" + total + ", " + ", time cost:" + time_real);
        System.out.println("GBFilter, 错误数: " + (total - num_gb.get()) + ",准确率:" + (num_gb.get() * 1.0
                / total) + ", time cost:" + time_gb);
        System.out.println("Hash1Filter, 错误数: " + (total - num_hash1.get()) + ",准确率:" + (num_hash1.get() * 1.0
                / total) + ", time cost:" + time_hash1);
        System.out.println("Hash2Filter, 错误数: " + (total - num_hash2.get()) + ",准确率:" + (num_hash2.get() * 1.0
                / total) + ", time cost:" + time_hash2);

    }

    private long testFilter(List<String> partials, Function<String, Boolean> booleanFunction, AtomicInteger counter) {
        long now = System.currentTimeMillis();
        for (String text : partials) {
            if (text.length() > 1 && !booleanFunction.apply(text)) {
                //计数
                counter.incrementAndGet();
            }
        }
        return System.currentTimeMillis() - now;
    }


    /**
     * 测试hash冲突
     */
    @Test
    public void testConflict() {
        List<String> similars = loadLines("similar.txt");
        List<String> wordlist = loadLines("wordlist.txt");
        Collection<String> words = new HashSet<>();
        for (String word : wordlist) {
            String[] splits = word.toLowerCase().split("\\s+");
            for (String split : splits) {
                words.add(split.trim());
            }
        }
        for (String similar : similars) {
            String[] arr = similar.split("\t");
            if (arr.length < 1) {
                continue;
            }
            String origin = arr[0].trim();
            String[] sims = arr[1].trim().split(";;");
            for (String word : wordlist) {
                if (word.indexOf(origin) != -1) {
                    for (String sim : sims) {
                        String replace = word.replace(origin, sim.trim());
                        if (!replace.equals(word)) {
                            String[] splits = replace.toLowerCase().split("\\s+");
                            for (String split : splits) {
                                words.add(split.trim());
                            }
                        }
                    }
                }
            }
        }
        Map<String, Set<String>> signMap = new HashMap<>();
        int wordLength = 2;
        for (String word : words) {
            int length = word.length();
            //空词不能作为过滤词
            if (length < wordLength) {
                continue;
            }
            //首字符与尾字符不同，其他相同
            for (int i = 0; i <= length - wordLength; i++) {
                String segment = word.substring(i, i + wordLength);
                String signs = signByHash(segment, 5);
                signMap.computeIfAbsent(signs, k -> new HashSet<>()).add(segment);
            }
        }
        int total = signMap.size();
        int conflict = signMap.values().stream().mapToInt(n -> n.size() - 1).sum();
        signMap.values().stream().filter(n -> n.size() > 1).forEach(System.out::println);
        System.out.println("==>total:" + total + ", conflict:" + conflict + ",rate:" + Math.round(conflict * 100000.0 / total) / 1000.0 + "%");
    }


    @SneakyThrows
    private static List<String> loadLines(String path) {
        return Resources.readLines(Resources.getResource(path), Charset.forName("UTF8"));
    }


    private String signByHash(String word, int nHashFuncs) {
        Funnel<CharSequence> funnel = Funnels.unencodedCharsFunnel();
        byte[] bytes = Hashing.murmur3_128().hashObject(word, funnel).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);

        int bitSiz = 4727232;
        long combinedHash = hash1;
        Long[] longs = new Long[nHashFuncs];
        for (int i = 0; i < longs.length; i++) {
            // Make the combined hash positive and indexable
            longs[i] = (combinedHash & Long.MAX_VALUE) % bitSiz;
            combinedHash += hash2;
        }
        return Arrays.stream(longs).sorted().map(Object::toString).collect(Collectors.joining("|"));
    }

    private /* static */ long lowerEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    private /* static */ long upperEight(byte[] bytes) {
        return Longs.fromBytes(
                bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }

}
