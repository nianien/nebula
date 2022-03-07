package com.tinge.nebula.algorithm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 一个{@link Term}匹配项由多个{@link Word}组成, 只有命中Term包含的全部Word, 当前Term才会匹配成功
 *
 * @author scorpio
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Term implements Serializable {

    /**
     * 同义词组
     */
    private Word[] words;


    /**
     * 规则ID
     */
    @Include
    private long id;


    /**
     * Term在Phrase中的顺序
     */
    @Include
    private int termIndex;

    /**
     * 每个词面
     *
     * @param words
     */
    public Term(String[] words, long id, int termIndex) {
        this.id = id;
        this.termIndex = termIndex;
        this.words = new Word[words.length];
        for (int i = 0; i < words.length; i++) {
            this.words[i] = new Word(words[i], id, termIndex, i);
        }
    }


    /**
     * 根据命中词判断是否匹配当前{@link Term}对象
     *
     * @param hitWords 命中词表
     * @return
     */
    public boolean match(Set<Word> hitWords) {
        int size = hitWords.stream().mapToInt(w -> w.getTermIndex() == termIndex ? 1 : 0).sum();
        return words.length == size;
    }

    /**
     * 根据命中词判断是否匹配当前{@link Term}对象,并返回匹配结果
     *
     * @param hitWords 命中词表
     * @return
     */
    public List<String> match(Map<Word, Set<HitWord>> hitWords) {
        if (words.length != hitWords.size()) {
            return null;
        }
        //备选命中词集合
        Set<HitWord> candidates = hitWords.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        //词表规则中的命中词,每个候选词选一个
        List<String> hits = new ArrayList<>();
        for (Word word : this.getWords()) {
            //查找可用的命中词
            Optional<HitWord> found = hitWords.getOrDefault(word, Collections.EMPTY_SET).stream().filter(candidates::contains).findFirst();
            if (!found.isPresent()) {
                return null;
            }
            candidates.remove(found.get());
            hits.add(found.get().getWord());
        }
        return hits;
    }

}
