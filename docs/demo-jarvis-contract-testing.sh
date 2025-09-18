#!/bin/bash

# Jarvis Core Contract Testing Demo Script
# This script demonstrates Jarvis Core's capabilities for contract testing analysis

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
JARVIS_URL="http://localhost:8085"
DEMO_DELAY=2

echo -e "${CYAN}╔══════════════════════════════════════════════════════════════════════════════╗${NC}"
echo -e "${CYAN}║                    🤖 JARVIS CORE CONTRACT TESTING DEMO                     ║${NC}"
echo -e "${CYAN}║                        Intelligent Test Analysis                            ║${NC}"
echo -e "${CYAN}╚══════════════════════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Function to print section headers
print_section() {
    echo -e "\n${BLUE}═══════════════════════════════════════════════════════════════════════════════${NC}"
    echo -e "${BLUE} $1${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════════════════════════════════════${NC}"
}

# Function to print step headers
print_step() {
    echo -e "\n${YELLOW}▶ $1${NC}"
}

# Function to make API calls with error handling
api_call() {
    local endpoint="$1"
    local description="$2"
    
    echo -e "${PURPLE}📡 $description${NC}"
    echo -e "   ${CYAN}GET $endpoint${NC}"
    
    response=$(curl -s "$JARVIS_URL$endpoint" 2>/dev/null)
    
    if [ $? -eq 0 ] && [ -n "$response" ]; then
        echo -e "   ${GREEN}✅ Success${NC}"
        echo "$response" | jq . 2>/dev/null || echo "$response"
    else
        echo -e "   ${RED}❌ Failed to connect to Jarvis Core${NC}"
        echo -e "   ${YELLOW}💡 Make sure Jarvis Core is running on $JARVIS_URL${NC}"
        return 1
    fi
    
    echo ""
    sleep $DEMO_DELAY
}

# Function to wait for user input
wait_for_user() {
    echo -e "\n${YELLOW}Press Enter to continue...${NC}"
    read -r
}

# Check if Jarvis Core is running
check_jarvis_core() {
    print_section "🔍 CHECKING JARVIS CORE STATUS"
    
    echo -e "${PURPLE}📡 Checking if Jarvis Core is running...${NC}"
    
    if curl -s "$JARVIS_URL/actuator/health" > /dev/null 2>&1; then
        echo -e "   ${GREEN}✅ Jarvis Core is running and healthy${NC}"
        return 0
    else
        echo -e "   ${RED}❌ Jarvis Core is not running or not accessible${NC}"
        echo -e "   ${YELLOW}💡 Please start Jarvis Core first:${NC}"
        echo -e "   ${CYAN}   cd /Users/kanishkabagri/Workspace/microservice-testing-lab/microservice-testing-lab/jarvis-core${NC}"
        echo -e "   ${CYAN}   SPRING_PROFILES_ACTIVE=test mvn spring-boot:run -Dserver.port=8085${NC}"
        return 1
    fi
}

# Demo Scenario 1: Run Contract Tests for 2 Services
demo_contract_tests() {
    print_section "🎯 DEMO SCENARIO 1: CONTRACT TEST EXECUTION"
    
    print_step "Running Contract Tests for User Service"
    api_call "/api/demo/contract-tests/user-service" "Executing contract tests for user-service"
    
    print_step "Running Contract Tests for Product Service"
    api_call "/api/demo/contract-tests/product-service" "Executing contract tests for product-service"
    
    print_step "Getting Available Services"
    api_call "/api/demo/services" "Retrieving list of available services"
    
    wait_for_user
}

# Demo Scenario 1.5: Run Integration Tests for User Service
demo_integration_tests() {
    print_section "🔧 DEMO SCENARIO 1.5: INTEGRATION TEST EXECUTION"
    
    print_step "Running Integration Tests for User Service"
    echo -e "${PURPLE}🧪 Testing end-to-end user service functionality${NC}"
    echo ""
    
    # Show live logs during integration test
    echo -e "${CYAN}📋 Live Log Monitoring:${NC}"
    echo -e "   • Monitoring user-service logs during test execution"
    echo -e "   • Real-time integration test feedback"
    echo ""
    
    # Start log monitoring in background
    echo -e "${YELLOW}🔍 Starting log monitoring...${NC}"
    docker logs -f microservice-testing-lab-user-service-1 --tail=5 &
    LOG_PID=$!
    
    # Give a moment for logs to start
    sleep 2
    
    # Execute the integration test
    api_call "/api/demo/integration-tests/user-service" "Executing integration tests for user-service"
    
    # Stop log monitoring
    kill $LOG_PID 2>/dev/null
    echo -e "\n${GREEN}✅ Log monitoring stopped${NC}"
    
    print_step "Running Integration Tests for Order Service"
    echo -e "${PURPLE}🧪 Testing complex order processing workflows${NC}"
    echo ""
    
    api_call "/api/demo/integration-tests/order-service" "Executing integration tests for order-service"
    
    echo -e "${GREEN}💡 Integration Test Insights:${NC}"
    echo -e "   • Tests database connectivity and data persistence"
    echo -e "   • Validates service-to-service communication"
    echo -e "   • Includes dependency analysis and execution timing"
    echo -e "   • Provides detailed test categorization"
    echo -e "   • Real-time log monitoring during test execution"
    
    wait_for_user
}

