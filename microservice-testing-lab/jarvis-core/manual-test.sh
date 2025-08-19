#!/bin/bash

# Manual Test Script for Jarvis Core NLP to AI Integration
# This script allows interactive testing of the complete pipeline

echo "🤖 Jarvis Core - Manual NLP to AI Integration Testing"
echo "======================================================"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${CYAN}$1${NC}"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if the application is running
check_application() {
    print_info "Checking if Jarvis Core is running..."
    
    # Try to connect to the application
    if curl -s http://localhost:8085/actuator/health > /dev/null 2>&1; then
        print_success "Jarvis Core is running on port 8085"
        return 0
    else
        print_warning "Jarvis Core is not running on port 8085"
        print_info "Starting Jarvis Core..."
        
        # Start the application in background
        mvn spring-boot:run -Dspring-boot.run.profiles=dev > jarvis.log 2>&1 &
        JARVIS_PID=$!
        
        # Wait for application to start
        print_info "Waiting for application to start..."
        for i in {1..30}; do
            if curl -s http://localhost:8085/actuator/health > /dev/null 2>&1; then
                print_success "Jarvis Core started successfully (PID: $JARVIS_PID)"
                return 0
            fi
            sleep 1
        done
        
        print_error "Failed to start Jarvis Core"
        return 1
    fi
}

# Test a command via REST API
test_command() {
    local command="$1"
    local description="$2"
    
    print_header "Testing: $description"
    print_info "Command: $command"
    
    # Send command to the API
    response=$(curl -s -X POST http://localhost:8085/api/jarvis/command \
        -H "Content-Type: application/json" \
        -d "{\"input\": \"$command\"}")
    
    if [ $? -eq 0 ]; then
        print_success "API call successful"
        echo "Response: $response" | jq '.' 2>/dev/null || echo "Response: $response"
    else
        print_error "API call failed"
    fi
    
    echo ""
}

# Test NLP parsing
test_nlp_parsing() {
    local command="$1"
    local description="$2"
    
    print_header "Testing NLP Parsing: $description"
    print_info "Command: $command"
    
    # Send command to NLP API
    response=$(curl -s -X POST http://localhost:8085/api/nlp/parse-intent \
        -H "Content-Type: application/json" \
        -d "{\"input\": \"$command\"}")
    
    if [ $? -eq 0 ]; then
        print_success "NLP parsing successful"
        echo "Parsed Intent: $response" | jq '.' 2>/dev/null || echo "Parsed Intent: $response"
    else
        print_error "NLP parsing failed"
    fi
    
    echo ""
}

# Test comprehensive NLP analysis
test_comprehensive_nlp() {
    local command="$1"
    local description="$2"
    
    print_header "Testing Comprehensive NLP Analysis: $description"
    print_info "Command: $command"
    
    # Send command to comprehensive NLP API
    response=$(curl -s -X POST http://localhost:8085/api/nlp/comprehensive-analysis \
        -H "Content-Type: application/json" \
        -d "{\"input\": \"$command\"}")
    
    if [ $? -eq 0 ]; then
        print_success "Comprehensive NLP analysis successful"
        echo "Analysis: $response" | jq '.' 2>/dev/null || echo "Analysis: $response"
    else
        print_error "Comprehensive NLP analysis failed"
    fi
    
    echo ""
}

# Main test execution
main() {
    # Check if application is running
    if ! check_application; then
        print_error "Cannot proceed without running application"
        exit 1
    fi
    
    print_header "Starting Manual Integration Tests"
    echo ""
    
    # Test 1: Basic command processing
    test_command "Run integration tests for user-service" "Basic Integration Test Command"
    
    # Test 2: Performance tests
    test_command "Run performance tests on order-service with load testing" "Performance Test Command"
    
    # Test 3: Failure analysis
    test_command "Analyze recent test failures in notification-service" "Failure Analysis Command"
    
    # Test 4: Health check
    test_command "Check system health and performance metrics" "Health Check Command"
    
    # Test 5: Test generation
    test_command "Generate new API tests for gateway-service" "Test Generation Command"
    
    # Test 6: Complex multi-service command
    test_command "Run comprehensive tests including integration, performance, and security tests for user-service, product-service, and order-service with parallel execution and high priority" "Complex Multi-Service Command"
    
    # Test 7: Low confidence input
    test_command "xyz abc def ghi" "Low Confidence Input"
    
    print_header "NLP Component Tests"
    echo ""
    
    # Test NLP parsing specifically
    test_nlp_parsing "Run security tests for gateway-service" "Security Test NLP Parsing"
    
    # Test comprehensive NLP analysis
    test_comprehensive_nlp "Run chaos tests for all services with maximum load" "Chaos Test Comprehensive Analysis"
    
    print_header "Test Summary"
    echo "=============="
    print_success "Manual integration tests completed!"
    print_info "Check the responses above for detailed analysis"
    print_info "Application logs are available in jarvis.log"
    
    # Ask if user wants to continue testing
    echo ""
    read -p "Do you want to test additional commands? (y/n): " -n 1 -r
    echo ""
    
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        interactive_mode
    else
        print_info "Cleaning up..."
        if [ ! -z "$JARVIS_PID" ]; then
            kill $JARVIS_PID 2>/dev/null
            print_info "Stopped Jarvis Core (PID: $JARVIS_PID)"
        fi
    fi
}

# Interactive mode for custom commands
interactive_mode() {
    print_header "Interactive Testing Mode"
    print_info "Enter commands to test (type 'quit' to exit):"
    echo ""
    
    while true; do
        read -p "🤖 Enter command: " user_command
        
        if [ "$user_command" = "quit" ] || [ "$user_command" = "exit" ]; then
            break
        fi
        
        if [ ! -z "$user_command" ]; then
            test_command "$user_command" "Custom Command"
        fi
    done
    
    print_info "Interactive mode ended"
    if [ ! -z "$JARVIS_PID" ]; then
        kill $JARVIS_PID 2>/dev/null
        print_info "Stopped Jarvis Core (PID: $JARVIS_PID)"
    fi
}

# Handle script interruption
trap 'echo ""; print_warning "Script interrupted"; if [ ! -z "$JARVIS_PID" ]; then kill $JARVIS_PID 2>/dev/null; fi; exit 1' INT

# Run main function
main
