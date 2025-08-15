package com.kb.jarvis.core.nlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class NLPService {
    
    private static final Logger log = LoggerFactory.getLogger(NLPService.class);
    
    // Sentiment analysis patterns
    private static final List<Pattern> POSITIVE_PATTERNS = Arrays.asList(
        Pattern.compile("\\b(good|great|excellent|perfect|awesome|amazing)\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(please|kindly|thank you|thanks)\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(working|success|passed|completed)\\b", Pattern.CASE_INSENSITIVE)
    );
    
    private static final List<Pattern> NEGATIVE_PATTERNS = Arrays.asList(
        Pattern.compile("\\b(bad|terrible|awful|broken|failed|error)\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(urgent|critical|emergency|fix|broken)\\b", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\b(slow|slowly|timeout|hung|stuck)\\b", Pattern.CASE_INSENSITIVE)
    );
    
    // Complexity indicators
    private static final List<String> COMPLEXITY_INDICATORS = Arrays.asList(
        "complex", "complicated", "difficult", "challenging", "advanced", "sophisticated",
        "multiple", "several", "various", "different", "comprehensive", "thorough"
    );
    
    /**
     * Analyze the sentiment of user input
     */
    public SentimentAnalysis analyzeSentiment(String userInput) {
        String normalizedInput = userInput.toLowerCase();
        
        int positiveScore = 0;
        int negativeScore = 0;
        
        // Count positive patterns
        for (Pattern pattern : POSITIVE_PATTERNS) {
            var matcher = pattern.matcher(normalizedInput);
            while (matcher.find()) {
                positiveScore++;
            }
        }
        
        // Count negative patterns
        for (Pattern pattern : NEGATIVE_PATTERNS) {
            var matcher = pattern.matcher(normalizedInput);
            while (matcher.find()) {
                negativeScore++;
            }
        }
        
        // Determine sentiment
        SentimentType sentimentType;
        double confidence;
        
        if (positiveScore > negativeScore) {
            sentimentType = SentimentType.POSITIVE;
            confidence = (double) positiveScore / (positiveScore + negativeScore + 1);
        } else if (negativeScore > positiveScore) {
            sentimentType = SentimentType.NEGATIVE;
            confidence = (double) negativeScore / (positiveScore + negativeScore + 1);
        } else {
            sentimentType = SentimentType.NEUTRAL;
            confidence = 0.5;
        }
        
        return new SentimentAnalysis(sentimentType, confidence, positiveScore, negativeScore);
    }
    
    /**
     * Calculate the complexity score of user input
     */
    public ComplexityAnalysis analyzeComplexity(String userInput) {
        String normalizedInput = userInput.toLowerCase();
        
        int complexityScore = 0;
        List<String> foundIndicators = new ArrayList<>();
        
        // Check for complexity indicators
        for (String indicator : COMPLEXITY_INDICATORS) {
            if (normalizedInput.contains(indicator)) {
                complexityScore += 2;
                foundIndicators.add(indicator);
            }
        }
        
        // Check for multiple entities (services, test types, etc.)
        String[] entityTypes = {"service", "test", "type", "parameter", "timeout", "retry"};
        for (String entityType : entityTypes) {
            if (normalizedInput.contains(entityType)) {
                complexityScore += 1;
            }
        }
        
        // Check for multiple clauses (and, or, but, also)
        String[] connectors = {"and", "or", "but", "also", "additionally", "furthermore"};
        for (String connector : connectors) {
            if (normalizedInput.contains(connector)) {
                complexityScore += 1;
            }
        }
        
        // Determine complexity level
        ComplexityLevel level;
        if (complexityScore >= 8) {
            level = ComplexityLevel.HIGH;
        } else if (complexityScore >= 4) {
            level = ComplexityLevel.MEDIUM;
        } else {
            level = ComplexityLevel.LOW;
        }
        
        return new ComplexityAnalysis(level, complexityScore, foundIndicators);
    }
    
    /**
     * Validate and suggest improvements for user commands
     */
    public CommandValidation validateCommand(String userInput) {
        List<String> suggestions = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        boolean isValid = true;
        
        // Check for common issues
        if (userInput.trim().isEmpty()) {
            warnings.add("Command is empty");
            isValid = false;
        }
        
        if (userInput.length() < 3) {
            warnings.add("Command is too short");
            isValid = false;
        }
        
        if (userInput.length() > 500) {
            warnings.add("Command is too long");
            suggestions.add("Consider breaking down into multiple commands");
        }
        
        // Check for missing context
        if (!userInput.toLowerCase().contains("test") && 
            !userInput.toLowerCase().contains("run") && 
            !userInput.toLowerCase().contains("check") &&
            !userInput.toLowerCase().contains("analyze")) {
            warnings.add("Command may be missing action context");
            suggestions.add("Consider adding action words like 'run', 'test', 'check', 'analyze'");
        }
        
        // Check for ambiguous service references
        if (userInput.toLowerCase().contains("service") && 
            !userInput.toLowerCase().contains("user") &&
            !userInput.toLowerCase().contains("product") &&
            !userInput.toLowerCase().contains("order") &&
            !userInput.toLowerCase().contains("notification") &&
            !userInput.toLowerCase().contains("gateway")) {
            warnings.add("Service reference is ambiguous");
            suggestions.add("Specify which service: user-service, product-service, order-service, etc.");
        }
        
        // Check for missing time constraints
        if (userInput.toLowerCase().contains("urgent") || 
            userInput.toLowerCase().contains("critical")) {
            if (!userInput.toLowerCase().contains("now") && 
                !userInput.toLowerCase().contains("immediately")) {
                suggestions.add("For urgent requests, consider adding 'now' or 'immediately'");
            }
        }
        
        return new CommandValidation(isValid, suggestions, warnings);
    }
    
    /**
     * Extract key phrases from user input
     */
    public List<String> extractKeyPhrases(String userInput) {
        List<String> keyPhrases = new ArrayList<>();
        String normalizedInput = userInput.toLowerCase();
        
        // Extract service names
        String[] services = {"user-service", "product-service", "order-service", "notification-service", "gateway-service"};
        for (String service : services) {
            if (normalizedInput.contains(service)) {
                keyPhrases.add(service);
            }
        }
        
        // Extract test types
        String[] testTypes = {"unit test", "integration test", "api test", "performance test", "security test"};
        for (String testType : testTypes) {
            if (normalizedInput.contains(testType)) {
                keyPhrases.add(testType);
            }
        }
        
        // Extract Java annotation-style test types
        String[] annotationTestTypes = {
            "@test", "@unittest", "@integrationtest", "@apitest", "@performancetest",
            "@securitytest", "@contracttest", "@chaostest", "@smoketest", "@regressiontest",
            "@e2etest", "@loadtest", "@stresstest", "@penetrationtest", "@accessibilitytest",
            "@ui test", "@uitest", "@componenttest", "@component test", "@servicetest",
            "@service test", "@repositorytest", "@repository test", "@controllertest",
            "@controller test", "@resttest", "@rest test", "@webclienttest", "@webclient test"
        };
        
        for (String annotationType : annotationTestTypes) {
            if (normalizedInput.contains(annotationType)) {
                keyPhrases.add(annotationType);
            }
        }
        
        // Extract priority annotations
        String[] priorityAnnotations = {"p0", "p1", "p2", "p3", "priority 0", "priority 1", "priority 2", "priority 3"};
        for (String priority : priorityAnnotations) {
            if (normalizedInput.contains(priority)) {
                keyPhrases.add(priority);
            }
        }
        
        // Extract actions
        String[] actions = {"run", "execute", "start", "analyze", "generate", "optimize", "check"};
        for (String action : actions) {
            if (normalizedInput.contains(action)) {
                keyPhrases.add(action);
            }
        }
        
        return keyPhrases;
    }
    
    /**
     * Check if input contains multiple commands
     */
    public boolean hasMultipleCommands(String userInput) {
        String normalizedInput = userInput.toLowerCase();
        
        // Check for multiple action words
        String[] actions = {"run", "execute", "start", "analyze", "generate", "optimize", "check"};
        int actionCount = 0;
        
        for (String action : actions) {
            if (normalizedInput.contains(action)) {
                actionCount++;
            }
        }
        
        // Check for connectors that suggest multiple commands
        String[] connectors = {"and", "also", "additionally", "furthermore", "then", "next"};
        for (String connector : connectors) {
            if (normalizedInput.contains(connector)) {
                actionCount++;
            }
        }
        
        return actionCount > 1;
    }
    
    /**
     * Suggest alternative phrasings for better understanding
     */
    public List<String> suggestAlternatives(String userInput) {
        List<String> alternatives = new ArrayList<>();
        String normalizedInput = userInput.toLowerCase();
        
        // Suggest more specific commands
        if (normalizedInput.contains("test") && !normalizedInput.contains("run")) {
            alternatives.add("run tests for " + extractServiceSuggestion(normalizedInput));
        }
        
        if (normalizedInput.contains("check") && !normalizedInput.contains("health")) {
            alternatives.add("check health status of " + extractServiceSuggestion(normalizedInput));
        }
        
        if (normalizedInput.contains("problem") || normalizedInput.contains("issue")) {
            alternatives.add("analyze failures in " + extractServiceSuggestion(normalizedInput));
        }
        
        if (normalizedInput.contains("slow") || normalizedInput.contains("performance")) {
            alternatives.add("run performance tests for " + extractServiceSuggestion(normalizedInput));
        }
        
        return alternatives;
    }
    
    private String extractServiceSuggestion(String input) {
        if (input.contains("user")) return "user-service";
        if (input.contains("product")) return "product-service";
        if (input.contains("order")) return "order-service";
        if (input.contains("notification")) return "notification-service";
        if (input.contains("gateway")) return "gateway-service";
        return "all services";
    }
    
    // Inner classes for analysis results
    public static class SentimentAnalysis {
        private final SentimentType type;
        private final double confidence;
        private final int positiveScore;
        private final int negativeScore;
        
        public SentimentAnalysis(SentimentType type, double confidence, int positiveScore, int negativeScore) {
            this.type = type;
            this.confidence = confidence;
            this.positiveScore = positiveScore;
            this.negativeScore = negativeScore;
        }
        
        public SentimentType getType() { return type; }
        public double getConfidence() { return confidence; }
        public int getPositiveScore() { return positiveScore; }
        public int getNegativeScore() { return negativeScore; }
    }
    
    public static class ComplexityAnalysis {
        private final ComplexityLevel level;
        private final int score;
        private final List<String> indicators;
        
        public ComplexityAnalysis(ComplexityLevel level, int score, List<String> indicators) {
            this.level = level;
            this.score = score;
            this.indicators = indicators;
        }
        
        public ComplexityLevel getLevel() { return level; }
        public int getScore() { return score; }
        public List<String> getIndicators() { return indicators; }
    }
    
    public static class CommandValidation {
        private final boolean isValid;
        private final List<String> suggestions;
        private final List<String> warnings;
        
        public CommandValidation(boolean isValid, List<String> suggestions, List<String> warnings) {
            this.isValid = isValid;
            this.suggestions = suggestions;
            this.warnings = warnings;
        }
        
        public boolean isValid() { return isValid; }
        public List<String> getSuggestions() { return suggestions; }
        public List<String> getWarnings() { return warnings; }
    }
    
    public enum SentimentType {
        POSITIVE, NEGATIVE, NEUTRAL
    }
    
    public enum ComplexityLevel {
        LOW, MEDIUM, HIGH
    }
}
