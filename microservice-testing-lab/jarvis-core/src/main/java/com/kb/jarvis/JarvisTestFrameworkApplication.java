package com.kb.jarvis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.kb.jarvis")
// @EntityScan(basePackages = "com.kb.jarvis.core.model")
// @EnableJpaRepositories(basePackages = "com.kb.jarvis.core.repository")
@EnableScheduling
public class JarvisTestFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(JarvisTestFrameworkApplication.class, args);
        System.out.println("Jarvis Test Framework Application started");
        System.out.println("**************************************************");        
        System.out.println("Hello Kanishq! I am awake, How can i help you today");
        System.out.println("**************************************************");        
    }
} 