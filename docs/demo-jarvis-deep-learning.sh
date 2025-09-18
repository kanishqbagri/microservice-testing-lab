#!/bin/bash

# Jarvis Deep Learning Integration Demo
# This script demonstrates the Phase 3 deep learning capabilities

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
check_jarvis() {
    if ! curl -s "$JARVIS_URL/health" > /dev/null 2>&1; then
        log_error "Jarvis Core is not running at $JARVIS_URL"
        log_info "Please start Jarvis Core first:"
        log_info "cd microservice-testing-lab/jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
        exit 1
    fi
}

# Send command to Jarvis AI and display response
send_command() {
    local command="$1"
    local description="$2"
    
    echo
    log_demo "🎯 $description"
    log_jarvis "Command: $command"
    echo -e "${CYAN}⏳ Processing with Deep Learning...${NC}"
    
    # Send the command to Jarvis AI
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d "{\"command\": \"$command\"}")
    
    # Display the response
    echo -e "${GREEN}🤖 Jarvis AI Deep Learning Response:${NC}"
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
        
        # Extract deep learning insights
        local insights=$(echo "$response" | jq -r '.insights[]? // empty' 2>/dev/null)
        if [ ! -z "$insights" ]; then
            echo -e "${PURPLE}🧠 Deep Learning Insights:${NC}"
            echo "$insights" | while read -r insight; do
                if [ ! -z "$insight" ] && [ "$insight" != "null" ]; then
                    echo -e "${CYAN}   • $insight${NC}"
                fi
            done
            echo
        fi
        
        # Extract pattern recognition results
        local patterns=$(echo "$response" | jq -r '.patterns[]? // empty' 2>/dev/null)
        if [ ! -z "$patterns" ]; then
            echo -e "${GREEN}🔍 Pattern Recognition Results:${NC}"
            echo "$patterns" | while read -r pattern; do
                if [ ! -z "$pattern" ] && [ "$pattern" != "null" ]; then
                    echo -e "${YELLOW}   • $pattern${NC}"
                fi
            done
            echo
        fi
        
        # Extract framework recommendations
        local frameworks=$(echo "$response" | jq -r '.frameworks[]? // empty' 2>/dev/null)
        if [ ! -z "$frameworks" ]; then
            echo -e "${BLUE}🛠️  Framework Recommendations:${NC}"
            echo "$frameworks" | while read -r framework; do
                if [ ! -z "$framework" ] && [ "$framework" != "null" ]; then
                    echo -e "${GREEN}   • $framework${NC}"
                fi
            done
            echo
        fi
        
    else
        # Fallback: show raw JSON
        echo "$response"
    fi
    
    echo "----------------------------------------"
    echo
}

# Demo scenarios
demo_pattern_recognition() {
    log_demo "🧠 DEMO 1: Deep Learning Pattern Recognition"
    echo "This demo shows how Jarvis uses deep learning to recognize test patterns"
    echo "and predict optimal test execution strategies."
    echo
    
    send_command "analyze test patterns for user-service and identify optimal execution strategies" \
        "Pattern Recognition Analysis"
    
    sleep $DEMO_DELAY
    
    send_command "predict test execution time and success probability for performance tests on order-service" \
        "Test Execution Prediction"
    
    sleep $DEMO_DELAY
    
    send_command "identify anomalies in recent test executions and provide recommendations" \
        "Anomaly Detection and Recommendations"
}

demo_framework_selection() {
    log_demo "🛠️  DEMO 2: Intelligent Test Framework Selection"
    echo "This demo shows how Jarvis intelligently selects the best test framework"
    echo "for each scenario using machine learning."
    echo
    
    send_command "select optimal test framework for API testing on gateway-service with high performance requirements" \
        "Framework Selection for API Testing"
    
    sleep $DEMO_DELAY
    
    send_command "compare test frameworks for integration testing on user-service and product-service" \
        "Framework Comparison for Integration Testing"
    
    sleep $DEMO_DELAY
    
    send_command "recommend test framework for performance testing with load testing capabilities" \
        "Framework Recommendation for Performance Testing"
}

