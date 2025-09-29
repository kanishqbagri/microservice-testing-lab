package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.context.ComprehensiveContextService;
import com.kb.jarvis.core.demo.ComprehensiveContextDemo;
import com.kb.jarvis.core.model.ComprehensiveContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Comprehensive Context Understanding
 * Exposes endpoints for analyzing complex commands and understanding context
 */
@RestController
@RequestMapping("/api/jarvis/context")
public class ComprehensiveContextController {
    
    private static final Logger log = LoggerFactory.getLogger(ComprehensiveContextController.class);
    
    @Autowired
    private ComprehensiveContextService comprehensiveContextService;
    
    @Autowired
    private ComprehensiveContextDemo comprehensiveContextDemo;
    
    /**
     * Analyze a natural language command and return comprehensive context
     */
    @PostMapping("/analyze")
    public ResponseEntity<ComprehensiveContext> analyzeCommand(@RequestBody Map<String, String> request) {
        String command = request.get("command");
        if (command == null || command.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Analyzing command: {}", command);
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
            return ResponseEntity.ok(context);
        } catch (Exception e) {
            log.error("Error analyzing command: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get context analysis for a simple command
     */
    @GetMapping("/analyze/simple")
    public ResponseEntity<ComprehensiveContext> analyzeSimpleCommand(@RequestParam String command) {
        log.info("Analyzing simple command: {}", command);
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
            return ResponseEntity.ok(context);
        } catch (Exception e) {
            log.error("Error analyzing simple command: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Run comprehensive demo
     */
    @PostMapping("/demo/comprehensive")
    public ResponseEntity<Map<String, String>> runComprehensiveDemo() {
        log.info("Running comprehensive context demo");
        
        try {
            comprehensiveContextDemo.runComprehensiveDemo();
            return ResponseEntity.ok(Map.of("status", "success", "message", "Comprehensive demo completed successfully"));
        } catch (Exception e) {
            log.error("Error running comprehensive demo: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    /**
     * Run command pattern demo
     */
    @PostMapping("/demo/patterns")
    public ResponseEntity<Map<String, String>> runCommandPatternDemo() {
        log.info("Running command pattern demo");
        
        try {
            comprehensiveContextDemo.demoCommandPatterns();
            return ResponseEntity.ok(Map.of("status", "success", "message", "Command pattern demo completed successfully"));
        } catch (Exception e) {
            log.error("Error running command pattern demo: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    /**
     * Run error handling demo
     */
    @PostMapping("/demo/errors")
    public ResponseEntity<Map<String, String>> runErrorHandlingDemo() {
        log.info("Running error handling demo");
        
        try {
            comprehensiveContextDemo.demoErrorHandling();
            return ResponseEntity.ok(Map.of("status", "success", "message", "Error handling demo completed successfully"));
        } catch (Exception e) {
            log.error("Error running error handling demo: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Comprehensive Context Service",
            "version", "1.0.0",
            "capabilities", "Multi-service, Multi-test-type, Multi-action command analysis"
        ));
    }
    
    /**
     * Get supported test types
     */
    @GetMapping("/test-types")
    public ResponseEntity<Map<String, Object>> getSupportedTestTypes() {
        return ResponseEntity.ok(Map.of(
            "testTypes", new String[]{
                "UNIT_TEST", "INTEGRATION_TEST", "API_TEST", "PERFORMANCE_TEST",
                "SECURITY_TEST", "CHAOS_TEST", "CONTRACT_TEST", "END_TO_END_TEST",
                "SMOKE_TEST", "REGRESSION_TEST", "EXPLORATORY_TEST", "ACCESSIBILITY_TEST",
                "COMPATIBILITY_TEST", "LOCALIZATION_TEST"
            },
            "count", 15
        ));
    }
    
    /**
     * Get supported services
     */
    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> getSupportedServices() {
        return ResponseEntity.ok(Map.of(
            "services", new String[]{
                "gateway-service", "user-service", "product-service", 
                "order-service", "notification-service"
            },
            "count", 5
        ));
    }
    
    /**
     * Get supported actions
     */
    @GetMapping("/actions")
    public ResponseEntity<Map<String, Object>> getSupportedActions() {
        return ResponseEntity.ok(Map.of(
            "actions", new String[]{
                "RUN_TESTS", "RUN_PERFORMANCE_TESTS", "RUN_SECURITY_TESTS",
                "RUN_INTEGRATION_TESTS", "RUN_ISOLATED_TESTS", "RUN_CHAOS_TESTS",
                "ANALYZE_FAILURES", "GENERATE_TESTS", "OPTIMIZE_TESTS",
                "HEALTH_CHECK", "MONITOR_SYSTEM", "GENERATE_REPORT",
                "REQUEST_CLARIFICATION", "QUEUE_ACTION", "SELF_HEAL",
                "SCALE_RESOURCES"
            },
            "count", 17
        ));
    }
}
