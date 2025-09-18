#!/bin/bash

# Comprehensive Jarvis Demo with Real Test Data
# Covers all services: User, Product, Order, Notification, Gateway
# Covers test types: Unit, Integration, Security

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
JARVIS_URL="http://localhost:8085"
DEMO_DELAY=2

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_jarvis() {
    echo -e "${PURPLE}[JARVIS AI]${NC} $1"
}

log_demo() {
    echo -e "${CYAN}[DEMO]${NC} $1"
}

# Check if Jarvis is running
check_jarvis_health() {
    if curl -s "$JARVIS_URL/health" > /dev/null 2>&1; then
        return 0
    else
        return 1
    fi
}

# Display formatted JSON response
display_json_response() {
    local response="$1"
    local title="$2"
    
    echo
    log_demo "🎯 $title"
    echo "----------------------------------------"
    
    if command -v jq &> /dev/null; then
        echo "$response" | jq .
    else
        echo "$response"
    fi
    
    echo "----------------------------------------"
    echo
}

# Demo scenarios
demo_overview() {
    log_demo "📊 DEMO 1: Comprehensive Test Overview"
    echo "This demo shows the overall test coverage across all services and test types."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/overview")
    display_json_response "$response" "Test Overview - All Services & Test Types"
    
    sleep $DEMO_DELAY
}

demo_user_service() {
    log_demo "👤 DEMO 2: User Service Analysis"
    echo "This demo shows detailed analysis of User Service tests including unit, integration, and security tests."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/services/user-service/tests")
    display_json_response "$response" "User Service Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_product_service() {
    log_demo "🛍️  DEMO 3: Product Service Analysis"
    echo "This demo shows detailed analysis of Product Service tests including inventory management and pricing."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/services/product-service/tests")
    display_json_response "$response" "Product Service Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_order_service() {
    log_demo "📦 DEMO 4: Order Service Analysis"
    echo "This demo shows detailed analysis of Order Service tests including payment processing and order flow."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/services/order-service/tests")
    display_json_response "$response" "Order Service Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_notification_service() {
    log_demo "📧 DEMO 5: Notification Service Analysis"
    echo "This demo shows detailed analysis of Notification Service tests including email, SMS, and push notifications."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/services/notification-service/tests")
    display_json_response "$response" "Notification Service Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_gateway_service() {
    log_demo "🚪 DEMO 6: Gateway Service Analysis"
    echo "This demo shows detailed analysis of Gateway Service tests including routing, load balancing, and security."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/services/gateway-service/tests")
    display_json_response "$response" "Gateway Service Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_unit_tests() {
    log_demo "🧪 DEMO 7: Unit Test Analysis"
    echo "This demo shows comprehensive analysis of all unit tests across services."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/test-types/UNIT_TEST/analysis")
    display_json_response "$response" "Unit Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_integration_tests() {
    log_demo "🔗 DEMO 8: Integration Test Analysis"
    echo "This demo shows comprehensive analysis of all integration tests across services."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/test-types/INTEGRATION_TEST/analysis")
    display_json_response "$response" "Integration Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_security_tests() {
    log_demo "🔒 DEMO 9: Security Test Analysis"
    echo "This demo shows comprehensive analysis of all security tests and threat detection."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/security/analysis")
    display_json_response "$response" "Security Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_performance_tests() {
    log_demo "⚡ DEMO 10: Performance Test Analysis"
    echo "This demo shows comprehensive analysis of all performance tests and optimization recommendations."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/performance/analysis")
    display_json_response "$response" "Performance Test Analysis"
    
    sleep $DEMO_DELAY
}

demo_failure_analysis() {
    log_demo "❌ DEMO 11: Failure Analysis"
    echo "This demo shows comprehensive analysis of test failures and remediation strategies."
    echo
    
    local response=$(curl -s "$JARVIS_URL/api/demo/failures/analysis")
    display_json_response "$response" "Failure Analysis"
    
    sleep $DEMO_DELAY
}

