package com.tinge.nebula.rule;

import com.tinge.nebula.rule.define.WordType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * 命中规则集合<br/>
 * 注: 对{@link #getHitRules()}进行去重时,仅计算{@link HitRule#getRule()}属性
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Data
@AllArgsConstructor
public class HitRuleSet implements Serializable {

    /**
     * 查询ID,对于{@link Criterion}而言是指{@link Criterion#getId()}
     */
    private long id;
    /**
     * 命中规则列表
     */
    private Set<HitRule> hitRules;

    /**
     * 附加词,配对使用
     */
    private String extraWord;

    /**
     * 防止pb序列化为null
     */
    public HitRuleSet() {
        this.hitRules = new HashSet<>();
    }

    /**
     * @param id       查询id
     * @param hitRules 命中规则
     */
    public HitRuleSet(long id, Collection<HitRule> hitRules) {
        this(id, hitRules, null);
    }

    /**
     * @param id        查询id
     * @param hitRules  命中规则
     * @param extraWord 附加配对词
     */
    public HitRuleSet(long id, Collection<HitRule> hitRules, String extraWord) {
        this.id = id;
        if (hitRules instanceof Set) {
            this.hitRules = (Set<HitRule>) hitRules;
        } else {
            this.hitRules = new HashSet<>(hitRules);
        }
        this.extraWord = extraWord;
    }


    /**
     * 规则打平
     *
     * @return
     */
    public Map<WordType, Set<String>> flatMap() {
        Map<WordType, Set<String>> hitMap = new HashMap<>();
        Optional.ofNullable(hitRules)
                .orElse(Collections.emptySet())
                .stream()
                .filter(hitRule -> hitRule.getRule() != null
                        && hitRule.getHits() != null
                        && !hitRule.getHits().isEmpty())
                .forEach(hitRule -> {
                    WordType wordType = hitRule.getRule().getWordType();
                    hitMap.computeIfAbsent(wordType, e -> new HashSet<>()).addAll(hitRule.getHits());
                });
        return hitMap;
    }

}
