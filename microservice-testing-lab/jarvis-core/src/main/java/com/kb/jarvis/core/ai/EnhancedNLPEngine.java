package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.IntentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Enhanced NLP Engine with spaCy-like capabilities
 * Features:
 * - Advanced intent classification with fuzzy matching
 * - Named Entity Recognition (NER) for services, test types, actions
 * - Context-aware processing
 * - Comprehensive lookup dictionaries
 * - Confidence scoring with multiple factors
 */
@Component
public class EnhancedNLPEngine {
    
    private static final Logger log = LoggerFactory.getLogger(EnhancedNLPEngine.class);
    
    @Value("${jarvis.nlp.confidence-threshold:0.7}")
    private double confidenceThreshold;
    
    @Value("${jarvis.nlp.fuzzy-threshold:0.8}")
    private double fuzzyThreshold;
    
    // Enhanced lookup dictionaries
    private static final Map<String, List<String>> SERVICE_SYNONYMS = new HashMap<>();
    private static final Map<String, List<String>> TEST_TYPE_SYNONYMS = new HashMap<>();
    private static final Map<String, List<String>> ACTION_SYNONYMS = new HashMap<>();
    private static final Map<String, List<String>> CONTEXT_KEYWORDS = new HashMap<>();
    
    // Advanced patterns for intent classification
    private static final Map<IntentType, List<IntentPattern>> INTENT_PATTERNS = new HashMap<>();
    
    // Entity patterns for NER-like extraction
    private static final Map<String, List<Pattern>> ENTITY_PATTERNS = new HashMap<>();
    
    static {
        initializeLookupDictionaries();
        initializeIntentPatterns();
        initializeEntityPatterns();
    }
    
    /**
     * Enhanced intent parsing with context awareness
     */
    public UserIntent parseIntent(String userInput) {
        log.info("Enhanced parsing user intent: {}", userInput);
        
        try {
            // Step 1: Preprocess and normalize input
            ProcessedInput processedInput = preprocessInput(userInput);
            
            // Step 2: Extract entities using NER-like approach
            Map<String, List<Entity>> entities = extractEntities(processedInput);
            
            // Step 3: Classify intent with context awareness
            IntentClassificationResult intentResult = classifyIntent(processedInput, entities);
            
            // Step 4: Generate structured parameters
            Map<String, Object> parameters = generateStructuredParameters(entities, intentResult);
            
            // Step 5: Calculate enhanced confidence
            double confidence = calculateEnhancedConfidence(processedInput, intentResult, entities);
            
            // Step 6: Generate natural description
            String description = generateNaturalDescription(intentResult, entities);
            
            UserIntent intent = UserIntent.builder()
                .rawInput(userInput)
                .type(intentResult.getIntentType())
                .description(description)
                .parameters(parameters)
                .confidence(confidence)
                .timestamp(LocalDateTime.now())
                .build();
            
            log.info("Enhanced parsed intent: {} with confidence: {}", intentResult.getIntentType(), confidence);
            return intent;
            
        } catch (Exception e) {
            log.error("Error in enhanced intent parsing: {}", e.getMessage(), e);
            return createFallbackIntent(userInput);
        }
    }
    
    /**
     * Preprocess input with advanced normalization
     */
    private ProcessedInput preprocessInput(String input) {
        if (input == null) {
            return new ProcessedInput("", new ArrayList<>(), new ArrayList<>());
        }
        
        // Advanced normalization
        String normalized = input.toLowerCase()
            .replaceAll("\\s+", " ")
            .replaceAll("[^a-zA-Z0-9\\s@#]", " ")
            .trim();
        
        // Tokenize
        List<String> tokens = Arrays.asList(normalized.split("\\s+"));
        
        // Extract special tokens (annotations, priorities, etc.)
        List<String> specialTokens = tokens.stream()
            .filter(token -> token.startsWith("@") || token.matches("p\\d+") || token.matches("\\d+"))
            .collect(Collectors.toList());
        
        return new ProcessedInput(normalized, tokens, specialTokens);
    }
    
