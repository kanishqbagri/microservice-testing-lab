package com.kb.jarvis.core.nlp;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.IntentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class NLPEngine {
    
    private static final Logger log = LoggerFactory.getLogger(NLPEngine.class);
    
    @Value("${jarvis.nlp.confidence-threshold:0.7}")
    private double confidenceThreshold;
    
    // Intent patterns for test-related commands
    private static final Map<IntentType, List<Pattern>> INTENT_PATTERNS = new HashMap<>();
    private static final Map<String, String> ENTITY_SYNONYMS = new HashMap<>();
    
    static {
        // Initialize intent patterns
        initializeIntentPatterns();
        initializeEntitySynonyms();
    }
    
    public UserIntent parseIntent(String userInput) {
        log.info("Parsing user intent: {}", userInput);
        
        try {
            // Normalize input
            String normalizedInput = normalizeInput(userInput);
            
            // Extract intent type
            IntentType intentType = classifyIntent(normalizedInput);
            
            // Extract entities
            Map<String, Object> entities = extractEntities(normalizedInput, intentType);
            
            // Generate description
            String description = generateDescription(intentType, entities);
            
            // Calculate confidence
            double confidence = calculateConfidence(normalizedInput, intentType, entities);
            
            UserIntent intent = UserIntent.builder()
                .rawInput(userInput)
                .type(intentType)
                .description(description)
                .parameters(entities)
                .confidence(confidence)
                .timestamp(LocalDateTime.now())
                .build();
            
            log.info("Parsed intent: {} with confidence: {}", intentType, confidence);
            return intent;
            
        } catch (Exception e) {
            log.error("Error parsing user intent: {}", e.getMessage(), e);
            return createFallbackIntent(userInput);
        }
    }
    
    private String normalizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.toLowerCase()
            .replaceAll("\\s+", " ")
            .trim();
    }
    
    private IntentType classifyIntent(String normalizedInput) {
        Map<IntentType, Double> scores = new HashMap<>();
        
        // Score each intent type based on pattern matching
        for (Map.Entry<IntentType, List<Pattern>> entry : INTENT_PATTERNS.entrySet()) {
            IntentType intentType = entry.getKey();
            List<Pattern> patterns = entry.getValue();
            
            double score = 0.0;
            for (Pattern pattern : patterns) {
                if (pattern.matcher(normalizedInput).find()) {
                    score += 1.0;
                }
            }
            
            if (score > 0) {
                scores.put(intentType, score / patterns.size());
            }
        }
        
        // Return the intent with highest score, or UNKNOWN if no match
        return scores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(IntentType.UNKNOWN);
    }
    
    private Map<String, Object> extractEntities(String normalizedInput, IntentType intentType) {
        Map<String, Object> entities = new HashMap<>();
        
        // Extract service names
        List<String> services = extractServiceNames(normalizedInput);
        if (!services.isEmpty()) {
            entities.put("services", services);
        }
        
        // Extract test types
        List<String> testTypes = extractTestTypes(normalizedInput);
        if (!testTypes.isEmpty()) {
            entities.put("testTypes", testTypes);
        }
        
        // Extract test scopes
        String scope = extractTestScope(normalizedInput);
        if (scope != null) {
            entities.put("scope", scope);
        }
        
        // Extract parameters
        Map<String, String> parameters = extractParameters(normalizedInput);
        if (!parameters.isEmpty()) {
            entities.put("parameters", parameters);
        }
        
        // Extract time constraints
        String timeConstraint = extractTimeConstraint(normalizedInput);
        if (timeConstraint != null) {
            entities.put("timeConstraint", timeConstraint);
        }
        
        // Extract priority
        String priority = extractPriority(normalizedInput);
        if (priority != null) {
            entities.put("priority", priority);
        }
        
        return entities;
    }
    
    private List<String> extractServiceNames(String input) {
        List<String> services = new ArrayList<>();
        
        // Common service name patterns
        String[] servicePatterns = {
            "user-service", "product-service", "order-service", "notification-service", "gateway-service",
            "user service", "product service", "order service", "notification service", "gateway service",
            "userservice", "productservice", "orderservice", "notificationservice", "gatewayservice"
        };
        
        for (String pattern : servicePatterns) {
            if (input.contains(pattern)) {
                String normalizedService = normalizeServiceName(pattern);
                if (!services.contains(normalizedService)) {
                    services.add(normalizedService);
                }
            }
        }
        
        // Check for synonyms only if no exact service name was found
        if (services.isEmpty()) {
            for (Map.Entry<String, String> synonym : ENTITY_SYNONYMS.entrySet()) {
                if (input.contains(synonym.getKey())) {
                    String normalizedService = synonym.getValue();
                    if (!services.contains(normalizedService)) {
                        services.add(normalizedService);
                    }
                }
            }
        }
        
        return services;
    }
    
    private List<String> extractTestTypes(String input) {
        List<String> testTypes = new ArrayList<>();
        
        // Test type patterns
        String[] typePatterns = {
            "unit test", "integration test", "api test", "performance test", "security test",
            "contract test", "chaos test", "smoke test", "regression test", "end-to-end test",
            "e2e test", "load test", "stress test", "penetration test", "accessibility test"
        };
        
        for (String pattern : typePatterns) {
            if (input.contains(pattern)) {
                testTypes.add(normalizeTestType(pattern));
            }
        }
        
        // Java annotation-style test types
        String[] annotationPatterns = {
            "@test", "@unittest", "@integrationtest", "@apitest", "@performancetest",
            "@securitytest", "@contracttest", "@chaostest", "@smoketest", "@regressiontest",
            "@e2etest", "@loadtest", "@stresstest", "@penetrationtest", "@accessibilitytest",
            "@ui test", "@uitest", "@componenttest", "@component test", "@servicetest",
            "@service test", "@repositorytest", "@repository test", "@controllertest",
            "@controller test", "@resttest", "@rest test", "@webclienttest", "@webclient test"
        };
        
        for (String pattern : annotationPatterns) {
            if (input.toLowerCase().contains(pattern)) {
                String normalizedType = normalizeAnnotationTestType(pattern);
                if (!testTypes.contains(normalizedType)) {
                    testTypes.add(normalizedType);
                }
            }
        }
        
        return testTypes;
    }
    
    private String extractTestScope(String input) {
        // Java annotation-style priorities (higher priority)
        if (input.toLowerCase().contains("p1") || input.toLowerCase().contains("priority 1")) {
            return "P1";
        } else if (input.toLowerCase().contains("p2") || input.toLowerCase().contains("priority 2")) {
            return "P2";
        } else if (input.toLowerCase().contains("p3") || input.toLowerCase().contains("priority 3")) {
            return "P3";
        } else if (input.toLowerCase().contains("p0") || input.toLowerCase().contains("priority 0")) {
            return "P0";
        }
        
        // Other scope patterns (lower priority)
        if (input.contains("all") || input.contains("everything")) {
            return "ALL";
        } else if (input.contains("critical") || input.contains("important")) {
            return "CRITICAL";
        } else if (input.contains("recent") || input.contains("latest")) {
            return "RECENT";
        } else if (input.contains("failed") || input.contains("broken")) {
            return "FAILED";
        }
        
        return null;
    }
    
    private Map<String, String> extractParameters(String input) {
        Map<String, String> parameters = new HashMap<>();
        
        // Extract timeout values
        Pattern timeoutPattern = Pattern.compile("(\\d+)\\s*(seconds?|minutes?|hours?)");
        var timeoutMatcher = timeoutPattern.matcher(input);
        if (timeoutMatcher.find()) {
            parameters.put("timeout", timeoutMatcher.group(1) + " " + timeoutMatcher.group(2));
        }
        
        // Extract retry count
        Pattern retryPattern = Pattern.compile("(\\d+)\\s*retries?");
        var retryMatcher = retryPattern.matcher(input);
        if (retryMatcher.find()) {
            parameters.put("retries", retryMatcher.group(1));
        }
        
        // Extract parallel execution
        if (input.contains("parallel") || input.contains("concurrent")) {
            parameters.put("parallel", "true");
        }
        
        return parameters;
    }
    
    private String extractTimeConstraint(String input) {
        if (input.contains("now") || input.contains("immediately")) {
            return "IMMEDIATE";
        } else if (input.contains("later") || input.contains("background")) {
            return "BACKGROUND";
        } else if (input.contains("scheduled") || input.contains("at")) {
            return "SCHEDULED";
        }
        return null;
    }
    
    private String extractPriority(String input) {
        if (input.contains("high") || input.contains("urgent") || input.contains("critical")) {
            return "HIGH";
        } else if (input.contains("low") || input.contains("background")) {
            return "LOW";
        } else if (input.contains("medium") || input.contains("normal")) {
            return "MEDIUM";
        }
        
        // Java annotation-style priorities
        if (input.toLowerCase().contains("p1") || input.toLowerCase().contains("priority 1")) {
            return "P1";
        } else if (input.toLowerCase().contains("p2") || input.toLowerCase().contains("priority 2")) {
            return "P2";
        } else if (input.toLowerCase().contains("p3") || input.toLowerCase().contains("priority 3")) {
            return "P3";
        } else if (input.toLowerCase().contains("p0") || input.toLowerCase().contains("priority 0")) {
            return "P0";
        }
        
        return "NORMAL";
    }
    
    private String generateDescription(IntentType intentType, Map<String, Object> entities) {
        StringBuilder description = new StringBuilder();
        
        switch (intentType) {
            case RUN_TESTS:
                description.append("run tests");
                if (entities.containsKey("services")) {
                    description.append(" for ").append(entities.get("services"));
                }
                if (entities.containsKey("testTypes")) {
                    description.append(" of type ").append(entities.get("testTypes"));
                }
                break;
                
            case ANALYZE_FAILURES:
                description.append("analyze test failures");
                if (entities.containsKey("services")) {
                    description.append(" for ").append(entities.get("services"));
                }
                break;
                
            case GENERATE_TESTS:
                description.append("generate new tests");
                if (entities.containsKey("services")) {
                    description.append(" for ").append(entities.get("services"));
                }
                break;
                
            case OPTIMIZE_TESTS:
                description.append("optimize test suite");
                if (entities.containsKey("services")) {
                    description.append(" for ").append(entities.get("services"));
                }
                break;
                
            case HEALTH_CHECK:
                description.append("perform system health check");
                break;
                
            default:
                description.append("Unable to parse intent from input");
                break;
        }
        
        return description.toString();
    }
    
    private double calculateConfidence(String input, IntentType intentType, Map<String, Object> entities) {
        // For unknown intents, return 0.0
        if (intentType == IntentType.UNKNOWN) {
            return 0.0;
        }
        
        double confidence = 0.5; // Base confidence
        
        // Boost confidence based on intent type match
        confidence += 0.3;
        
        // Boost confidence based on entity extraction
        if (!entities.isEmpty()) {
            confidence += 0.2;
        }
        
        // Boost confidence for specific patterns
        if (input != null && (input.contains("test") || input.contains("run") || input.contains("execute"))) {
            confidence += 0.1;
        }
        
        return Math.min(confidence, 1.0);
    }
    
    private UserIntent createFallbackIntent(String userInput) {
        return UserIntent.builder()
            .rawInput(userInput != null ? userInput : "")
            .type(IntentType.UNKNOWN)
            .description("Unable to parse intent from input")
            .confidence(0.0)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    private String normalizeServiceName(String serviceName) {
        return serviceName.replaceAll("\\s+", "-").toLowerCase();
    }
    
    private String normalizeTestType(String testType) {
        return testType.replaceAll("\\s+", "_").toUpperCase();
    }
    
    private static void initializeIntentPatterns() {
        // RUN_TESTS patterns
        INTENT_PATTERNS.put(IntentType.RUN_TESTS, Arrays.asList(
            Pattern.compile("\\b(run|execute|start|launch)\\b.*\\btests?\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\btests?\\b.*\\b(run|execute|start|launch)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(test|testing)\\b.*\\b(now|immediately|please)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(run|execute|start|launch)\\b.*@\\w+", Pattern.CASE_INSENSITIVE),
            Pattern.compile("@\\w+.*\\b(run|execute|start|launch)\\b", Pattern.CASE_INSENSITIVE)
        ));
        
        // ANALYZE_FAILURES patterns
        INTENT_PATTERNS.put(IntentType.ANALYZE_FAILURES, Arrays.asList(
            Pattern.compile("\\b(analyze|investigate|examine)\\b.*\\b(failures?|errors?|issues?)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(failures?|errors?|issues?)\\b.*\\b(analyze|investigate|examine)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(why|what|how)\\b.*\\b(failed|broke|error)\\b", Pattern.CASE_INSENSITIVE)
        ));
        
        // GENERATE_TESTS patterns
        INTENT_PATTERNS.put(IntentType.GENERATE_TESTS, Arrays.asList(
            Pattern.compile("\\b(generate|create|make)\\b.*\\btests?\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(tests?)\\b.*\\b(generate|create|make)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(new|additional)\\b.*\\btests?\\b", Pattern.CASE_INSENSITIVE)
        ));
        
        // OPTIMIZE_TESTS patterns
        INTENT_PATTERNS.put(IntentType.OPTIMIZE_TESTS, Arrays.asList(
            Pattern.compile("\\b(optimize|improve|enhance)\\b.*\\btests?\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(tests?)\\b.*\\b(optimize|improve|enhance)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(faster|better|efficient)\\b.*\\btests?\\b", Pattern.CASE_INSENSITIVE)
        ));
        
        // HEALTH_CHECK patterns
        INTENT_PATTERNS.put(IntentType.HEALTH_CHECK, Arrays.asList(
            Pattern.compile("\\b(health|status)\\b.*\\b(check|monitor|verify)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(check|monitor|verify)\\b.*\\b(health|status)\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(system|service)\\b.*\\b(health|status|alive)\\b", Pattern.CASE_INSENSITIVE)
        ));
    }
    
    private static void initializeEntitySynonyms() {
        ENTITY_SYNONYMS.put("user", "user-service");
        ENTITY_SYNONYMS.put("users", "user-service");
        ENTITY_SYNONYMS.put("product", "product-service");
        ENTITY_SYNONYMS.put("products", "product-service");
        ENTITY_SYNONYMS.put("order", "order-service");
        ENTITY_SYNONYMS.put("orders", "order-service");
        ENTITY_SYNONYMS.put("notification", "notification-service");
        ENTITY_SYNONYMS.put("notifications", "notification-service");
        ENTITY_SYNONYMS.put("gateway", "gateway-service");
        ENTITY_SYNONYMS.put("api", "gateway-service");
    }

    private String normalizeAnnotationTestType(String annotation) {
        String cleanAnnotation = annotation.replace("@", "").toLowerCase();
        
        switch (cleanAnnotation) {
            case "test":
                return "UNIT_TEST";
            case "unittest":
                return "UNIT_TEST";
            case "integrationtest":
                return "INTEGRATION_TEST";
            case "apitest":
                return "API_TEST";
            case "performancetest":
                return "PERFORMANCE_TEST";
            case "securitytest":
                return "SECURITY_TEST";
            case "contracttest":
                return "CONTRACT_TEST";
            case "chaostest":
                return "CHAOS_TEST";
            case "smoketest":
                return "SMOKE_TEST";
            case "regressiontest":
                return "REGRESSION_TEST";
            case "e2etest":
                return "END_TO_END_TEST";
            case "loadtest":
                return "LOAD_TEST";
            case "stresstest":
                return "STRESS_TEST";
            case "penetrationtest":
                return "PENETRATION_TEST";
            case "accessibilitytest":
                return "ACCESSIBILITY_TEST";
            case "ui test":
            case "uitest":
                return "UI_TEST";
            case "componenttest":
            case "component test":
                return "COMPONENT_TEST";
            case "servicetest":
            case "service test":
                return "SERVICE_TEST";
            case "repositorytest":
            case "repository test":
                return "REPOSITORY_TEST";
            case "controllertest":
            case "controller test":
                return "CONTROLLER_TEST";
            case "resttest":
            case "rest test":
                return "REST_TEST";
            case "webclienttest":
            case "webclient test":
                return "WEBCLIENT_TEST";
            default:
                return "CUSTOM_TEST";
        }
    }
} 