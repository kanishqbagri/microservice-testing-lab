package com.kb.contractgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.kb.contractgenerator")
public class ContractTestGeneratorApplication {

    public static void main(String[] args) {
        // Check if running in CLI mode
        if (args.length > 0 && args[0].equals("cli")) {
            // Run in CLI mode
            SpringApplication.run(ContractTestGeneratorApplication.class, args);
        } else {
            // Run in API mode
            SpringApplication.run(ContractTestGeneratorApplication.class, args);
        }
    }
} 