    /**
     * Extract entities using NER-like approach
     */
    private Map<String, List<Entity>> extractEntities(ProcessedInput input) {
        Map<String, List<Entity>> entities = new HashMap<>();
        
        // Extract services
        entities.put("services", extractServiceEntities(input));
        
        // Extract test types
        entities.put("testTypes", extractTestTypeEntities(input));
        
        // Extract actions
        entities.put("actions", extractActionEntities(input));
        
        // Extract parameters
        entities.put("parameters", extractParameterEntities(input));
        
        // Extract context
        entities.put("context", extractContextEntities(input));
        
        return entities;
    }
    
    /**
     * Extract service entities with fuzzy matching
     */
    private List<Entity> extractServiceEntities(ProcessedInput input) {
        List<Entity> services = new ArrayList<>();
        
        // Direct pattern matching
        for (Map.Entry<String, List<String>> entry : SERVICE_SYNONYMS.entrySet()) {
            String canonicalService = entry.getKey();
            List<String> synonyms = entry.getValue();
            
            for (String synonym : synonyms) {
                if (input.getNormalized().contains(synonym)) {
                    services.add(new Entity("service", canonicalService, synonym, 1.0));
                }
            }
        }
        
        // Fuzzy matching for partial matches
        for (String token : input.getTokens()) {
            for (Map.Entry<String, List<String>> entry : SERVICE_SYNONYMS.entrySet()) {
                String canonicalService = entry.getKey();
                List<String> synonyms = entry.getValue();
                
                for (String synonym : synonyms) {
                    double similarity = calculateSimilarity(token, synonym);
                    if (similarity >= fuzzyThreshold) {
                        services.add(new Entity("service", canonicalService, token, similarity));
                    }
                }
            }
        }
        
        return services.stream()
            .distinct()
            .sorted((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()))
            .collect(Collectors.toList());
    }
    
    /**
     * Extract test type entities
     */
    private List<Entity> extractTestTypeEntities(ProcessedInput input) {
        List<Entity> testTypes = new ArrayList<>();
        
        // Direct pattern matching
        for (Map.Entry<String, List<String>> entry : TEST_TYPE_SYNONYMS.entrySet()) {
            String canonicalType = entry.getKey();
            List<String> synonyms = entry.getValue();
            
            for (String synonym : synonyms) {
                if (input.getNormalized().contains(synonym)) {
                    testTypes.add(new Entity("testType", canonicalType, synonym, 1.0));
                }
            }
        }
        
        // Annotation-style test types
        for (String specialToken : input.getSpecialTokens()) {
            if (specialToken.startsWith("@")) {
                String testType = mapAnnotationToTestType(specialToken);
                if (testType != null) {
                    testTypes.add(new Entity("testType", testType, specialToken, 1.0));
                }
            }
        }
        
        return testTypes.stream()
            .distinct()
            .sorted((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()))
            .collect(Collectors.toList());
    }
    
    /**
     * Extract action entities
     */
    private List<Entity> extractActionEntities(ProcessedInput input) {
        List<Entity> actions = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : ACTION_SYNONYMS.entrySet()) {
            String canonicalAction = entry.getKey();
            List<String> synonyms = entry.getValue();
            
            for (String synonym : synonyms) {
                if (input.getNormalized().contains(synonym)) {
                    actions.add(new Entity("action", canonicalAction, synonym, 1.0));
                }
            }
        }
        
