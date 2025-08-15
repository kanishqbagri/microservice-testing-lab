package com.kb.jarvis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.kb.jarvis")
@EnableScheduling
public class JarvisTestFrameworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(JarvisTestFrameworkApplication.class, args);
    }
} 