package com.tinge.nebula.loader;

import com.tinge.nebula.loader.LoadOption.LoadStrategy;
import com.tinge.nebula.rule.RuleLoader;
import com.tinge.nebula.rule.define.EntityType;
import com.tinge.nebula.rule.define.SourceType;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 默认词表加载
 *
 * @author scorpio
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class DefaultLoaderConfig {

    @Bean
    public RuleLoader ruleLoader() {
        LoadOption loadOption = LoadOption.builder()
                .loadStrategy(LoadStrategy.FOR_AUDIT)
                .sourceTypes(SourceType.FEED, SourceType.APP, SourceType.VIDEO)
                .entityTypes(EntityType.values())
                .except(EntityType.PAGE)
                .build();
        log.info("load default rule...");
        return new DefaultRuleLoader(loadOption);
    }


    @Bean
    @Primary
    public DSLContext ruleDslContext(DataSource dataSource,
                                     @Value("${spring.jooq.sql-dialect:MySQL}") SQLDialect sqlDialect) {
        DefaultConfiguration config = new DefaultConfiguration();
        config.setSQLDialect(sqlDialect);
        config.setDataSource(dataSource);
        config.settings()
                .withRenderSchema(false)
                .withRenderNameCase(RenderNameCase.AS_IS)
                .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED);
        return DSL.using(config);
    }

}
