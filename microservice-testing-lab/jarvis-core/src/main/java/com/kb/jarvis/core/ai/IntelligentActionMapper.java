package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.IntentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Intelligent Action Mapper
 * Converts parsed intents into specific executable actions
 * 
 * Example:
 * Input: "Run chaos on Orders"
 * Output: {action: "run-test", testType: "chaos", service: "order-service", parameters: {...}}
 */
@Component
public class IntelligentActionMapper {
    
    private static final Logger log = LoggerFactory.getLogger(IntelligentActionMapper.class);
    
    // Action templates for different intent types
    private static final Map<IntentType, List<ActionTemplate>> ACTION_TEMPLATES = new HashMap<>();
    
    // Service mapping for common variations
    private static final Map<String, String> SERVICE_MAPPING = new HashMap<>();
    
    // Test type mapping for common variations
    private static final Map<String, String> TEST_TYPE_MAPPING = new HashMap<>();
    
    static {
        initializeActionTemplates();
        initializeMappings();
    }
    
    /**
     * Map user intent to executable action
     */
    public ExecutableAction mapToAction(UserIntent intent) {
        log.info("Mapping intent to action: {} with confidence: {}", intent.getType(), intent.getConfidence());
        
        try {
            // Get action templates for this intent type
            List<ActionTemplate> templates = ACTION_TEMPLATES.get(intent.getType());
            if (templates == null || templates.isEmpty()) {
                return createUnknownAction(intent);
            }
            
            // Find best matching template
            ActionTemplate bestTemplate = findBestTemplate(intent, templates);
            
            // Generate action parameters
            Map<String, Object> actionParameters = generateActionParameters(intent, bestTemplate);
            
            // Create executable action
            ExecutableAction action = ExecutableAction.builder()
                .actionType(bestTemplate.getActionType())
                .testType(extractTestType(intent))
                .serviceName(extractServiceName(intent))
                .parameters(actionParameters)
                .confidence(intent.getConfidence())
                .description(generateActionDescription(intent, bestTemplate))
                .estimatedDuration(estimateDuration(intent, bestTemplate))
                .priority(extractPriority(intent))
                .build();
            
            log.info("Mapped to action: {} for service: {} with test type: {}", 
                action.getActionType(), action.getServiceName(), action.getTestType());
            
            return action;
            
        } catch (Exception e) {
            log.error("Error mapping intent to action: {}", e.getMessage(), e);
            return createErrorAction(intent, e.getMessage());
        }
    }
    
    /**
     * Find the best matching action template
     */
    private ActionTemplate findBestTemplate(UserIntent intent, List<ActionTemplate> templates) {
        ActionTemplate bestTemplate = templates.get(0);
        double bestScore = 0.0;
        
        for (ActionTemplate template : templates) {
            double score = calculateTemplateScore(intent, template);
            if (score > bestScore) {
                bestScore = score;
                bestTemplate = template;
            }
        }
        
        return bestTemplate;
    }
    
    /**
     * Calculate template matching score
     */
    private double calculateTemplateScore(UserIntent intent, ActionTemplate template) {
        double score = 0.5; // Base score
        
        // Check if required entities are present
        Map<String, Object> parameters = intent.getParameters();
        if (parameters != null) {
            for (String requiredEntity : template.getRequiredEntities()) {
                if (parameters.containsKey(requiredEntity)) {
                    score += 0.2;
                }
            }
            
            // Boost score for specific test types
            if (template.getPreferredTestTypes() != null && parameters.containsKey("testTypes")) {
                @SuppressWarnings("unchecked")
                List<String> intentTestTypes = (List<String>) parameters.get("testTypes");
                for (String preferredType : template.getPreferredTestTypes()) {
                    if (intentTestTypes.contains(preferredType)) {
                        score += 0.3;
                    }
                }
            }
            
            // Boost score for specific services
            if (template.getPreferredServices() != null && parameters.containsKey("services")) {
                @SuppressWarnings("unchecked")
                List<String> intentServices = (List<String>) parameters.get("services");
                for (String preferredService : template.getPreferredServices()) {
                    if (intentServices.contains(preferredService)) {
                        score += 0.2;
                    }
                }
            }
        }
        
        return Math.min(score, 1.0);
    }
    
