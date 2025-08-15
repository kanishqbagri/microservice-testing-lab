package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.JarvisCoreEngine;
import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jarvis")
@CrossOrigin(origins = "*")
public class JarvisController {
    
    private static final Logger log = LoggerFactory.getLogger(JarvisController.class);
    
    @Autowired
    private JarvisCoreEngine jarvisCoreEngine;
    
    /**
     * Process a natural language command
     */
    @PostMapping("/command")
    public ResponseEntity<JarvisResponse> processCommand(@RequestBody CommandRequest request) {
        log.info("Received command: {}", request.getCommand());
        
        try {
            JarvisResponse response = jarvisCoreEngine.processCommand(request.getCommand());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing command: {}", e.getMessage(), e);
            
            JarvisResponse errorResponse = JarvisResponse.builder()
                .message("I apologize, but I encountered an error: " + e.getMessage())
                .status(JarvisResponseStatus.ERROR)
                .build();
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    /**
     * Get current system status
     */
    @GetMapping("/status")
    public ResponseEntity<SystemStatus> getSystemStatus() {
        log.debug("Getting system status");
        
        try {
            SystemStatus status = jarvisCoreEngine.getSystemStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("Error getting system status: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get learning insights
     */
    @GetMapping("/insights")
    public ResponseEntity<LearningInsights> getLearningInsights() {
        log.debug("Getting learning insights");
        
        try {
            LearningInsights insights = jarvisCoreEngine.getLearningInsights();
            return ResponseEntity.ok(insights);
        } catch (Exception e) {
            log.error("Error getting learning insights: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check requested");
        
        Map<String, Object> health = Map.of(
            "status", "UP",
            "service", "Jarvis AI Core",
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Get system statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        log.debug("Getting system statistics");
        
        try {
            SystemStatus status = jarvisCoreEngine.getSystemStatus();
            LearningInsights insights = jarvisCoreEngine.getLearningInsights();
            
            Map<String, Object> stats = Map.of(
                "systemStatus", status,
                "learningInsights", insights,
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error getting statistics: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Request body for command processing
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
