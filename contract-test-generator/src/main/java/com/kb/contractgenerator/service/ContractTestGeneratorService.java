package com.kb.contractgenerator.service;

import com.kb.contractgenerator.model.ContractTestRequest;
import com.kb.contractgenerator.model.ContractTestResponse;
import com.kb.contractgenerator.model.SwaggerSpec;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContractTestGeneratorService {

    @Autowired
    private SwaggerParserService swaggerParserService;

    @Autowired
    private Configuration freemarkerConfig;

    public ContractTestResponse generateContractTests(ContractTestRequest request) {
        ContractTestResponse response = new ContractTestResponse();
        List<ContractTestResponse.GeneratedTest> generatedTests = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try {
            // Parse Swagger specification
            SwaggerSpec swaggerSpec = request.getSwaggerSpec();
            List<SwaggerParserService.ContractEndpoint> endpoints = 
                swaggerParserService.extractContractEndpoints(swaggerSpec);

            // Filter endpoints based on include/exclude paths
            endpoints = filterEndpoints(endpoints, request.getIncludePaths(), request.getExcludePaths());

            // Generate consumer tests
            if ("pact".equals(request.getTestFramework())) {
                generatedTests.addAll(generatePactConsumerTests(endpoints, request));
                generatedTests.addAll(generatePactProviderTests(endpoints, request));
            } else if ("spring-cloud-contract".equals(request.getTestFramework())) {
                generatedTests.addAll(generateSpringCloudContractTests(endpoints, request));
            }

            // Create output directory
            Path outputDir = Paths.get(request.getOutputDirectory());
            Files.createDirectories(outputDir);

            // Write generated tests to files
            for (ContractTestResponse.GeneratedTest test : generatedTests) {
                Path filePath = outputDir.resolve(test.getFilePath());
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, test.getContent().getBytes());
            }

            response.setSuccess(true);
            response.setMessage("Successfully generated " + generatedTests.size() + " contract tests");
            response.setGeneratedTests(generatedTests);
            response.setWarnings(warnings);
            response.setErrors(errors);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to generate contract tests: " + e.getMessage());
            errors.add(e.getMessage());
            response.setErrors(errors);
        }

        return response;
    }

    private List<SwaggerParserService.ContractEndpoint> filterEndpoints(
            List<SwaggerParserService.ContractEndpoint> endpoints,
            List<String> includePaths,
            List<String> excludePaths) {
        
        return endpoints.stream()
            .filter(endpoint -> {
                // Include paths filter
                if (includePaths != null && !includePaths.isEmpty()) {
                    boolean included = includePaths.stream()
                        .anyMatch(pattern -> endpoint.getPath().matches(pattern));
                    if (!included) return false;
                }
                
                // Exclude paths filter
                if (excludePaths != null && !excludePaths.isEmpty()) {
                    boolean excluded = excludePaths.stream()
                        .anyMatch(pattern -> endpoint.getPath().matches(pattern));
                    if (excluded) return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());
    }

    private List<ContractTestResponse.GeneratedTest> generatePactConsumerTests(
            List<SwaggerParserService.ContractEndpoint> endpoints,
            ContractTestRequest request) throws IOException, TemplateException {
        
        List<ContractTestResponse.GeneratedTest> tests = new ArrayList<>();
        
        for (SwaggerParserService.ContractEndpoint endpoint : endpoints) {
            Map<String, Object> templateData = createTemplateData(endpoint, request);
            
            // Generate consumer test
            Template template = freemarkerConfig.getTemplate("pact-consumer-test.ftl");
            StringWriter writer = new StringWriter();
            template.process(templateData, writer);
            
            ContractTestResponse.GeneratedTest test = new ContractTestResponse.GeneratedTest();
            test.setTestName(endpoint.getOperationId() + "ConsumerTest");
            test.setTestType("consumer");
            test.setFilePath("consumer/" + endpoint.getOperationId() + "ConsumerTest.java");
            test.setContent(writer.toString());
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("path", endpoint.getPath());
            metadata.put("method", endpoint.getMethod());
            metadata.put("operationId", endpoint.getOperationId());
            test.setMetadata(metadata);
            
            tests.add(test);
        }
        
        return tests;
    }

    private List<ContractTestResponse.GeneratedTest> generatePactProviderTests(
            List<SwaggerParserService.ContractEndpoint> endpoints,
            ContractTestRequest request) throws IOException, TemplateException {
        
        List<ContractTestResponse.GeneratedTest> tests = new ArrayList<>();
        
        // Generate single provider test file
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("providerName", request.getProviderName());
        templateData.put("endpoints", endpoints);
        templateData.put("port", request.getPort());
        
        Template template = freemarkerConfig.getTemplate("pact-provider-test.ftl");
        StringWriter writer = new StringWriter();
        template.process(templateData, writer);
        
        ContractTestResponse.GeneratedTest test = new ContractTestResponse.GeneratedTest();
        test.setTestName(request.getProviderName() + "ProviderTest");
        test.setTestType("provider");
        test.setFilePath("provider/" + request.getProviderName() + "ProviderTest.java");
        test.setContent(writer.toString());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("providerName", request.getProviderName());
        metadata.put("endpointCount", endpoints.size());
        test.setMetadata(metadata);
        
        tests.add(test);
        
        return tests;
    }

    private List<ContractTestResponse.GeneratedTest> generateSpringCloudContractTests(
            List<SwaggerParserService.ContractEndpoint> endpoints,
            ContractTestRequest request) throws IOException, TemplateException {
        
        List<ContractTestResponse.GeneratedTest> tests = new ArrayList<>();
        
        for (SwaggerParserService.ContractEndpoint endpoint : endpoints) {
            Map<String, Object> templateData = createTemplateData(endpoint, request);
            
            // Generate Spring Cloud Contract test
            Template template = freemarkerConfig.getTemplate("spring-cloud-contract-test.ftl");
            StringWriter writer = new StringWriter();
            template.process(templateData, writer);
            
            ContractTestResponse.GeneratedTest test = new ContractTestResponse.GeneratedTest();
            test.setTestName(endpoint.getOperationId() + "ContractTest");
            test.setTestType("contract");
            test.setFilePath("contracts/" + endpoint.getOperationId() + ".groovy");
            test.setContent(writer.toString());
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("path", endpoint.getPath());
            metadata.put("method", endpoint.getMethod());
            metadata.put("operationId", endpoint.getOperationId());
            test.setMetadata(metadata);
            
            tests.add(test);
        }
        
        return tests;
    }

    private Map<String, Object> createTemplateData(
            SwaggerParserService.ContractEndpoint endpoint,
            ContractTestRequest request) {
        
        Map<String, Object> data = new HashMap<>();
        data.put("endpoint", endpoint);
        data.put("providerName", request.getProviderName());
        data.put("consumerName", request.getConsumerName());
        data.put("baseUrl", request.getBaseUrl());
        data.put("port", request.getPort());
        data.put("customHeaders", request.getCustomHeaders());
        
        // Generate sample request body
        if (endpoint.getRequestBody() != null) {
            data.put("sampleRequestBody", generateSampleRequestBody(endpoint.getRequestBody()));
        }
        
        // Generate sample response body
        if (endpoint.getResponses() != null && !endpoint.getResponses().isEmpty()) {
            String successResponse = endpoint.getResponses().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("2"))
                .findFirst()
                .map(entry -> generateSampleResponseBody(entry.getValue()))
                .orElse("{}");
            data.put("sampleResponseBody", successResponse);
        }
        
        return data;
    }

    private String generateSampleRequestBody(SwaggerSpec.RequestBody requestBody) {
        if (requestBody.getContent() == null || requestBody.getContent().isEmpty()) {
            return "{}";
        }
        
        // Get JSON content
        SwaggerSpec.MediaType jsonContent = requestBody.getContent().get("application/json");
        if (jsonContent != null && jsonContent.getSchema() != null) {
            return generateSampleJson(jsonContent.getSchema());
        }
        
        return "{}";
    }

    private String generateSampleResponseBody(SwaggerSpec.Response response) {
        if (response.getContent() == null || response.getContent().isEmpty()) {
            return "{}";
        }
        
        // Get JSON content
        SwaggerSpec.MediaType jsonContent = response.getContent().get("application/json");
        if (jsonContent != null && jsonContent.getSchema() != null) {
            return generateSampleJson(jsonContent.getSchema());
        }
        
        return "{}";
    }

    private String generateSampleJson(SwaggerSpec.Schema schema) {
        if (schema == null) {
            return "{}";
        }
        
        if ("object".equals(schema.getType())) {
            Map<String, Object> sample = new HashMap<>();
            if (schema.getProperties() != null) {
                for (Map.Entry<String, SwaggerSpec.Schema> entry : schema.getProperties().entrySet()) {
                    sample.put(entry.getKey(), generateSampleValue(entry.getValue()));
                }
            }
            return convertToJson(sample);
        } else if ("array".equals(schema.getType())) {
            List<Object> sample = Arrays.asList(generateSampleValue(schema.getItems()));
            return convertToJson(sample);
        } else {
            return convertToJson(generateSampleValue(schema));
        }
    }

    private Object generateSampleValue(SwaggerSpec.Schema schema) {
        if (schema == null) {
            return null;
        }
        
        switch (schema.getType()) {
            case "string":
                if ("email".equals(schema.getFormat())) {
                    return "user@example.com";
                } else if ("uuid".equals(schema.getFormat())) {
                    return "123e4567-e89b-12d3-a456-426614174000";
                } else {
                    return "sample_string";
                }
            case "integer":
                return 42;
            case "number":
                return 3.14;
            case "boolean":
                return true;
            case "object":
                return generateSampleJson(schema);
            case "array":
                return Arrays.asList(generateSampleValue(schema.getItems()));
            default:
                return null;
        }
    }

    private String convertToJson(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
} 