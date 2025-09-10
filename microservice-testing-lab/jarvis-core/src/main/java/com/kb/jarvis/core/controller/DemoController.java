package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.model.TestResult;
import com.kb.jarvis.core.model.TestStatus;
import com.kb.jarvis.core.model.TestType;
import com.kb.jarvis.core.model.RiskLevel;
import com.kb.jarvis.core.service.TestResultService;
import com.kb.jarvis.core.service.TestDataInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/demo")
public class DemoController {

    @Autowired
    private TestResultService testResultService;

    @Autowired
    private TestDataInitializationService testDataService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDemoOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        // Basic statistics from database
        overview.put("totalTests", testResultService.getTotalCount());
        overview.put("passedTests", testResultService.getPassedCount());
        overview.put("failedTests", testResultService.getFailedCount());
        overview.put("successRate", calculateSuccessRate());
        
        // Services covered from database
        overview.put("services", testDataService.getServiceNames());
        overview.put("testTypes", testDataService.getTestTypes());
        
        // Demo scenarios
        overview.put("demoScenarios", Arrays.asList(
            "Service Health Monitoring",
            "Test Execution Analysis", 
            "Security Test Results",
            "Performance Analysis",
            "Failure Pattern Recognition",
            "Cross-Service Integration"
        ));
        