    /**
     * Generate action parameters from intent
     */
    private Map<String, Object> generateActionParameters(UserIntent intent, ActionTemplate template) {
        Map<String, Object> parameters = new HashMap<>();
        Map<String, Object> intentParams = intent.getParameters();
        
        if (intentParams != null) {
            // Copy relevant parameters
            for (String key : template.getParameterMappings().keySet()) {
                String intentKey = template.getParameterMappings().get(key);
                if (intentParams.containsKey(intentKey)) {
                    parameters.put(key, intentParams.get(intentKey));
                }
            }
            
            // Add default parameters
            parameters.putAll(template.getDefaultParameters());
            
            // Add context-specific parameters
            if (intentParams.containsKey("context")) {
                @SuppressWarnings("unchecked")
                List<String> context = (List<String>) intentParams.get("context");
                if (context.contains("urgency")) {
                    parameters.put("timeout", "30s");
                    parameters.put("retries", 3);
                }
                if (context.contains("scope")) {
                    parameters.put("scope", "FULL");
                }
            }
            
            // Add priority-based parameters
            String priority = extractPriority(intent);
            if ("HIGH".equals(priority)) {
                parameters.put("timeout", "60s");
                parameters.put("retries", 5);
                parameters.put("parallel", true);
            } else if ("LOW".equals(priority)) {
                parameters.put("timeout", "300s");
                parameters.put("retries", 1);
                parameters.put("parallel", false);
            }
        }
        
        return parameters;
    }
    
    /**
     * Extract test type from intent
     */
    private String extractTestType(UserIntent intent) {
        Map<String, Object> parameters = intent.getParameters();
        if (parameters != null && parameters.containsKey("testTypes")) {
            @SuppressWarnings("unchecked")
            List<String> testTypes = (List<String>) parameters.get("testTypes");
            if (!testTypes.isEmpty()) {
                String testType = testTypes.get(0);
                return TEST_TYPE_MAPPING.getOrDefault(testType, testType);
            }
        }
        
        // Default based on intent type
        switch (intent.getType()) {
            case RUN_TESTS:
                return "INTEGRATION_TEST";
            case ANALYZE_FAILURES:
                return "DIAGNOSTIC_TEST";
            case GENERATE_TESTS:
                return "UNIT_TEST";
            case OPTIMIZE_TESTS:
                return "PERFORMANCE_TEST";
            case HEALTH_CHECK:
                return "HEALTH_CHECK";
            default:
                return "UNKNOWN";
        }
    }
    
    /**
     * Extract service name from intent
     */
    private String extractServiceName(UserIntent intent) {
        Map<String, Object> parameters = intent.getParameters();
        if (parameters != null && parameters.containsKey("services")) {
            @SuppressWarnings("unchecked")
            List<String> services = (List<String>) parameters.get("services");
            if (!services.isEmpty()) {
                String service = services.get(0);
                return SERVICE_MAPPING.getOrDefault(service, service);
            }
        }
        
        // Check for service name in parameters
        if (parameters != null && parameters.containsKey("serviceName")) {
            String serviceName = (String) parameters.get("serviceName");
            return SERVICE_MAPPING.getOrDefault(serviceName, serviceName);
        }
        
        return "all-services"; // Default to all services
    }
    
    /**
     * Extract priority from intent
     */
    private String extractPriority(UserIntent intent) {
        Map<String, Object> parameters = intent.getParameters();
        if (parameters != null) {
            if (parameters.containsKey("priority")) {
                return (String) parameters.get("priority");
            }
            
            if (parameters.containsKey("context")) {
                @SuppressWarnings("unchecked")
                List<String> context = (List<String>) parameters.get("context");
                if (context.contains("urgency")) {
                    return "HIGH";
                }
                if (context.contains("priority")) {
                    return "MEDIUM";
                }
            }
        }
        
        return "NORMAL";
    }
    
