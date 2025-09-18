#!/bin/bash

# Jarvis AI Simple Demo Script
# Assumes Jarvis Core is already running and showcases AI capabilities

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

log_demo() {
    echo -e "${PURPLE}[DEMO]${NC} $1"
}

log_jarvis() {
    echo -e "${CYAN}[JARVIS AI]${NC} $1"
}

# Check if Jarvis Core is running
check_jarvis() {
    if ! curl -s "$JARVIS_URL/health" > /dev/null 2>&1; then
        log_error "Jarvis Core is not running at $JARVIS_URL"
        log_info "Please start Jarvis Core first:"
        log_info "cd ../jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
        exit 1
    fi
    log_success "Jarvis Core is running and ready!"
}

# Send command to Jarvis AI and display response
send_jarvis_command() {
    local command="$1"
    local context="$2"
    local description="$3"
    
    echo
    log_demo "=== $description ==="
    echo
    
    local json_data="{\"command\": \"$command\""
    if [ ! -z "$context" ]; then
        json_data="$json_data, \"context\": $context"
    fi
    json_data="$json_data}"
    
    log_jarvis "Sending command: $command"
    echo -e "${CYAN}⏳ Processing...${NC}"
    
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d "$json_data")
    
    # Display response in English
    echo -e "${GREEN}🤖 Jarvis AI Response:${NC}"
    echo "----------------------------------------"
    
    if command -v jq &> /dev/null; then
        # Extract and display message
        local message=$(echo "$response" | jq -r '.message // empty' 2>/dev/null)
        if [ ! -z "$message" ] && [ "$message" != "null" ]; then
            echo -e "${YELLOW}💬 $message${NC}"
            echo
        fi
        
        # Extract and display status
        local status=$(echo "$response" | jq -r '.status // empty' 2>/dev/null)
        if [ ! -z "$status" ] && [ "$status" != "null" ]; then
            echo -e "${BLUE}📊 Status: $status${NC}"
            echo
        fi
        
        # Extract and display action details
        local action_type=$(echo "$response" | jq -r '.action.type // empty' 2>/dev/null)
        local description=$(echo "$response" | jq -r '.action.description // empty' 2>/dev/null)
        local estimated_time=$(echo "$response" | jq -r '.action.estimatedTime // empty' 2>/dev/null)
        local priority=$(echo "$response" | jq -r '.action.priority // empty' 2>/dev/null)
        local confidence=$(echo "$response" | jq -r '.action.confidence // empty' 2>/dev/null)
        
        if [ ! -z "$action_type" ] && [ "$action_type" != "null" ]; then
            echo -e "${PURPLE}🎯 Action: $action_type${NC}"
            
            if [ ! -z "$description" ] && [ "$description" != "null" ]; then
                echo -e "${CYAN}   Description: $description${NC}"
            fi
            
            if [ ! -z "$estimated_time" ] && [ "$estimated_time" != "null" ]; then
                echo -e "${GREEN}   ⏱️  Estimated Time: $estimated_time${NC}"
            fi
            
            if [ ! -z "$priority" ] && [ "$priority" != "null" ]; then
                echo -e "${YELLOW}   🎯 Priority: $priority${NC}"
            fi
            
            if [ ! -z "$confidence" ] && [ "$confidence" != "null" ]; then
                local confidence_percent=$(echo "$confidence * 100" | awk '{printf "%.0f", $1}')
                echo -e "${BLUE}   🎯 Confidence: ${confidence_percent}%${NC}"
            fi
            
            echo
        fi
    else
        # Fallback to raw JSON display
        echo "$response" | jq . 2>/dev/null || echo "$response"
    fi
    
    echo "----------------------------------------"
    sleep $DEMO_DELAY
}

