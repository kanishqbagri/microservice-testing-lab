package com.kb.jarvis.core.service;

import com.kb.jarvis.core.model.TestResult;
import com.kb.jarvis.core.model.TestStatus;
import com.kb.jarvis.core.model.TestType;
import com.kb.jarvis.core.model.RiskLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Service
public class TestDataInitializationService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestResultService testResultService;

    @PostConstruct
    @Transactional
    public void initializeTestData() {
        try {
            // Check if data already exists
            Long count = testResultService.getTotalCount();
            if (count > 0) {
                System.out.println("Test data already exists. Skipping initialization.");
                return;
            }

            System.out.println("Initializing test data...");
            
            // Create comprehensive test data programmatically
            createComprehensiveTestData();
            
            System.out.println("Test data initialization completed successfully!");
            
            // Verify data was loaded
            Long finalCount = testResultService.getTotalCount();
            System.out.println("Total test results loaded: " + finalCount);
            
        } catch (Exception e) {
            System.err.println("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createComprehensiveTestData() {
        // User Service Test Data
        createUserServiceTestData();
        
        // Product Service Test Data
        createProductServiceTestData();
        
        // Order Service Test Data
        createOrderServiceTestData();
        
        // Notification Service Test Data
        createNotificationServiceTestData();
        
        // Gateway Service Test Data
        createGatewayServiceTestData();
        
        // Performance Test Data
        createPerformanceTestData();
    }

    private void createUserServiceTestData() {
        // Unit Tests
        testResultService.createAndSaveTestResult(
            "UserServiceTest.testCreateUser", "user-service", TestType.UNIT_TEST, TestStatus.PASSED, 45L,
            Map.of("userId", "user123", "email", "test@example.com", "role", "USER"),
            Map.of("created", true, "userId", "user123", "status", "ACTIVE"),
            Map.of("memoryUsage", "12MB", "cpuUsage", "5%", "responseTime", "45ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.95, RiskLevel.LOW, "unit,user,creation"
        );

        testResultService.createAndSaveTestResult(
            "UserServiceTest.testAuthenticateUser", "user-service", TestType.UNIT_TEST, TestStatus.PASSED, 32L,
            Map.of("email", "test@example.com", "password", "hashed_password"),
            Map.of("authenticated", true, "token", "jwt_token_123", "expiresIn", "3600"),
            Map.of("memoryUsage", "8MB", "cpuUsage", "3%", "responseTime", "32ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.98, RiskLevel.LOW, "unit,user,authentication"
        );

        testResultService.createAndSaveTestResult(
            "UserServiceTest.testUpdateUserProfile", "user-service", TestType.UNIT_TEST, TestStatus.FAILED, 67L,
            Map.of("userId", "user123", "email", "invalid-email", "firstName", "John"),
            Map.of("error", "Validation failed", "field", "email", "message", "Invalid email format"),
            Map.of("memoryUsage", "15MB", "cpuUsage", "8%", "responseTime", "67ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.85, RiskLevel.MEDIUM, "unit,user,validation"
        );

        // Integration Tests
        testResultService.createAndSaveTestResult(
            "UserServiceIntegrationTest.testUserRegistrationFlow", "user-service", TestType.INTEGRATION_TEST, TestStatus.PASSED, 1250L,
            Map.of("email", "newuser@example.com", "password", "securePass123", "firstName", "Jane", "lastName", "Doe"),
            Map.of("userId", "user456", "status", "ACTIVE", "verificationRequired", true, "welcomeEmailSent", true),
            Map.of("memoryUsage", "45MB", "cpuUsage", "25%", "responseTime", "1250ms", "databaseQueries", 8),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "database", "postgresql"),
            0.92, RiskLevel.LOW, "integration,user,registration"
        );

        // Security Tests
        testResultService.createAndSaveTestResult(
            "UserServiceSecurityTest.testSQLInjectionPrevention", "user-service", TestType.SECURITY_TEST, TestStatus.PASSED, 156L,
            Map.of("email", "admin@example.com'; DROP TABLE users; --", "password", "password123"),
            Map.of("authenticated", false, "error", "Invalid credentials", "securityEvent", "SQL injection attempt blocked"),
            Map.of("memoryUsage", "22MB", "cpuUsage", "12%", "responseTime", "156ms", "securityChecks", 15),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "security", "enabled"),
            0.99, RiskLevel.LOW, "security,user,sql-injection"
        );
    }

    private void createProductServiceTestData() {
        // Unit Tests
        testResultService.createAndSaveTestResult(
            "ProductServiceTest.testCreateProduct", "product-service", TestType.UNIT_TEST, TestStatus.PASSED, 38L,
            Map.of("name", "Test Product", "price", 29.99, "category", "Electronics", "stock", 100),
            Map.of("productId", "prod123", "created", true, "status", "ACTIVE", "sku", "TEST-001"),
            Map.of("memoryUsage", "10MB", "cpuUsage", "4%", "responseTime", "38ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.96, RiskLevel.LOW, "unit,product,creation"
        );

        testResultService.createAndSaveTestResult(
            "ProductServiceTest.testUpdateProductPrice", "product-service", TestType.UNIT_TEST, TestStatus.PASSED, 42L,
            Map.of("productId", "prod123", "newPrice", 24.99, "reason", "Sale"),
            Map.of("updated", true, "oldPrice", 29.99, "newPrice", 24.99, "priceHistory", "updated"),
            Map.of("memoryUsage", "11MB", "cpuUsage", "5%", "responseTime", "42ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.94, RiskLevel.LOW, "unit,product,pricing"
        );

        // Integration Tests
        testResultService.createAndSaveTestResult(
            "ProductServiceIntegrationTest.testProductCatalogWithDatabase", "product-service", TestType.INTEGRATION_TEST, TestStatus.PASSED, 1100L,
            Map.of("category", "Electronics", "page", 0, "size", 20, "sortBy", "price"),
            Map.of("products", List.of(Map.of("id", "prod123", "name", "Test Product", "price", 24.99)), "totalElements", 1, "totalPages", 1),
            Map.of("memoryUsage", "52MB", "cpuUsage", "28%", "responseTime", "1100ms", "databaseQueries", 12),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "database", "postgresql"),
            0.91, RiskLevel.LOW, "integration,product,catalog"
        );

        // Security Tests
        testResultService.createAndSaveTestResult(
            "ProductServiceSecurityTest.testPriceManipulationPrevention", "product-service", TestType.SECURITY_TEST, TestStatus.PASSED, 189L,
            Map.of("productId", "prod123", "newPrice", -10.00, "userId", "user123", "role", "CUSTOMER"),
            Map.of("error", "Access denied", "message", "Insufficient privileges to modify product price", "securityEvent", "Unauthorized price modification attempt"),
            Map.of("memoryUsage", "25MB", "cpuUsage", "13%", "responseTime", "189ms", "securityChecks", 12),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "security", "enabled"),
            0.98, RiskLevel.MEDIUM, "security,product,authorization"
        );
    }

    private void createOrderServiceTestData() {
        // Unit Tests
        testResultService.createAndSaveTestResult(
            "OrderServiceTest.testCreateOrder", "order-service", TestType.UNIT_TEST, TestStatus.PASSED, 52L,
            Map.of("userId", "user123", "items", List.of(Map.of("productId", "prod123", "quantity", 2, "price", 24.99)), "total", 49.98),
            Map.of("orderId", "order123", "status", "PENDING", "total", 49.98, "items", 1),
            Map.of("memoryUsage", "14MB", "cpuUsage", "6%", "responseTime", "52ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.95, RiskLevel.LOW, "unit,order,creation"
        );

        testResultService.createAndSaveTestResult(
            "OrderServiceTest.testProcessPayment", "order-service", TestType.UNIT_TEST, TestStatus.PASSED, 78L,
            Map.of("orderId", "order123", "paymentMethod", "CREDIT_CARD", "amount", 49.98, "cardToken", "card_token_123"),
            Map.of("paymentId", "payment123", "status", "SUCCESS", "transactionId", "txn_456", "amount", 49.98),
            Map.of("memoryUsage", "16MB", "cpuUsage", "8%", "responseTime", "78ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.97, RiskLevel.LOW, "unit,order,payment"
        );

        // Integration Tests
        testResultService.createAndSaveTestResult(
            "OrderServiceIntegrationTest.testCompleteOrderFlow", "order-service", TestType.INTEGRATION_TEST, TestStatus.PASSED, 2100L,
            Map.of("userId", "user123", "items", List.of(Map.of("productId", "prod123", "quantity", 1)), "paymentMethod", "CREDIT_CARD"),
            Map.of("orderId", "order456", "status", "CONFIRMED", "paymentStatus", "SUCCESS", "inventoryUpdated", true, "notificationSent", true),
            Map.of("memoryUsage", "68MB", "cpuUsage", "35%", "responseTime", "2100ms", "databaseQueries", 15, "externalCalls", 3),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "database", "postgresql"),
            0.90, RiskLevel.LOW, "integration,order,complete-flow"
        );

        // Security Tests
        testResultService.createAndSaveTestResult(
            "OrderServiceSecurityTest.testOrderAccessControl", "order-service", TestType.SECURITY_TEST, TestStatus.PASSED, 167L,
            Map.of("orderId", "order123", "requestingUserId", "user456", "action", "VIEW_ORDER"),
            Map.of("error", "Access denied", "message", "User can only access their own orders", "securityEvent", "Unauthorized order access attempt"),
            Map.of("memoryUsage", "20MB", "cpuUsage", "10%", "responseTime", "167ms", "securityChecks", 8),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "security", "enabled"),
            0.96, RiskLevel.MEDIUM, "security,order,access-control"
        );
    }

    private void createNotificationServiceTestData() {
        // Unit Tests
        testResultService.createAndSaveTestResult(
            "NotificationServiceTest.testSendEmail", "notification-service", TestType.UNIT_TEST, TestStatus.PASSED, 89L,
            Map.of("to", "user@example.com", "subject", "Order Confirmation", "template", "order-confirmation", "data", Map.of("orderId", "order123")),
            Map.of("messageId", "msg123", "status", "SENT", "deliveryTime", "2025-01-27T10:45:00.089"),
            Map.of("memoryUsage", "18MB", "cpuUsage", "9%", "responseTime", "89ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.94, RiskLevel.LOW, "unit,notification,email"
        );

        testResultService.createAndSaveTestResult(
            "NotificationServiceTest.testSendSMS", "notification-service", TestType.UNIT_TEST, TestStatus.PASSED, 76L,
            Map.of("to", "+1234567890", "message", "Your order #order123 has been confirmed", "priority", "HIGH"),
            Map.of("messageId", "sms123", "status", "SENT", "deliveryTime", "2025-01-27T10:46:00.076"),
            Map.of("memoryUsage", "15MB", "cpuUsage", "7%", "responseTime", "76ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.96, RiskLevel.LOW, "unit,notification,sms"
        );

        // Integration Tests
        testResultService.createAndSaveTestResult(
            "NotificationServiceIntegrationTest.testOrderNotificationFlow", "notification-service", TestType.INTEGRATION_TEST, TestStatus.PASSED, 1200L,
            Map.of("orderId", "order123", "userId", "user123", "notificationTypes", List.of("EMAIL", "SMS"), "template", "order-confirmation"),
            Map.of("notifications", List.of(Map.of("type", "EMAIL", "status", "SENT"), Map.of("type", "SMS", "status", "SENT")), "totalSent", 2),
            Map.of("memoryUsage", "45MB", "cpuUsage", "22%", "responseTime", "1200ms", "externalCalls", 2),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "externalServices", "enabled"),
            0.91, RiskLevel.LOW, "integration,notification,order-flow"
        );

        // Security Tests
        testResultService.createAndSaveTestResult(
            "NotificationServiceSecurityTest.testEmailInjectionPrevention", "notification-service", TestType.SECURITY_TEST, TestStatus.PASSED, 134L,
            Map.of("to", "user@example.com\nBcc: hacker@evil.com", "subject", "Test", "body", "Hello"),
            Map.of("error", "Invalid email format", "message", "Email injection attempt detected", "securityEvent", "Email injection blocked"),
            Map.of("memoryUsage", "19MB", "cpuUsage", "9%", "responseTime", "134ms", "securityChecks", 6),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "security", "enabled"),
            0.98, RiskLevel.MEDIUM, "security,notification,email-injection"
        );
    }

    private void createGatewayServiceTestData() {
        // Unit Tests
        testResultService.createAndSaveTestResult(
            "GatewayServiceTest.testRouteRequest", "gateway-service", TestType.UNIT_TEST, TestStatus.PASSED, 34L,
            Map.of("path", "/api/users", "method", "GET", "headers", Map.of("Authorization", "Bearer token123")),
            Map.of("routed", true, "targetService", "user-service", "path", "/users", "loadBalanced", true),
            Map.of("memoryUsage", "9MB", "cpuUsage", "4%", "responseTime", "34ms"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test"),
            0.97, RiskLevel.LOW, "unit,gateway,routing"
        );

        // Integration Tests
        testResultService.createAndSaveTestResult(
            "GatewayServiceIntegrationTest.testEndToEndRouting", "gateway-service", TestType.INTEGRATION_TEST, TestStatus.PASSED, 850L,
            Map.of("path", "/api/orders", "method", "POST", "body", Map.of("userId", "user123", "items", List.of()), "headers", Map.of("Authorization", "Bearer token123")),
            Map.of("status", 201, "responseTime", "850ms", "targetService", "order-service", "loadBalanced", true),
            Map.of("memoryUsage", "35MB", "cpuUsage", "18%", "responseTime", "850ms", "circuitBreaker", "CLOSED"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "loadBalancer", "enabled"),
            0.93, RiskLevel.LOW, "integration,gateway,end-to-end"
        );

        // Security Tests
        testResultService.createAndSaveTestResult(
            "GatewayServiceSecurityTest.testJWTValidation", "gateway-service", TestType.SECURITY_TEST, TestStatus.PASSED, 156L,
            Map.of("token", "invalid.jwt.token", "path", "/api/users/profile", "method", "GET"),
            Map.of("authenticated", false, "error", "Invalid JWT token", "securityEvent", "Invalid token attempt"),
            Map.of("memoryUsage", "24MB", "cpuUsage", "12%", "responseTime", "156ms", "securityChecks", 10),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "security", "enabled"),
            0.97, RiskLevel.MEDIUM, "security,gateway,jwt"
        );
    }

    private void createPerformanceTestData() {
        testResultService.createAndSaveTestResult(
            "PerformanceTest.testUserServiceLoad", "user-service", TestType.PERFORMANCE_TEST, TestStatus.PASSED, 30000L,
            Map.of("concurrentUsers", 100, "duration", "30s", "endpoint", "/api/users", "rampUpTime", "10s"),
            Map.of("totalRequests", 1500, "successfulRequests", 1485, "failedRequests", 15, "avgResponseTime", "180ms", "p95ResponseTime", "450ms"),
            Map.of("memoryUsage", "256MB", "cpuUsage", "85%", "responseTime", "180ms", "throughput", "50 req/s", "errorRate", "1%"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "loadTest", "enabled"),
            0.88, RiskLevel.MEDIUM, "performance,user,load-test"
        );

        testResultService.createAndSaveTestResult(
            "PerformanceTest.testOrderServiceSpike", "order-service", TestType.PERFORMANCE_TEST, TestStatus.FAILED, 45000L,
            Map.of("concurrentUsers", 500, "duration", "45s", "endpoint", "/api/orders", "spikePattern", "sudden"),
            Map.of("totalRequests", 2000, "successfulRequests", 1200, "failedRequests", 800, "avgResponseTime", "1200ms", "p95ResponseTime", "3000ms"),
            Map.of("memoryUsage", "1GB", "cpuUsage", "98%", "responseTime", "1200ms", "throughput", "27 req/s", "errorRate", "40%"),
            Map.of("javaVersion", "17", "springBootVersion", "3.2.0", "environment", "test", "loadTest", "enabled"),
            0.75, RiskLevel.CRITICAL, "performance,order,spike-test"
        );
    }

    public void reloadTestData() {
        // Clear existing data
        jdbcTemplate.execute("DELETE FROM test_results");
        
        // Reinitialize
        initializeTestData();
    }

    public List<String> getServiceNames() {
        return jdbcTemplate.queryForList(
            "SELECT DISTINCT service_name FROM test_results ORDER BY service_name", 
            String.class
        );
    }

    public List<String> getTestTypes() {
        return jdbcTemplate.queryForList(
            "SELECT DISTINCT test_type FROM test_results ORDER BY test_type", 
            String.class
        );
    }
}
