package com.kb.contractgenerator.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class ContractTestRequest {
    @NotBlank
    private String providerName;
    
    @NotBlank
    private String consumerName;
    
    @NotNull
    private SwaggerSpec swaggerSpec;
    
    private String outputDirectory = "generated-contracts";
    private String testFramework = "pact"; // pact, spring-cloud-contract
    private String language = "java"; // java, kotlin, groovy
    private Map<String, Object> generationOptions;
    private List<String> includePaths;
    private List<String> excludePaths;
    private Map<String, String> customHeaders;
    private String baseUrl;
    private Integer port = 8080;
    
    // Getters and setters
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    
    public String getConsumerName() { return consumerName; }
    public void setConsumerName(String consumerName) { this.consumerName = consumerName; }
    
    public SwaggerSpec getSwaggerSpec() { return swaggerSpec; }
    public void setSwaggerSpec(SwaggerSpec swaggerSpec) { this.swaggerSpec = swaggerSpec; }
    
    public String getOutputDirectory() { return outputDirectory; }
    public void setOutputDirectory(String outputDirectory) { this.outputDirectory = outputDirectory; }
    
    public String getTestFramework() { return testFramework; }
    public void setTestFramework(String testFramework) { this.testFramework = testFramework; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public Map<String, Object> getGenerationOptions() { return generationOptions; }
    public void setGenerationOptions(Map<String, Object> generationOptions) { this.generationOptions = generationOptions; }
    
    public List<String> getIncludePaths() { return includePaths; }
    public void setIncludePaths(List<String> includePaths) { this.includePaths = includePaths; }
    
    public List<String> getExcludePaths() { return excludePaths; }
    public void setExcludePaths(List<String> excludePaths) { this.excludePaths = excludePaths; }
    
    public Map<String, String> getCustomHeaders() { return customHeaders; }
    public void setCustomHeaders(Map<String, String> customHeaders) { this.customHeaders = customHeaders; }
    
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
} 