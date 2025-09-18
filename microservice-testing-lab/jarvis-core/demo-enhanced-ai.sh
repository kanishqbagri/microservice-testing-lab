#!/bin/bash

# Enhanced AI Engine Demonstration Script
# This script demonstrates the enhanced AI engine capabilities

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
JARVIS_URL="http://localhost:8080"
DEMO_COMMANDS=(
    "Run chaos on Orders"
    "Execute performance tests for user service"
    "Analyze failures in product service"
    "Generate integration tests for all services"
    "Check health status of gateway service"
    "Run smoke tests on notification service"
    "Start load testing for order service"
    "Optimize test suite for user service"
)

echo -e "${CYAN}🤖 Enhanced AI Engine Demonstration${NC}"
echo -e "${CYAN}====================================${NC}"
echo

# Function to test AI processing
test_ai_processing() {
    local command="$1"
    echo -e "${YELLOW}📝 Testing Command:${NC} $command"
    echo -e "${BLUE}⏳ Processing...${NC}"
    
    # Call the enhanced AI endpoint
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/ai/process" \
        -H "Content-Type: application/json" \
        -d "{\"command\": \"$command\"}")
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Processing successful${NC}"
        
        # Extract key information using jq if available
        if command -v jq &> /dev/null; then
            echo -e "${PURPLE}📊 Results:${NC}"
            echo "  Action Type: $(echo "$response" | jq -r '.executableAction.actionType // "N/A"')"
            echo "  Test Type: $(echo "$response" | jq -r '.executableAction.testType // "N/A"')"
            echo "  Service: $(echo "$response" | jq -r '.executableAction.serviceName // "N/A"')"
            echo "  Confidence: $(echo "$response" | jq -r '.processingConfidence // "N/A"')"
            echo "  Estimated Duration: $(echo "$response" | jq -r '.executableAction.estimatedDuration // "N/A"')"
            
            # Show suggestions
            echo -e "${CYAN}💡 Suggestions:${NC}"
            echo "$response" | jq -r '.suggestions[]? // empty' | while read -r suggestion; do
                echo "  • $suggestion"
            done
            
            # Show warnings
            echo -e "${YELLOW}⚠️  Warnings:${NC}"
            echo "$response" | jq -r '.warnings[]? // empty' | while read -r warning; do
                echo "  • $warning"
            done
        else
            echo -e "${YELLOW}📄 Raw Response:${NC}"
            echo "$response" | head -20
        fi
    else
        echo -e "${RED}❌ Processing failed${NC}"
    fi
    
    echo
    echo "----------------------------------------"
    echo
}

# Function to test example commands
test_example_commands() {
    echo -e "${CYAN}🎯 Testing Example Commands${NC}"
    echo -e "${CYAN}============================${NC}"
    echo
    
    response=$(curl -s -X GET "$JARVIS_URL/api/jarvis/ai/examples")
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Example commands processed successfully${NC}"
        
        if command -v jq &> /dev/null; then
            echo -e "${PURPLE}📊 Summary:${NC}"
            echo "$response" | jq -r 'to_entries[] | "\(.key) -> \(.value.executableAction.actionType // "N/A")"' | while read -r line; do
                echo "  $line"
            done
        else
            echo -e "${YELLOW}📄 Raw Response:${NC}"
            echo "$response" | head -30
        fi
    else
        echo -e "${RED}❌ Failed to process example commands${NC}"
    fi
    
    echo
}

# Function to get AI statistics
get_ai_statistics() {
    echo -e "${CYAN}📈 AI Processing Statistics${NC}"
    echo -e "${CYAN}============================${NC}"
    echo
    
    response=$(curl -s -X GET "$JARVIS_URL/api/jarvis/ai/statistics")
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Statistics retrieved successfully${NC}"
        
        if command -v jq &> /dev/null; then
            echo -e "${PURPLE}📊 Statistics:${NC}"
            echo "  Total Processed: $(echo "$response" | jq -r '.totalProcessed // "N/A"')"
            echo "  Successful: $(echo "$response" | jq -r '.successfulProcessings // "N/A"')"
            echo "  Average Confidence: $(echo "$response" | jq -r '.averageConfidence // "N/A"')"
            echo "  Most Common Intent: $(echo "$response" | jq -r '.mostCommonIntent // "N/A"')"
            echo "  Average Processing Time: $(echo "$response" | jq -r '.averageProcessingTime // "N/A"')"
        else
            echo -e "${YELLOW}📄 Raw Response:${NC}"
            echo "$response"
        fi
    else
        echo -e "${RED}❌ Failed to retrieve statistics${NC}"
    fi
    
    echo
}

