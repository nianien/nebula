package com.tinge.nebula.algorithm;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.tinge.nebula.algorithm.trie.AhoCorasickDoubleArrayTrie;
import com.tinge.nebula.algorithm.trie.AhoCorasickDoubleArrayTrie.Hit;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 词表搜索,支持同义词和非限词匹配
 * <p/>
 *
 * @author scorpio
 * @version 1.0.0
 */
@Slf4j
public class WordMatcher implements Serializable {

    /**
     * 词表映射
     */
    private Map<Long, Phrase> phraseMap;
    /**
     * 所有词表项包含的词面构造的匹配树
     */
    private AhoCorasickDoubleArrayTrie<Set<Word>> trie;


    /**
     * @param size 初始大小
     */
    public WordMatcher(int size) {
        this.phraseMap = Maps.newHashMapWithExpectedSize(size);
    }


    /**
     * 使用默认短语匹配实现
     */
    public WordMatcher() {
        this(64);
    }

    /**
     * 添加词表项
     *
     * @param phrase
     * @return
     */
    public WordMatcher addItem(Phrase phrase) {
        phraseMap.put(phrase.getId(), phrase);
        return this;
    }

    /**
     * 添加词表项
     *
     * @param id
     * @param text
     * @param exempts
     * @param fuzzy
     * @return
     */
    public WordMatcher addItem(long id, String text, String[] exempts, boolean fuzzy) {
        return addItem(Phrase.create(id, text, exempts, fuzzy));
    }


    /**
     * 添加词表项
     *
     * @param id
     * @param text
     * @param exempts
     * @return
     */
    public WordMatcher addItem(long id, String text, String[] exempts) {
        return addItem(Phrase.create(id, text, exempts, true));
    }

    /**
     * 构建词表容器,生成匹配树
     *
     * @param synonymMap 同义词表
     * @return
     */
    public WordMatcher build(Map<String, Collection<String>> synonymMap) {
        return this.build(synonymMap, null);
    }

    /**
     * 构建词表容器,生成匹配树
     *
     * @param synonymMap 同义词表
     * @param consumer   规则词表消费者,用于额外处理规则词表
     * @return
     */
    public WordMatcher build(Map<String, Collection<String>> synonymMap, Consumer<String> consumer) {
        log.info("==>begin to build trie tree...");
        Stopwatch stopwatch = Stopwatch.createStarted();
        //构建trie树需要的Map,包含词表和非限词
        Map<String, Set<Word>> trieMap = new HashMap<>(phraseMap.size());
        for (Phrase phrase : phraseMap.values()) {
            Word exemptWord = new Word("", phrase.getId(), -1, -1);
            //扩展变体词
            phrase.synonyms(synonymMap);
            //添加词表
            for (Term term : phrase.getTerms()) {
                for (Word word : term.getWords()) {
                    //添加变体词
                    for (String synonym : word.getSynonyms()) {
                        //绑定变体词与词表Id对应关系
                        trieMap.computeIfAbsent(synonym, (k) -> new HashSet<>())
                                .add(word);
                        //支持对规则词面的附加处理
                        if (consumer != null) {
                            consumer.accept(synonym);
                        }
                    }
                }
            }
            //添加非限词
            for (String word : phrase.getExemptWords()) {
                //非限词不绑定词表ID
                trieMap.computeIfAbsent(word, (k) -> new HashSet<>()).add(exemptWord);
                if (consumer != null) {
                    consumer.accept(word);
                }
            }
        }
        trie = new AhoCorasickDoubleArrayTrie<>();
        log.info("==>building trie tree...");
        trie.build(trieMap);
        log.info("==>succeed to build trie tree, with {} word-terms, cost {} ms!",
                phraseMap.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return this;
    }


    /**
     * 匹配规则
     *
     * @param text
     * @return
     */
    public List<HitResult> match(String text) {
        List<HitResult> results = new ArrayList<>();
        apply(text, (phrase, map) -> {
            HitResult hitResult = phrase.match(map);
            if (hitResult != null) {
                results.add(hitResult);
            }
        });
        return results;
    }


    /**
     * 部分匹配规则
     *
     * @param text
     * @return
     */
    public Map<Phrase, Map<HitWord, Set<Word>>> matchPartial(String text) {
        Map<Phrase, Map<HitWord, Set<Word>>> results = new HashMap<>();
        apply(text, (phrase, map) -> {
            if (!map.isEmpty()) {
                results.put(phrase, map);
            }
        });
        return results;
    }


    /**
     * 匹配词表并处理结果
     *
     * @param text
     * @param consumer
     * @return
     */
    private void apply(String text, BiConsumer<Phrase, Map<HitWord, Set<Word>>> consumer) {
        //hit literal到IdSet的映射
        List<Hit<Set<Word>>> hits = trie.parseText(text);
        //命中词表
        Map<Phrase, Map<HitWord, Set<Word>>> hitTable = new HashMap<>();
        for (Hit<Set<Word>> hit : hits) {
            HitWord hitWord = new HitWord(hit.begin, hit.end, hit.hit(text));
            for (Word word : hit.value) {
                Phrase phrase = phraseMap.get(word.getId());
                hitTable.computeIfAbsent(phrase, k -> new HashMap<>())
                        .computeIfAbsent(hitWord, k -> new HashSet<>()).add(word);
            }
        }
        hitTable.forEach((k, v) -> consumer.accept(k, k.hit(v)));

    }

}