    /**
     * Generate action description
     */
    private String generateActionDescription(UserIntent intent, ActionTemplate template) {
        StringBuilder description = new StringBuilder();
        
        description.append(template.getActionType().toLowerCase().replace("_", " "));
        
        String testType = extractTestType(intent);
        if (!"UNKNOWN".equals(testType)) {
            description.append(" ").append(testType.toLowerCase().replace("_", " "));
        }
        
        String serviceName = extractServiceName(intent);
        if (!"all-services".equals(serviceName)) {
            description.append(" for ").append(serviceName);
        }
        
        return description.toString();
    }
    
    /**
     * Estimate action duration
     */
    private String estimateDuration(UserIntent intent, ActionTemplate template) {
        String testType = extractTestType(intent);
        String serviceName = extractServiceName(intent);
        
        // Base duration from template
        int baseMinutes = template.getEstimatedMinutes();
        
        // Adjust based on test type
        switch (testType) {
            case "UNIT_TEST":
                baseMinutes = Math.min(baseMinutes, 5);
                break;
            case "INTEGRATION_TEST":
                baseMinutes = Math.max(baseMinutes, 10);
                break;
            case "PERFORMANCE_TEST":
                baseMinutes = Math.max(baseMinutes, 30);
                break;
            case "CHAOS_TEST":
                baseMinutes = Math.max(baseMinutes, 15);
                break;
            case "END_TO_END_TEST":
                baseMinutes = Math.max(baseMinutes, 20);
                break;
        }
        
        // Adjust based on service scope
        if ("all-services".equals(serviceName)) {
            baseMinutes *= 3;
        }
        
        return baseMinutes + " minutes";
    }
    
    /**
     * Create unknown action for unrecognized intents
     */
    private ExecutableAction createUnknownAction(UserIntent intent) {
        return ExecutableAction.builder()
            .actionType("UNKNOWN")
            .testType("UNKNOWN")
            .serviceName("unknown")
            .parameters(new HashMap<>())
            .confidence(0.0)
            .description("Unknown action - unable to map intent")
            .estimatedDuration("unknown")
            .priority("NORMAL")
            .build();
    }
    
    /**
     * Create error action for failed mappings
     */
    private ExecutableAction createErrorAction(UserIntent intent, String errorMessage) {
        return ExecutableAction.builder()
            .actionType("ERROR")
            .testType("UNKNOWN")
            .serviceName("unknown")
            .parameters(Map.of("error", errorMessage))
            .confidence(0.0)
            .description("Error mapping intent: " + errorMessage)
            .estimatedDuration("unknown")
            .priority("NORMAL")
            .build();
    }
    
