package com.tinge.nebula.rule.filter.rulefilter.predicate;

import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.WordRule;
import com.tinge.nebula.rule.define.EntityType;
import lombok.AllArgsConstructor;

import java.util.function.Predicate;

/**
 * 规则对应类型列表包含指定{@link EntityType}, 则命中规则
 *
 * @author scorpio
 * @version 1.0.0
 * @email skyfalling@live.com
 */
@AllArgsConstructor
public class EntityTypePredicate implements Predicate<WordRule> {

    private EntityType entityType;


    public EntityTypePredicate(Criterion criterion) {
        this(criterion.getEntityType());
    }

    /**
     * 未提供实体类型或者类型在规则对应实体列表中
     *
     * @param rule
     * @return
     */
    @Override
    public boolean test(WordRule rule) {
        int entityTypes = rule.getExtra().getEntityTypes();
        return entityType == null || entityTypes == 0 || entityType.oneOf(entityTypes);
    }
}
