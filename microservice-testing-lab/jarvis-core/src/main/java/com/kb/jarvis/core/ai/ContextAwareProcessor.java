package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.UserIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Context-Aware Processor
 * Enhances intent understanding by analyzing context, relationships, and dependencies
 * 
 * Features:
 * - Service dependency analysis
 * - Test execution context
 * - Historical pattern recognition
 * - Smart defaults and suggestions
 */
@Component
public class ContextAwareProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(ContextAwareProcessor.class);
    
    // Service dependency graph
    private static final Map<String, List<String>> SERVICE_DEPENDENCIES = new HashMap<>();
    
    // Test execution context rules
    private static final Map<String, List<String>> TEST_CONTEXT_RULES = new HashMap<>();
    
    // Historical patterns for learning
    private final Map<String, List<ExecutionPattern>> historicalPatterns = new HashMap<>();
    
    // Current system context
    private SystemContext currentContext = new SystemContext();
    
    static {
        initializeServiceDependencies();
        initializeTestContextRules();
    }
    
    /**
     * Process intent with context awareness
     */
    public ContextualIntent processWithContext(UserIntent intent) {
        log.info("Processing intent with context: {}", intent.getType());
        
        try {
            // Analyze service dependencies
            List<String> affectedServices = analyzeServiceDependencies(intent);
            
            // Determine execution context
            ExecutionContext executionContext = determineExecutionContext(intent);
            
            // Apply context rules
            Map<String, Object> contextualParameters = applyContextRules(intent, executionContext);
            
            // Generate smart suggestions
            List<String> suggestions = generateSuggestions(intent, executionContext);
            
            // Calculate context confidence
            double contextConfidence = calculateContextConfidence(intent, executionContext);
            
            // Create contextual intent
            ContextualIntent contextualIntent = ContextualIntent.builder()
                .originalIntent(intent)
                .affectedServices(affectedServices)
                .executionContext(executionContext)
                .contextualParameters(contextualParameters)
                .suggestions(suggestions)
                .contextConfidence(contextConfidence)
                .build();
            
            log.info("Context processing complete with confidence: {}", contextConfidence);
            return contextualIntent;
            
        } catch (Exception e) {
            log.error("Error in context processing: {}", e.getMessage(), e);
            return createFallbackContextualIntent(intent);
        }
    }
    
    /**
     * Analyze service dependencies for the given intent
     */
    private List<String> analyzeServiceDependencies(UserIntent intent) {
        List<String> services = extractServices(intent);
        Set<String> affectedServices = new HashSet<>(services);
        
        // Add dependent services
        for (String service : services) {
            List<String> dependencies = SERVICE_DEPENDENCIES.get(service);
            if (dependencies != null) {
                affectedServices.addAll(dependencies);
            }
        }
        
        // Add services that depend on the target services
        for (String service : services) {
            for (Map.Entry<String, List<String>> entry : SERVICE_DEPENDENCIES.entrySet()) {
                if (entry.getValue().contains(service)) {
                    affectedServices.add(entry.getKey());
                }
            }
        }
        
        return new ArrayList<>(affectedServices);
    }
    
    /**
     * Determine execution context based on intent and system state
     */
    private ExecutionContext determineExecutionContext(UserIntent intent) {
        ExecutionContext context = new ExecutionContext();
        
        // Determine execution scope
        context.setScope(determineExecutionScope(intent));
        
        // Determine execution strategy
        context.setStrategy(determineExecutionStrategy(intent));
        
        // Determine resource requirements
        context.setResourceRequirements(determineResourceRequirements(intent));
        
        // Determine timing constraints
        context.setTimingConstraints(determineTimingConstraints(intent));
        
        // Determine risk level
        context.setRiskLevel(determineRiskLevel(intent));
        
        return context;
    }
    
    /**
     * Apply context-specific rules to enhance parameters
     */
    private Map<String, Object> applyContextRules(UserIntent intent, ExecutionContext executionContext) {
        Map<String, Object> contextualParameters = new HashMap<>();
        Map<String, Object> originalParameters = intent.getParameters();
        
        if (originalParameters != null) {
            contextualParameters.putAll(originalParameters);
        }
        
        // Apply scope-based rules
        if ("FULL".equals(executionContext.getScope())) {
            contextualParameters.put("timeout", "600s");
            contextualParameters.put("parallel", true);
            contextualParameters.put("retries", 3);
        } else if ("TARGETED".equals(executionContext.getScope())) {
            contextualParameters.put("timeout", "120s");
            contextualParameters.put("parallel", false);
            contextualParameters.put("retries", 2);
        }
        
        // Apply strategy-based rules
        if ("AGGRESSIVE".equals(executionContext.getStrategy())) {
            contextualParameters.put("chaosLevel", "high");
            contextualParameters.put("loadLevel", "high");
            contextualParameters.put("timeout", "300s");
        } else if ("CONSERVATIVE".equals(executionContext.getStrategy())) {
            contextualParameters.put("chaosLevel", "low");
            contextualParameters.put("loadLevel", "low");
            contextualParameters.put("timeout", "60s");
        }
        
        // Apply risk-based rules
        if ("HIGH".equals(executionContext.getRiskLevel())) {
            contextualParameters.put("rollbackEnabled", true);
            contextualParameters.put("monitoringEnabled", true);
            contextualParameters.put("alertingEnabled", true);
        }
        
        // Apply timing-based rules
        if ("IMMEDIATE".equals(executionContext.getTimingConstraints())) {
            contextualParameters.put("priority", "HIGH");
            contextualParameters.put("timeout", "30s");
        } else if ("BACKGROUND".equals(executionContext.getTimingConstraints())) {
            contextualParameters.put("priority", "LOW");
            contextualParameters.put("timeout", "1800s");
        }
        
        return contextualParameters;
    }
    
    /**
     * Generate smart suggestions based on context
     */
    private List<String> generateSuggestions(UserIntent intent, ExecutionContext executionContext) {
        List<String> suggestions = new ArrayList<>();
        
        // Service-specific suggestions
        List<String> services = extractServices(intent);
        for (String service : services) {
            List<String> serviceSuggestions = getServiceSuggestions(service, intent);
            suggestions.addAll(serviceSuggestions);
        }
        
        // Test type-specific suggestions
        List<String> testTypes = extractTestTypes(intent);
        for (String testType : testTypes) {
            List<String> testSuggestions = getTestTypeSuggestions(testType, intent);
            suggestions.addAll(testSuggestions);
        }
        
        // Context-specific suggestions
        if ("HIGH".equals(executionContext.getRiskLevel())) {
            suggestions.add("Consider running in a staging environment first");
            suggestions.add("Enable comprehensive monitoring during execution");
            suggestions.add("Prepare rollback procedures");
        }
        
        if ("FULL".equals(executionContext.getScope())) {
            suggestions.add("This will affect multiple services - ensure all dependencies are healthy");
            suggestions.add("Consider running during low-traffic periods");
        }
        
        // Historical pattern suggestions
        List<String> historicalSuggestions = getHistoricalSuggestions(intent);
        suggestions.addAll(historicalSuggestions);
        
        return suggestions.stream().distinct().collect(Collectors.toList());
    }
    
    /**
     * Calculate context confidence
     */
    private double calculateContextConfidence(UserIntent intent, ExecutionContext executionContext) {
        double confidence = intent.getConfidence();
        
        // Boost confidence for well-defined context
        if (executionContext.getScope() != null) {
            confidence += 0.1;
        }
        
        if (executionContext.getStrategy() != null) {
            confidence += 0.1;
        }
        
        // Boost confidence for low-risk operations
        if ("LOW".equals(executionContext.getRiskLevel())) {
            confidence += 0.1;
        }
        
        // Reduce confidence for high-risk operations without proper context
        if ("HIGH".equals(executionContext.getRiskLevel()) && 
            !"CONSERVATIVE".equals(executionContext.getStrategy())) {
            confidence -= 0.1;
        }
        
        return Math.max(0.0, Math.min(confidence, 1.0));
    }
    
    /**
     * Extract services from intent
     */
    private List<String> extractServices(UserIntent intent) {
        List<String> services = new ArrayList<>();
        Map<String, Object> parameters = intent.getParameters();
        
        if (parameters != null) {
            if (parameters.containsKey("services")) {
                @SuppressWarnings("unchecked")
                List<String> serviceList = (List<String>) parameters.get("services");
                services.addAll(serviceList);
            }
            
            if (parameters.containsKey("serviceName")) {
                String serviceName = (String) parameters.get("serviceName");
                if (!services.contains(serviceName)) {
                    services.add(serviceName);
                }
            }
        }
        
        return services;
    }
    
    /**
     * Extract test types from intent
     */
    private List<String> extractTestTypes(UserIntent intent) {
        List<String> testTypes = new ArrayList<>();
        Map<String, Object> parameters = intent.getParameters();
        
        if (parameters != null && parameters.containsKey("testTypes")) {
            @SuppressWarnings("unchecked")
            List<String> testTypeList = (List<String>) parameters.get("testTypes");
            testTypes.addAll(testTypeList);
        }
        
        return testTypes;
    }
    
    /**
     * Determine execution scope
     */
    private String determineExecutionScope(UserIntent intent) {
        Map<String, Object> parameters = intent.getParameters();
        
        if (parameters != null) {
            if (parameters.containsKey("context")) {
                @SuppressWarnings("unchecked")
                List<String> context = (List<String>) parameters.get("context");
                if (context.contains("scope")) {
                    return "FULL";
                }
            }
            
            List<String> services = extractServices(intent);
            if (services.size() > 2 || services.contains("all-services")) {
                return "FULL";
            }
        }
        
        return "TARGETED";
    }
    
    /**
     * Determine execution strategy
     */
    private String determineExecutionStrategy(UserIntent intent) {
        Map<String, Object> parameters = intent.getParameters();
        
        if (parameters != null) {
            if (parameters.containsKey("context")) {
                @SuppressWarnings("unchecked")
                List<String> context = (List<String>) parameters.get("context");
                if (context.contains("urgency")) {
                    return "AGGRESSIVE";
                }
            }
            
            List<String> testTypes = extractTestTypes(intent);
            if (testTypes.contains("CHAOS_TEST") || testTypes.contains("STRESS_TEST")) {
                return "AGGRESSIVE";
            }
        }
        
        return "BALANCED";
    }
    
    /**
     * Determine resource requirements
     */
    private Map<String, Object> determineResourceRequirements(UserIntent intent) {
        Map<String, Object> requirements = new HashMap<>();
        
        List<String> testTypes = extractTestTypes(intent);
        List<String> services = extractServices(intent);
        
        // CPU requirements
        if (testTypes.contains("PERFORMANCE_TEST") || testTypes.contains("LOAD_TEST")) {
            requirements.put("cpu", "high");
            requirements.put("memory", "high");
        } else {
            requirements.put("cpu", "medium");
            requirements.put("memory", "medium");
        }
        
        // Network requirements
        if (services.size() > 1) {
            requirements.put("network", "high");
        } else {
            requirements.put("network", "medium");
        }
        
        // Storage requirements
        if (testTypes.contains("PERFORMANCE_TEST") || testTypes.contains("CHAOS_TEST")) {
            requirements.put("storage", "high");
        } else {
            requirements.put("storage", "low");
        }
        
        return requirements;
    }
    
    /**
     * Determine timing constraints
     */
    private String determineTimingConstraints(UserIntent intent) {
        Map<String, Object> parameters = intent.getParameters();
        
        if (parameters != null) {
            if (parameters.containsKey("context")) {
                @SuppressWarnings("unchecked")
                List<String> context = (List<String>) parameters.get("context");
                if (context.contains("urgency")) {
                    return "IMMEDIATE";
                }
                if (context.contains("timing")) {
                    return "BACKGROUND";
                }
            }
        }
        
        return "NORMAL";
    }
    
    /**
     * Determine risk level
     */
    private String determineRiskLevel(UserIntent intent) {
        List<String> testTypes = extractTestTypes(intent);
        List<String> services = extractServices(intent);
        
        // High risk indicators
        if (testTypes.contains("CHAOS_TEST") || testTypes.contains("STRESS_TEST")) {
            return "HIGH";
        }
        
        if (services.size() > 3 || services.contains("all-services")) {
            return "HIGH";
        }
        
        // Medium risk indicators
        if (testTypes.contains("PERFORMANCE_TEST") || testTypes.contains("LOAD_TEST")) {
            return "MEDIUM";
        }
        
        if (services.size() > 1) {
            return "MEDIUM";
        }
        
        return "LOW";
    }
    
    /**
     * Get service-specific suggestions
     */
    private List<String> getServiceSuggestions(String service, UserIntent intent) {
        List<String> suggestions = new ArrayList<>();
        
        switch (service) {
            case "user-service":
                suggestions.add("User service is critical - ensure database connectivity");
                suggestions.add("Consider user session impact during testing");
                break;
            case "order-service":
                suggestions.add("Order service handles transactions - ensure data consistency");
                suggestions.add("Consider payment gateway dependencies");
                break;
            case "product-service":
                suggestions.add("Product service is read-heavy - consider cache impact");
                break;
            case "notification-service":
                suggestions.add("Notification service is async - consider message queue health");
                break;
            case "gateway-service":
                suggestions.add("Gateway service is the entry point - test routing logic");
                break;
        }
        
        return suggestions;
    }
    
    /**
     * Get test type-specific suggestions
     */
    private List<String> getTestTypeSuggestions(String testType, UserIntent intent) {
        List<String> suggestions = new ArrayList<>();
        
        switch (testType) {
            case "CHAOS_TEST":
                suggestions.add("Chaos testing can cause service disruption - schedule during low traffic");
                suggestions.add("Ensure monitoring is enabled to track chaos effects");
                break;
            case "PERFORMANCE_TEST":
                suggestions.add("Performance testing requires dedicated resources");
                suggestions.add("Consider baseline performance metrics");
                break;
            case "LOAD_TEST":
                suggestions.add("Load testing may impact other services - coordinate with teams");
                break;
            case "INTEGRATION_TEST":
                suggestions.add("Integration tests require all dependent services to be healthy");
                break;
        }
        
        return suggestions;
    }
    
    /**
     * Get historical pattern suggestions
     */
    private List<String> getHistoricalSuggestions(UserIntent intent) {
        List<String> suggestions = new ArrayList<>();
        
        // This would typically query a database of historical patterns
        // For now, return some general suggestions based on common patterns
        
        List<String> services = extractServices(intent);
        List<String> testTypes = extractTestTypes(intent);
        
        if (services.contains("order-service") && testTypes.contains("CHAOS_TEST")) {
            suggestions.add("Previous chaos tests on order service showed payment gateway sensitivity");
        }
        
        if (services.size() > 2 && testTypes.contains("PERFORMANCE_TEST")) {
            suggestions.add("Multi-service performance tests typically take 30+ minutes");
        }
        
        return suggestions;
    }
    
    /**
     * Create fallback contextual intent
     */
    private ContextualIntent createFallbackContextualIntent(UserIntent intent) {
        return ContextualIntent.builder()
            .originalIntent(intent)
            .affectedServices(extractServices(intent))
            .executionContext(new ExecutionContext())
            .contextualParameters(intent.getParameters() != null ? intent.getParameters() : new HashMap<>())
            .suggestions(Arrays.asList("Unable to process context - proceeding with basic execution"))
            .contextConfidence(0.0)
            .build();
    }
    
    /**
     * Initialize service dependencies
     */
    private static void initializeServiceDependencies() {
        SERVICE_DEPENDENCIES.put("user-service", Arrays.asList("gateway-service"));
        SERVICE_DEPENDENCIES.put("product-service", Arrays.asList("gateway-service"));
        SERVICE_DEPENDENCIES.put("order-service", Arrays.asList("user-service", "product-service", "gateway-service"));
        SERVICE_DEPENDENCIES.put("notification-service", Arrays.asList("order-service", "user-service"));
        SERVICE_DEPENDENCIES.put("gateway-service", new ArrayList<>());
    }
    
    /**
     * Initialize test context rules
     */
    private static void initializeTestContextRules() {
        TEST_CONTEXT_RULES.put("CHAOS_TEST", Arrays.asList("requires-monitoring", "affects-dependencies", "high-risk"));
        TEST_CONTEXT_RULES.put("PERFORMANCE_TEST", Arrays.asList("requires-resources", "affects-performance", "medium-risk"));
        TEST_CONTEXT_RULES.put("INTEGRATION_TEST", Arrays.asList("requires-dependencies", "affects-data", "medium-risk"));
        TEST_CONTEXT_RULES.put("UNIT_TEST", Arrays.asList("isolated", "fast-execution", "low-risk"));
    }
    
    // Inner classes
    
    private static class SystemContext {
        private Map<String, String> serviceHealth = new HashMap<>();
        private Map<String, Object> systemMetrics = new HashMap<>();
        private List<String> activeTests = new ArrayList<>();
        
        // Getters and setters
        public Map<String, String> getServiceHealth() { return serviceHealth; }
        public void setServiceHealth(Map<String, String> serviceHealth) { this.serviceHealth = serviceHealth; }
        
        public Map<String, Object> getSystemMetrics() { return systemMetrics; }
        public void setSystemMetrics(Map<String, Object> systemMetrics) { this.systemMetrics = systemMetrics; }
        
        public List<String> getActiveTests() { return activeTests; }
        public void setActiveTests(List<String> activeTests) { this.activeTests = activeTests; }
    }
    
    private static class ExecutionContext {
        private String scope;
        private String strategy;
        private Map<String, Object> resourceRequirements;
        private String timingConstraints;
        private String riskLevel;
        
        // Getters and setters
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        
        public String getStrategy() { return strategy; }
        public void setStrategy(String strategy) { this.strategy = strategy; }
        
        public Map<String, Object> getResourceRequirements() { return resourceRequirements; }
        public void setResourceRequirements(Map<String, Object> resourceRequirements) { this.resourceRequirements = resourceRequirements; }
        
        public String getTimingConstraints() { return timingConstraints; }
        public void setTimingConstraints(String timingConstraints) { this.timingConstraints = timingConstraints; }
        
        public String getRiskLevel() { return riskLevel; }
        public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    }
    
    private static class ExecutionPattern {
        private String pattern;
        private double successRate;
        private String recommendation;
        
        public ExecutionPattern(String pattern, double successRate, String recommendation) {
            this.pattern = pattern;
            this.successRate = successRate;
            this.recommendation = recommendation;
        }
        
        // Getters
        public String getPattern() { return pattern; }
        public double getSuccessRate() { return successRate; }
        public String getRecommendation() { return recommendation; }
    }
    
    /**
     * Contextual Intent model
     */
    public static class ContextualIntent {
        private UserIntent originalIntent;
        private List<String> affectedServices;
        private ExecutionContext executionContext;
        private Map<String, Object> contextualParameters;
        private List<String> suggestions;
        private double contextConfidence;
        
        public ContextualIntent() {}
        
        public static ContextualIntentBuilder builder() {
            return new ContextualIntentBuilder();
        }
        
        // Getters and setters
        public UserIntent getOriginalIntent() { return originalIntent; }
        public void setOriginalIntent(UserIntent originalIntent) { this.originalIntent = originalIntent; }
        
        public List<String> getAffectedServices() { return affectedServices; }
        public void setAffectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; }
        
        public ExecutionContext getExecutionContext() { return executionContext; }
        public void setExecutionContext(ExecutionContext executionContext) { this.executionContext = executionContext; }
        
        public Map<String, Object> getContextualParameters() { return contextualParameters; }
        public void setContextualParameters(Map<String, Object> contextualParameters) { this.contextualParameters = contextualParameters; }
        
        public List<String> getSuggestions() { return suggestions; }
        public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
        
        public double getContextConfidence() { return contextConfidence; }
        public void setContextConfidence(double contextConfidence) { this.contextConfidence = contextConfidence; }
        
        public static class ContextualIntentBuilder {
            private UserIntent originalIntent;
            private List<String> affectedServices;
            private ExecutionContext executionContext;
            private Map<String, Object> contextualParameters;
            private List<String> suggestions;
            private double contextConfidence;
            
            public ContextualIntentBuilder originalIntent(UserIntent originalIntent) {
                this.originalIntent = originalIntent;
                return this;
            }
            
            public ContextualIntentBuilder affectedServices(List<String> affectedServices) {
                this.affectedServices = affectedServices;
                return this;
            }
            
            public ContextualIntentBuilder executionContext(ExecutionContext executionContext) {
                this.executionContext = executionContext;
                return this;
            }
            
            public ContextualIntentBuilder contextualParameters(Map<String, Object> contextualParameters) {
                this.contextualParameters = contextualParameters;
                return this;
            }
            
            public ContextualIntentBuilder suggestions(List<String> suggestions) {
                this.suggestions = suggestions;
                return this;
            }
            
            public ContextualIntentBuilder contextConfidence(double contextConfidence) {
                this.contextConfidence = contextConfidence;
                return this;
            }
            
            public ContextualIntent build() {
                ContextualIntent intent = new ContextualIntent();
                intent.originalIntent = this.originalIntent;
                intent.affectedServices = this.affectedServices;
                intent.executionContext = this.executionContext;
                intent.contextualParameters = this.contextualParameters;
                intent.suggestions = this.suggestions;
                intent.contextConfidence = this.contextConfidence;
                return intent;
            }
        }
    }
}
