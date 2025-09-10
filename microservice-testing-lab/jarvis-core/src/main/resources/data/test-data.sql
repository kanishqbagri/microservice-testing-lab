-- Comprehensive Test Data for Jarvis Demo
-- Covers all services: User, Product, Order, Notification, Gateway
-- Covers test types: Unit, Integration, Security

-- Clear existing data
DELETE FROM test_results;
DELETE FROM learning_data;
DELETE FROM ai_analysis_history;
DELETE FROM system_logs;

-- Reset sequences
ALTER SEQUENCE IF EXISTS test_results_id_seq RESTART WITH 1;

-- User Service Test Data
INSERT INTO test_results (id, test_name, service_name, test_type, status, execution_time_ms, start_time, end_time, 
                         error_message, stack_trace, test_parameters, test_output, performance_metrics, 
                         environment_info, confidence_score, risk_level, tags, created_at, updated_at) VALUES

-- Unit Tests for User Service
(UUID(), 'UserServiceTest.testCreateUser', 'user-service', 'UNIT_TEST', 'PASSED', 45, 
 '2025-01-27 10:00:00', '2025-01-27 10:00:00.045', NULL, NULL,
 '{"userId": "user123", "email": "test@example.com", "role": "USER"}',
 '{"created": true, "userId": "user123", "status": "ACTIVE"}',
 '{"memoryUsage": "12MB", "cpuUsage": "5%", "responseTime": "45ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.95, 'LOW', 'unit,user,creation', NOW(), NOW()),

(UUID(), 'UserServiceTest.testAuthenticateUser', 'user-service', 'UNIT_TEST', 'PASSED', 32, 
 '2025-01-27 10:01:00', '2025-01-27 10:01:00.032', NULL, NULL,
 '{"email": "test@example.com", "password": "hashed_password"}',
 '{"authenticated": true, "token": "jwt_token_123", "expiresIn": "3600"}',
 '{"memoryUsage": "8MB", "cpuUsage": "3%", "responseTime": "32ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.98, 'LOW', 'unit,user,authentication', NOW(), NOW()),

(UUID(), 'UserServiceTest.testUpdateUserProfile', 'user-service', 'UNIT_TEST', 'FAILED', 67, 
 '2025-01-27 10:02:00', '2025-01-27 10:02:00.067', 'Validation failed: Invalid email format', 
 'com.kb.user.service.UserServiceException: Invalid email format\n\tat UserServiceTest.testUpdateUserProfile(UserServiceTest.java:45)',
 '{"userId": "user123", "email": "invalid-email", "firstName": "John"}',
 '{"error": "Validation failed", "field": "email", "message": "Invalid email format"}',
 '{"memoryUsage": "15MB", "cpuUsage": "8%", "responseTime": "67ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.85, 'MEDIUM', 'unit,user,validation', NOW(), NOW()),

-- Integration Tests for User Service
(UUID(), 'UserServiceIntegrationTest.testUserRegistrationFlow', 'user-service', 'INTEGRATION_TEST', 'PASSED', 1250, 
 '2025-01-27 10:05:00', '2025-01-27 10:05:01.250', NULL, NULL,
 '{"email": "newuser@example.com", "password": "securePass123", "firstName": "Jane", "lastName": "Doe"}',
 '{"userId": "user456", "status": "ACTIVE", "verificationRequired": true, "welcomeEmailSent": true}',
 '{"memoryUsage": "45MB", "cpuUsage": "25%", "responseTime": "1250ms", "databaseQueries": 8}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "database": "postgresql"}',
 0.92, 'LOW', 'integration,user,registration', NOW(), NOW()),

(UUID(), 'UserServiceIntegrationTest.testUserLoginWithDatabase', 'user-service', 'INTEGRATION_TEST', 'PASSED', 890, 
 '2025-01-27 10:06:00', '2025-01-27 10:06:00.890', NULL, NULL,
 '{"email": "existing@example.com", "password": "userPassword123"}',
 '{"authenticated": true, "userId": "user789", "token": "jwt_token_456", "refreshToken": "refresh_789"}',
 '{"memoryUsage": "38MB", "cpuUsage": "18%", "responseTime": "890ms", "databaseQueries": 5}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "database": "postgresql"}',
 0.94, 'LOW', 'integration,user,login', NOW(), NOW()),

-- Security Tests for User Service
(UUID(), 'UserServiceSecurityTest.testSQLInjectionPrevention', 'user-service', 'SECURITY_TEST', 'PASSED', 156, 
 '2025-01-27 10:10:00', '2025-01-27 10:10:00.156', NULL, NULL,
 '{"email": "admin@example.com'; DROP TABLE users; --", "password": "password123"}',
 '{"authenticated": false, "error": "Invalid credentials", "securityEvent": "SQL injection attempt blocked"}',
 '{"memoryUsage": "22MB", "cpuUsage": "12%", "responseTime": "156ms", "securityChecks": 15}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "security": "enabled"}',
 0.99, 'LOW', 'security,user,sql-injection', NOW(), NOW()),

(UUID(), 'UserServiceSecurityTest.testPasswordBruteForceProtection', 'user-service', 'SECURITY_TEST', 'PASSED', 234, 
 '2025-01-27 10:11:00', '2025-01-27 10:11:00.234', NULL, NULL,
 '{"email": "test@example.com", "password": "wrong_password", "attempts": 5}',
 '{"authenticated": false, "accountLocked": true, "lockoutDuration": "15 minutes", "securityEvent": "Brute force attempt detected"}',
 '{"memoryUsage": "28MB", "cpuUsage": "15%", "responseTime": "234ms", "securityChecks": 8}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "security": "enabled"}',
 0.97, 'MEDIUM', 'security,user,brute-force', NOW(), NOW()),

-- Product Service Test Data
INSERT INTO test_results (id, test_name, service_name, test_type, status, execution_time_ms, start_time, end_time, 
                         error_message, stack_trace, test_parameters, test_output, performance_metrics, 
                         environment_info, confidence_score, risk_level, tags, created_at, updated_at) VALUES

-- Unit Tests for Product Service
(UUID(), 'ProductServiceTest.testCreateProduct', 'product-service', 'UNIT_TEST', 'PASSED', 38, 
 '2025-01-27 10:15:00', '2025-01-27 10:15:00.038', NULL, NULL,
 '{"name": "Test Product", "price": 29.99, "category": "Electronics", "stock": 100}',
 '{"productId": "prod123", "created": true, "status": "ACTIVE", "sku": "TEST-001"}',
 '{"memoryUsage": "10MB", "cpuUsage": "4%", "responseTime": "38ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.96, 'LOW', 'unit,product,creation', NOW(), NOW()),

(UUID(), 'ProductServiceTest.testUpdateProductPrice', 'product-service', 'UNIT_TEST', 'PASSED', 42, 
 '2025-01-27 10:16:00', '2025-01-27 10:16:00.042', NULL, NULL,
 '{"productId": "prod123", "newPrice": 24.99, "reason": "Sale"}',
 '{"updated": true, "oldPrice": 29.99, "newPrice": 24.99, "priceHistory": "updated"}',
 '{"memoryUsage": "11MB", "cpuUsage": "5%", "responseTime": "42ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.94, 'LOW', 'unit,product,pricing', NOW(), NOW()),

(UUID(), 'ProductServiceTest.testLowStockAlert', 'product-service', 'UNIT_TEST', 'FAILED', 55, 
 '2025-01-27 10:17:00', '2025-01-27 10:17:00.055', 'Stock alert threshold not configured', 
 'com.kb.product.service.ProductServiceException: Stock alert threshold not configured\n\tat ProductServiceTest.testLowStockAlert(ProductServiceTest.java:78)',
 '{"productId": "prod123", "currentStock": 5, "threshold": null}',
 '{"error": "Configuration missing", "field": "stockThreshold", "message": "Stock alert threshold not configured"}',
 '{"memoryUsage": "13MB", "cpuUsage": "6%", "responseTime": "55ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.88, 'MEDIUM', 'unit,product,stock', NOW(), NOW()),

-- Integration Tests for Product Service
(UUID(), 'ProductServiceIntegrationTest.testProductCatalogWithDatabase', 'product-service', 'INTEGRATION_TEST', 'PASSED', 1100, 
 '2025-01-27 10:20:00', '2025-01-27 10:20:01.100', NULL, NULL,
 '{"category": "Electronics", "page": 0, "size": 20, "sortBy": "price"}',
 '{"products": [{"id": "prod123", "name": "Test Product", "price": 24.99}], "totalElements": 1, "totalPages": 1}',
 '{"memoryUsage": "52MB", "cpuUsage": "28%", "responseTime": "1100ms", "databaseQueries": 12}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "database": "postgresql"}',
 0.91, 'LOW', 'integration,product,catalog', NOW(), NOW()),

(UUID(), 'ProductServiceIntegrationTest.testInventoryManagement', 'product-service', 'INTEGRATION_TEST', 'PASSED', 980, 
 '2025-01-27 10:21:00', '2025-01-27 10:21:00.980', NULL, NULL,
 '{"productId": "prod123", "quantity": 50, "operation": "ADD_STOCK"}',
 '{"productId": "prod123", "oldStock": 100, "newStock": 150, "operation": "ADD_STOCK", "timestamp": "2025-01-27T10:21:00"}',
 '{"memoryUsage": "48MB", "cpuUsage": "24%", "responseTime": "980ms", "databaseQueries": 6}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "database": "postgresql"}',
 0.93, 'LOW', 'integration,product,inventory', NOW(), NOW()),

-- Security Tests for Product Service
(UUID(), 'ProductServiceSecurityTest.testPriceManipulationPrevention', 'product-service', 'SECURITY_TEST', 'PASSED', 189, 
 '2025-01-27 10:25:00', '2025-01-27 10:25:00.189', NULL, NULL,
 '{"productId": "prod123", "newPrice": -10.00, "userId": "user123", "role": "CUSTOMER"}',
 '{"error": "Access denied", "message": "Insufficient privileges to modify product price", "securityEvent": "Unauthorized price modification attempt"}',
 '{"memoryUsage": "25MB", "cpuUsage": "13%", "responseTime": "189ms", "securityChecks": 12}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "security": "enabled"}',
 0.98, 'MEDIUM', 'security,product,authorization', NOW(), NOW()),

-- Order Service Test Data
INSERT INTO test_results (id, test_name, service_name, test_type, status, execution_time_ms, start_time, end_time, 
                         error_message, stack_trace, test_parameters, test_output, performance_metrics, 
                         environment_info, confidence_score, risk_level, tags, created_at, updated_at) VALUES

-- Unit Tests for Order Service
(UUID(), 'OrderServiceTest.testCreateOrder', 'order-service', 'UNIT_TEST', 'PASSED', 52, 
 '2025-01-27 10:30:00', '2025-01-27 10:30:00.052', NULL, NULL,
 '{"userId": "user123", "items": [{"productId": "prod123", "quantity": 2, "price": 24.99}], "total": 49.98}',
 '{"orderId": "order123", "status": "PENDING", "total": 49.98, "items": 1}',
 '{"memoryUsage": "14MB", "cpuUsage": "6%", "responseTime": "52ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.95, 'LOW', 'unit,order,creation', NOW(), NOW()),

(UUID(), 'OrderServiceTest.testProcessPayment', 'order-service', 'UNIT_TEST', 'PASSED', 78, 
 '2025-01-27 10:31:00', '2025-01-27 10:31:00.078', NULL, NULL,
 '{"orderId": "order123", "paymentMethod": "CREDIT_CARD", "amount": 49.98, "cardToken": "card_token_123"}',
 '{"paymentId": "payment123", "status": "SUCCESS", "transactionId": "txn_456", "amount": 49.98}',
 '{"memoryUsage": "16MB", "cpuUsage": "8%", "responseTime": "78ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.97, 'LOW', 'unit,order,payment', NOW(), NOW()),

(UUID(), 'OrderServiceTest.testOrderCancellation', 'order-service', 'UNIT_TEST', 'FAILED', 65, 
 '2025-01-27 10:32:00', '2025-01-27 10:32:00.065', 'Order cannot be cancelled after shipping', 
 'com.kb.order.service.OrderServiceException: Order cannot be cancelled after shipping\n\tat OrderServiceTest.testOrderCancellation(OrderServiceTest.java:92)',
 '{"orderId": "order123", "status": "SHIPPED", "reason": "Customer request"}',
 '{"error": "Order cannot be cancelled", "currentStatus": "SHIPPED", "allowedStatuses": ["PENDING", "CONFIRMED"]}',
 '{"memoryUsage": "12MB", "cpuUsage": "5%", "responseTime": "65ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.89, 'MEDIUM', 'unit,order,cancellation', NOW(), NOW()),

-- Integration Tests for Order Service
(UUID(), 'OrderServiceIntegrationTest.testCompleteOrderFlow', 'order-service', 'INTEGRATION_TEST', 'PASSED', 2100, 
 '2025-01-27 10:35:00', '2025-01-27 10:35:02.100', NULL, NULL,
 '{"userId": "user123", "items": [{"productId": "prod123", "quantity": 1}], "paymentMethod": "CREDIT_CARD"}',
 '{"orderId": "order456", "status": "CONFIRMED", "paymentStatus": "SUCCESS", "inventoryUpdated": true, "notificationSent": true}',
 '{"memoryUsage": "68MB", "cpuUsage": "35%", "responseTime": "2100ms", "databaseQueries": 15, "externalCalls": 3}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "database": "postgresql"}',
 0.90, 'LOW', 'integration,order,complete-flow', NOW(), NOW()),

(UUID(), 'OrderServiceIntegrationTest.testOrderWithInventoryCheck', 'order-service', 'INTEGRATION_TEST', 'PASSED', 1450, 
 '2025-01-27 10:36:00', '2025-01-27 10:36:01.450', NULL, NULL,
 '{"userId": "user123", "items": [{"productId": "prod123", "quantity": 5}], "checkInventory": true}',
 '{"orderId": "order789", "status": "CONFIRMED", "inventoryReserved": true, "availableStock": 145}',
 '{"memoryUsage": "58MB", "cpuUsage": "30%", "responseTime": "1450ms", "databaseQueries": 10}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "database": "postgresql"}',
 0.92, 'LOW', 'integration,order,inventory', NOW(), NOW()),

-- Security Tests for Order Service
(UUID(), 'OrderServiceSecurityTest.testOrderAccessControl', 'order-service', 'SECURITY_TEST', 'PASSED', 167, 
 '2025-01-27 10:40:00', '2025-01-27 10:40:00.167', NULL, NULL,
 '{"orderId": "order123", "requestingUserId": "user456", "action": "VIEW_ORDER"}',
 '{"error": "Access denied", "message": "User can only access their own orders", "securityEvent": "Unauthorized order access attempt"}',
 '{"memoryUsage": "20MB", "cpuUsage": "10%", "responseTime": "167ms", "securityChecks": 8}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "security": "enabled"}',
 0.96, 'MEDIUM', 'security,order,access-control', NOW(), NOW()),

-- Notification Service Test Data
INSERT INTO test_results (id, test_name, service_name, test_type, status, execution_time_ms, start_time, end_time, 
                         error_message, stack_trace, test_parameters, test_output, performance_metrics, 
                         environment_info, confidence_score, risk_level, tags, created_at, updated_at) VALUES

-- Unit Tests for Notification Service
(UUID(), 'NotificationServiceTest.testSendEmail', 'notification-service', 'UNIT_TEST', 'PASSED', 89, 
 '2025-01-27 10:45:00', '2025-01-27 10:45:00.089', NULL, NULL,
 '{"to": "user@example.com", "subject": "Order Confirmation", "template": "order-confirmation", "data": {"orderId": "order123"}}',
 '{"messageId": "msg123", "status": "SENT", "deliveryTime": "2025-01-27T10:45:00.089"}',
 '{"memoryUsage": "18MB", "cpuUsage": "9%", "responseTime": "89ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.94, 'LOW', 'unit,notification,email', NOW(), NOW()),

(UUID(), 'NotificationServiceTest.testSendSMS', 'notification-service', 'UNIT_TEST', 'PASSED', 76, 
 '2025-01-27 10:46:00', '2025-01-27 10:46:00.076', NULL, NULL,
 '{"to": "+1234567890", "message": "Your order #order123 has been confirmed", "priority": "HIGH"}',
 '{"messageId": "sms123", "status": "SENT", "deliveryTime": "2025-01-27T10:46:00.076"}',
 '{"memoryUsage": "15MB", "cpuUsage": "7%", "responseTime": "76ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.96, 'LOW', 'unit,notification,sms', NOW(), NOW()),

(UUID(), 'NotificationServiceTest.testPushNotification', 'notification-service', 'UNIT_TEST', 'FAILED', 95, 
 '2025-01-27 10:47:00', '2025-01-27 10:47:00.095', 'Push notification service unavailable', 
 'com.kb.notification.service.NotificationServiceException: Push notification service unavailable\n\tat NotificationServiceTest.testPushNotification(NotificationServiceTest.java:67)',
 '{"deviceToken": "device_token_123", "title": "New Order", "body": "Your order has been processed"}',
 '{"error": "Service unavailable", "service": "push-notification", "retryAfter": "30 seconds"}',
 '{"memoryUsage": "22MB", "cpuUsage": "11%", "responseTime": "95ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.87, 'MEDIUM', 'unit,notification,push', NOW(), NOW()),

-- Integration Tests for Notification Service
(UUID(), 'NotificationServiceIntegrationTest.testOrderNotificationFlow', 'notification-service', 'INTEGRATION_TEST', 'PASSED', 1200, 
 '2025-01-27 10:50:00', '2025-01-27 10:50:01.200', NULL, NULL,
 '{"orderId": "order123", "userId": "user123", "notificationTypes": ["EMAIL", "SMS"], "template": "order-confirmation"}',
 '{"notifications": [{"type": "EMAIL", "status": "SENT"}, {"type": "SMS", "status": "SENT"}], "totalSent": 2}',
 '{"memoryUsage": "45MB", "cpuUsage": "22%", "responseTime": "1200ms", "externalCalls": 2}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "externalServices": "enabled"}',
 0.91, 'LOW', 'integration,notification,order-flow', NOW(), NOW()),

-- Security Tests for Notification Service
(UUID(), 'NotificationServiceSecurityTest.testEmailInjectionPrevention', 'notification-service', 'SECURITY_TEST', 'PASSED', 134, 
 '2025-01-27 10:55:00', '2025-01-27 10:55:00.134', NULL, NULL,
 '{"to": "user@example.com\nBcc: hacker@evil.com", "subject": "Test", "body": "Hello"}',
 '{"error": "Invalid email format", "message": "Email injection attempt detected", "securityEvent": "Email injection blocked"}',
 '{"memoryUsage": "19MB", "cpuUsage": "9%", "responseTime": "134ms", "securityChecks": 6}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "security": "enabled"}',
 0.98, 'MEDIUM', 'security,notification,email-injection', NOW(), NOW()),

-- Gateway Service Test Data
INSERT INTO test_results (id, test_name, service_name, test_type, status, execution_time_ms, start_time, end_time, 
                         error_message, stack_trace, test_parameters, test_output, performance_metrics, 
                         environment_info, confidence_score, risk_level, tags, created_at, updated_at) VALUES

-- Unit Tests for Gateway Service
(UUID(), 'GatewayServiceTest.testRouteRequest', 'gateway-service', 'UNIT_TEST', 'PASSED', 34, 
 '2025-01-27 11:00:00', '2025-01-27 11:00:00.034', NULL, NULL,
 '{"path": "/api/users", "method": "GET", "headers": {"Authorization": "Bearer token123"}}',
 '{"routed": true, "targetService": "user-service", "path": "/users", "loadBalanced": true}',
 '{"memoryUsage": "9MB", "cpuUsage": "4%", "responseTime": "34ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.97, 'LOW', 'unit,gateway,routing', NOW(), NOW()),

(UUID(), 'GatewayServiceTest.testRateLimiting', 'gateway-service', 'UNIT_TEST', 'PASSED', 28, 
 '2025-01-27 11:01:00', '2025-01-27 11:01:00.028', NULL, NULL,
 '{"clientId": "client123", "endpoint": "/api/products", "requestsPerMinute": 100}',
 '{"allowed": true, "remainingRequests": 99, "resetTime": "2025-01-27T11:02:00"}',
 '{"memoryUsage": "7MB", "cpuUsage": "3%", "responseTime": "28ms"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test"}',
 0.99, 'LOW', 'unit,gateway,rate-limiting', NOW(), NOW()),

-- Integration Tests for Gateway Service
(UUID(), 'GatewayServiceIntegrationTest.testEndToEndRouting', 'gateway-service', 'INTEGRATION_TEST', 'PASSED', 850, 
 '2025-01-27 11:05:00', '2025-01-27 11:05:00.850', NULL, NULL,
 '{"path": "/api/orders", "method": "POST", "body": {"userId": "user123", "items": []}, "headers": {"Authorization": "Bearer token123"}}',
 '{"status": 201, "responseTime": "850ms", "targetService": "order-service", "loadBalanced": true}',
 '{"memoryUsage": "35MB", "cpuUsage": "18%", "responseTime": "850ms", "circuitBreaker": "CLOSED"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "loadBalancer": "enabled"}',
 0.93, 'LOW', 'integration,gateway,end-to-end', NOW(), NOW()),

-- Security Tests for Gateway Service
(UUID(), 'GatewayServiceSecurityTest.testJWTValidation', 'gateway-service', 'SECURITY_TEST', 'PASSED', 156, 
 '2025-01-27 11:10:00', '2025-01-27 11:10:00.156', NULL, NULL,
 '{"token": "invalid.jwt.token", "path": "/api/users/profile", "method": "GET"}',
 '{"authenticated": false, "error": "Invalid JWT token", "securityEvent": "Invalid token attempt"}',
 '{"memoryUsage": "24MB", "cpuUsage": "12%", "responseTime": "156ms", "securityChecks": 10}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "security": "enabled"}',
 0.97, 'MEDIUM', 'security,gateway,jwt', NOW(), NOW()),

(UUID(), 'GatewayServiceSecurityTest.testCORSValidation', 'gateway-service', 'SECURITY_TEST', 'PASSED', 89, 
 '2025-01-27 11:11:00', '2025-01-27 11:11:00.089', NULL, NULL,
 '{"origin": "https://malicious-site.com", "path": "/api/users", "method": "GET"}',
 '{"corsAllowed": false, "error": "CORS policy violation", "securityEvent": "Unauthorized origin blocked"}',
 '{"memoryUsage": "16MB", "cpuUsage": "8%", "responseTime": "89ms", "securityChecks": 5}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "security": "enabled"}',
 0.95, 'MEDIUM', 'security,gateway,cors', NOW(), NOW());

-- Performance Test Data
INSERT INTO test_results (id, test_name, service_name, test_type, status, execution_time_ms, start_time, end_time, 
                         error_message, stack_trace, test_parameters, test_output, performance_metrics, 
                         environment_info, confidence_score, risk_level, tags, created_at, updated_at) VALUES

(UUID(), 'PerformanceTest.testUserServiceLoad', 'user-service', 'PERFORMANCE_TEST', 'PASSED', 30000, 
 '2025-01-27 11:15:00', '2025-01-27 11:15:30.000', NULL, NULL,
 '{"concurrentUsers": 100, "duration": "30s", "endpoint": "/api/users", "rampUpTime": "10s"}',
 '{"totalRequests": 1500, "successfulRequests": 1485, "failedRequests": 15, "avgResponseTime": "180ms", "p95ResponseTime": "450ms"}',
 '{"memoryUsage": "256MB", "cpuUsage": "85%", "responseTime": "180ms", "throughput": "50 req/s", "errorRate": "1%"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "loadTest": "enabled"}',
 0.88, 'MEDIUM', 'performance,user,load-test', NOW(), NOW()),

(UUID(), 'PerformanceTest.testProductServiceStress', 'product-service', 'PERFORMANCE_TEST', 'PASSED', 60000, 
 '2025-01-27 11:20:00', '2025-01-27 11:21:00.000', NULL, NULL,
 '{"concurrentUsers": 200, "duration": "60s", "endpoint": "/api/products", "rampUpTime": "20s"}',
 '{"totalRequests": 3000, "successfulRequests": 2940, "failedRequests": 60, "avgResponseTime": "220ms", "p95ResponseTime": "600ms"}',
 '{"memoryUsage": "512MB", "cpuUsage": "92%", "responseTime": "220ms", "throughput": "49 req/s", "errorRate": "2%"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "loadTest": "enabled"}',
 0.85, 'HIGH', 'performance,product,stress-test', NOW(), NOW()),

(UUID(), 'PerformanceTest.testOrderServiceSpike', 'order-service', 'PERFORMANCE_TEST', 'FAILED', 45000, 
 '2025-01-27 11:25:00', '2025-01-27 11:25:45.000', 'Service degraded under spike load', 
 'com.kb.order.service.OrderServiceException: Service degraded under spike load\n\tat PerformanceTest.testOrderServiceSpike(PerformanceTest.java:145)',
 '{"concurrentUsers": 500, "duration": "45s", "endpoint": "/api/orders", "spikePattern": "sudden"}',
 '{"totalRequests": 2000, "successfulRequests": 1200, "failedRequests": 800, "avgResponseTime": "1200ms", "p95ResponseTime": "3000ms"}',
 '{"memoryUsage": "1GB", "cpuUsage": "98%", "responseTime": "1200ms", "throughput": "27 req/s", "errorRate": "40%"}',
 '{"javaVersion": "17", "springBootVersion": "3.2.0", "environment": "test", "loadTest": "enabled"}',
 0.75, 'CRITICAL', 'performance,order,spike-test', NOW(), NOW());

-- Learning Data for AI Analysis
INSERT INTO learning_data (id, test_id, pattern_type, pattern_data, confidence_score, created_at, updated_at) VALUES
(UUID(), (SELECT id FROM test_results WHERE test_name = 'UserServiceTest.testCreateUser' LIMIT 1), 'SUCCESS_PATTERN', 
 '{"executionTime": 45, "memoryUsage": "12MB", "cpuUsage": "5%", "service": "user-service", "testType": "UNIT_TEST"}', 0.95, NOW(), NOW()),

(UUID(), (SELECT id FROM test_results WHERE test_name = 'UserServiceTest.testUpdateUserProfile' LIMIT 1), 'FAILURE_PATTERN', 
 '{"executionTime": 67, "errorType": "VALIDATION_ERROR", "service": "user-service", "testType": "UNIT_TEST", "field": "email"}', 0.85, NOW(), NOW()),

(UUID(), (SELECT id FROM test_results WHERE test_name = 'PerformanceTest.testOrderServiceSpike' LIMIT 1), 'PERFORMANCE_DEGRADATION', 
 '{"concurrentUsers": 500, "errorRate": 0.4, "responseTime": 1200, "service": "order-service", "testType": "PERFORMANCE_TEST"}', 0.75, NOW(), NOW()),

(UUID(), (SELECT id FROM test_results WHERE test_name = 'UserServiceSecurityTest.testSQLInjectionPrevention' LIMIT 1), 'SECURITY_PATTERN', 
 '{"securityEvent": "SQL_INJECTION_BLOCKED", "service": "user-service", "testType": "SECURITY_TEST", "threatLevel": "HIGH"}', 0.99, NOW(), NOW());

-- AI Analysis History
INSERT INTO ai_analysis_history (id, test_id, analysis_type, analysis_result, confidence_score, created_at, updated_at) VALUES
(UUID(), (SELECT id FROM test_results WHERE test_name = 'UserServiceIntegrationTest.testUserRegistrationFlow' LIMIT 1), 'PATTERN_ANALYSIS', 
 '{"patterns": ["SUCCESS_PATTERN", "INTEGRATION_PATTERN"], "recommendations": ["Optimize database queries", "Add caching layer"], "riskLevel": "LOW"}', 0.92, NOW(), NOW()),

(UUID(), (SELECT id FROM test_results WHERE test_name = 'PerformanceTest.testOrderServiceSpike' LIMIT 1), 'PERFORMANCE_ANALYSIS', 
 '{"issues": ["High error rate under load", "Memory pressure", "CPU saturation"], "recommendations": ["Scale horizontally", "Optimize database queries", "Add circuit breaker"], "riskLevel": "CRITICAL"}', 0.75, NOW(), NOW()),

(UUID(), (SELECT id FROM test_results WHERE test_name = 'GatewayServiceSecurityTest.testJWTValidation' LIMIT 1), 'SECURITY_ANALYSIS', 
 '{"securityEvents": ["Invalid token attempt"], "threats": ["Token manipulation"], "recommendations": ["Strengthen token validation", "Add rate limiting", "Monitor suspicious activity"], "riskLevel": "MEDIUM"}', 0.97, NOW(), NOW());

-- System Logs
INSERT INTO system_logs (id, level, message, service_name, timestamp, metadata, created_at, updated_at) VALUES
(UUID(), 'INFO', 'Test execution completed successfully', 'user-service', '2025-01-27 10:00:00', 
 '{"testName": "UserServiceTest.testCreateUser", "executionTime": 45, "status": "PASSED"}', NOW(), NOW()),

(UUID(), 'ERROR', 'Test execution failed with validation error', 'user-service', '2025-01-27 10:02:00', 
 '{"testName": "UserServiceTest.testUpdateUserProfile", "error": "Validation failed: Invalid email format", "status": "FAILED"}', NOW(), NOW()),

(UUID(), 'WARN', 'Performance degradation detected', 'order-service', '2025-01-27 11:25:45', 
 '{"testName": "PerformanceTest.testOrderServiceSpike", "errorRate": 0.4, "responseTime": 1200, "status": "FAILED"}', NOW(), NOW()),

(UUID(), 'INFO', 'Security test passed - threat blocked', 'user-service', '2025-01-27 10:10:00', 
 '{"testName": "UserServiceSecurityTest.testSQLInjectionPrevention", "threat": "SQL injection", "status": "PASSED"}', NOW(), NOW());
