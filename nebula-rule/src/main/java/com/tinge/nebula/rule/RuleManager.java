package com.tinge.nebula.rule;

import com.google.common.base.Preconditions;
import com.tinge.nebula.rule.define.WordType;
import com.tinge.nebula.rule.filter.rulefilter.PredicateRuleFilter;
import com.tinge.nebula.rule.filter.rulefilter.predicate.CriterionPredicate;
import com.tinge.nebula.rule.filter.rulefilter.predicate.WordTypePredicate;
import com.tinge.nebula.rule.pair.PairMatcher;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 规则管理器,支持规则定时加载并进行词表匹配
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@Slf4j
public class RuleManager implements Serializable {

    private transient volatile AtomicBoolean loading = new AtomicBoolean(false);
    private transient Supplier<RuleEngine> ruleLoader;
    private volatile RuleEngine ruleEngine;
    private Function<String[], String[]> textFormatter;


    /**
     * 提供规则加载器&文本格式化
     *
     * @param ruleLoader
     * @param textFormatter
     */
    public RuleManager(Supplier<RuleEngine> ruleLoader, Function<String[], String[]> textFormatter) {
        this.ruleLoader = ruleLoader;
        this.ruleEngine = ruleLoader.get();
        this.textFormatter = textFormatter;
    }

    /**
     * 提供规则加载器
     *
     * @param ruleLoader
     */
    public RuleManager(Supplier<RuleEngine> ruleLoader) {
        this(ruleLoader, null/*Function.identity()不可序列化*/);
    }

    /**
     * 提供规则引擎&文本格式化
     *
     * @param ruleEngine
     * @param textFormatter
     */
    public RuleManager(RuleEngine ruleEngine, Function<String[], String[]> textFormatter) {
        this(() -> ruleEngine, textFormatter);
    }

    /**
     * 提供规则引擎
     *
     * @param ruleEngine
     */
    public RuleManager(RuleEngine ruleEngine) {
        this(() -> ruleEngine, null/*Function.identity()不可序列化*/);
    }

    /**
     * 加载规则<p/>
     * 如果成功执行规则加载,则返回true; 如果已经被其他线程执行加载,则返回false; 如果加载失败,则抛出异常.<p/>
     * 该方法是线程安全的
     */
    public boolean load() {
        Preconditions.checkNotNull(ruleLoader, "rule loader cannot be null");
        //非阻塞式抢占,如果抢占成功,则加载规则,否则直接放弃
        if (loading.compareAndSet(false, true)) {
            try {
                long begin = System.currentTimeMillis();
                log.warn("begin to load rules...");
                ruleEngine = ruleLoader.get();
                log.warn("succeed to load rules, time cost {} ms!",
                        System.currentTimeMillis() - begin);
                return true;
            } finally {
                loading.set(false);
            }
        }
        log.warn("rules-loading has been started!");
        return false;
    }


    /**
     * 匹配指定文本列表, 返回命中的规则
     *
     * @param texts     查询对象
     * @param wordTypes 需要匹配的词表类型, 如果未指定,则匹配全部词表
     * @return
     */
    public List<HitRule> apply(String[] texts, WordType[] wordTypes) {
        List<HitRule> hitRules = doApply(texts, new WordTypePredicate(wordTypes));
        if (hitRules.size() > 0) {
            log.info("==>texts:{}, hitRules:{}", Arrays.asList(texts), hitRules);
        }
        return hitRules;
    }

    /**
     * 匹配查询范式, 返回命中的规则
     *
     * @param criterion 查询对象
     * @return
     */
    public HitRuleSet apply(Criterion criterion) {
        List<HitRule> hitRules = doApply(criterion.getTexts(), new CriterionPredicate(criterion));
        if (hitRules.size() > 0) {
            log.info("==>criterion:{}, hitRules:{}", criterion, hitRules);
        }
        return new HitRuleSet(criterion.getId(), hitRules);
    }


    /**
     * 匹配查询范式, 返回命中的规则
     *
     * @param criteria 查询对象
     * @return
     */
    public List<HitRuleSet> apply(List<Criterion> criteria) {
        List<HitRuleSet> hitRuleSets = new ArrayList<>();
        criteria.stream().map(this::apply).forEach(hitRuleSets::add);
        return hitRuleSets;
    }


    /**
     * 匹配配对列表, 返回命中的规则
     *
     * @param criteria
     * @param words
     * @return
     */
    public List<HitRuleSet> apply4Pair(List<Criterion> criteria, String[] words) {
        List<HitRuleSet> hitRuleSets = new PairMatcher(ruleEngine, textFormatter).matchPairs(criteria, words);
        return hitRuleSets;
    }


    /**
     * 匹配配对列表, 返回命中的规则
     *
     * @param criterion 查询范式
     * @param words     配对词列表
     * @return
     */
    public List<HitRuleSet> apply4Pair(Criterion criterion, String[] words) {
        return apply4Pair(Arrays.asList(criterion), words);
    }


    /**
     * 匹配词表规则并进行过滤
     *
     * @param texts
     * @param predicate
     * @return
     */
    private List<HitRule> doApply(String[] texts, Predicate<WordRule> predicate) {
        List<HitRule> hitRules = ruleEngine.match(textFormatter == null ? texts : textFormatter.apply(texts));
        return predicate == null ? hitRules : PredicateRuleFilter.create(predicate).filter(hitRules);
    }


}