# Simulate test execution and results
simulate_test_execution() {
    local test_type="$1"
    local service_name="$2"
    
    echo
    log_demo "=== Simulating $test_type Test Execution ==="
    echo
    
    log_info "Executing $test_type tests for $service_name..."
    echo -e "${CYAN}🧪 Running $test_type tests...${NC}"
    sleep 2
    
    case "$test_type" in
        "health")
            echo -e "${GREEN}✅ Health Check: PASSED${NC}"
            echo -e "${GREEN}✅ Database Connection: PASSED${NC}"
            echo -e "${GREEN}✅ API Endpoints: PASSED${NC}"
            echo -e "${GREEN}✅ Service Discovery: PASSED${NC}"
            ;;
        "performance")
            echo -e "${GREEN}✅ Response Time: 45ms (Target: <100ms)${NC}"
            echo -e "${GREEN}✅ Throughput: 1500 req/s (Target: >1000 req/s)${NC}"
            echo -e "${YELLOW}⚠️  Memory Usage: 85% (Target: <90%)${NC}"
            echo -e "${GREEN}✅ CPU Usage: 65% (Target: <80%)${NC}"
            ;;
        "contract")
            echo -e "${GREEN}✅ API Contract Validation: PASSED${NC}"
            echo -e "${GREEN}✅ Schema Validation: PASSED${NC}"
            echo -e "${GREEN}✅ Response Format: PASSED${NC}"
            echo -e "${GREEN}✅ Field Validation: PASSED${NC}"
            ;;
        "chaos")
            echo -e "${GREEN}✅ Network Latency Injection: SUCCESS${NC}"
            echo -e "${GREEN}✅ Service Recovery: SUCCESS${NC}"
            echo -e "${GREEN}✅ Resilience Validation: PASSED${NC}"
            echo -e "${YELLOW}⚠️  Performance Impact: 15% degradation (Acceptable)${NC}"
            ;;
    esac
    
    echo
    log_success "$test_type tests completed for $service_name"
    sleep $DEMO_DELAY
}

# Simulate contract test failure scenario
simulate_contract_failure() {
    echo
    log_demo "=== Contract Test Failure Scenario ==="
    echo
    
    log_warning "Simulating data model change in order-service..."
    echo -e "${YELLOW}📝 Changing field 'email' to 'userEmail' in UserDTO${NC}"
    echo -e "${YELLOW}📝 Adding new field 'phoneNumber' to UserDTO${NC}"
    sleep 2
    
    log_info "Running contract tests after model change..."
    echo -e "${CYAN}🧪 Executing contract validation...${NC}"
    sleep 2
    
    # Simulate failure
    echo -e "${RED}❌ Contract Test: FAILED${NC}"
    echo -e "${RED}❌ Schema Validation: FAILED - Field 'email' not found${NC}"
    echo -e "${RED}❌ Response Format: FAILED - Expected 'email', got 'userEmail'${NC}"
    echo -e "${RED}❌ Field Validation: FAILED - Unexpected field 'phoneNumber'${NC}"
    
    echo
    log_error "Contract test failure detected! Breaking change identified."
    sleep $DEMO_DELAY
}

# Show AI analysis and recommendations
show_ai_analysis() {
    echo
    log_demo "=== AI-Powered Analysis & Recommendations ==="
    echo
    
    log_jarvis "Analyzing test results and providing intelligent insights..."
    echo -e "${CYAN}🔍 Pattern Recognition: Detecting performance degradation trend${NC}"
    echo -e "${CYAN}🎯 Risk Assessment: Medium risk - memory usage approaching threshold${NC}"
    echo -e "${CYAN}📊 Trend Analysis: Response time improved by 15% over last week${NC}"
    echo -e "${CYAN}🔧 Root Cause Analysis: Database connection pooling optimization needed${NC}"
    
    echo
    echo -e "${PURPLE}🤖 AI Recommendations:${NC}"
    echo -e "${GREEN}   • Monitor memory usage closely and consider scaling${NC}"
    echo -e "${GREEN}   • Optimize database connection pooling configuration${NC}"
    echo -e "${GREEN}   • Review and update API contracts to prevent breaking changes${NC}"
    echo -e "${GREEN}   • Implement automated contract testing in CI/CD pipeline${NC}"
    
    echo
    log_success "AI analysis completed with actionable insights"
    sleep $DEMO_DELAY
}

