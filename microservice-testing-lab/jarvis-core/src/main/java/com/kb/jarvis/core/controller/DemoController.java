package com.kb.jarvis.core.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    // Mock test data for demo purposes
    private final List<Map<String, Object>> mockTestResults = createMockTestData();

    @GetMapping("/contract-tests/{serviceName}")
    public ResponseEntity<Map<String, Object>> runContractTests(@PathVariable String serviceName) {
        Map<String, Object> response = new HashMap<>();
        
        // Simulate contract test execution
        List<Map<String, Object>> contractTests = getContractTestsForService(serviceName);
        
        response.put("service", serviceName);
        response.put("testType", "CONTRACT_TEST");
        response.put("totalTests", contractTests.size());
        response.put("passedTests", contractTests.stream().mapToInt(t -> (Boolean) t.get("passed") ? 1 : 0).sum());
        response.put("failedTests", contractTests.stream().mapToInt(t -> (Boolean) t.get("passed") ? 0 : 1).sum());
        response.put("executionTime", "2.3s");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("tests", contractTests);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/integration-tests/{serviceName}")
    public ResponseEntity<Map<String, Object>> runIntegrationTests(@PathVariable String serviceName) {
        Map<String, Object> response = new HashMap<>();
        
        // Simulate integration test execution with more realistic timing
        List<Map<String, Object>> integrationTests = getIntegrationTestsForService(serviceName);
        
        response.put("service", serviceName);
        response.put("testType", "INTEGRATION_TEST");
        response.put("totalTests", integrationTests.size());
        response.put("passedTests", integrationTests.stream().mapToInt(t -> (Boolean) t.get("passed") ? 1 : 0).sum());
        response.put("failedTests", integrationTests.stream().mapToInt(t -> (Boolean) t.get("passed") ? 0 : 1).sum());
        response.put("executionTime", calculateIntegrationTestTime(integrationTests));
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("tests", integrationTests);
        response.put("environment", "test");
        response.put("database", "H2");
        response.put("message", "Integration tests completed successfully");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contract-tests/failure-analysis")
    public ResponseEntity<Map<String, Object>> getContractTestFailureAnalysis() {
        Map<String, Object> response = new HashMap<>();
        
        // Find the most recent contract test failure
        Optional<Map<String, Object>> lastFailure = mockTestResults.stream()
            .filter(test -> "CONTRACT_TEST".equals(test.get("testType")))
            .filter(test -> "FAILED".equals(test.get("status")))
            .max(Comparator.comparing(test -> (LocalDateTime) test.get("timestamp")));
        
        if (lastFailure.isPresent()) {
            Map<String, Object> failure = lastFailure.get();
            response.put("lastFailureFound", true);
            response.put("service", failure.get("serviceName"));
            response.put("testName", failure.get("testName"));
            response.put("failureTime", failure.get("timestamp"));
            response.put("errorMessage", failure.get("errorMessage"));
            response.put("daysSinceFailure", calculateDaysSince((LocalDateTime) failure.get("timestamp")));
        } else {
            response.put("lastFailureFound", false);
            response.put("message", "No contract test failures found in recent history");
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/coverage-analysis")
    public ResponseEntity<Map<String, Object>> getCoverageAnalysis() {
        Map<String, Object> response = new HashMap<>();
        
        // Calculate test coverage by service
        Map<String, Map<String, Integer>> serviceCoverage = new HashMap<>();
        
        for (Map<String, Object> test : mockTestResults) {
            String serviceName = (String) test.get("serviceName");
            String testType = (String) test.get("testType");
            
            serviceCoverage.computeIfAbsent(serviceName, k -> new HashMap<>())
                .merge(testType, 1, Integer::sum);
        }
        
        // Find service with most total tests
        String serviceWithMostTests = serviceCoverage.entrySet().stream()
            .max(Comparator.comparing(entry -> 
                entry.getValue().values().stream().mapToInt(Integer::intValue).sum()))
            .map(Map.Entry::getKey)
            .orElse("No services found");
        
        response.put("serviceCoverage", serviceCoverage);
        response.put("serviceWithMostTests", serviceWithMostTests);
        response.put("totalServices", serviceCoverage.size());
        response.put("analysisTimestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/services")
    public ResponseEntity<List<String>> getAvailableServices() {
        List<String> services = Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service");
        return ResponseEntity.ok(services);
    }

    @GetMapping("/test-summary")
    public ResponseEntity<Map<String, Object>> getTestSummary() {
        Map<String, Object> response = new HashMap<>();
        
        long totalTests = mockTestResults.size();
        long passedTests = mockTestResults.stream().mapToLong(t -> "PASSED".equals(t.get("status")) ? 1 : 0).sum();
        long failedTests = mockTestResults.stream().mapToLong(t -> "FAILED".equals(t.get("status")) ? 1 : 0).sum();
        
        response.put("totalTests", totalTests);
        response.put("passedTests", passedTests);
        response.put("failedTests", failedTests);
        response.put("successRate", totalTests > 0 ? (double) passedTests / totalTests * 100 : 0);
        response.put("lastUpdated", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return ResponseEntity.ok(response);
    }

    private List<Map<String, Object>> getContractTestsForService(String serviceName) {
        return mockTestResults.stream()
            .filter(test -> serviceName.equals(test.get("serviceName")))
            .filter(test -> "CONTRACT_TEST".equals(test.get("testType")))
            .collect(ArrayList::new, (list, test) -> {
                Map<String, Object> contractTest = new HashMap<>();
                contractTest.put("testName", test.get("testName"));
                contractTest.put("passed", "PASSED".equals(test.get("status")));
                contractTest.put("executionTime", test.get("executionTimeMs") + "ms");
                contractTest.put("timestamp", test.get("timestamp"));
                if ("FAILED".equals(test.get("status"))) {
                    contractTest.put("errorMessage", test.get("errorMessage"));
                }
                list.add(contractTest);
            }, ArrayList::addAll);
    }

    private List<Map<String, Object>> getIntegrationTestsForService(String serviceName) {
        return mockTestResults.stream()
            .filter(test -> serviceName.equals(test.get("serviceName")))
            .filter(test -> "INTEGRATION_TEST".equals(test.get("testType")))
            .collect(ArrayList::new, (list, test) -> {
                Map<String, Object> integrationTest = new HashMap<>();
                integrationTest.put("testName", test.get("testName"));
                integrationTest.put("passed", "PASSED".equals(test.get("status")));
                integrationTest.put("executionTime", test.get("executionTimeMs") + "ms");
                integrationTest.put("timestamp", test.get("timestamp"));
                integrationTest.put("testCategory", "Integration");
                integrationTest.put("dependencies", getTestDependencies(test.get("testName").toString()));
                if ("FAILED".equals(test.get("status"))) {
                    integrationTest.put("errorMessage", test.get("errorMessage"));
                }
                list.add(integrationTest);
            }, ArrayList::addAll);
    }

    private String calculateIntegrationTestTime(List<Map<String, Object>> tests) {
        if (tests.isEmpty()) return "0ms";
        
        long totalTime = tests.stream()
            .mapToLong(test -> {
                String execTime = test.get("executionTime").toString();
                return Long.parseLong(execTime.replace("ms", ""));
            })
            .sum();
        
        if (totalTime > 1000) {
            return String.format("%.1fs", totalTime / 1000.0);
        } else {
            return totalTime + "ms";
        }
    }

    private List<String> getTestDependencies(String testName) {
        List<String> dependencies = new ArrayList<>();
        
        if (testName.contains("User")) {
            dependencies.add("UserRepository");
            dependencies.add("UserService");
            dependencies.add("H2 Database");
        } else if (testName.contains("Product")) {
            dependencies.add("ProductRepository");
            dependencies.add("ProductService");
            dependencies.add("H2 Database");
        } else if (testName.contains("Order")) {
            dependencies.add("OrderRepository");
            dependencies.add("OrderService");
            dependencies.add("UserService");
            dependencies.add("ProductService");
            dependencies.add("H2 Database");
        } else if (testName.contains("Notification")) {
            dependencies.add("NotificationService");
            dependencies.add("EmailService");
            dependencies.add("SMSService");
        } else if (testName.contains("Gateway")) {
            dependencies.add("GatewayService");
            dependencies.add("AuthenticationService");
            dependencies.add("RateLimitingService");
        }
        
        return dependencies;
    }

    private long calculateDaysSince(LocalDateTime timestamp) {
        return java.time.Duration.between(timestamp, LocalDateTime.now()).toDays();
    }

    private List<Map<String, Object>> createMockTestData() {
        List<Map<String, Object>> testData = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // User Service Tests
        testData.add(createTestResult("user-service", "UserServiceContractTest.testCreateUser", "CONTRACT_TEST", "PASSED", now.minusHours(2), 45));
        testData.add(createTestResult("user-service", "UserServiceContractTest.testGetUser", "CONTRACT_TEST", "PASSED", now.minusHours(2), 32));
        testData.add(createTestResult("user-service", "UserServiceContractTest.testUpdateUser", "CONTRACT_TEST", "FAILED", now.minusDays(3), 67, "Contract mismatch: expected field 'email' but received 'emailAddress'"));
        testData.add(createTestResult("user-service", "UserServiceTest.testCreateUser", "UNIT_TEST", "PASSED", now.minusHours(1), 23));
        testData.add(createTestResult("user-service", "UserServiceTest.testValidateUser", "UNIT_TEST", "PASSED", now.minusHours(1), 18));
        testData.add(createTestResult("user-service", "UserServiceIntegrationTest.testUserFlow", "INTEGRATION_TEST", "PASSED", now.minusHours(3), 156));
        
        // Product Service Tests
        testData.add(createTestResult("product-service", "ProductServiceContractTest.testGetProduct", "CONTRACT_TEST", "PASSED", now.minusHours(1), 38));
        testData.add(createTestResult("product-service", "ProductServiceContractTest.testCreateProduct", "CONTRACT_TEST", "PASSED", now.minusHours(1), 52));
        testData.add(createTestResult("product-service", "ProductServiceContractTest.testUpdateProduct", "CONTRACT_TEST", "PASSED", now.minusHours(1), 41));
        testData.add(createTestResult("product-service", "ProductServiceTest.testProductValidation", "UNIT_TEST", "PASSED", now.minusMinutes(30), 15));
        testData.add(createTestResult("product-service", "ProductServiceTest.testProductSearch", "UNIT_TEST", "PASSED", now.minusMinutes(30), 28));
        testData.add(createTestResult("product-service", "ProductServiceIntegrationTest.testProductCatalog", "INTEGRATION_TEST", "PASSED", now.minusHours(2), 89));
        testData.add(createTestResult("product-service", "ProductServiceSecurityTest.testProductAccess", "SECURITY_TEST", "PASSED", now.minusHours(4), 134));
        
        // Order Service Tests
        testData.add(createTestResult("order-service", "OrderServiceContractTest.testCreateOrder", "CONTRACT_TEST", "PASSED", now.minusMinutes(45), 78));
        testData.add(createTestResult("order-service", "OrderServiceContractTest.testGetOrder", "CONTRACT_TEST", "PASSED", now.minusMinutes(45), 34));
        testData.add(createTestResult("order-service", "OrderServiceTest.testOrderValidation", "UNIT_TEST", "PASSED", now.minusMinutes(20), 22));
        testData.add(createTestResult("order-service", "OrderServiceTest.testOrderCalculation", "UNIT_TEST", "PASSED", now.minusMinutes(20), 31));
        testData.add(createTestResult("order-service", "OrderServiceIntegrationTest.testOrderFlow", "INTEGRATION_TEST", "PASSED", now.minusHours(1), 203));
        testData.add(createTestResult("order-service", "OrderServicePerformanceTest.testOrderProcessing", "PERFORMANCE_TEST", "PASSED", now.minusHours(5), 1200));
        
        // Notification Service Tests
        testData.add(createTestResult("notification-service", "NotificationServiceContractTest.testSendNotification", "CONTRACT_TEST", "PASSED", now.minusMinutes(30), 56));
        testData.add(createTestResult("notification-service", "NotificationServiceTest.testEmailTemplate", "UNIT_TEST", "PASSED", now.minusMinutes(15), 19));
        testData.add(createTestResult("notification-service", "NotificationServiceTest.testSmsTemplate", "UNIT_TEST", "PASSED", now.minusMinutes(15), 17));
        testData.add(createTestResult("notification-service", "NotificationServiceIntegrationTest.testNotificationFlow", "INTEGRATION_TEST", "PASSED", now.minusHours(2), 145));
        
        // Gateway Service Tests
        testData.add(createTestResult("gateway-service", "GatewayServiceContractTest.testRouteRequest", "CONTRACT_TEST", "PASSED", now.minusMinutes(15), 42));
        testData.add(createTestResult("gateway-service", "GatewayServiceTest.testAuthentication", "UNIT_TEST", "PASSED", now.minusMinutes(10), 25));
        testData.add(createTestResult("gateway-service", "GatewayServiceTest.testRateLimiting", "UNIT_TEST", "PASSED", now.minusMinutes(10), 33));
        testData.add(createTestResult("gateway-service", "GatewayServiceIntegrationTest.testRequestRouting", "INTEGRATION_TEST", "PASSED", now.minusHours(1), 98));
        testData.add(createTestResult("gateway-service", "GatewayServiceSecurityTest.testSecurityHeaders", "SECURITY_TEST", "PASSED", now.minusHours(3), 87));
        testData.add(createTestResult("gateway-service", "GatewayServicePerformanceTest.testLoadBalancing", "PERFORMANCE_TEST", "PASSED", now.minusHours(6), 2100));
        
        return testData;
    }

    private Map<String, Object> createTestResult(String serviceName, String testName, String testType, String status, LocalDateTime timestamp, long executionTimeMs) {
        return createTestResult(serviceName, testName, testType, status, timestamp, executionTimeMs, null);
    }

    private Map<String, Object> createTestResult(String serviceName, String testName, String testType, String status, LocalDateTime timestamp, long executionTimeMs, String errorMessage) {
        Map<String, Object> testResult = new HashMap<>();
        testResult.put("serviceName", serviceName);
        testResult.put("testName", testName);
        testResult.put("testType", testType);
        testResult.put("status", status);
        testResult.put("executionTimeMs", executionTimeMs);
        testResult.put("timestamp", timestamp);
        if (errorMessage != null) {
            testResult.put("errorMessage", errorMessage);
        }
        return testResult;
    }
}