        overview.put("timestamp", LocalDateTime.now());
        overview.put("status", "READY");
        
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/service/{serviceName}")
    public ResponseEntity<Map<String, Object>> getServiceHealth(@PathVariable String serviceName) {
        Map<String, Object> health = new HashMap<>();
        health.put("serviceName", serviceName);
        health.put("status", "HEALTHY");
        health.put("lastChecked", LocalDateTime.now());
        
        // Get real test results from database
        List<TestResult> recentTests = testResultService.findByServiceName(serviceName);
        health.put("recentTests", recentTests.stream().limit(10).map(this::convertToMap).toList());
        
        // Get service statistics from database
        Map<String, Object> stats = testResultService.getServiceStatistics(serviceName);
        health.putAll(stats);
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/services/{serviceName}/tests")
    public ResponseEntity<Map<String, Object>> getServiceTests(@PathVariable String serviceName) {
        Map<String, Object> response = new HashMap<>();
        
        // Get test statistics for the service from database
        response.put("serviceName", serviceName);
        Map<String, Object> stats = testResultService.getServiceStatistics(serviceName);
        response.putAll(stats);
        
        // Get test types for this service
        List<TestType> testTypes = testResultService.findByServiceName(serviceName).stream()
            .map(TestResult::getTestType)
            .distinct()
            .collect(Collectors.toList());
        response.put("testTypes", testTypes);
        
        // Get recent test results
        List<TestResult> recentTests = testResultService.findByServiceName(serviceName).stream()
            .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
            .limit(5)
            .collect(Collectors.toList());
        response.put("recentTests", recentTests.stream().map(this::convertToMap).toList());
        
        // Get performance metrics
        response.put("performanceMetrics", getServicePerformanceMetrics(serviceName));
        
        // Get security test results
        List<TestResult> securityTests = testResultService.findByServiceNameAndTestType(serviceName, TestType.SECURITY_TEST);
        response.put("securityTests", securityTests.stream().map(this::convertToMap).toList());
        
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test-types/{testType}/analysis")
    public ResponseEntity<Map<String, Object>> getTestTypeAnalysis(@PathVariable String testType) {
        Map<String, Object> response = new HashMap<>();
        
        TestType type = TestType.valueOf(testType);
        response.put("testType", testType);
        
        // Get test type statistics from database
        Map<String, Object> stats = testResultService.getTestTypeStatistics(type);
        response.putAll(stats);
        
        // Get services using this test type
        List<String> services = testResultService.findByTestType(type).stream()
            .map(TestResult::getServiceName)
            .distinct()
            .collect(Collectors.toList());
        response.put("services", services);
        
        // Get common failure patterns
        List<TestResult> failedTests = testResultService.findByStatus(TestStatus.FAILED).stream()
            .filter(t -> t.getTestType() == type)
            .collect(Collectors.toList());
        response.put("failedTests", failedTests.stream().map(this::convertToMap).toList());
        
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/security/analysis")
    public ResponseEntity<Map<String, Object>> getSecurityAnalysis() {
        Map<String, Object> response = new HashMap<>();
        
        // Get all security tests
        List<TestResult> securityTests = testResultService.findByTestType(TestType.SECURITY_TEST);
        response.put("totalSecurityTests", securityTests.size());
        
        // Get passed security tests
        List<TestResult> passedSecurityTests = securityTests.stream()
            .filter(t -> t.getStatus() == TestStatus.PASSED)
            .collect(Collectors.toList());
        response.put("passedSecurityTests", passedSecurityTests.size());
        
        // Get failed security tests
        List<TestResult> failedSecurityTests = securityTests.stream()
            .filter(t -> t.getStatus() == TestStatus.FAILED)
            .collect(Collectors.toList());
        response.put("failedSecurityTests", failedSecurityTests.size());
        
        // Calculate security score
        double securityScore = securityTests.size() > 0 ? 
            (double) passedSecurityTests.size() / securityTests.size() * 100 : 0;
        response.put("securityScore", securityScore);
        
        // Get security test details
        response.put("securityTestDetails", securityTests.stream().map(this::convertToMap).toList());
        
        // Get risk level distribution
        Map<RiskLevel, Long> riskDistribution = securityTests.stream()
            .filter(t -> t.getRiskLevel() != null)
            .collect(Collectors.groupingBy(TestResult::getRiskLevel, Collectors.counting()));
        response.put("riskDistribution", riskDistribution);
        
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/performance/analysis")
    public ResponseEntity<Map<String, Object>> getPerformanceAnalysis() {
        Map<String, Object> response = new HashMap<>();
        
        // Get all performance tests
        List<TestResult> performanceTests = testResultService.findByTestType(TestType.PERFORMANCE_TEST);
        response.put("totalPerformanceTests", performanceTests.size());
        
        // Get performance test details
        response.put("performanceTestDetails", performanceTests.stream().map(this::convertToMap).toList());
        
        // Calculate average performance metrics
        double avgResponseTime = performanceTests.stream()
            .filter(t -> t.getExecutionTimeMs() != null)
            .mapToLong(TestResult::getExecutionTimeMs)
            .average()
            .orElse(0.0);
        response.put("averageResponseTime", avgResponseTime);
        
        // Get slow tests
        List<TestResult> slowTests = testResultService.findSlowTests(1000L); // Tests taking more than 1 second
        response.put("slowTests", slowTests.stream().map(this::convertToMap).toList());
        
        // Get performance anomalies
        List<String> services = testDataService.getServiceNames();
        Map<String, List<TestResult>> anomalies = new HashMap<>();
        for (String service : services) {
            List<TestResult> serviceAnomalies = testResultService.findPerformanceAnomalies(service);
            if (!serviceAnomalies.isEmpty()) {
                anomalies.put(service, serviceAnomalies);
            }
        }
        response.put("performanceAnomalies", anomalies);
        
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/failures/analysis")
    public ResponseEntity<Map<String, Object>> getFailureAnalysis() {
        Map<String, Object> response = new HashMap<>();
        
        // Get all failed tests
        List<TestResult> failedTests = testResultService.findByStatus(TestStatus.FAILED);
        response.put("totalFailedTests", failedTests.size());
        
        // Get failed tests by service
        Map<String, Long> failuresByService = failedTests.stream()
            .collect(Collectors.groupingBy(TestResult::getServiceName, Collectors.counting()));
        response.put("failuresByService", failuresByService);
        
        // Get failed tests by test type
        Map<TestType, Long> failuresByType = failedTests.stream()
            .collect(Collectors.groupingBy(TestResult::getTestType, Collectors.counting()));
        response.put("failuresByType", failuresByType);
        
        // Get recent failures
        List<TestResult> recentFailures = failedTests.stream()
            .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
            .limit(10)
            .collect(Collectors.toList());
        response.put("recentFailures", recentFailures.stream().map(this::convertToMap).toList());
        
        // Get tests with errors
        List<TestResult> testsWithErrors = testResultService.findTestsWithErrors();
        response.put("testsWithErrors", testsWithErrors.stream().map(this::convertToMap).toList());
        
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/integration/analysis")
    public ResponseEntity<Map<String, Object>> getIntegrationAnalysis() {
        Map<String, Object> response = new HashMap<>();
        
        // Get all integration tests
        List<TestResult> integrationTests = testResultService.findByTestType(TestType.INTEGRATION_TEST);
        response.put("totalIntegrationTests", integrationTests.size());
        
        // Get integration test details
        response.put("integrationTestDetails", integrationTests.stream().map(this::convertToMap).toList());
        
        // Get integration tests by service
        Map<String, List<TestResult>> integrationByService = integrationTests.stream()
            .collect(Collectors.groupingBy(TestResult::getServiceName));
        response.put("integrationByService", integrationByService);
        
        // Calculate integration success rate
        long passedIntegrationTests = integrationTests.stream()
            .filter(t -> t.getStatus() == TestStatus.PASSED)
            .count();
        double integrationSuccessRate = integrationTests.size() > 0 ? 
            (double) passedIntegrationTests / integrationTests.size() * 100 : 0;
        response.put("integrationSuccessRate", integrationSuccessRate);
        
        // Get cross-service integration tests
        List<TestResult> crossServiceTests = integrationTests.stream()
            .filter(t -> t.getTestName().toLowerCase().contains("cross") || 
                        t.getTestName().toLowerCase().contains("end-to-end") ||
                        t.getTestName().toLowerCase().contains("flow"))
            .collect(Collectors.toList());
        response.put("crossServiceTests", crossServiceTests.stream().map(this::convertToMap).toList());
        
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reload-data")
    public ResponseEntity<Map<String, Object>> reloadTestData() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            testDataService.reloadTestData();
            response.put("status", "SUCCESS");
            response.put("message", "Test data reloaded successfully");
            response.put("totalTests", testResultService.getTotalCount());
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Failed to reload test data: " + e.getMessage());
        }
        
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    // Helper methods
    private double calculateSuccessRate() {
        long total = testResultService.getTotalCount();
        long passed = testResultService.getPassedCount();
        return total > 0 ? (double) passed / total * 100 : 0;
    }

    private Map<String, Object> getServicePerformanceMetrics(String serviceName) {
        List<TestResult> serviceTests = testResultService.findByServiceName(serviceName);
        
        double avgExecutionTime = serviceTests.stream()
            .filter(t -> t.getExecutionTimeMs() != null)
            .mapToLong(TestResult::getExecutionTimeMs)
            .average()
            .orElse(0.0);
        
        long maxExecutionTime = serviceTests.stream()
            .filter(t -> t.getExecutionTimeMs() != null)
            .mapToLong(TestResult::getExecutionTimeMs)
            .max()
            .orElse(0L);
        
        return Map.of(
            "averageExecutionTime", avgExecutionTime,
            "maxExecutionTime", maxExecutionTime,
            "totalTests", serviceTests.size()
        );
    }

    private Map<String, Object> convertToMap(TestResult testResult) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", testResult.getId());
        result.put("testName", testResult.getTestName());
        result.put("serviceName", testResult.getServiceName());
        result.put("testType", testResult.getTestType());
        result.put("status", testResult.getStatus());
        result.put("executionTimeMs", testResult.getExecutionTimeMs());
        result.put("startTime", testResult.getStartTime());
        result.put("endTime", testResult.getEndTime());
        result.put("errorMessage", testResult.getErrorMessage());
        result.put("confidenceScore", testResult.getConfidenceScore());
        result.put("riskLevel", testResult.getRiskLevel());
        result.put("tags", testResult.getTags());
        result.put("testParameters", testResult.getTestParameters());
        result.put("testOutput", testResult.getTestOutput());
        result.put("performanceMetrics", testResult.getPerformanceMetrics());
        result.put("environmentInfo", testResult.getEnvironmentInfo());
        return result;
    }
}