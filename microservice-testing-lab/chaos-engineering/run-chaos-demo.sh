#!/bin/bash

# 🎭 Chaos Engineering Demo Runner
# Quick launcher for the chaos engineering live demo

set -e

# Colors
CYAN='\033[0;36m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# Function to print colored output
print_header() {
    echo -e "${CYAN}${BOLD}$1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Function to show banner
show_banner() {
    clear
    echo -e "${CYAN}${BOLD}"
    echo "╔══════════════════════════════════════════════════════════════════════════════╗"
    echo "║                                                                              ║"
    echo "║  🎭 CHAOS ENGINEERING DEMO RUNNER                                            ║"
    echo "║                                                                              ║"
    echo "║  Quick launcher for real-time chaos experiment demonstrations                ║"
    echo "║                                                                              ║"
    echo "╚══════════════════════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to check if we're in the right directory
check_directory() {
    if [ ! -f "chaos-demo-live.sh" ]; then
        print_error "chaos-demo-live.sh not found in current directory"
        print_info "Please run this script from the chaos-engineering directory"
        exit 1
    fi
}

# Function to show menu
show_menu() {
    echo ""
    print_header "🎭 Chaos Engineering Demo Options"
    echo ""
    echo -e "${WHITE}1.${NC} ${BOLD}Run Full Live Demo${NC}"
    echo -e "   ${CYAN}Interactive menu with real-time monitoring${NC}"
    echo ""
    echo -e "${WHITE}2.${NC} ${BOLD}Validate Services${NC}"
    echo -e "   ${CYAN}Check service health and prerequisites${NC}"
    echo ""
    echo -e "${WHITE}3.${NC} ${BOLD}Quick Pod Failure Demo${NC}"
    echo -e "   ${CYAN}Run pod failure experiment with monitoring${NC}"
    echo ""
    echo -e "${WHITE}4.${NC} ${BOLD}Quick Network Latency Demo${NC}"
    echo -e "   ${CYAN}Run network latency experiment with monitoring${NC}"
    echo ""
    echo -e "${WHITE}5.${NC} ${BOLD}Show Service Status${NC}"
    echo -e "   ${CYAN}Display current microservice status${NC}"
    echo ""
    echo -e "${WHITE}6.${NC} ${BOLD}Exit${NC}"
    echo -e "   ${CYAN}Exit the demo runner${NC}"
    echo ""
}

# Function to run full demo
run_full_demo() {
    print_info "Starting full chaos engineering live demo..."
    ./chaos-demo-live.sh
}

# Function to validate services
validate_services() {
    print_info "Validating services and prerequisites..."
    if [ -f "scripts/validate-services.sh" ]; then
        ./scripts/validate-services.sh
    else
        print_error "Service validation script not found"
    fi
}

# Function to run quick pod failure demo
run_pod_failure_demo() {
    print_info "Running quick pod failure demo..."
    
    # Check if experiment file exists
    if [ ! -f "experiments/service/pod-failure.yaml" ]; then
        print_error "Pod failure experiment file not found"
        return 1
    fi
    
    # Apply the experiment
    print_info "Applying pod failure experiment..."
    if kubectl apply -f experiments/service/pod-failure.yaml; then
        print_success "Pod failure experiment applied"
    else
        print_error "Failed to apply pod failure experiment"
        return 1
    fi
    
    # Start monitoring
    if [ -f "scripts/monitor-experiment.sh" ]; then
        print_info "Starting monitoring..."
        ./scripts/monitor-experiment.sh pod-failure microservices-product-service 2
    else
        print_warning "Monitoring script not found, experiment is running but not monitored"
    fi
}

# Function to run quick network latency demo
run_network_latency_demo() {
    print_info "Running quick network latency demo..."
    
    # Check if experiment file exists
    if [ ! -f "experiments/network/network-latency.yaml" ]; then
        print_error "Network latency experiment file not found"
        return 1
    fi
    
    # Apply the experiment
    print_info "Applying network latency experiment..."
    if kubectl apply -f experiments/network/network-latency.yaml; then
        print_success "Network latency experiment applied"
    else
        print_error "Failed to apply network latency experiment"
        return 1
    fi
    
    # Start monitoring
    if [ -f "scripts/monitor-experiment.sh" ]; then
        print_info "Starting monitoring..."
        ./scripts/monitor-experiment.sh network-latency microservices 2
    else
        print_warning "Monitoring script not found, experiment is running but not monitored"
    fi
}

# Function to show service status
show_service_status() {
    print_info "Showing current service status..."
    if [ -f "scripts/validate-services.sh" ]; then
        ./scripts/validate-services.sh status
    else
        print_error "Service status script not found"
    fi
}

# Function to cleanup experiments
cleanup_experiments() {
    print_info "Cleaning up any running experiments..."
    
    local experiments=("pod-failure" "network-latency" "database-connection" "node-failure")
    
    for exp in "${experiments[@]}"; do
        if kubectl get chaosengine "$exp" -n litmus &> /dev/null; then
            print_info "Cleaning up experiment: $exp"
            kubectl delete chaosengine "$exp" -n litmus --ignore-not-found=true
        fi
        
        if kubectl get chaosresult "$exp" -n litmus &> /dev/null; then
            kubectl delete chaosresult "$exp" -n litmus --ignore-not-found=true
        fi
    done
    
    print_success "Cleanup completed"
}

# Function to handle cleanup on exit
cleanup_on_exit() {
    print_info "Cleaning up on exit..."
    cleanup_experiments
}

# Set up signal handlers
trap cleanup_on_exit EXIT INT TERM

# Main function
main() {
    show_banner
    check_directory
    
    while true; do
        show_menu
        echo -e "${WHITE}Select an option (1-6): ${NC}"
        read -r choice
        
        case $choice in
            1)
                run_full_demo
                ;;
            2)
                validate_services
                echo ""
                print_info "Press Enter to continue..."
                read -r
                ;;
            3)
                run_pod_failure_demo
                echo ""
                print_info "Press Enter to continue..."
                read -r
                ;;
            4)
                run_network_latency_demo
                echo ""
                print_info "Press Enter to continue..."
                read -r
                ;;
            5)
                show_service_status
                echo ""
                print_info "Press Enter to continue..."
                read -r
                ;;
            6)
                print_info "Exiting chaos engineering demo runner..."
                exit 0
                ;;
            *)
                print_error "Invalid option. Please select a valid number (1-6)."
                sleep 2
                ;;
        esac
    done
}

# Run main function
main "$@"