# Demo Scenario 2: Failure Analysis
demo_failure_analysis() {
    print_section "🔍 DEMO SCENARIO 2: CONTRACT TEST FAILURE ANALYSIS"
    
    print_step "Analyzing Contract Test Failures"
    echo -e "${PURPLE}🤔 Question: When was the last time contract tests for an app failed?${NC}"
    echo ""
    
    api_call "/api/demo/contract-tests/failure-analysis" "Analyzing contract test failure history"
    
    echo -e "${GREEN}💡 Jarvis Core Analysis:${NC}"
    echo -e "   • Identified the most recent contract test failure"
    echo -e "   • Calculated time since last failure"
    echo -e "   • Provided detailed failure information"
    echo -e "   • Enabled quick root cause analysis"
    
    wait_for_user
}

# Demo Scenario 3: Coverage Analysis
demo_coverage_analysis() {
    print_section "📊 DEMO SCENARIO 3: TEST COVERAGE ANALYSIS"
    
    print_step "Analyzing Test Coverage Across Services"
    echo -e "${PURPLE}🤔 Question: Which application seems to have the most coverage?${NC}"
    echo ""
    
    api_call "/api/demo/coverage-analysis" "Analyzing test coverage by service and test type"
    
    print_step "Getting Overall Test Summary"
    api_call "/api/demo/test-summary" "Retrieving comprehensive test summary"
    
    echo -e "${GREEN}💡 Jarvis Core Analysis:${NC}"
    echo -e "   • Calculated test counts by service and test type"
    echo -e "   • Identified service with highest test coverage"
    echo -e "   • Provided breakdown of unit, integration, contract, security, and performance tests"
    echo -e "   • Enabled data-driven coverage decisions"
    
    wait_for_user
}

# Advanced Demo: Interactive Analysis
demo_interactive_analysis() {
    print_section "🚀 ADVANCED DEMO: INTERACTIVE ANALYSIS"
    
    print_step "Running Contract Tests for Order Service"
    api_call "/api/demo/contract-tests/order-service" "Executing contract tests for order-service"
    
    print_step "Running Contract Tests for Notification Service"
    api_call "/api/demo/contract-tests/notification-service" "Executing contract tests for notification-service"
    
    print_step "Updated Coverage Analysis"
    api_call "/api/demo/coverage-analysis" "Re-analyzing coverage after additional tests"
    
    echo -e "${GREEN}💡 Jarvis Core Capabilities Demonstrated:${NC}"
    echo -e "   • Real-time contract test execution"
    echo -e "   • Intelligent failure analysis and root cause identification"
    echo -e "   • Comprehensive coverage analysis across multiple test types"
    echo -e "   • Historical trend analysis"
    echo -e "   • Service-specific test insights"
    
    wait_for_user
}

# Main demo execution
main() {
    echo -e "${CYAN}Starting Jarvis Core Contract Testing Demo...${NC}"
    echo ""
    
    # Check if Jarvis Core is running
    if ! check_jarvis_core; then
        exit 1
    fi
    
    echo -e "\n${GREEN}🎉 Jarvis Core is ready! Starting demo scenarios...${NC}"
    wait_for_user
    
    # Execute demo scenarios
    demo_contract_tests
    demo_integration_tests
    demo_failure_analysis
    demo_coverage_analysis
    demo_interactive_analysis
    
    # Demo conclusion
    print_section "🎯 DEMO CONCLUSION"
    
    echo -e "${GREEN}✅ Demo Scenarios Completed Successfully!${NC}"
    echo ""
    echo -e "${CYAN}📋 What We Demonstrated:${NC}"
    echo -e "   1. ${YELLOW}Contract Test Execution${NC} - Ran contract tests for multiple services"
    echo -e "   2. ${YELLOW}Integration Test Execution${NC} - Ran integration tests with dependency analysis"
    echo -e "   3. ${YELLOW}Failure Analysis${NC} - Analyzed when contract tests last failed"
    echo -e "   4. ${YELLOW}Coverage Analysis${NC} - Identified service with most test coverage"
    echo -e "   5. ${YELLOW}Interactive Analysis${NC} - Real-time test execution and analysis"
    echo ""
    echo -e "${CYAN}🚀 Jarvis Core Benefits:${NC}"
    echo -e "   • ${GREEN}Intelligent Test Analysis${NC} - AI-powered insights into test results"
    echo -e "   • ${GREEN}Historical Trend Analysis${NC} - Track test performance over time"
    echo -e "   • ${GREEN}Service-Specific Insights${NC} - Detailed analysis per microservice"
    echo -e "   • ${GREEN}Real-time Monitoring${NC} - Instant feedback on test execution"
    echo -e "   • ${GREEN}Coverage Optimization${NC} - Data-driven decisions on test coverage"
    echo ""
    echo -e "${PURPLE}🎉 Thank you for exploring Jarvis Core's Contract Testing capabilities!${NC}"
    echo ""
}

# Check if jq is installed
if ! command -v jq &> /dev/null; then
    echo -e "${YELLOW}⚠️  Warning: jq is not installed. JSON output will not be formatted.${NC}"
    echo -e "${YELLOW}   Install jq for better output formatting: brew install jq${NC}"
    echo ""
fi

# Run the demo
main "$@"
