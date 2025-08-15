package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class TestParameters {
    private String serviceName;
    private String testType;
    private List<String> endpoints;
    private Map<String, Object> configuration;
    private TestScope scope;
    private ExecutionStrategy strategy;
} 