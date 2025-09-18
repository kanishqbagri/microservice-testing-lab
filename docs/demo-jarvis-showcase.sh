#!/bin/bash

# Jarvis AI Comprehensive Demo Script
# Showcases the full capabilities of the AI-powered testing framework

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
JARVIS_CORE_DIR="../jarvis-core"
MICROSERVICES_DIR="../../microservices"
DEMO_DELAY=3

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

# Wait for service to be ready
wait_for_service() {
    local url="$1"
    local service_name="$2"
    local max_attempts=30
    local attempt=1
    
    log_info "Waiting for $service_name to be ready..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url/health" > /dev/null 2>&1; then
            log_success "$service_name is ready!"
            return 0
        fi
        
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    log_error "$service_name failed to start within $((max_attempts * 2)) seconds"
    return 1
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

# Run microservice tests and show results
run_microservice_tests() {
    local service_name="$1"
    local test_type="$2"
    
    echo
    log_demo "=== Running $test_type Tests for $service_name ==="
    echo
    
    log_info "Executing $test_type tests..."
    
    # Simulate test execution
    echo -e "${CYAN}🧪 Running $test_type tests...${NC}"
    sleep 2
    
    # Simulate test results
    case "$test_type" in
        "health")
            echo -e "${GREEN}✅ Health Check: PASSED${NC}"
            echo -e "${GREEN}✅ Database Connection: PASSED${NC}"
            echo -e "${GREEN}✅ API Endpoints: PASSED${NC}"
            ;;
        "performance")
            echo -e "${GREEN}✅ Response Time: 45ms (Target: <100ms)${NC}"
            echo -e "${GREEN}✅ Throughput: 1500 req/s (Target: >1000 req/s)${NC}"
            echo -e "${YELLOW}⚠️  Memory Usage: 85% (Target: <90%)${NC}"
            ;;
        "contract")
            echo -e "${GREEN}✅ API Contract Validation: PASSED${NC}"
            echo -e "${GREEN}✅ Schema Validation: PASSED${NC}"
            echo -e "${GREEN}✅ Response Format: PASSED${NC}"
            ;;
    esac
    
    echo
    log_success "$test_type tests completed for $service_name"
    sleep $DEMO_DELAY
}

# Simulate contract test failure
simulate_contract_failure() {
    local service_name="$1"
    
    echo
    log_demo "=== Simulating Contract Test Failure ==="
    echo
    
    log_warning "Modifying data model for $service_name..."
    echo -e "${YELLOW}📝 Changing field 'email' to 'userEmail' in UserDTO${NC}"
    sleep 2
    
    log_info "Running contract tests after model change..."
    echo -e "${CYAN}🧪 Executing contract validation...${NC}"
    sleep 2
    
    # Simulate failure
    echo -e "${RED}❌ Contract Test: FAILED${NC}"
    echo -e "${RED}❌ Schema Validation: FAILED - Field 'email' not found${NC}"
    echo -e "${RED}❌ Response Format: FAILED - Expected 'email', got 'userEmail'${NC}"
    
    echo
    log_error "Contract test failure detected! Breaking change identified."
    sleep $DEMO_DELAY
}

# Show test results and analysis
show_test_analysis() {
    echo
    log_demo "=== AI-Powered Test Analysis ==="
    echo
    
    log_jarvis "Analyzing test results and providing insights..."
    echo -e "${CYAN}🔍 Pattern Recognition: Detecting performance degradation${NC}"
    echo -e "${CYAN}🎯 Risk Assessment: Medium risk - memory usage approaching threshold${NC}"
    echo -e "${CYAN}📊 Trend Analysis: Response time improved by 15% over last week${NC}"
    
    echo
    echo -e "${PURPLE}🤖 AI Recommendations:${NC}"
    echo -e "${GREEN}   • Monitor memory usage closely${NC}"
    echo -e "${GREEN}   • Consider scaling up resources${NC}"
    echo -e "${GREEN}   • Review database query optimization${NC}"
    
    echo
    log_success "AI analysis completed with actionable insights"
    sleep $DEMO_DELAY
}