    /**
     * Initialize action templates
     */
    private static void initializeActionTemplates() {
        // RUN_TESTS templates
        ACTION_TEMPLATES.put(IntentType.RUN_TESTS, Arrays.asList(
            new ActionTemplate(
                "RUN_TEST",
                Arrays.asList("services", "testTypes"),
                Arrays.asList("UNIT_TEST", "INTEGRATION_TEST", "API_TEST"),
                null,
                Map.of("testType", "testTypes", "serviceName", "services"),
                Map.of("timeout", "120s", "retries", 2, "parallel", false),
                10
            ),
            new ActionTemplate(
                "RUN_CHAOS_TEST",
                Arrays.asList("services"),
                Arrays.asList("CHAOS_TEST"),
                null,
                Map.of("testType", "testTypes", "serviceName", "services"),
                Map.of("timeout", "300s", "retries", 1, "chaosLevel", "medium"),
                15
            ),
            new ActionTemplate(
                "RUN_PERFORMANCE_TEST",
                Arrays.asList("services"),
                Arrays.asList("PERFORMANCE_TEST", "LOAD_TEST", "STRESS_TEST"),
                null,
                Map.of("testType", "testTypes", "serviceName", "services"),
                Map.of("timeout", "600s", "retries", 1, "loadLevel", "medium"),
                30
            )
        ));
        
        // ANALYZE_FAILURES templates
        ACTION_TEMPLATES.put(IntentType.ANALYZE_FAILURES, Arrays.asList(
            new ActionTemplate(
                "ANALYZE_FAILURES",
                Arrays.asList("services"),
                null,
                null,
                Map.of("serviceName", "services"),
                Map.of("timeout", "60s", "retries", 1, "analysisDepth", "detailed"),
                5
            )
        ));
        
        // GENERATE_TESTS templates
        ACTION_TEMPLATES.put(IntentType.GENERATE_TESTS, Arrays.asList(
            new ActionTemplate(
                "GENERATE_TESTS",
                Arrays.asList("services"),
                Arrays.asList("UNIT_TEST", "INTEGRATION_TEST"),
                null,
                Map.of("serviceName", "services", "testType", "testTypes"),
                Map.of("timeout", "300s", "retries", 1, "generationType", "comprehensive"),
                20
            )
        ));
        
        // OPTIMIZE_TESTS templates
        ACTION_TEMPLATES.put(IntentType.OPTIMIZE_TESTS, Arrays.asList(
            new ActionTemplate(
                "OPTIMIZE_TESTS",
                Arrays.asList("services"),
                null,
                null,
                Map.of("serviceName", "services"),
                Map.of("timeout", "180s", "retries", 1, "optimizationLevel", "aggressive"),
                15
            )
        ));
        
        // HEALTH_CHECK templates
        ACTION_TEMPLATES.put(IntentType.HEALTH_CHECK, Arrays.asList(
            new ActionTemplate(
                "HEALTH_CHECK",
                Arrays.asList("services"),
                Arrays.asList("HEALTH_CHECK"),
                null,
                Map.of("serviceName", "services"),
                Map.of("timeout", "30s", "retries", 3, "checkType", "comprehensive"),
                2
            )
        ));
    }
    
    /**
     * Initialize service and test type mappings
     */
    private static void initializeMappings() {
        // Service mappings
        SERVICE_MAPPING.put("user", "user-service");
        SERVICE_MAPPING.put("users", "user-service");
        SERVICE_MAPPING.put("product", "product-service");
        SERVICE_MAPPING.put("products", "product-service");
        SERVICE_MAPPING.put("order", "order-service");
        SERVICE_MAPPING.put("orders", "order-service");
        SERVICE_MAPPING.put("notification", "notification-service");
        SERVICE_MAPPING.put("notifications", "notification-service");
        SERVICE_MAPPING.put("gateway", "gateway-service");
        SERVICE_MAPPING.put("api", "gateway-service");
        
        // Test type mappings
        TEST_TYPE_MAPPING.put("chaos", "CHAOS_TEST");
        TEST_TYPE_MAPPING.put("chaos test", "CHAOS_TEST");
        TEST_TYPE_MAPPING.put("unit", "UNIT_TEST");
        TEST_TYPE_MAPPING.put("unit test", "UNIT_TEST");
        TEST_TYPE_MAPPING.put("integration", "INTEGRATION_TEST");
        TEST_TYPE_MAPPING.put("integration test", "INTEGRATION_TEST");
        TEST_TYPE_MAPPING.put("api", "API_TEST");
        TEST_TYPE_MAPPING.put("api test", "API_TEST");
        TEST_TYPE_MAPPING.put("performance", "PERFORMANCE_TEST");
        TEST_TYPE_MAPPING.put("performance test", "PERFORMANCE_TEST");
        TEST_TYPE_MAPPING.put("load", "LOAD_TEST");
        TEST_TYPE_MAPPING.put("load test", "LOAD_TEST");
        TEST_TYPE_MAPPING.put("stress", "STRESS_TEST");
        TEST_TYPE_MAPPING.put("stress test", "STRESS_TEST");
        TEST_TYPE_MAPPING.put("security", "SECURITY_TEST");
        TEST_TYPE_MAPPING.put("security test", "SECURITY_TEST");
        TEST_TYPE_MAPPING.put("contract", "CONTRACT_TEST");
        TEST_TYPE_MAPPING.put("contract test", "CONTRACT_TEST");
        TEST_TYPE_MAPPING.put("smoke", "SMOKE_TEST");
        TEST_TYPE_MAPPING.put("smoke test", "SMOKE_TEST");
        TEST_TYPE_MAPPING.put("regression", "REGRESSION_TEST");
        TEST_TYPE_MAPPING.put("regression test", "REGRESSION_TEST");
        TEST_TYPE_MAPPING.put("e2e", "END_TO_END_TEST");
        TEST_TYPE_MAPPING.put("end to end", "END_TO_END_TEST");
        TEST_TYPE_MAPPING.put("end-to-end", "END_TO_END_TEST");
    }
    
