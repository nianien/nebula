package com.tinge.nebula.loader;

import com.tinge.nebula.rule.RuleLoader;
import com.tinge.nebula.rule.RuleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(DefaultLoaderConfig.class)
public class ApplicationTestConfig {

    @Bean
    public RuleManager ruleManager(@Autowired RuleLoader ruleLoader) {
        RuleManager ruleManager = new RuleManager(ruleLoader, (strings -> {
            for (int i = 0; i < strings.length; i++) {
                if (strings[i] == "华而") {
                    strings[i] = "华尔";
                }
                System.out.println("===>"+strings[i]);
            }
            return strings;
        }));
        ruleManager.load();
        return ruleManager;
    }


}