# Function to check AI health
check_ai_health() {
    echo -e "${CYAN}🏥 AI Engine Health Check${NC}"
    echo -e "${CYAN}==========================${NC}"
    echo
    
    response=$(curl -s -X GET "$JARVIS_URL/api/jarvis/ai/health")
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Health check successful${NC}"
        
        if command -v jq &> /dev/null; then
            echo -e "${PURPLE}📊 Health Status:${NC}"
            echo "  Status: $(echo "$response" | jq -r '.status // "N/A"')"
            echo "  Engine: $(echo "$response" | jq -r '.engine // "N/A"')"
            echo "  Test Result: $(echo "$response" | jq -r '.testResult // "N/A"')"
        else
            echo -e "${YELLOW}📄 Raw Response:${NC}"
            echo "$response"
        fi
    else
        echo -e "${RED}❌ Health check failed${NC}"
    fi
    
    echo
}

# Function to run interactive demo
run_interactive_demo() {
    echo -e "${CYAN}🎮 Interactive Demo${NC}"
    echo -e "${CYAN}==================${NC}"
    echo
    echo -e "${YELLOW}Enter commands to test the Enhanced AI Engine (type 'quit' to exit):${NC}"
    echo
    
    while true; do
        echo -e "${BLUE}Jarvis AI>${NC} "
        read -r user_input
        
        if [ "$user_input" = "quit" ] || [ "$user_input" = "exit" ]; then
            echo -e "${GREEN}👋 Goodbye!${NC}"
            break
        fi
        
        if [ -z "$user_input" ]; then
            continue
        fi
        
        test_ai_processing "$user_input"
    done
}

# Main execution
main() {
    echo -e "${GREEN}🚀 Starting Enhanced AI Engine Demonstration${NC}"
    echo
    
    # Check if Jarvis is running
    echo -e "${YELLOW}🔍 Checking if Jarvis is running...${NC}"
    if ! curl -s "$JARVIS_URL/api/jarvis/status" > /dev/null; then
        echo -e "${RED}❌ Jarvis is not running at $JARVIS_URL${NC}"
        echo -e "${YELLOW}💡 Please start Jarvis first:${NC}"
        echo "   cd microservice-testing-lab/jarvis-core"
        echo "   mvn spring-boot:run"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Jarvis is running${NC}"
    echo
    
    # Run health check
    check_ai_health
    
    # Test individual commands
    echo -e "${CYAN}🧪 Testing Individual Commands${NC}"
    echo -e "${CYAN}==============================${NC}"
    echo
    
    for command in "${DEMO_COMMANDS[@]}"; do
        test_ai_processing "$command"
    done
    
    # Test example commands
    test_example_commands
    
    # Get statistics
    get_ai_statistics
    
    # Ask if user wants interactive demo
    echo -e "${YELLOW}Would you like to run an interactive demo? (y/n):${NC} "
    read -r response
    
    if [ "$response" = "y" ] || [ "$response" = "Y" ] || [ "$response" = "yes" ]; then
        run_interactive_demo
    fi
    
    echo -e "${GREEN}🎉 Demonstration complete!${NC}"
    echo
    echo -e "${CYAN}📚 Key Features Demonstrated:${NC}"
    echo "  • Enhanced NLP processing with spaCy-like capabilities"
    echo "  • Intelligent entity extraction (services, test types, actions)"
    echo "  • Context-aware processing with dependency analysis"
    echo "  • Smart action mapping with confidence scoring"
    echo "  • Comprehensive suggestions and warnings"
    echo "  • Fuzzy matching for typos and variations"
    echo "  • Priority and parameter extraction"
    echo
    echo -e "${YELLOW}🔗 API Endpoints:${NC}"
    echo "  POST /api/jarvis/ai/process - Process natural language commands"
    echo "  GET  /api/jarvis/ai/examples - Get example command results"
    echo "  GET  /api/jarvis/ai/statistics - Get processing statistics"
    echo "  GET  /api/jarvis/ai/health - Health check"
}

# Run main function
main "$@"