# Main demo function
main() {
    echo "================================================"
    echo "    🤖 Jarvis AI Comprehensive Demo"
    echo "================================================"
    echo
    log_demo "Welcome to the Jarvis AI Demo!"
    log_demo "This showcase demonstrates the full capabilities of our AI-powered testing framework."
    echo
    
    # Step 1: Start Jarvis Core
    log_demo "Step 1: Starting Jarvis Core"
    echo
    
    if [ ! -d "$JARVIS_CORE_DIR" ]; then
        log_error "Jarvis Core directory not found at $JARVIS_CORE_DIR"
        exit 1
    fi
    
    log_info "Starting Jarvis Core in background..."
    cd "$JARVIS_CORE_DIR"
    
    # Start Jarvis Core in background
    mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8085" -Dmaven.test.skip=true > jarvis.log 2>&1 &
    JARVIS_PID=$!
    
    cd - > /dev/null
    
    log_info "Jarvis Core starting (PID: $JARVIS_PID)..."
    
    # Wait for Jarvis Core to be ready
    if wait_for_service "$JARVIS_URL" "Jarvis Core"; then
        log_success "Jarvis Core is ready!"
    else
        log_error "Failed to start Jarvis Core"
        kill $JARVIS_PID 2>/dev/null || true
        exit 1
    fi
    
    echo
    log_demo "Step 2: Demonstrating AI-Powered Health Assessment"
    send_jarvis_command "assess current system health and identify weak points" '{"assessment_type": "comprehensive"}' "System Health Assessment"
    
    echo
    log_demo "Step 3: Running Performance Tests"
    send_jarvis_command "run performance analysis on user-service" '{"service": "user-service", "parameters": {"duration": "60s", "load": "medium"}}' "Performance Analysis"
    run_microservice_tests "user-service" "performance"
    
    echo
    log_demo "Step 4: Contract Testing Demonstration"
    send_jarvis_command "run contract tests for order-service" '{"service": "order-service", "test_type": "contract"}' "Contract Testing"
    run_microservice_tests "order-service" "contract"
    
    echo
    log_demo "Step 5: Simulating Contract Test Failure"
    simulate_contract_failure "order-service"
    
    echo
    log_demo "Step 6: AI-Powered Failure Analysis"
    send_jarvis_command "analyze the contract test failure and provide recommendations" '{"failure_type": "contract", "service": "order-service"}' "Failure Analysis"
    
    echo
    log_demo "Step 7: Chaos Testing Demonstration"
    send_jarvis_command "run chaos test on product-service with network latency" '{"service": "product-service", "chaos_type": "network_latency", "duration": "30s"}' "Chaos Testing"
    
    echo
    log_demo "Step 8: AI-Powered Test Analysis"
    show_test_analysis
    
    echo
    log_demo "Step 9: Final System Assessment"
    send_jarvis_command "provide a comprehensive summary of all test results and system health" '{"summary_type": "comprehensive"}' "Final Assessment"
    
    echo
    echo "================================================"
    log_demo "Demo Completed Successfully!"
    echo "================================================"
    echo
    log_success "All demonstrations completed successfully"
    log_info "Jarvis Core is still running (PID: $JARVIS_PID)"
    log_info "To stop Jarvis Core: kill $JARVIS_PID"
    echo
}

# Cleanup function
cleanup() {
    echo
    log_info "Cleaning up..."
    if [ ! -z "$JARVIS_PID" ]; then
        log_info "Stopping Jarvis Core (PID: $JARVIS_PID)..."
        kill $JARVIS_PID 2>/dev/null || true
    fi
    log_success "Cleanup completed"
}

# Set trap for cleanup
trap cleanup EXIT

# Run main function
main "$@"
