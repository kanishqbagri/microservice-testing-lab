package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.service.MicroserviceIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/microservices")
public class MicroserviceIntegrationController {
    
    private static final Logger logger = LoggerFactory.getLogger(MicroserviceIntegrationController.class);
    
    @Autowired
    private MicroserviceIntegrationService microserviceIntegrationService;
    
    /**
     * Check health of all microservices
     */
    @GetMapping("/health")
    public ResponseEntity<SystemHealth> checkAllServicesHealth() {
        logger.info("🔍 Health check requested for all microservices");
        
        try {
            SystemHealth systemHealth = microserviceIntegrationService.checkAllServicesHealth();
            return ResponseEntity.ok(systemHealth);
        } catch (Exception e) {
            logger.error("Error checking microservices health: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Execute performance tests on specified services
     */
    @PostMapping("/performance-tests")
    public ResponseEntity<TestResult> executePerformanceTests(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        List<String> services = (List<String>) request.get("services");
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) request.get("parameters");
        
        logger.info("🚀 Performance test requested for services: {}", services);
        
        try {
            TestResult result = microserviceIntegrationService.executePerformanceTests(services, parameters);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error executing performance tests: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Execute integration tests on specified services
     */
    @PostMapping("/integration-tests")
    public ResponseEntity<TestResult> executeIntegrationTests(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        List<String> services = (List<String>) request.get("services");
        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) request.get("parameters");
        
        logger.info("🔗 Integration test requested for services: {}", services);
        
        try {
            TestResult result = microserviceIntegrationService.executeIntegrationTests(services, parameters);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error executing integration tests: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Analyze failures for specified services
     */
    @PostMapping("/failure-analysis")
    public ResponseEntity<List<TestFailure>> analyzeFailures(
            @RequestBody Map<String, Object> request) {
        
        @SuppressWarnings("unchecked")
        List<String> services = (List<String>) request.get("services");
        
        logger.info("🔍 Failure analysis requested for services: {}", services);
        
        try {
            List<TestFailure> failures = microserviceIntegrationService.analyzeFailures(services);
            return ResponseEntity.ok(failures);
        } catch (Exception e) {
            logger.error("Error analyzing failures: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get performance metrics for a specific service
     */
    @GetMapping("/metrics/{serviceName}")
    public ResponseEntity<PerformanceMetrics> getServiceMetrics(
            @PathVariable String serviceName) {
        
        logger.info("📊 Metrics requested for service: {}", serviceName);
        
        try {
            PerformanceMetrics metrics = microserviceIntegrationService.getServiceMetrics(serviceName);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("Error getting metrics for {}: {}", serviceName, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Execute comprehensive end-to-end test
     */
    @PostMapping("/end-to-end-test")
    public ResponseEntity<Map<String, Object>> executeEndToEndTest(
            @RequestBody Map<String, Object> request) {
        
        logger.info("🎯 End-to-end test requested");
        
        try {
            // 1. Check system health
            SystemHealth systemHealth = microserviceIntegrationService.checkAllServicesHealth();
            
            // 2. Execute performance tests
            @SuppressWarnings("unchecked")
            List<String> services = (List<String>) request.getOrDefault("services", 
                List.of("user-service", "product-service", "order-service"));
            
            TestResult performanceResult = microserviceIntegrationService.executePerformanceTests(
                services, Map.of("duration", "5m", "load", "high"));
            
            // 3. Execute integration tests
            TestResult integrationResult = microserviceIntegrationService.executeIntegrationTests(
                services, Map.of("scenarios", "full"));
            
            // 4. Analyze failures
            List<TestFailure> failures = microserviceIntegrationService.analyzeFailures(services);
            
            // 5. Compile results
            Map<String, Object> results = Map.of(
                "systemHealth", systemHealth,
                "performanceTests", performanceResult,
                "integrationTests", integrationResult,
                "failures", failures,
                "timestamp", java.time.LocalDateTime.now(),
                "overallStatus", determineOverallStatus(systemHealth, performanceResult, integrationResult, failures)
            );
            
            return ResponseEntity.ok(results);
            
        } catch (Exception e) {
            logger.error("Error executing end-to-end test: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Determine overall test status
     */
    private String determineOverallStatus(SystemHealth systemHealth, TestResult performanceResult, 
                                        TestResult integrationResult, List<TestFailure> failures) {
        
        if (systemHealth.getOverallStatus() != HealthStatus.HEALTHY) {
            return "FAILED - System health issues";
        }
        
        if (performanceResult.getStatus() == TestStatus.FAILED) {
            return "FAILED - Performance tests failed";
        }
        
        if (integrationResult.getStatus() == TestStatus.FAILED) {
            return "FAILED - Integration tests failed";
        }
        
        if (!failures.isEmpty()) {
            return "DEGRADED - Failures detected";
        }
        
        return "PASSED - All tests successful";
    }
}