# Main demo function
main() {
    echo "================================================"
    echo "    🤖 Jarvis AI Capabilities Demo"
    echo "================================================"
    echo
    log_demo "Welcome to the Jarvis AI Demo!"
    log_demo "This showcase demonstrates the AI-powered testing framework capabilities."
    echo
    
    # Check if Jarvis Core is running
    check_jarvis
    echo
    
    # Step 1: System Health Assessment
    log_demo "Step 1: AI-Powered System Health Assessment"
    send_jarvis_command "assess current system health and identify weak points" '{"assessment_type": "comprehensive"}' "System Health Assessment"
    
    # Step 2: Performance Testing
    log_demo "Step 2: Performance Testing with AI Analysis"
    send_jarvis_command "run performance analysis on user-service" '{"service": "user-service", "parameters": {"duration": "60s", "load": "medium"}}' "Performance Analysis"
    simulate_test_execution "performance" "user-service"
    
    # Step 3: Contract Testing
    log_demo "Step 3: Contract Testing Demonstration"
    send_jarvis_command "run contract tests for order-service" '{"service": "order-service", "test_type": "contract"}' "Contract Testing"
    simulate_test_execution "contract" "order-service"
    
    # Step 4: Contract Test Failure Scenario
    log_demo "Step 4: Contract Test Failure Scenario"
    simulate_contract_failure
    
    # Step 5: AI-Powered Failure Analysis
    log_demo "Step 5: AI-Powered Failure Analysis"
    send_jarvis_command "analyze the contract test failure and provide recommendations" '{"failure_type": "contract", "service": "order-service"}' "Failure Analysis"
    
    # Step 6: Chaos Testing
    log_demo "Step 6: Chaos Engineering with AI Orchestration"
    send_jarvis_command "run chaos test on product-service with network latency" '{"service": "product-service", "chaos_type": "network_latency", "duration": "30s"}' "Chaos Testing"
    simulate_test_execution "chaos" "product-service"
    
    # Step 7: AI Analysis and Recommendations
    log_demo "Step 7: AI-Powered Analysis and Recommendations"
    show_ai_analysis
    
    # Step 8: Final Comprehensive Assessment
    log_demo "Step 8: Final Comprehensive System Assessment"
    send_jarvis_command "provide a comprehensive summary of all test results and system health" '{"summary_type": "comprehensive"}' "Final Assessment"
    
    echo
    echo "================================================"
    log_demo "Demo Completed Successfully!"
    echo "================================================"
    echo
    log_success "All demonstrations completed successfully"
    echo
    log_demo "Key Features Demonstrated:"
    echo -e "${GREEN}   ✅ Natural Language Processing for Test Commands${NC}"
    echo -e "${GREEN}   ✅ AI-Powered Test Orchestration${NC}"
    echo -e "${GREEN}   ✅ Intelligent Health Assessment${NC}"
    echo -e "${GREEN}   ✅ Performance Testing with AI Analysis${NC}"
    echo -e "${GREEN}   ✅ Contract Testing with Failure Detection${NC}"
    echo -e "${GREEN}   ✅ Chaos Engineering Integration${NC}"
    echo -e "${GREEN}   ✅ AI-Powered Insights and Recommendations${NC}"
    echo -e "${GREEN}   ✅ Comprehensive System Assessment${NC}"
    echo
    log_demo "Next Steps:"
    echo -e "${CYAN}   • Try the interactive CLI: ./jarvis-cli.sh${NC}"
    echo -e "${CYAN}   • Explore chaos engineering: ./scripts/setup/install-litmus.sh${NC}"
    echo -e "${CYAN}   • Review test results in the database${NC}"
    echo
}

# Run main function
main "$@"
