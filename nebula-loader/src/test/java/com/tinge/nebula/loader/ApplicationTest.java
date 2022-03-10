package com.tinge.nebula.loader;

import com.google.common.base.Stopwatch;
import com.tinge.nebula.rule.Criterion;
import com.tinge.nebula.rule.HitRule;
import com.tinge.nebula.rule.HitRuleSet;
import com.tinge.nebula.rule.RuleManager;
import com.tinge.nebula.rule.define.EntityType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = ApplicationTestConfig.class)/*@ExtendWith({SpringExtension.class})*/
@EnableAutoConfiguration/*(exclude={DataSourceAutoConfiguration.class})*/
@Slf4j
public class ApplicationTest {


    @Resource
    private RuleManager ruleManager;
    @Resource
    private DataSource dataSource;

    @BeforeEach
    public void setup() {
        ruleManager.load();
    }

    @Test
    public void testRepeatedWord() {
        //规则词为中国 中国
        Criterion.CriterionBuilder builder = Criterion.builder()
                .entityType(EntityType.IDEA);
        Criterion criterion = builder.texts(new String[]{"中国共产党"}).build();
        Set<HitRule> rules = ruleManager.apply(criterion).getHitRules();
        //只包含一个中国,不命中
        assertThat(rules.size()).isEqualTo(0);

        criterion = builder.texts(new String[]{"中国和中国共产党"}).build();
        rules = ruleManager.apply(criterion).getHitRules();
        //包含两个中国,命中
        assertThat(rules.size()).isEqualTo(1);

        criterion = builder.texts(new String[]{"中国和中国人民"}).build();
        rules = ruleManager.apply(criterion).getHitRules();
        //包含两个中国,但一个被非限,不命中
        assertThat(rules.size()).isEqualTo(0);
    }

    @Test
    public void testExactSimilarWord() {
        Criterion.CriterionBuilder builder = Criterion.builder()
                .entityType(EntityType.IDEA)
                .userId(0);
        Criterion criterion = builder.texts(new String[]{"咨询VX", "薇.芯"}).build();
        long now = System.currentTimeMillis();
        Set<HitRule> rules = ruleManager.apply(criterion).getHitRules();
        log.info("==>word:[{}] hits {} rules cost {} ms", criterion.getTexts(), rules.size(),
                System.currentTimeMillis() - now);
        for (HitRule rule : rules) {
            System.out.println(rule);
        }
        //变体命中精确匹配, 未命中包含匹配
        assertThat(rules.size()).isEqualTo(1);
    }


    @Test
    public void testExactAndContain() {
        String text = "原血神座";
        Criterion.CriterionBuilder builder = Criterion.builder()
                .entityType(EntityType.IDEA)
                .userId(0)
                //过滤行业
                .industries(new int[]{10001});
        Criterion criterion = builder.texts(new String[]{text}).build();
        Set<HitRule> rules = ruleManager.apply(criterion).getHitRules();
        //精确黑词因为行业未命中,商标包含匹配命中
        assertThat(rules.size()).isEqualTo(1);
    }

    @Test
    public void testPair() {
        String text = "物料流{}街分钟";
        String[] words = new String[]{"有什么办法", "华而"};
        Criterion.CriterionBuilder builder = Criterion.builder()
                .entityType(EntityType.IDEA)
                .userId(0);
        Criterion criterion = builder.texts(new String[]{text}).build();
        long now = System.currentTimeMillis();
        List<HitRuleSet> hitRuleSets = ruleManager.apply4Pair(criterion, words);
        int count = (int) hitRuleSets
                .stream()
                .flatMap(e -> e.getHitRules().stream())
                .count();
        log.info("==>word:[{}] hits {} rules cost {} ms", criterion.getTexts(), count,
                System.currentTimeMillis() - now);
        for (HitRuleSet ruleSet : hitRuleSets) {
            System.out.println(ruleSet);
        }
        //连接处命中, 非连接处组合命中
        assertThat(count).isEqualTo(2);

    }


    @Test
    public void testExcludeUser() {
        Criterion.CriterionBuilder builder = Criterion.builder()
                .entityType(EntityType.IDEA);
        Criterion criterion = builder.texts(new String[]{"中国医药大学"}).build();
        Set<HitRule> rules = ruleManager.apply(criterion).getHitRules();
        //不是非限用户则命中
        assertThat(rules.size()).isEqualTo(1);
        criterion = builder.texts(new String[]{"中国医药大学"})
                .userId(12428198).build();
        rules = ruleManager.apply(criterion).getHitRules();
        //非限用户不命中
        assertThat(rules.size()).isEqualTo(0);
    }

    @Test
    public void testExcludeWord() {
        Criterion.CriterionBuilder builder = Criterion.builder()
                .entityType(EntityType.IDEA);
        Criterion criterion = builder.texts(new String[]{"中国医药大学"}).build();
        Set<HitRule> rules = ruleManager.apply(criterion).getHitRules();
        //无非限词则命中
        assertThat(rules.size()).isEqualTo(1);

        criterion = builder.texts(new String[]{"无痛药流"}).build();
        rules = ruleManager.apply(criterion).getHitRules();
        //有非限词不命中
        assertThat(rules.size()).isEqualTo(0);
    }

    @Test
    @SneakyThrows
    public void testMemoryUsed() {
        Criterion.CriterionBuilder builder = Criterion.builder()
                .entityType(EntityType.IDEA)
                .userId(0);
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        List<String> texts = Arrays.asList("中国医药大学", "无痛药流", "中国和中国共产党", "中国和中国人民");
        for (String text : texts) {
            stopwatch.start();
            Criterion criterion = builder.texts(new String[]{text}).build();
            Set<HitRule> rules = ruleManager.apply(criterion).getHitRules();
            stopwatch.stop();
            log.info("==>word:[{}] hits {} rules cost {} ms", criterion.getTexts(), rules.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
            for (HitRule rule : rules) {
                System.out.println(rule);
            }
        }
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        Thread.sleep(1000);
        log.info("memory total cost {} MB", (runtime.totalMemory() - runtime.freeMemory()) / (1024.0 * 1024));
    }

}