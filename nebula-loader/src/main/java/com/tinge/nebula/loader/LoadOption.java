package com.tinge.nebula.loader;

import com.tinge.nebula.rule.define.EntityType;
import com.tinge.nebula.rule.define.SourceType;
import com.tinge.nebula.rule.define.Valuable;
import com.tinge.nebula.rule.define.WordType;
import lombok.Builder;
import lombok.Getter;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.tinge.nebula.entity.rule_dict.Tables.RULE_WORD;
import static com.tinge.nebula.entity.rule_dict.Tables.SIMILAR_WORD;


/**
 * 规则加载选项
 */
public class LoadOption {

    /**
     * 生效位掩码
     */
    final static int ENABLE_MASK = 0b111;
    /**
     * 评估位掩码
     */
    final static int EVALUATE_MASK = 0b111000;
    final static int TRUE = 1;
    final static int FALSE = 0;

    /**
     * 词表规则查询条件
     */
    @Getter
    private Condition condition;

    /**
     * 相似词查询条件
     */
    @Getter
    private Condition similarCondition;


    @Builder
    public LoadOption(LoadStrategy loadStrategy, EnumSet<SourceType> sourceTypes, EnumSet<EntityType> entityTypes, EnumSet<WordType> wordTypes) {
        this.condition = buildCondition(loadStrategy, sourceTypes, entityTypes, wordTypes);
        this.similarCondition = buildSimilarCondition(sourceTypes);
    }

    /**
     * 规则来源计算
     *
     * @param sourceTypes
     * @return
     */
    public static <E extends Enum<E> & Valuable> int valueOf(EnumSet<E> sourceTypes) {
        int value = 0;
        for (Valuable valuable : sourceTypes) {
            value |= valuable.getValue();
        }
        return value;
    }

    /**
     * 构建加载条件
     *
     * @param loadStrategy
     * @param sourceTypes
     * @param entityTypes
     * @param wordTypes
     * @return
     */
    private Condition buildCondition(LoadStrategy loadStrategy,
                                     EnumSet<SourceType> sourceTypes,
                                     EnumSet<EntityType> entityTypes,
                                     EnumSet<WordType> wordTypes) {
        Condition condition = DSL.trueCondition();
        if (sourceTypes.size() > 0) {
            condition = condition.and(RULE_WORD.SOURCE.bitAnd(valueOf(sourceTypes)).ne(FALSE));
        }
        if (entityTypes.size() > 0) {
            condition = condition.and(RULE_WORD.ENTITY_TYPE.bitAnd(valueOf(entityTypes)).ne(FALSE));
        }
        if (wordTypes.size() > 0) {
            List<Integer> wordTypeValues = wordTypes.stream().map(Valuable::getValue).collect(Collectors.toList());
            condition = condition.and(RULE_WORD.WORD_TYPE.in(wordTypeValues));
        }
        if (loadStrategy == LoadStrategy.FOR_AUDIT) {
            //已生效
            condition = condition.and(RULE_WORD.STATUS.bitAnd(ENABLE_MASK).eq(TRUE));
        } else if (loadStrategy == LoadStrategy.FOR_EVALUATE) {
            //未评估
            condition = condition.and(RULE_WORD.STATUS.bitAnd(EVALUATE_MASK).eq(FALSE));
        }
        return condition;
    }

    /**
     * 构建变体词条件
     *
     * @param sourceTypes
     * @return
     */
    private Condition buildSimilarCondition(EnumSet<SourceType> sourceTypes) {
        Condition condition = DSL.trueCondition();
        if (sourceTypes.size() > 0) {
            condition = condition.and(SIMILAR_WORD.SOURCE.bitAnd(valueOf(sourceTypes)).ne(FALSE));
        }
        return condition.and(SIMILAR_WORD.STATUS.eq(1));
    }

    /**
     * 加载策略
     */
    public enum LoadStrategy {
        /**
         * 加载生效规则
         */
        FOR_AUDIT,
        /**
         * 加载待评估规则
         */
        FOR_EVALUATE
    }

    public static class LoadOptionBuilder {

        /**
         * builder默认参数
         */
        public LoadOptionBuilder() {
            this.sourceTypes = EnumSet.noneOf(SourceType.class);
            this.entityTypes = EnumSet.noneOf(EntityType.class);
            this.wordTypes = EnumSet.noneOf(WordType.class);
        }

        /**
         * 包含实体类型
         *
         * @param entityTypes
         * @return
         */
        public LoadOptionBuilder entityTypes(EntityType... entityTypes) {
            Arrays.stream(entityTypes).forEach(this.entityTypes::add);
            return this;
        }

        /**
         * 排除实体类型
         *
         * @param entityTypes
         * @return
         */
        public LoadOptionBuilder except(EntityType... entityTypes) {
            Arrays.stream(entityTypes).forEach(this.entityTypes::remove);
            return this;
        }


        /**
         * 包含规则来源
         *
         * @param sourceTypes
         * @return
         */
        public LoadOptionBuilder sourceTypes(SourceType... sourceTypes) {
            Arrays.stream(sourceTypes).forEach(this.sourceTypes::add);
            return this;
        }

        /**
         * 排除规则来源
         *
         * @param sourceTypes
         * @return
         */
        public LoadOptionBuilder except(SourceType... sourceTypes) {
            Arrays.stream(sourceTypes).forEach(this.sourceTypes::remove);
            return this;
        }


        /**
         * 包含词表类型
         *
         * @param wordTypes
         * @return
         */
        public LoadOptionBuilder wordTypes(WordType... wordTypes) {
            Arrays.stream(wordTypes).forEach(this.wordTypes::add);
            return this;
        }

        /**
         * 排除词表类型
         *
         * @param wordTypes
         * @return
         */
        public LoadOptionBuilder except(WordType... wordTypes) {
            Arrays.stream(wordTypes).forEach(this.wordTypes::remove);
            return this;
        }

    }

}