demo_intelligent_execution() {
    log_demo "⚡ DEMO 3: Intelligent Test Execution with Deep Learning"
    echo "This demo shows how Jarvis executes tests intelligently using"
    echo "deep learning for optimization and monitoring."
    echo
    
    send_command "execute comprehensive test suite for all services using intelligent framework selection and adaptive execution" \
        "Intelligent Test Suite Execution"
    
    sleep $DEMO_DELAY
    
    send_command "run performance tests on user-service with intelligent resource allocation and real-time monitoring" \
        "Intelligent Performance Testing"
    
    sleep $DEMO_DELAY
    
    send_command "execute parallel tests across multiple services with intelligent load balancing" \
        "Intelligent Parallel Test Execution"
}

demo_learning_and_adaptation() {
    log_demo "🎓 DEMO 4: Continuous Learning and Adaptation"
    echo "This demo shows how Jarvis continuously learns from test executions"
    echo "and adapts its recommendations over time."
    echo
    
    send_command "analyze learning progress and show how recommendations have improved over time" \
        "Learning Progress Analysis"
    
    sleep $DEMO_DELAY
    
    send_command "show adaptive improvements in test execution based on historical data" \
        "Adaptive Improvement Analysis"
    
    sleep $DEMO_DELAY
    
    send_command "demonstrate how the system learns from failures and improves success rates" \
        "Failure Learning and Improvement"
}

demo_predictive_analytics() {
    log_demo "🔮 DEMO 5: Predictive Analytics and Forecasting"
    echo "This demo shows how Jarvis uses predictive analytics to forecast"
    echo "test outcomes and system behavior."
    echo
    
    send_command "predict test failure probability for upcoming test executions based on current system state" \
        "Test Failure Prediction"
    
    sleep $DEMO_DELAY
    
    send_command "forecast system performance under different test load scenarios" \
        "Performance Forecasting"
    
    sleep $DEMO_DELAY
    
    send_command "predict optimal test scheduling based on historical patterns and system capacity" \
        "Optimal Test Scheduling Prediction"
}

demo_advanced_analytics() {
    log_demo "📊 DEMO 6: Advanced Analytics and Insights"
    echo "This demo shows advanced analytics capabilities including"
    echo "trend analysis, correlation detection, and intelligent reporting."
    echo
    
    send_command "analyze test execution trends over the last 30 days and identify key insights" \
        "Test Execution Trend Analysis"
    
    sleep $DEMO_DELAY
    
    send_command "detect correlations between test failures and system performance metrics" \
        "Correlation Analysis"
    
    sleep $DEMO_DELAY
    
    send_command "generate intelligent test report with actionable recommendations for improvement" \
        "Intelligent Test Reporting"
}

# Main demo function
main() {
    echo "=========================================="
    echo "    🤖 Jarvis Deep Learning Integration"
    echo "           Phase 3 Demo Showcase"
    echo "=========================================="
    echo
    echo -e "${GREEN}🚀 Welcome to the Jarvis Deep Learning Demo!${NC}"
    echo
    echo "This demo showcases the advanced deep learning capabilities"
    echo "integrated into Jarvis for intelligent test automation."
    echo
    
    check_jarvis
    log_success "Connected to Jarvis AI with Deep Learning at $JARVIS_URL"
    echo
    
    # Run all demo scenarios
    demo_pattern_recognition
    demo_framework_selection
    demo_intelligent_execution
    demo_learning_and_adaptation
    demo_predictive_analytics
    demo_advanced_analytics
    
    echo
    echo "=========================================="
    echo -e "${GREEN}🎉 Deep Learning Demo Completed!${NC}"
    echo "=========================================="
    echo
    echo -e "${BLUE}Key Deep Learning Features Demonstrated:${NC}"
    echo "• 🧠 Pattern Recognition and Analysis"
    echo "• 🛠️  Intelligent Framework Selection"
    echo "• ⚡ Adaptive Test Execution"
    echo "• 🎓 Continuous Learning and Improvement"
    echo "• 🔮 Predictive Analytics and Forecasting"
    echo "• 📊 Advanced Analytics and Insights"
    echo
    echo -e "${YELLOW}Next Steps:${NC}"
    echo "• Deploy deep learning models to production"
    echo "• Train models with real test data"
    echo "• Integrate with CI/CD pipelines"
    echo "• Monitor model performance and accuracy"
    echo
    echo -e "${GREEN}Thank you for exploring Jarvis Deep Learning! 🚀${NC}"
}

# Run main function
main "$@"
