package com.tinge.nebula.algorithm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NonNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 匹配的最小单元,一个词可对应多个同义词
 *
 * @author scorpio
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Word implements Serializable {


    /**
     * 词面
     */
    private String word;

    /**
     * Phrase ID
     */
    @Include
    private long id;


    /**
     * Term在Phrase中的顺序
     */
    @Include
    private int termIndex;


    /**
     * 词面在Term中的顺序
     */
    @Include
    private int wordIndex;


    /**
     * 同义词列表,应该包含{@link #word}
     */
    private Set<String> synonyms = new LinkedHashSet<>();


    public Word(String word, long id, int termIndex, int wordIndex) {
        this.word = word;
        this.id = id;
        this.termIndex = termIndex;
        this.wordIndex = wordIndex;
        this.synonyms.add(word);
    }


    /**
     * 添加同义词
     *
     * @param synonym
     * @return
     */
    public Word addSynonyms(String synonym) {
        this.synonyms.add(synonym);
        return this;
    }

}