demo_jarvis_ai_commands() {
    log_demo "🤖 DEMO 12: Jarvis AI Commands"
    echo "This demo shows Jarvis AI processing natural language commands for test analysis."
    echo
    
    # Test health check command
    log_jarvis "Processing: 'Check health of all services'"
    local response1=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{"command": "Check health of all services"}')
    display_json_response "$response1" "Health Check Command"
    
    sleep $DEMO_DELAY
    
    # Test performance analysis command
    log_jarvis "Processing: 'Analyze performance of order service'"
    local response2=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{"command": "Analyze performance of order service"}')
    display_json_response "$response2" "Performance Analysis Command"
    
    sleep $DEMO_DELAY
    
    # Test security analysis command
    log_jarvis "Processing: 'Run security tests for user service'"
    local response3=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{"command": "Run security tests for user service"}')
    display_json_response "$response3" "Security Test Command"
    
    sleep $DEMO_DELAY
}

# Main demo function
main() {
    echo "=========================================="
    echo "    🤖 Jarvis Comprehensive Demo"
    echo "      Real Test Data Showcase"
    echo "=========================================="
    echo
    echo -e "${GREEN}🚀 Welcome to the Jarvis Comprehensive Demo!${NC}"
    echo
    echo "This demo showcases real test data covering:"
    echo "• All 5 microservices (User, Product, Order, Notification, Gateway)"
    echo "• All 3 core test types (Unit, Integration, Security)"
    echo "• Comprehensive test analysis and insights"
    echo "• AI-powered test automation and recommendations"
    echo
    
    # Check if Jarvis is running
    if ! check_jarvis_health; then
        log_error "Jarvis Core is not running at $JARVIS_URL"
        log_info "Please start Jarvis Core first:"
        log_info "cd microservice-testing-lab/jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
        exit 1
    fi
    
    log_success "Jarvis Core is running and ready!"
    echo
    
    # Run all demo scenarios
    demo_overview
    demo_user_service
    demo_product_service
    demo_order_service
    demo_notification_service
    demo_gateway_service
    demo_unit_tests
    demo_integration_tests
    demo_security_tests
    demo_performance_tests
    demo_failure_analysis
    demo_jarvis_ai_commands
    
    echo
    echo "=========================================="
    echo -e "${GREEN}🎉 Comprehensive Demo Completed!${NC}"
    echo "=========================================="
    echo
    echo -e "${BLUE}Demo Coverage Summary:${NC}"
    echo "• ✅ All 5 Microservices Analyzed"
    echo "• ✅ All 3 Core Test Types Covered"
    echo "• ✅ Real Test Data with 25+ Test Cases"
    echo "• ✅ Comprehensive Analysis & Insights"
    echo "• ✅ AI-Powered Test Automation"
    echo "• ✅ Security & Performance Analysis"
    echo "• ✅ Failure Pattern Recognition"
    echo
    echo -e "${YELLOW}Test Data Statistics:${NC}"
    echo "• Total Tests: 25+"
    echo "• Services: 5 (User, Product, Order, Notification, Gateway)"
    echo "• Test Types: 3 (Unit, Integration, Security)"
    echo "• Success Rate: ~85%"
    echo "• Security Score: 87.5%"
    echo
    echo -e "${GREEN}Key Features Demonstrated:${NC}"
    echo "• 🧪 Unit Testing: Component-level validation"
    echo "• 🔗 Integration Testing: Service-to-service communication"
    echo "• 🔒 Security Testing: Threat detection and prevention"
    echo "• ⚡ Performance Testing: Load and stress analysis"
    echo "• 🤖 AI Analysis: Intelligent test insights and recommendations"
    echo "• 📊 Comprehensive Reporting: Detailed test analytics"
    echo
    echo -e "${GREEN}Thank you for exploring Jarvis Comprehensive Testing! 🚀${NC}"
}

# Run main function
main "$@"
