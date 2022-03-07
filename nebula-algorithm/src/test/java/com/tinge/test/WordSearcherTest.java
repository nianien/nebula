package com.tinge.test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tinge.nebula.algorithm.HitResult;
import com.tinge.nebula.algorithm.WordMatcher;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author scorpio
 * @version 1.0.0
 */
public class WordSearcherTest {

    @Test
    public void test() {
        Multimap<String, String> similarMap = HashMultimap.create();
        similarMap.put("共产党", "共产主义");
        similarMap.put("万岁", "永垂不朽");

        WordMatcher wordSearcher = new WordMatcher()
                .addItem(1, "中国 共产党", new String[]{"共产主义万岁"})
                .addItem(2, "美国 共产党", new String[]{"共产党万岁q"})
                .build(similarMap.asMap());
        List<HitResult> match = wordSearcher.match("中国共产主义永垂不朽美国");

        for (HitResult hitItem : match) {
            System.out.println(hitItem);
        }
        assertEquals(match.size(), 1);

    }

    @Test
    public void test2() {
        Multimap<String, String> similarMap = HashMultimap.create();
        similarMap.put("鲜花 速递", "鲜花 快递");
        similarMap.put("德 邦", "德 帮");

        WordMatcher wordSearcher = new WordMatcher()
                .addItem(1, "美丽鲜花 速递", new String[0])
                .addItem(1, "德 邦", new String[0])
                .build(similarMap.asMap());
        List<HitResult> match = wordSearcher.match("中国美丽鲜花快递到北京，还有德**邦品牌");

        for (HitResult hitItem : match) {
            System.out.println(hitItem);
        }

    }

    @Test
    public void testWord() {
        assertEquals(new Param("天猫商城天猫旗舰店双十一促销活动",
                "天猫",
                new String[]{"天猫商城天猫旗舰店"}).match().size(), 0);
        assertEquals(new Param("天猫旗舰店双十一促销活动",
                "天猫 天猫",
                new String[]{}).match().size(), 0);
        assertEquals(new Param("天猫商城天猫旗舰店双十一促销活动",
                "天猫 天猫",
                new String[]{}).match().size(), 1);
    }


    @Test
    public void testWord2() {
        System.out.println(new Param("在外地考驾照",
                "外地 驾照",
                new String[]{"考驾照1", "外地考驾照1"}
        ).match());
    }

    @AllArgsConstructor
    static class Param {
        String text;
        String word;
        String[] excludes;

        public List<HitResult> match() {
            WordMatcher wordSearcher = new WordMatcher()
                    .addItem(2, word, excludes)
                    .build(HashMultimap.<String, String>create().asMap());
            return wordSearcher.match(text);
        }
    }

}
