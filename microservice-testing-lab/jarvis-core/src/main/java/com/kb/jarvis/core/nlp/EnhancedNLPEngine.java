package com.kb.jarvis.core.nlp;

import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Enhanced NLP Engine for Complex Command Processing
 * Handles multi-service, multi-test-type, multi-action commands with advanced pattern recognition
 */
@Component
public class EnhancedNLPEngine {
    
    private static final Logger log = LoggerFactory.getLogger(EnhancedNLPEngine.class);
    
    // Service mapping for common variations
    private static final Map<String, String> SERVICE_MAPPING = new HashMap<>();
    
    // Test type mapping for common variations
    private static final Map<String, TestType> TEST_TYPE_MAPPING = new HashMap<>();
    
    // Action/intent mapping
    private static final Map<String, IntentType> INTENT_MAPPING = new HashMap<>();
    
    // Complex command patterns
    private static final List<Pattern> COMPLEX_PATTERNS = new ArrayList<>();
    
    static {
        initializeMappings();
        initializeComplexPatterns();
    }
    
    /**
     * Parse a complex natural language command
     */
    public ParsedCommand parseCommand(String command) {
        log.info("Parsing complex command: {}", command);
        
        try {
            // 1. Multi-intent detection
            List<IntentType> intents = detectMultipleIntents(command);
            log.debug("Detected intents: {}", intents);
            
            // 2. Multi-service extraction
            List<String> services = extractMultipleServices(command);
            log.debug("Extracted services: {}", services);
            
            // 3. Multi-test-type extraction
            List<TestType> testTypes = extractMultipleTestTypes(command);
            log.debug("Extracted test types: {}", testTypes);
            
            // 4. Parameter extraction
            Map<String, Object> parameters = extractParameters(command);
            log.debug("Extracted parameters: {}", parameters);
            
            // 5. Context analysis
            CommandContext context = analyzeCommandContext(command);
            log.debug("Command context: {}", context);
            
            // 6. Calculate overall confidence
            double confidence = calculateOverallConfidence(intents, services, testTypes, parameters);
            
            ParsedCommand parsedCommand = ParsedCommand.builder()
                .originalCommand(command)
                .intents(intents)
                .services(services)
                .testTypes(testTypes)
                .parameters(parameters)
                .context(context)
                .confidence(confidence)
                .build();
            
            log.info("Command parsing completed with confidence: {}", confidence);
            return parsedCommand;
            
        } catch (Exception e) {
            log.error("Error parsing command: {}", e.getMessage(), e);
            return createErrorParsedCommand(command, e.getMessage());
        }
    }
    
    /**
     * Detect multiple intents in a single command
     */
    private List<IntentType> detectMultipleIntents(String command) {
        List<IntentType> intents = new ArrayList<>();
        String lowerCommand = command.toLowerCase();
        
        // Pattern matching for multiple intents
        if (containsPattern(lowerCommand, "run|execute|start|launch")) {
            intents.add(IntentType.RUN_TESTS);
        }
        if (containsPattern(lowerCommand, "analyze|investigate|debug|examine")) {
            intents.add(IntentType.ANALYZE_FAILURES);
        }
        if (containsPattern(lowerCommand, "generate|create|write|build")) {
            intents.add(IntentType.GENERATE_TESTS);
        }
        if (containsPattern(lowerCommand, "optimize|improve|enhance|tune")) {
            intents.add(IntentType.OPTIMIZE_TESTS);
        }
        if (containsPattern(lowerCommand, "health|status|check|monitor")) {
            intents.add(IntentType.HEALTH_CHECK);
        }
        if (containsPattern(lowerCommand, "status|state|info|details")) {
            intents.add(IntentType.GET_STATUS);
        }
        if (containsPattern(lowerCommand, "help|assist|support|guide")) {
            intents.add(IntentType.HELP);
        }
        
        // If no intents detected, default to RUN_TESTS
        if (intents.isEmpty()) {
            intents.add(IntentType.RUN_TESTS);
        }
        
        return intents;
    }
    
