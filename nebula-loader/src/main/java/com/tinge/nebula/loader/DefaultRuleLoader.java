package com.tinge.nebula.loader;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tinge.nebula.entity.rule_dict.tables.pojos.RuleWord;
import com.tinge.nebula.entity.rule_dict.tables.pojos.SimilarWord;
import com.tinge.nebula.rule.RuleEngine;
import com.tinge.nebula.rule.RuleLoader;
import com.tinge.nebula.rule.WordExtra;
import com.tinge.nebula.rule.WordRule;
import com.tinge.nebula.rule.define.MatchType;
import com.tinge.nebula.rule.define.WordType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.tinge.nebula.entity.rule_dict.Tables.RULE_WORD;
import static com.tinge.nebula.entity.rule_dict.Tables.SIMILAR_WORD;
import static com.tinge.nebula.rule.utils.TypeConverter.*;
import static java.util.stream.Collectors.toList;

/**
 * 默认规则加载器
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Slf4j
public class DefaultRuleLoader implements RuleLoader {


    @Resource
    @Setter/*for non-spring*/
    private DSLContext ruleDslContext;

    /**
     * 规则加载选项
     */
    private final LoadOption loadOption;

    /**
     * 文本格式化
     */
    private Function<String, String> formatter = Function.identity();


    /**
     * 加载指定来源的规则
     *
     * @param loadOption
     */
    public DefaultRuleLoader(LoadOption loadOption) {
        this.loadOption = loadOption;
    }


    @Override
    public RuleEngine load() {
        List<WordRule> dbRules = loadWordRules();
        Multimap<String, String> similarMap = loadSimilarWords();
        log.info("==>loading {} rules, {} similar words.", dbRules.size(), similarMap.size());
        return RuleEngine.create(dbRules, similarMap.asMap());
    }


    /**
     * 加载词表规则
     *
     * @return
     */
    private List<WordRule> loadWordRules() {
        List<RuleWord> dbRules = ruleDslContext.select()
                .from(RULE_WORD)
                .where(loadOption.getCondition())
                .fetchInto(RuleWord.class);
        return dbRules.stream()
                .map(this::from)
                .collect(toList());
    }


    /**
     * 加载变体词
     *
     * @return
     */
    private Multimap<String, String> loadSimilarWords() {
        List<SimilarWord> similarWords = ruleDslContext.select()
                .from(SIMILAR_WORD)
                .where(loadOption.getSimilarCondition())
                .fetchInto(SimilarWord.class);
        Multimap<String, String> similarMap = HashMultimap
                .create(similarWords.size(), 2);
        for (SimilarWord word : similarWords) {
            String originWord = format(word.getOriginalWord());
            for (String similar : word.getSimilarWord().split("\n")) {
                similarMap.put(originWord, format(similar));
            }
        }
        return similarMap;
    }


    /**
     * 规则转换
     *
     * @param rule
     * @return
     */
    public WordRule from(RuleWord rule) {
        WordRule wordRule = WordRule.builder()
                .id(rule.getId())
                .word(format(rule.getWord()))
                .wordType(WordType.of(rule.getWordType()))
                .matchType(MatchType.of(rule.getMatchType()))
                .extra(WordExtra.builder()
                        .entityTypes(rule.getEntityType())
                        .sourceTypes(rule.getSource())
                        .industries(toInts(rule.getIndustries()))
                        .excludeCustomers(toLongs(rule.getExcludeCustomers()))
                        .excludeUsers(toLongs(rule.getExcludeUsers()))
                        .excludeWords(format(toLines(rule.getExcludeWords())))
                        .riskTag(rule.getRiskTag())
                        .grade(rule.getGrade())
                        .build())
                .build();
        return wordRule;
    }

    private String format(String word) {
        return formatter.apply(word);
    }

    private String[] format(String[] words) {
        return Arrays.stream(Optional.ofNullable(words).orElse(new String[0])).map(formatter).toArray(String[]::new);
    }

}
