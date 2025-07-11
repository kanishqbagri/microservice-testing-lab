package com.kb.contractgenerator.config;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class FreemarkerConfig {

    @Bean
    @Primary
    public Configuration freemarkerConfig() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        
        // Set template loader to classpath
        cfg.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "templates");
        
        // Set default encoding
        cfg.setDefaultEncoding("UTF-8");
        
        // Set template exception handler
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        
        // Set log template exceptions
        cfg.setLogTemplateExceptions(false);
        
        return cfg;
    }
} 