        return actions.stream()
            .distinct()
            .sorted((a, b) -> Double.compare(b.getConfidence(), a.getConfidence()))
            .collect(Collectors.toList());
    }
    
    /**
     * Extract parameter entities (timeouts, retries, etc.)
     */
    private List<Entity> extractParameterEntities(ProcessedInput input) {
        List<Entity> parameters = new ArrayList<>();
        
        // Timeout patterns
        Pattern timeoutPattern = Pattern.compile("(\\d+)\\s*(seconds?|minutes?|hours?|secs?|mins?|hrs?)");
        var timeoutMatcher = timeoutPattern.matcher(input.getNormalized());
        if (timeoutMatcher.find()) {
            parameters.add(new Entity("parameter", "timeout", timeoutMatcher.group(0), 1.0));
        }
        
        // Retry patterns
        Pattern retryPattern = Pattern.compile("(\\d+)\\s*retries?");
        var retryMatcher = retryPattern.matcher(input.getNormalized());
        if (retryMatcher.find()) {
            parameters.add(new Entity("parameter", "retries", retryMatcher.group(0), 1.0));
        }
        
        // Priority patterns
        Pattern priorityPattern = Pattern.compile("p(\\d+)");
        var priorityMatcher = priorityPattern.matcher(input.getNormalized());
        if (priorityMatcher.find()) {
            parameters.add(new Entity("parameter", "priority", "P" + priorityMatcher.group(1), 1.0));
        }
        
        return parameters;
    }
    
    /**
     * Extract context entities
     */
    private List<Entity> extractContextEntities(ProcessedInput input) {
        List<Entity> context = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : CONTEXT_KEYWORDS.entrySet()) {
            String contextType = entry.getKey();
            List<String> keywords = entry.getValue();
            
            for (String keyword : keywords) {
                if (input.getNormalized().contains(keyword)) {
                    context.add(new Entity("context", contextType, keyword, 1.0));
                }
            }
        }
        
        return context;
    }
    
    /**
     * Enhanced intent classification with context awareness
     */
    private IntentClassificationResult classifyIntent(ProcessedInput input, Map<String, List<Entity>> entities) {
        Map<IntentType, Double> scores = new HashMap<>();
        
        // Score each intent type based on patterns and context
        for (Map.Entry<IntentType, List<IntentPattern>> entry : INTENT_PATTERNS.entrySet()) {
            IntentType intentType = entry.getKey();
            List<IntentPattern> patterns = entry.getValue();
            
            double score = 0.0;
            for (IntentPattern pattern : patterns) {
                double patternScore = pattern.calculateScore(input, entities);
                score += patternScore;
            }
            
            if (score > 0) {
                scores.put(intentType, score / patterns.size());
            }
        }
        
        // Find best intent
        IntentType bestIntent = scores.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(IntentType.UNKNOWN);
        
        double confidence = scores.getOrDefault(bestIntent, 0.0);
        
        return new IntentClassificationResult(bestIntent, confidence, scores);
    }
    
    /**
     * Generate structured parameters from entities
     */
    private Map<String, Object> generateStructuredParameters(Map<String, List<Entity>> entities, IntentClassificationResult intentResult) {
        Map<String, Object> parameters = new HashMap<>();
        
        // Extract services
        List<String> services = entities.get("services").stream()
            .map(Entity::getValue)
            .collect(Collectors.toList());
        if (!services.isEmpty()) {
            parameters.put("services", services);
            parameters.put("serviceName", services.get(0)); // Primary service
        }
        
        // Extract test types
        List<String> testTypes = entities.get("testTypes").stream()
            .map(Entity::getValue)
            .collect(Collectors.toList());
        if (!testTypes.isEmpty()) {
            parameters.put("testTypes", testTypes);
        }
        
        // Extract actions
        List<String> actions = entities.get("actions").stream()
            .map(Entity::getValue)
            .collect(Collectors.toList());
        if (!actions.isEmpty()) {
            parameters.put("actions", actions);
        }
        
        // Extract parameters
        Map<String, String> paramMap = new HashMap<>();
        for (Entity param : entities.get("parameters")) {
            paramMap.put(param.getType(), param.getOriginalText());
        }
        if (!paramMap.isEmpty()) {
            parameters.put("parameters", paramMap);
        }
        
        // Extract context
        List<String> context = entities.get("context").stream()
            .map(Entity::getValue)
            .collect(Collectors.toList());
        if (!context.isEmpty()) {
            parameters.put("context", context);
        }
        
        // Add intent confidence
        parameters.put("intentConfidence", intentResult.getConfidence());
        
        return parameters;
    }
    
    /**
     * Calculate enhanced confidence with multiple factors
     */
    private double calculateEnhancedConfidence(ProcessedInput input, IntentClassificationResult intentResult, Map<String, List<Entity>> entities) {
        if (intentResult.getIntentType() == IntentType.UNKNOWN) {
            return 0.0;
        }
        
        double confidence = intentResult.getConfidence();
        
        // Boost confidence based on entity extraction quality
        int totalEntities = entities.values().stream().mapToInt(List::size).sum();
        if (totalEntities > 0) {
            confidence += Math.min(0.2, totalEntities * 0.05);
        }
        
        // Boost confidence for specific high-confidence entities
        for (List<Entity> entityList : entities.values()) {
            for (Entity entity : entityList) {
                if (entity.getConfidence() > 0.9) {
                    confidence += 0.05;
                }
            }
        }
        
        // Boost confidence for clear action words
        if (input.getNormalized().matches(".*\\b(run|execute|start|launch|test|analyze|generate|optimize)\\b.*")) {
            confidence += 0.1;
        }
        
        return Math.min(confidence, 1.0);
    }
    
    /**
     * Generate natural language description
     */
    private String generateNaturalDescription(IntentClassificationResult intentResult, Map<String, List<Entity>> entities) {
        StringBuilder description = new StringBuilder();
        
        // Get primary action
        List<Entity> actions = entities.get("actions");
        String primaryAction = actions.isEmpty() ? "process" : actions.get(0).getValue();
        
        description.append(primaryAction);
        
        // Add test type if available
        List<Entity> testTypes = entities.get("testTypes");
        if (!testTypes.isEmpty()) {
            description.append(" ").append(testTypes.get(0).getValue().toLowerCase().replace("_", " "));
        }
        
        // Add service if available
        List<Entity> services = entities.get("services");
        if (!services.isEmpty()) {
            description.append(" for ").append(services.get(0).getValue());
        }
        
        return description.toString();
    }
    
    /**
     * Calculate string similarity using Levenshtein distance
     */
    private double calculateSimilarity(String s1, String s2) {
        if (s1.equals(s2)) return 1.0;
        if (s1.length() == 0 || s2.length() == 0) return 0.0;
        
        int maxLength = Math.max(s1.length(), s2.length());
        int distance = levenshteinDistance(s1, s2);
        
        return 1.0 - (double) distance / maxLength;
    }
    
    /**
     * Calculate Levenshtein distance between two strings
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * Map annotation to test type
     */
    private String mapAnnotationToTestType(String annotation) {
        String cleanAnnotation = annotation.replace("@", "").toLowerCase();
        
        switch (cleanAnnotation) {
            case "test": return "UNIT_TEST";
            case "unittest": return "UNIT_TEST";
            case "integrationtest": return "INTEGRATION_TEST";
            case "apitest": return "API_TEST";
            case "performancetest": return "PERFORMANCE_TEST";
            case "securitytest": return "SECURITY_TEST";
            case "contracttest": return "CONTRACT_TEST";
            case "chaostest": return "CHAOS_TEST";
            case "smoketest": return "SMOKE_TEST";
            case "regressiontest": return "REGRESSION_TEST";
            case "e2etest": return "END_TO_END_TEST";
            case "loadtest": return "LOAD_TEST";
            case "stresstest": return "STRESS_TEST";
            case "penetrationtest": return "PENETRATION_TEST";
            default: return null;
        }
    }
    
    /**
     * Create fallback intent for unknown inputs
     */
    private UserIntent createFallbackIntent(String userInput) {
        return UserIntent.builder()
            .rawInput(userInput != null ? userInput : "")
            .type(IntentType.UNKNOWN)
            .description("Unable to parse intent from input")
            .confidence(0.0)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    /**
     * Initialize comprehensive lookup dictionaries
     */
    private static void initializeLookupDictionaries() {
        // Service synonyms
        SERVICE_SYNONYMS.put("user-service", Arrays.asList(
            "user-service", "user service", "userservice", "user", "users", "user management", "user mgmt"
        ));
        SERVICE_SYNONYMS.put("product-service", Arrays.asList(
            "product-service", "product service", "productservice", "product", "products", "catalog", "inventory"
        ));
        SERVICE_SYNONYMS.put("order-service", Arrays.asList(
            "order-service", "order service", "orderservice", "order", "orders", "order management", "order mgmt"
        ));
        SERVICE_SYNONYMS.put("notification-service", Arrays.asList(
            "notification-service", "notification service", "notificationservice", "notification", "notifications", "notify", "alert"
        ));
        SERVICE_SYNONYMS.put("gateway-service", Arrays.asList(
            "gateway-service", "gateway service", "gatewayservice", "gateway", "api gateway", "api", "proxy"
        ));
        
        // Test type synonyms
        TEST_TYPE_SYNONYMS.put("UNIT_TEST", Arrays.asList(
            "unit test", "unittest", "unit", "component test", "component"
        ));
        TEST_TYPE_SYNONYMS.put("INTEGRATION_TEST", Arrays.asList(
            "integration test", "integrationtest", "integration", "integrated test"
        ));
        TEST_TYPE_SYNONYMS.put("API_TEST", Arrays.asList(
            "api test", "apitest", "api", "rest test", "resttest", "endpoint test"
        ));
        TEST_TYPE_SYNONYMS.put("PERFORMANCE_TEST", Arrays.asList(
            "performance test", "performancetest", "performance", "perf test", "perftest"
        ));
        TEST_TYPE_SYNONYMS.put("SECURITY_TEST", Arrays.asList(
            "security test", "securitytest", "security", "sec test", "sectest"
        ));
        TEST_TYPE_SYNONYMS.put("CONTRACT_TEST", Arrays.asList(
            "contract test", "contracttest", "contract", "pact test", "pacttest"
        ));
        TEST_TYPE_SYNONYMS.put("CHAOS_TEST", Arrays.asList(
            "chaos test", "chaostest", "chaos", "chaos engineering", "fault injection"
        ));
        TEST_TYPE_SYNONYMS.put("SMOKE_TEST", Arrays.asList(
            "smoke test", "smoketest", "smoke", "sanity test", "sanitytest"
        ));
        TEST_TYPE_SYNONYMS.put("REGRESSION_TEST", Arrays.asList(
            "regression test", "regressiontest", "regression", "regression suite"
        ));
        TEST_TYPE_SYNONYMS.put("END_TO_END_TEST", Arrays.asList(
            "end to end test", "e2e test", "e2etest", "end-to-end", "e2e", "full test"
        ));
        TEST_TYPE_SYNONYMS.put("LOAD_TEST", Arrays.asList(
            "load test", "loadtest", "load", "volume test", "volumetest"
        ));
        TEST_TYPE_SYNONYMS.put("STRESS_TEST", Arrays.asList(
            "stress test", "stresstest", "stress", "breaking test", "breakingtest"
        ));
        TEST_TYPE_SYNONYMS.put("PENETRATION_TEST", Arrays.asList(
            "penetration test", "penetrationtest", "pen test", "pentest", "penetration"
        ));
        
        // Action synonyms
        ACTION_SYNONYMS.put("run", Arrays.asList(
            "run", "execute", "start", "launch", "trigger", "begin", "initiate"
        ));
        ACTION_SYNONYMS.put("analyze", Arrays.asList(
            "analyze", "analyse", "investigate", "examine", "review", "assess", "evaluate"
        ));
        ACTION_SYNONYMS.put("generate", Arrays.asList(
            "generate", "create", "make", "build", "produce", "develop"
        ));
        ACTION_SYNONYMS.put("optimize", Arrays.asList(
            "optimize", "optimise", "improve", "enhance", "refine", "tune"
        ));
        ACTION_SYNONYMS.put("check", Arrays.asList(
            "check", "verify", "validate", "test", "monitor", "inspect"
        ));
        ACTION_SYNONYMS.put("stop", Arrays.asList(
            "stop", "halt", "terminate", "cancel", "abort", "end"
        ));
        ACTION_SYNONYMS.put("status", Arrays.asList(
            "status", "state", "health", "condition", "situation"
        ));
        
        // Context keywords
        CONTEXT_KEYWORDS.put("urgency", Arrays.asList(
            "urgent", "immediately", "now", "asap", "critical", "emergency"
        ));
        CONTEXT_KEYWORDS.put("scope", Arrays.asList(
            "all", "everything", "full", "complete", "entire", "whole"
        ));
        CONTEXT_KEYWORDS.put("priority", Arrays.asList(
            "high", "low", "medium", "normal", "important", "trivial"
        ));
        CONTEXT_KEYWORDS.put("timing", Arrays.asList(
            "later", "background", "scheduled", "delayed", "postponed"
        ));
    }
    
    /**
     * Initialize enhanced intent patterns
     */
    private static void initializeIntentPatterns() {
        // RUN_TESTS patterns
        INTENT_PATTERNS.put(IntentType.RUN_TESTS, Arrays.asList(
            new IntentPattern("run.*test", 1.0, Arrays.asList("action:run", "testType:any")),
            new IntentPattern("execute.*test", 1.0, Arrays.asList("action:run", "testType:any")),
            new IntentPattern("start.*test", 1.0, Arrays.asList("action:run", "testType:any")),
            new IntentPattern("launch.*test", 1.0, Arrays.asList("action:run", "testType:any")),
            new IntentPattern("test.*run", 0.9, Arrays.asList("action:run", "testType:any")),
            new IntentPattern("chaos.*run", 1.0, Arrays.asList("action:run", "testType:chaos")),
            new IntentPattern("run.*chaos", 1.0, Arrays.asList("action:run", "testType:chaos"))
        ));
        
        // ANALYZE_FAILURES patterns
        INTENT_PATTERNS.put(IntentType.ANALYZE_FAILURES, Arrays.asList(
            new IntentPattern("analyze.*fail", 1.0, Arrays.asList("action:analyze", "context:failure")),
            new IntentPattern("investigate.*error", 1.0, Arrays.asList("action:analyze", "context:error")),
            new IntentPattern("why.*fail", 0.9, Arrays.asList("action:analyze", "context:failure")),
            new IntentPattern("what.*wrong", 0.8, Arrays.asList("action:analyze", "context:problem"))
        ));
        
        // GENERATE_TESTS patterns
        INTENT_PATTERNS.put(IntentType.GENERATE_TESTS, Arrays.asList(
            new IntentPattern("generate.*test", 1.0, Arrays.asList("action:generate", "testType:any")),
            new IntentPattern("create.*test", 1.0, Arrays.asList("action:generate", "testType:any")),
            new IntentPattern("make.*test", 0.9, Arrays.asList("action:generate", "testType:any")),
            new IntentPattern("new.*test", 0.8, Arrays.asList("action:generate", "testType:any"))
        ));
        
        // OPTIMIZE_TESTS patterns
        INTENT_PATTERNS.put(IntentType.OPTIMIZE_TESTS, Arrays.asList(
            new IntentPattern("optimize.*test", 1.0, Arrays.asList("action:optimize", "testType:any")),
            new IntentPattern("improve.*test", 1.0, Arrays.asList("action:optimize", "testType:any")),
            new IntentPattern("enhance.*test", 0.9, Arrays.asList("action:optimize", "testType:any")),
            new IntentPattern("faster.*test", 0.8, Arrays.asList("action:optimize", "context:performance"))
        ));
        
        // HEALTH_CHECK patterns
        INTENT_PATTERNS.put(IntentType.HEALTH_CHECK, Arrays.asList(
            new IntentPattern("health.*check", 1.0, Arrays.asList("action:check", "context:health")),
            new IntentPattern("status.*check", 1.0, Arrays.asList("action:check", "context:status")),
            new IntentPattern("system.*health", 0.9, Arrays.asList("action:check", "context:health")),
            new IntentPattern("service.*status", 0.9, Arrays.asList("action:check", "context:status"))
        ));
    }
    
    /**
     * Initialize entity patterns for NER-like extraction
     */
    private static void initializeEntityPatterns() {
        ENTITY_PATTERNS.put("service", Arrays.asList(
            Pattern.compile("\\b(user|product|order|notification|gateway)\\s*service\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\b(user|product|order|notification|gateway)service\\b", Pattern.CASE_INSENSITIVE)
        ));
        
        ENTITY_PATTERNS.put("testType", Arrays.asList(
            Pattern.compile("\\b(unit|integration|api|performance|security|contract|chaos|smoke|regression|e2e|load|stress|penetration)\\s*test\\b", Pattern.CASE_INSENSITIVE),
            Pattern.compile("@\\w+", Pattern.CASE_INSENSITIVE)
        ));
        
        ENTITY_PATTERNS.put("action", Arrays.asList(
            Pattern.compile("\\b(run|execute|start|launch|analyze|generate|optimize|check|stop|status)\\b", Pattern.CASE_INSENSITIVE)
        ));
    }
    
    // Inner classes for enhanced processing
    
    private static class ProcessedInput {
        private final String normalized;
        private final List<String> tokens;
        private final List<String> specialTokens;
        
        public ProcessedInput(String normalized, List<String> tokens, List<String> specialTokens) {
            this.normalized = normalized;
            this.tokens = tokens;
            this.specialTokens = specialTokens;
        }
        
        public String getNormalized() { return normalized; }
        public List<String> getTokens() { return tokens; }
        public List<String> getSpecialTokens() { return specialTokens; }
    }
    
    private static class Entity {
        private final String type;
        private final String value;
        private final String originalText;
        private final double confidence;
        
        public Entity(String type, String value, String originalText, double confidence) {
            this.type = type;
            this.value = value;
            this.originalText = originalText;
            this.confidence = confidence;
        }
        
        public String getType() { return type; }
        public String getValue() { return value; }
        public String getOriginalText() { return originalText; }
        public double getConfidence() { return confidence; }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Entity entity = (Entity) obj;
            return Objects.equals(type, entity.type) && Objects.equals(value, entity.value);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }
    }
    
    private static class IntentPattern {
        private final String pattern;
        private final double weight;
        private final List<String> requiredEntities;
        
        public IntentPattern(String pattern, double weight, List<String> requiredEntities) {
            this.pattern = pattern;
            this.weight = weight;
            this.requiredEntities = requiredEntities;
        }
        
        public double calculateScore(ProcessedInput input, Map<String, List<Entity>> entities) {
            // Check if pattern matches
            if (!input.getNormalized().matches(".*" + pattern + ".*")) {
                return 0.0;
            }
            
            double score = weight;
            
            // Boost score based on required entities
            for (String requiredEntity : requiredEntities) {
                String[] parts = requiredEntity.split(":");
                String entityType = parts[0];
                String entityValue = parts[1];
                
                if (entityValue.equals("any")) {
                    if (entities.containsKey(entityType) && !entities.get(entityType).isEmpty()) {
                        score += 0.1;
                    }
                } else {
                    List<Entity> entityList = entities.get(entityType);
                    if (entityList != null && entityList.stream().anyMatch(e -> e.getValue().equals(entityValue))) {
                        score += 0.2;
                    }
                }
            }
            
            return Math.min(score, 1.0);
        }
    }
    
    private static class IntentClassificationResult {
        private final IntentType intentType;
        private final double confidence;
        private final Map<IntentType, Double> allScores;
        
        public IntentClassificationResult(IntentType intentType, double confidence, Map<IntentType, Double> allScores) {
            this.intentType = intentType;
            this.confidence = confidence;
            this.allScores = allScores;
        }
        
        public IntentType getIntentType() { return intentType; }
        public double getConfidence() { return confidence; }
        public Map<IntentType, Double> getAllScores() { return allScores; }
    }
}
