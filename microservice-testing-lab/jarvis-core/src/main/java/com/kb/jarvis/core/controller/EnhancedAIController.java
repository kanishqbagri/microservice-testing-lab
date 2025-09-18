package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.ai.EnhancedAIEngine;
import com.kb.jarvis.core.ai.EnhancedAIEngine.AIProcessingResult;
import com.kb.jarvis.core.ai.EnhancedAIEngine.ProcessingStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced AI Controller
 * Exposes the enhanced AI engine functionality through REST endpoints
 */
@RestController
@RequestMapping("/api/jarvis/ai")
@CrossOrigin(origins = "*")
public class EnhancedAIController {
    
    private static final Logger log = LoggerFactory.getLogger(EnhancedAIController.class);
    
    @Autowired
    private EnhancedAIEngine enhancedAIEngine;
    
    /**
     * Process natural language command through enhanced AI engine
     */
    @PostMapping("/process")
    public ResponseEntity<AIProcessingResult> processCommand(@RequestBody CommandRequest request) {
        log.info("Processing command through enhanced AI: {}", request.getCommand());
        
        try {
            AIProcessingResult result = enhancedAIEngine.processInput(request.getCommand());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error processing command: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Process example commands for demonstration
     */
    @GetMapping("/examples")
    public ResponseEntity<Map<String, AIProcessingResult>> processExamples() {
        log.info("Processing example commands for demonstration");
        
        try {
            Map<String, AIProcessingResult> results = enhancedAIEngine.processExampleCommands();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error processing examples: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get processing statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ProcessingStatistics> getStatistics() {
        log.info("Retrieving AI processing statistics");
        
        try {
            ProcessingStatistics stats = enhancedAIEngine.getProcessingStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving statistics: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Health check for enhanced AI engine
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("engine", "Enhanced AI Engine");
        health.put("timestamp", System.currentTimeMillis());
        
        try {
            // Test with a simple command
            AIProcessingResult testResult = enhancedAIEngine.processInput("test health");
            health.put("testResult", testResult.getProcessingConfidence() > 0 ? "PASS" : "FAIL");
        } catch (Exception e) {
            health.put("testResult", "FAIL");
            health.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Command request model
     */
    public static class CommandRequest {
        private String command;
        
        public CommandRequest() {}
        
        public CommandRequest(String command) {
            this.command = command;
        }
        
        public String getCommand() {
            return command;
        }
        
        public void setCommand(String command) {
            this.command = command;
        }
    }
}