    /**
     * Extract multiple services from command
     */
    private List<String> extractMultipleServices(String command) {
        List<String> services = new ArrayList<>();
        String lowerCommand = command.toLowerCase();
        
        // Extract all mentioned services
        if (containsPattern(lowerCommand, "user|users")) {
            services.add("user-service");
        }
        if (containsPattern(lowerCommand, "product|products")) {
            services.add("product-service");
        }
        if (containsPattern(lowerCommand, "order|orders")) {
            services.add("order-service");
        }
        if (containsPattern(lowerCommand, "notification|notifications")) {
            services.add("notification-service");
        }
        if (containsPattern(lowerCommand, "gateway|api")) {
            services.add("gateway-service");
        }
        
        // Handle "all" or "everything" keywords
        if (containsPattern(lowerCommand, "all|everything|entire|complete")) {
            services.clear();
            services.addAll(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"));
        }
        
        // Handle service combinations
        if (containsPattern(lowerCommand, "user.*order|order.*user")) {
            if (!services.contains("user-service")) services.add("user-service");
            if (!services.contains("order-service")) services.add("order-service");
        }
        if (containsPattern(lowerCommand, "product.*order|order.*product")) {
            if (!services.contains("product-service")) services.add("product-service");
            if (!services.contains("order-service")) services.add("order-service");
        }
        
        // If no services specified, default to all
        if (services.isEmpty()) {
            services.addAll(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"));
        }
        
        return services;
    }
    
    /**
     * Extract multiple test types from command
     */
    private List<TestType> extractMultipleTestTypes(String command) {
        List<TestType> testTypes = new ArrayList<>();
        String lowerCommand = command.toLowerCase();
        
        // Extract all mentioned test types
        if (containsPattern(lowerCommand, "unit|unit test")) {
            testTypes.add(TestType.UNIT_TEST);
        }
        if (containsPattern(lowerCommand, "integration|integration test")) {
            testTypes.add(TestType.INTEGRATION_TEST);
        }
        if (containsPattern(lowerCommand, "api|api test")) {
            testTypes.add(TestType.API_TEST);
        }
        if (containsPattern(lowerCommand, "performance|load|stress|benchmark")) {
            testTypes.add(TestType.PERFORMANCE_TEST);
        }
        if (containsPattern(lowerCommand, "security|penetration|vulnerability")) {
            testTypes.add(TestType.SECURITY_TEST);
        }
        if (containsPattern(lowerCommand, "chaos|resilience|failure")) {
            testTypes.add(TestType.CHAOS_TEST);
        }
        if (containsPattern(lowerCommand, "contract|pact|agreement")) {
            testTypes.add(TestType.CONTRACT_TEST);
        }
        if (containsPattern(lowerCommand, "e2e|end-to-end|end to end|e2e test")) {
            testTypes.add(TestType.END_TO_END_TEST);
        }
        if (containsPattern(lowerCommand, "smoke|basic|quick")) {
            testTypes.add(TestType.SMOKE_TEST);
        }
        if (containsPattern(lowerCommand, "regression|regression test")) {
            testTypes.add(TestType.REGRESSION_TEST);
        }
        if (containsPattern(lowerCommand, "exploratory|ad-hoc|manual")) {
            testTypes.add(TestType.EXPLORATORY_TEST);
        }
        if (containsPattern(lowerCommand, "accessibility|a11y|wcag")) {
            testTypes.add(TestType.ACCESSIBILITY_TEST);
        }
        if (containsPattern(lowerCommand, "compatibility|cross-platform|browser")) {
            testTypes.add(TestType.COMPATIBILITY_TEST);
        }
        if (containsPattern(lowerCommand, "localization|i18n|internationalization")) {
            testTypes.add(TestType.LOCALIZATION_TEST);
        }
        
        // Handle test type combinations
        if (containsPattern(lowerCommand, "unit.*integration|integration.*unit")) {
            if (!testTypes.contains(TestType.UNIT_TEST)) testTypes.add(TestType.UNIT_TEST);
            if (!testTypes.contains(TestType.INTEGRATION_TEST)) testTypes.add(TestType.INTEGRATION_TEST);
        }
        if (containsPattern(lowerCommand, "api.*performance|performance.*api")) {
            if (!testTypes.contains(TestType.API_TEST)) testTypes.add(TestType.API_TEST);
            if (!testTypes.contains(TestType.PERFORMANCE_TEST)) testTypes.add(TestType.PERFORMANCE_TEST);
        }
        
        // If no test types specified, infer from context
        if (testTypes.isEmpty()) {
            testTypes = inferTestTypesFromContext(command);
        }
        
        return testTypes;
    }
    
    /**
     * Extract parameters from command
     */
    private Map<String, Object> extractParameters(String command) {
        Map<String, Object> parameters = new HashMap<>();
        String lowerCommand = command.toLowerCase();
        
        // Extract timeout
        if (containsPattern(lowerCommand, "timeout|time limit")) {
            String timeout = extractTimeout(command);
            if (timeout != null) {
                parameters.put("timeout", timeout);
            }
        }
        
        // Extract retries
        if (containsPattern(lowerCommand, "retry|retries")) {
            int retries = extractRetries(command);
            if (retries > 0) {
                parameters.put("retries", retries);
            }
        }
        
        // Extract parallel execution
        if (containsPattern(lowerCommand, "parallel|concurrent|simultaneous")) {
            parameters.put("parallel", true);
        }
        
        // Extract priority
        if (containsPattern(lowerCommand, "high priority|urgent|critical")) {
            parameters.put("priority", "HIGH");
        } else if (containsPattern(lowerCommand, "low priority|background")) {
            parameters.put("priority", "LOW");
        } else {
            parameters.put("priority", "NORMAL");
        }
        
        // Extract scope
        if (containsPattern(lowerCommand, "full|complete|comprehensive")) {
            parameters.put("scope", "FULL");
        } else if (containsPattern(lowerCommand, "partial|limited|subset")) {
            parameters.put("scope", "PARTIAL");
        } else {
            parameters.put("scope", "DEFAULT");
        }
        
        // Extract environment
        if (containsPattern(lowerCommand, "production|prod")) {
            parameters.put("environment", "PRODUCTION");
        } else if (containsPattern(lowerCommand, "staging|stage")) {
            parameters.put("environment", "STAGING");
        } else if (containsPattern(lowerCommand, "development|dev")) {
            parameters.put("environment", "DEVELOPMENT");
        } else {
            parameters.put("environment", "DEFAULT");
        }
        
        // Extract specific test parameters
        if (containsPattern(lowerCommand, "chaos.*level|chaos.*intensity")) {
            String chaosLevel = extractChaosLevel(command);
            if (chaosLevel != null) {
                parameters.put("chaosLevel", chaosLevel);
            }
        }
        
        if (containsPattern(lowerCommand, "load.*level|load.*intensity")) {
            String loadLevel = extractLoadLevel(command);
            if (loadLevel != null) {
                parameters.put("loadLevel", loadLevel);
            }
        }
        
        return parameters;
    }
    
    /**
     * Analyze command context
     */
    private CommandContext analyzeCommandContext(String command) {
        String lowerCommand = command.toLowerCase();
        
        // Determine urgency
        String urgency = "NORMAL";
        if (containsPattern(lowerCommand, "urgent|asap|immediately|now")) {
            urgency = "HIGH";
        } else if (containsPattern(lowerCommand, "when possible|eventually|later")) {
            urgency = "LOW";
        }
        
        // Determine scope
        String scope = "DEFAULT";
        if (containsPattern(lowerCommand, "all|everything|entire|complete")) {
            scope = "COMPREHENSIVE";
        } else if (containsPattern(lowerCommand, "specific|particular|targeted")) {
            scope = "TARGETED";
        }
        
        // Determine priority
        String priority = "NORMAL";
        if (containsPattern(lowerCommand, "high priority|critical|important")) {
            priority = "HIGH";
        } else if (containsPattern(lowerCommand, "low priority|background|optional")) {
            priority = "LOW";
        }
        
        // Determine timing
        String timing = "IMMEDIATE";
        if (containsPattern(lowerCommand, "schedule|later|tomorrow|next week")) {
            timing = "SCHEDULED";
        }
        
        // Extract constraints
        List<String> constraints = new ArrayList<>();
        if (containsPattern(lowerCommand, "no downtime|zero downtime")) {
            constraints.add("NO_DOWNTIME");
        }
        if (containsPattern(lowerCommand, "minimal impact|low impact")) {
            constraints.add("MINIMAL_IMPACT");
        }
        if (containsPattern(lowerCommand, "safe mode|safety first")) {
            constraints.add("SAFE_MODE");
        }
        
        // Determine execution mode
        String executionMode = "STANDARD";
        if (containsPattern(lowerCommand, "dry run|simulation|test mode")) {
            executionMode = "DRY_RUN";
        } else if (containsPattern(lowerCommand, "production|live")) {
            executionMode = "PRODUCTION";
        }
        
        return CommandContext.builder()
            .urgency(urgency)
            .scope(scope)
            .priority(priority)
            .timing(timing)
            .constraints(constraints)
            .executionMode(executionMode)
            .build();
    }
    
    /**
     * Calculate overall confidence score
     */
    private double calculateOverallConfidence(List<IntentType> intents, List<String> services, 
                                             List<TestType> testTypes, Map<String, Object> parameters) {
        double confidence = 0.0;
        
        // Base confidence from intent detection
        if (!intents.isEmpty()) {
            confidence += 0.3;
        }
        
        // Confidence from service extraction
        if (!services.isEmpty()) {
            confidence += 0.3;
        }
        
        // Confidence from test type extraction
        if (!testTypes.isEmpty()) {
            confidence += 0.3;
        }
        
        // Confidence from parameter extraction
        if (!parameters.isEmpty()) {
            confidence += 0.1;
        }
        
        return Math.min(confidence, 1.0);
    }
    
    /**
     * Infer test types from context when not explicitly mentioned
     */
    private List<TestType> inferTestTypesFromContext(String command) {
        String lowerCommand = command.toLowerCase();
        List<TestType> testTypes = new ArrayList<>();
        
        // Infer based on service mentions
        if (containsPattern(lowerCommand, "user|auth|login")) {
            testTypes.add(TestType.UNIT_TEST);
            testTypes.add(TestType.SECURITY_TEST);
        }
        
        if (containsPattern(lowerCommand, "order|payment|transaction")) {
            testTypes.add(TestType.INTEGRATION_TEST);
            testTypes.add(TestType.API_TEST);
        }
        
        if (containsPattern(lowerCommand, "performance|load|stress")) {
            testTypes.add(TestType.PERFORMANCE_TEST);
        }
        
        if (containsPattern(lowerCommand, "chaos|failure|resilience")) {
            testTypes.add(TestType.CHAOS_TEST);
        }
        
        // Default to unit and integration tests
        if (testTypes.isEmpty()) {
            testTypes.add(TestType.UNIT_TEST);
            testTypes.add(TestType.INTEGRATION_TEST);
        }
        
        return testTypes;
    }
    
    // Helper methods for parameter extraction
    private String extractTimeout(String command) {
        // Extract timeout patterns like "5 minutes", "300s", "1 hour"
        Pattern timeoutPattern = Pattern.compile("(\\d+)\\s*(minutes?|mins?|seconds?|secs?|hours?|hrs?)", Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = timeoutPattern.matcher(command);
        if (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2).toLowerCase();
            
            if (unit.startsWith("hour") || unit.startsWith("hr")) {
                return (value * 60) + "m";
            } else if (unit.startsWith("minute") || unit.startsWith("min")) {
                return value + "m";
            } else if (unit.startsWith("second") || unit.startsWith("sec")) {
                return value + "s";
            }
        }
        return null;
    }
    
    private int extractRetries(String command) {
        // Extract retry patterns like "3 retries", "retry 5 times"
        Pattern retryPattern = Pattern.compile("(\\d+)\\s*retries?|retry\\s*(\\d+)\\s*times?", Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = retryPattern.matcher(command);
        if (matcher.find()) {
            String value1 = matcher.group(1);
            String value2 = matcher.group(2);
            return Integer.parseInt(value1 != null ? value1 : value2);
        }
        return 0;
    }
    
    private String extractChaosLevel(String command) {
        String lowerCommand = command.toLowerCase();
        if (containsPattern(lowerCommand, "low|minimal|light")) {
            return "LOW";
        } else if (containsPattern(lowerCommand, "high|maximum|heavy|intense")) {
            return "HIGH";
        } else {
            return "MEDIUM";
        }
    }
    
    private String extractLoadLevel(String command) {
        String lowerCommand = command.toLowerCase();
        if (containsPattern(lowerCommand, "low|minimal|light")) {
            return "LOW";
        } else if (containsPattern(lowerCommand, "high|maximum|heavy|intense")) {
            return "HIGH";
        } else {
            return "MEDIUM";
        }
    }
    
    private boolean containsPattern(String text, String pattern) {
        return text.matches(".*" + pattern.replace(" ", ".*") + ".*");
    }
    
    private ParsedCommand createErrorParsedCommand(String command, String errorMessage) {
        return ParsedCommand.builder()
            .originalCommand(command)
            .intents(Arrays.asList(IntentType.UNKNOWN))
            .services(new ArrayList<>())
            .testTypes(new ArrayList<>())
            .parameters(Map.of("error", errorMessage))
            .confidence(0.0)
            .build();
    }
    
    /**
     * Initialize service and test type mappings
     */
    private static void initializeMappings() {
        // Service mappings
        SERVICE_MAPPING.put("user", "user-service");
        SERVICE_MAPPING.put("users", "user-service");
        SERVICE_MAPPING.put("auth", "user-service");
        SERVICE_MAPPING.put("authentication", "user-service");
        
        SERVICE_MAPPING.put("product", "product-service");
        SERVICE_MAPPING.put("products", "product-service");
        SERVICE_MAPPING.put("catalog", "product-service");
        SERVICE_MAPPING.put("inventory", "product-service");
        
        SERVICE_MAPPING.put("order", "order-service");
        SERVICE_MAPPING.put("orders", "order-service");
        SERVICE_MAPPING.put("payment", "order-service");
        SERVICE_MAPPING.put("transaction", "order-service");
        
        SERVICE_MAPPING.put("notification", "notification-service");
        SERVICE_MAPPING.put("notifications", "notification-service");
        SERVICE_MAPPING.put("email", "notification-service");
        SERVICE_MAPPING.put("sms", "notification-service");
        
        SERVICE_MAPPING.put("gateway", "gateway-service");
        SERVICE_MAPPING.put("api", "gateway-service");
        SERVICE_MAPPING.put("proxy", "gateway-service");
        SERVICE_MAPPING.put("router", "gateway-service");
        
        // Test type mappings
        TEST_TYPE_MAPPING.put("unit", TestType.UNIT_TEST);
        TEST_TYPE_MAPPING.put("unit test", TestType.UNIT_TEST);
        TEST_TYPE_MAPPING.put("unit tests", TestType.UNIT_TEST);
        
        TEST_TYPE_MAPPING.put("integration", TestType.INTEGRATION_TEST);
        TEST_TYPE_MAPPING.put("integration test", TestType.INTEGRATION_TEST);
        TEST_TYPE_MAPPING.put("integration tests", TestType.INTEGRATION_TEST);
        
        TEST_TYPE_MAPPING.put("api", TestType.API_TEST);
        TEST_TYPE_MAPPING.put("api test", TestType.API_TEST);
        TEST_TYPE_MAPPING.put("api tests", TestType.API_TEST);
        TEST_TYPE_MAPPING.put("rest", TestType.API_TEST);
        
        TEST_TYPE_MAPPING.put("performance", TestType.PERFORMANCE_TEST);
        TEST_TYPE_MAPPING.put("performance test", TestType.PERFORMANCE_TEST);
        TEST_TYPE_MAPPING.put("performance tests", TestType.PERFORMANCE_TEST);
        TEST_TYPE_MAPPING.put("load", TestType.PERFORMANCE_TEST);
        TEST_TYPE_MAPPING.put("load test", TestType.PERFORMANCE_TEST);
        TEST_TYPE_MAPPING.put("stress", TestType.PERFORMANCE_TEST);
        TEST_TYPE_MAPPING.put("stress test", TestType.PERFORMANCE_TEST);
        
        TEST_TYPE_MAPPING.put("security", TestType.SECURITY_TEST);
        TEST_TYPE_MAPPING.put("security test", TestType.SECURITY_TEST);
        TEST_TYPE_MAPPING.put("security tests", TestType.SECURITY_TEST);
        TEST_TYPE_MAPPING.put("penetration", TestType.PENETRATION_TEST);
        TEST_TYPE_MAPPING.put("pen test", TestType.PENETRATION_TEST);
        
        TEST_TYPE_MAPPING.put("chaos", TestType.CHAOS_TEST);
        TEST_TYPE_MAPPING.put("chaos test", TestType.CHAOS_TEST);
        TEST_TYPE_MAPPING.put("chaos tests", TestType.CHAOS_TEST);
        TEST_TYPE_MAPPING.put("resilience", TestType.CHAOS_TEST);
        TEST_TYPE_MAPPING.put("failure", TestType.CHAOS_TEST);
        
        TEST_TYPE_MAPPING.put("contract", TestType.CONTRACT_TEST);
        TEST_TYPE_MAPPING.put("contract test", TestType.CONTRACT_TEST);
        TEST_TYPE_MAPPING.put("contract tests", TestType.CONTRACT_TEST);
        TEST_TYPE_MAPPING.put("pact", TestType.CONTRACT_TEST);
        
        TEST_TYPE_MAPPING.put("e2e", TestType.END_TO_END_TEST);
        TEST_TYPE_MAPPING.put("end to end", TestType.END_TO_END_TEST);
        TEST_TYPE_MAPPING.put("end-to-end", TestType.END_TO_END_TEST);
        TEST_TYPE_MAPPING.put("e2e test", TestType.END_TO_END_TEST);
        
        TEST_TYPE_MAPPING.put("smoke", TestType.SMOKE_TEST);
        TEST_TYPE_MAPPING.put("smoke test", TestType.SMOKE_TEST);
        TEST_TYPE_MAPPING.put("smoke tests", TestType.SMOKE_TEST);
        TEST_TYPE_MAPPING.put("basic", TestType.SMOKE_TEST);
        
        TEST_TYPE_MAPPING.put("regression", TestType.REGRESSION_TEST);
        TEST_TYPE_MAPPING.put("regression test", TestType.REGRESSION_TEST);
        TEST_TYPE_MAPPING.put("regression tests", TestType.REGRESSION_TEST);
        
        TEST_TYPE_MAPPING.put("exploratory", TestType.EXPLORATORY_TEST);
        TEST_TYPE_MAPPING.put("exploratory test", TestType.EXPLORATORY_TEST);
        TEST_TYPE_MAPPING.put("ad-hoc", TestType.EXPLORATORY_TEST);
        TEST_TYPE_MAPPING.put("manual", TestType.EXPLORATORY_TEST);
        
        TEST_TYPE_MAPPING.put("accessibility", TestType.ACCESSIBILITY_TEST);
        TEST_TYPE_MAPPING.put("a11y", TestType.ACCESSIBILITY_TEST);
        TEST_TYPE_MAPPING.put("wcag", TestType.ACCESSIBILITY_TEST);
        
        TEST_TYPE_MAPPING.put("compatibility", TestType.COMPATIBILITY_TEST);
        TEST_TYPE_MAPPING.put("cross-platform", TestType.COMPATIBILITY_TEST);
        TEST_TYPE_MAPPING.put("browser", TestType.COMPATIBILITY_TEST);
        
        TEST_TYPE_MAPPING.put("localization", TestType.LOCALIZATION_TEST);
        TEST_TYPE_MAPPING.put("i18n", TestType.LOCALIZATION_TEST);
        TEST_TYPE_MAPPING.put("internationalization", TestType.LOCALIZATION_TEST);
        
        // Intent mappings
        INTENT_MAPPING.put("run", IntentType.RUN_TESTS);
        INTENT_MAPPING.put("execute", IntentType.RUN_TESTS);
        INTENT_MAPPING.put("start", IntentType.RUN_TESTS);
        INTENT_MAPPING.put("launch", IntentType.RUN_TESTS);
        
        INTENT_MAPPING.put("analyze", IntentType.ANALYZE_FAILURES);
        INTENT_MAPPING.put("investigate", IntentType.ANALYZE_FAILURES);
        INTENT_MAPPING.put("debug", IntentType.ANALYZE_FAILURES);
        INTENT_MAPPING.put("examine", IntentType.ANALYZE_FAILURES);
        
        INTENT_MAPPING.put("generate", IntentType.GENERATE_TESTS);
        INTENT_MAPPING.put("create", IntentType.GENERATE_TESTS);
        INTENT_MAPPING.put("write", IntentType.GENERATE_TESTS);
        INTENT_MAPPING.put("build", IntentType.GENERATE_TESTS);
        
        INTENT_MAPPING.put("optimize", IntentType.OPTIMIZE_TESTS);
        INTENT_MAPPING.put("improve", IntentType.OPTIMIZE_TESTS);
        INTENT_MAPPING.put("enhance", IntentType.OPTIMIZE_TESTS);
        INTENT_MAPPING.put("tune", IntentType.OPTIMIZE_TESTS);
        
        INTENT_MAPPING.put("health", IntentType.HEALTH_CHECK);
        INTENT_MAPPING.put("status", IntentType.HEALTH_CHECK);
        INTENT_MAPPING.put("check", IntentType.HEALTH_CHECK);
        INTENT_MAPPING.put("monitor", IntentType.HEALTH_CHECK);
        
        INTENT_MAPPING.put("help", IntentType.HELP);
        INTENT_MAPPING.put("assist", IntentType.HELP);
        INTENT_MAPPING.put("support", IntentType.HELP);
        INTENT_MAPPING.put("guide", IntentType.HELP);
    }
    
    /**
     * Initialize complex command patterns
     */
    private static void initializeComplexPatterns() {
        // Pattern for multi-service commands
        COMPLEX_PATTERNS.add(Pattern.compile(".*(?:and|&|,).*", Pattern.CASE_INSENSITIVE));
        
        // Pattern for multi-test-type commands
        COMPLEX_PATTERNS.add(Pattern.compile(".*(?:unit.*integration|integration.*unit).*", Pattern.CASE_INSENSITIVE));
        COMPLEX_PATTERNS.add(Pattern.compile(".*(?:api.*performance|performance.*api).*", Pattern.CASE_INSENSITIVE));
        
        // Pattern for conditional commands
        COMPLEX_PATTERNS.add(Pattern.compile(".*(?:if|when|after|before).*", Pattern.CASE_INSENSITIVE));
        
        // Pattern for sequential commands
        COMPLEX_PATTERNS.add(Pattern.compile(".*(?:then|after that|next|followed by).*", Pattern.CASE_INSENSITIVE));
    }
}
