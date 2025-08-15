package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.nlp.NLPEngine;
import com.kb.jarvis.core.nlp.NLPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/nlp")
public class NLPController {
    
    private static final Logger log = LoggerFactory.getLogger(NLPController.class);
    
    @Autowired
    private NLPEngine nlpEngine;
    
    @Autowired
    private NLPService nlpService;
    
    /**
     * Parse user intent from natural language input
     */
    @PostMapping("/parse-intent")
    public ResponseEntity<Map<String, Object>> parseIntent(@RequestBody Map<String, String> request) {
        try {
            String userInput = request.get("input");
            if (userInput == null || userInput.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input text is required"));
            }
            
            log.info("Parsing intent for input: {}", userInput);
            
            UserIntent intent = nlpEngine.parseIntent(userInput);
            
            Map<String, Object> response = new HashMap<>();
            response.put("intent", intent);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error parsing intent: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to parse intent: " + e.getMessage()));
        }
    }
    
    /**
     * Analyze sentiment of user input
     */
    @PostMapping("/analyze-sentiment")
    public ResponseEntity<Map<String, Object>> analyzeSentiment(@RequestBody Map<String, String> request) {
        try {
            String userInput = request.get("input");
            if (userInput == null || userInput.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input text is required"));
            }
            
            log.info("Analyzing sentiment for input: {}", userInput);
            
            NLPService.SentimentAnalysis analysis = nlpService.analyzeSentiment(userInput);
            
            Map<String, Object> response = new HashMap<>();
            response.put("sentiment", analysis.getType().name());
            response.put("confidence", analysis.getConfidence());
            response.put("positiveScore", analysis.getPositiveScore());
            response.put("negativeScore", analysis.getNegativeScore());
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error analyzing sentiment: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to analyze sentiment: " + e.getMessage()));
        }
    }
    
    /**
     * Analyze complexity of user input
     */
    @PostMapping("/analyze-complexity")
    public ResponseEntity<Map<String, Object>> analyzeComplexity(@RequestBody Map<String, String> request) {
        try {
            String userInput = request.get("input");
            if (userInput == null || userInput.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input text is required"));
            }
            
            log.info("Analyzing complexity for input: {}", userInput);
            
            NLPService.ComplexityAnalysis analysis = nlpService.analyzeComplexity(userInput);
            
            Map<String, Object> response = new HashMap<>();
            response.put("complexityLevel", analysis.getLevel().name());
            response.put("complexityScore", analysis.getScore());
            response.put("indicators", analysis.getIndicators());
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error analyzing complexity: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to analyze complexity: " + e.getMessage()));
        }
    }
    
    /**
     * Validate user command and provide suggestions
     */
    @PostMapping("/validate-command")
    public ResponseEntity<Map<String, Object>> validateCommand(@RequestBody Map<String, String> request) {
        try {
            String userInput = request.get("input");
            if (userInput == null || userInput.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input text is required"));
            }
            
            log.info("Validating command: {}", userInput);
            
            NLPService.CommandValidation validation = nlpService.validateCommand(userInput);
            
            Map<String, Object> response = new HashMap<>();
            response.put("isValid", validation.isValid());
            response.put("suggestions", validation.getSuggestions());
            response.put("warnings", validation.getWarnings());
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error validating command: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to validate command: " + e.getMessage()));
        }
    }
    
    /**
     * Extract key phrases from user input
     */
    @PostMapping("/extract-phrases")
    public ResponseEntity<Map<String, Object>> extractKeyPhrases(@RequestBody Map<String, String> request) {
        try {
            String userInput = request.get("input");
            if (userInput == null || userInput.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input text is required"));
            }
            
            log.info("Extracting key phrases from: {}", userInput);
            
            var keyPhrases = nlpService.extractKeyPhrases(userInput);
            boolean hasMultipleCommands = nlpService.hasMultipleCommands(userInput);
            var alternatives = nlpService.suggestAlternatives(userInput);
            
            Map<String, Object> response = new HashMap<>();
            response.put("keyPhrases", keyPhrases);
            response.put("hasMultipleCommands", hasMultipleCommands);
            response.put("suggestedAlternatives", alternatives);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error extracting key phrases: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to extract key phrases: " + e.getMessage()));
        }
    }
    
    /**
     * Comprehensive NLP analysis combining all features
     */
    @PostMapping("/comprehensive-analysis")
    public ResponseEntity<Map<String, Object>> comprehensiveAnalysis(@RequestBody Map<String, String> request) {
        try {
            String userInput = request.get("input");
            if (userInput == null || userInput.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input text is required"));
            }
            
            log.info("Performing comprehensive NLP analysis for: {}", userInput);
            
            // Perform all analyses
            UserIntent intent = nlpEngine.parseIntent(userInput);
            NLPService.SentimentAnalysis sentiment = nlpService.analyzeSentiment(userInput);
            NLPService.ComplexityAnalysis complexity = nlpService.analyzeComplexity(userInput);
            NLPService.CommandValidation validation = nlpService.validateCommand(userInput);
            var keyPhrases = nlpService.extractKeyPhrases(userInput);
            boolean hasMultipleCommands = nlpService.hasMultipleCommands(userInput);
            var alternatives = nlpService.suggestAlternatives(userInput);
            
            Map<String, Object> response = new HashMap<>();
            response.put("intent", intent);
            response.put("sentiment", Map.of(
                "type", sentiment.getType().name(),
                "confidence", sentiment.getConfidence(),
                "positiveScore", sentiment.getPositiveScore(),
                "negativeScore", sentiment.getNegativeScore()
            ));
            response.put("complexity", Map.of(
                "level", complexity.getLevel().name(),
                "score", complexity.getScore(),
                "indicators", complexity.getIndicators()
            ));
            response.put("validation", Map.of(
                "isValid", validation.isValid(),
                "suggestions", validation.getSuggestions(),
                "warnings", validation.getWarnings()
            ));
            response.put("keyPhrases", keyPhrases);
            response.put("hasMultipleCommands", hasMultipleCommands);
            response.put("suggestedAlternatives", alternatives);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error performing comprehensive analysis: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to perform comprehensive analysis: " + e.getMessage()));
        }
    }
    
    /**
     * Health check endpoint for NLP services
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        try {
            // Test basic functionality
            String testInput = "run tests for user-service";
            UserIntent testIntent = nlpEngine.parseIntent(testInput);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "HEALTHY");
            response.put("nlpEngine", "OPERATIONAL");
            response.put("nlpService", "OPERATIONAL");
            response.put("testIntent", testIntent.getType().name());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("NLP health check failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of(
                    "status", "UNHEALTHY",
                    "error", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
                ));
        }
    }
}
