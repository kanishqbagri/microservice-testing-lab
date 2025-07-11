package com.kb.contractgenerator.model;

import java.util.List;
import java.util.Map;

public class ContractTestResponse {
    private boolean success;
    private String message;
    private List<GeneratedTest> generatedTests;
    private Map<String, Object> metadata;
    private List<String> warnings;
    private List<String> errors;
    
    public static class GeneratedTest {
        private String testName;
        private String testType; // consumer, provider
        private String filePath;
        private String content;
        private Map<String, Object> metadata;
        
        // Getters and setters
        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        
        public String getTestType() { return testType; }
        public void setTestType(String testType) { this.testType = testType; }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public List<GeneratedTest> getGeneratedTests() { return generatedTests; }
    public void setGeneratedTests(List<GeneratedTest> generatedTests) { this.generatedTests = generatedTests; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
} 