    // Inner classes
    
    private static class ActionTemplate {
        private final String actionType;
        private final List<String> requiredEntities;
        private final List<String> preferredTestTypes;
        private final List<String> preferredServices;
        private final Map<String, String> parameterMappings;
        private final Map<String, Object> defaultParameters;
        private final int estimatedMinutes;
        
        public ActionTemplate(String actionType, List<String> requiredEntities, 
                            List<String> preferredTestTypes, List<String> preferredServices,
                            Map<String, String> parameterMappings, Map<String, Object> defaultParameters,
                            int estimatedMinutes) {
            this.actionType = actionType;
            this.requiredEntities = requiredEntities;
            this.preferredTestTypes = preferredTestTypes;
            this.preferredServices = preferredServices;
            this.parameterMappings = parameterMappings;
            this.defaultParameters = defaultParameters;
            this.estimatedMinutes = estimatedMinutes;
        }
        
        public String getActionType() { return actionType; }
        public List<String> getRequiredEntities() { return requiredEntities; }
        public List<String> getPreferredTestTypes() { return preferredTestTypes; }
        public List<String> getPreferredServices() { return preferredServices; }
        public Map<String, String> getParameterMappings() { return parameterMappings; }
        public Map<String, Object> getDefaultParameters() { return defaultParameters; }
        public int getEstimatedMinutes() { return estimatedMinutes; }
    }
    
    /**
     * Executable Action model
     */
    public static class ExecutableAction {
        private String actionType;
        private String testType;
        private String serviceName;
        private Map<String, Object> parameters;
        private double confidence;
        private String description;
        private String estimatedDuration;
        private String priority;
        
        public ExecutableAction() {}
        
        public static ExecutableActionBuilder builder() {
            return new ExecutableActionBuilder();
        }
        
        // Getters and setters
        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }
        
        public String getTestType() { return testType; }
        public void setTestType(String testType) { this.testType = testType; }
        
        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        
        public double getConfidence() { return confidence; }
        public void setConfidence(double confidence) { this.confidence = confidence; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getEstimatedDuration() { return estimatedDuration; }
        public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public static class ExecutableActionBuilder {
            private String actionType;
            private String testType;
            private String serviceName;
            private Map<String, Object> parameters;
            private double confidence;
            private String description;
            private String estimatedDuration;
            private String priority;
            
            public ExecutableActionBuilder actionType(String actionType) {
                this.actionType = actionType;
                return this;
            }
            
            public ExecutableActionBuilder testType(String testType) {
                this.testType = testType;
                return this;
            }
            
            public ExecutableActionBuilder serviceName(String serviceName) {
                this.serviceName = serviceName;
                return this;
            }
            
            public ExecutableActionBuilder parameters(Map<String, Object> parameters) {
                this.parameters = parameters;
                return this;
            }
            
            public ExecutableActionBuilder confidence(double confidence) {
                this.confidence = confidence;
                return this;
            }
            
            public ExecutableActionBuilder description(String description) {
                this.description = description;
                return this;
            }
            
            public ExecutableActionBuilder estimatedDuration(String estimatedDuration) {
                this.estimatedDuration = estimatedDuration;
                return this;
            }
            
            public ExecutableActionBuilder priority(String priority) {
                this.priority = priority;
                return this;
            }
            
            public ExecutableAction build() {
                ExecutableAction action = new ExecutableAction();
                action.actionType = this.actionType;
                action.testType = this.testType;
                action.serviceName = this.serviceName;
                action.parameters = this.parameters;
                action.confidence = this.confidence;
                action.description = this.description;
                action.estimatedDuration = this.estimatedDuration;
                action.priority = this.priority;
                return action;
            }
        }
    }
}
