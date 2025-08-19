package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TestParameters {
    private String serviceName;
    private String testType;
    private List<String> endpoints;
    private Map<String, Object> configuration;
    private TestScope scope;
    private ExecutionStrategy strategy;

    // Default constructor
    public TestParameters() {}

    // Constructor with all fields
    public TestParameters(String serviceName, String testType, List<String> endpoints, Map<String, Object> configuration,
                         TestScope scope, ExecutionStrategy strategy) {
        this.serviceName = serviceName;
        this.testType = testType;
        this.endpoints = endpoints;
        this.configuration = configuration;
        this.scope = scope;
        this.strategy = strategy;
    }

    // Builder pattern
    public static TestParametersBuilder builder() {
        return new TestParametersBuilder();
    }

    // Getter methods
    public String getServiceName() { return serviceName; }
    public String getTestType() { return testType; }
    public List<String> getEndpoints() { return endpoints; }
    public Map<String, Object> getConfiguration() { return configuration; }
    public TestScope getScope() { return scope; }
    public ExecutionStrategy getStrategy() { return strategy; }

    // Setter methods
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setTestType(String testType) { this.testType = testType; }
    public void setEndpoints(List<String> endpoints) { this.endpoints = endpoints; }
    public void setConfiguration(Map<String, Object> configuration) { this.configuration = configuration; }
    public void setScope(TestScope scope) { this.scope = scope; }
    public void setStrategy(ExecutionStrategy strategy) { this.strategy = strategy; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestParameters that = (TestParameters) o;
        return Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(testType, that.testType) &&
                Objects.equals(endpoints, that.endpoints) &&
                Objects.equals(configuration, that.configuration) &&
                scope == that.scope &&
                strategy == that.strategy;
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(serviceName, testType, endpoints, configuration, scope, strategy);
    }

    // toString method
    @Override
    public String toString() {
        return "TestParameters{" +
                "serviceName='" + serviceName + '\'' +
                ", testType='" + testType + '\'' +
                ", endpoints=" + endpoints +
                ", configuration=" + configuration +
                ", scope=" + scope +
                ", strategy=" + strategy +
                '}';
    }

    // Builder class
    public static class TestParametersBuilder {
        private String serviceName;
        private String testType;
        private List<String> endpoints;
        private Map<String, Object> configuration;
        private TestScope scope;
        private ExecutionStrategy strategy;

        public TestParametersBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public TestParametersBuilder testType(String testType) { this.testType = testType; return this; }
        public TestParametersBuilder endpoints(List<String> endpoints) { this.endpoints = endpoints; return this; }
        public TestParametersBuilder configuration(Map<String, Object> configuration) { this.configuration = configuration; return this; }
        public TestParametersBuilder scope(TestScope scope) { this.scope = scope; return this; }
        public TestParametersBuilder strategy(ExecutionStrategy strategy) { this.strategy = strategy; return this; }

        public TestParameters build() {
            return new TestParameters(serviceName, testType, endpoints, configuration, scope, strategy);
        }
    }
} 