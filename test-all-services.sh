#!/bin/bash
# Test All Services Script

echo "🧪 Testing all microservices..."

# Test User Service
echo "Testing User Service..."
curl -s -u user:user-service-password http://localhost:8081/actuator/health && echo " ✅" || echo " ❌"

# Test Product Service
echo "Testing Product Service..."
curl -s -u user:product-service-password http://localhost:8082/actuator/health && echo " ✅" || echo " ❌"

# Test Order Service
echo "Testing Order Service..."
curl -s -u user:order-service-password http://localhost:8083/actuator/health && echo " ✅" || echo " ❌"

# Test Notification Service
echo "Testing Notification Service..."
curl -s -u user:notification-service-password http://localhost:8084/actuator/health && echo " ✅" || echo " ❌"

echo "🎉 Service testing completed!"
