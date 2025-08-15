package com.kb.contractgenerator.controller;

import com.kb.contractgenerator.model.ContractTestRequest;
import com.kb.contractgenerator.model.ContractTestResponse;
import com.kb.contractgenerator.service.ContractTestGeneratorService;
import com.kb.contractgenerator.service.SwaggerParserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contract-tests")
@CrossOrigin(origins = "*")
public class ContractTestController {

    @Autowired
    private ContractTestGeneratorService generatorService;

    @Autowired
    private SwaggerParserService swaggerParserService;

    @PostMapping("/generate")
    public ResponseEntity<ContractTestResponse> generateContractTests(
            @Valid @RequestBody ContractTestRequest request) {
        
        ContractTestResponse response = generatorService.generateContractTests(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/generate-from-url")
    public ResponseEntity<ContractTestResponse> generateFromUrl(
            @RequestParam String swaggerUrl,
            @RequestParam String providerName,
            @RequestParam String consumerName,
            @RequestParam(defaultValue = "pact") String testFramework,
            @RequestParam(defaultValue = "generated-contracts") String outputDirectory,
            @RequestParam(defaultValue = "8080") Integer port) {
        
        try {
            // Parse Swagger from URL
            var swaggerSpec = swaggerParserService.parseSwaggerFromUrl(swaggerUrl);
            
            // Create request
            ContractTestRequest request = new ContractTestRequest();
            request.setProviderName(providerName);
            request.setConsumerName(consumerName);
            request.setSwaggerSpec(swaggerSpec);
            request.setTestFramework(testFramework);
            request.setOutputDirectory(outputDirectory);
            request.setPort(port);
            
            ContractTestResponse response = generatorService.generateContractTests(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            ContractTestResponse response = new ContractTestResponse();
            response.setSuccess(false);
            response.setMessage("Failed to generate contract tests: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/generate-from-content")
    public ResponseEntity<ContractTestResponse> generateFromContent(
            @RequestParam String swaggerContent,
            @RequestParam String providerName,
            @RequestParam String consumerName,
            @RequestParam(defaultValue = "pact") String testFramework,
            @RequestParam(defaultValue = "generated-contracts") String outputDirectory,
            @RequestParam(defaultValue = "8080") Integer port) {
        
        try {
            // Parse Swagger content
            var swaggerSpec = swaggerParserService.parseSwaggerSpec(swaggerContent);
            
            // Create request
            ContractTestRequest request = new ContractTestRequest();
            request.setProviderName(providerName);
            request.setConsumerName(consumerName);
            request.setSwaggerSpec(swaggerSpec);
            request.setTestFramework(testFramework);
            request.setOutputDirectory(outputDirectory);
            request.setPort(port);
            
            ContractTestResponse response = generatorService.generateContractTests(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            ContractTestResponse response = new ContractTestResponse();
            response.setSuccess(false);
            response.setMessage("Failed to generate contract tests: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Contract Test Generator");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }

    @GetMapping("/supported-frameworks")
    public ResponseEntity<Map<String, Object>> getSupportedFrameworks() {
        Map<String, Object> frameworks = new HashMap<>();
        frameworks.put("pact", "Pact Contract Testing Framework");
        frameworks.put("spring-cloud-contract", "Spring Cloud Contract");
        return ResponseEntity.ok(frameworks);
    }

    @GetMapping("/supported-languages")
    public ResponseEntity<Map<String, Object>> getSupportedLanguages() {
        Map<String, Object> languages = new HashMap<>();
        languages.put("java", "Java");
        languages.put("kotlin", "Kotlin");
        languages.put("groovy", "Groovy");
        return ResponseEntity.ok(languages);
    }
} 