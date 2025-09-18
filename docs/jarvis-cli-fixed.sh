#!/bin/bash

# Jarvis AI Natural Language CLI - Fixed Version
# Type English commands directly in the terminal

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
USER_NAME="Kanishq"

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

# Check if Jarvis is running
check_jarvis() {
    if ! curl -s "$JARVIS_URL/health" > /dev/null 2>&1; then
        log_error "Jarvis Core is not running at $JARVIS_URL"
        log_info "Please start Jarvis Core first:"
        log_info "cd ../jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
        exit 1
    fi
}

# Send command to Jarvis AI and display response
send_command() {
    local command="$1"
    
    echo
    log_jarvis "Sending command: $command"
    echo -e "${CYAN}⏳ Processing...${NC}"
    
    # Send the command to Jarvis AI
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d "{\"command\": \"$command\"}")
    
    # Display the response
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
        # Fallback: show raw JSON
        echo "$response"
    fi
    
    echo "----------------------------------------"
    echo
}

# Show help
show_help() {
    echo
    echo "🤖 Jarvis AI Natural Language Commands"
    echo "======================================"
    echo
    echo "📊 Health & Status:"
    echo "  • 'check health'"
    echo "  • 'show system status'"
    echo
    echo "🧪 Chaos Testing:"
    echo "  • 'run chaos test'"
    echo "  • 'run chaos test on user-service'"
    echo "  • 'run chaos test on order-service'"
    echo
    echo "📈 Performance & Analysis:"
    echo "  • 'run performance test'"
    echo "  • 'analyze patterns'"
    echo
    echo "🛑 Control:"
    echo "  • 'stop all tests'"
    echo "  • 'emergency stop'"
    echo
    echo "Type 'quit' or 'exit' to leave"
    echo
}

# Main CLI loop
main() {
    echo "=========================================="
    echo "    🤖 Jarvis AI Natural Language CLI"
    echo "=========================================="
    echo
    echo -e "${GREEN}👋 Welcome back $USER_NAME!${NC}"
    echo
    echo "Type English commands directly. Type 'help' for examples."
    echo "Type 'quit' or 'exit' to leave."
    echo
    
    check_jarvis
    log_success "Connected to Jarvis AI at $JARVIS_URL"
    echo
    
    while true; do
        echo -e "${PURPLE}Jarvis>${NC} "
        read -r user_input
        
        if [ -z "$user_input" ]; then
            continue
        fi
        
        # Handle special commands
        case "$user_input" in
            "quit"|"exit")
                echo -e "${GREEN}👋 Goodbye $USER_NAME! Have a great day!${NC}"
                exit 0
                ;;
            "help")
                show_help
                continue
                ;;
        esac
        
        # Send all other commands directly to Jarvis AI
        send_command "$user_input"
    done
}

# Run main function
main "$@"
