#!/bin/bash

# 🎭 Chaos Engineering Live Demo Script
# Real-time chaos experiment demonstration with interactive monitoring
# Author: AI Assistant
# Version: 1.0.0

set -e

# Colors and formatting
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# Demo configuration
DEMO_TITLE="🎭 Chaos Engineering Live Demo"
DEMO_VERSION="v1.0.0"
EXPERIMENT_TIMEOUT=60
MONITORING_INTERVAL=2

# Service configurations
declare -A SERVICES=(
    ["gateway"]="8080:Gateway Service:microservices-gateway-service"
    ["user"]="8081:User Service:microservices-user-service"
    ["product"]="8082:Product Service:microservices-product-service"
    ["order"]="8083:Order Service:microservices-order-service"
    ["notification"]="8084:Notification Service:microservices-notification-service"
)

# Experiment configurations
declare -A EXPERIMENTS=(
    ["pod-failure"]="Pod Failure:Terminates pods to test restart resilience:experiments/service/pod-failure.yaml"
    ["network-latency"]="Network Latency:Injects network latency to test timeout handling:experiments/network/network-latency.yaml"
    ["database-connection"]="Database Connection:Simulates database connection failures:experiments/data/database-connection.yaml"
    ["node-failure"]="Node Failure:Drains nodes to test multi-node resilience:experiments/infrastructure/node-failure.yaml"
)

# Global variables
CURRENT_EXPERIMENT=""
EXPERIMENT_START_TIME=""
MONITORING_PID=""

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

print_chaos() {
    echo -e "${PURPLE}🎭 $1${NC}"
}

print_demo() {
    echo -e "${WHITE}${BOLD}$1${NC}"
}

# Function to display banner
show_banner() {
    clear
    echo -e "${CYAN}${BOLD}"
    echo "╔══════════════════════════════════════════════════════════════════════════════╗"
    echo "║                                                                              ║"
    echo "║  🎭 CHAOS ENGINEERING LIVE DEMO - Real-time Resilience Testing              ║"
    echo "║                                                                              ║"
    echo "║  Version: $DEMO_VERSION                                                           ║"
    echo "║  Purpose: Demonstrate microservice resilience through controlled chaos       ║"
    echo "║                                                                              ║"
    echo "╚══════════════════════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to check prerequisites
check_prerequisites() {
    print_header "🔍 Checking Prerequisites..."
    
    local all_good=true
    
    # Check kubectl
    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl is not installed or not in PATH"
        all_good=false
    else
        print_success "kubectl is available"
    fi
    
    # Check if kubectl can connect to cluster
    if ! kubectl cluster-info &> /dev/null; then
        print_error "Cannot connect to Kubernetes cluster"
        all_good=false
    else
        print_success "Connected to Kubernetes cluster"
    fi
    
    # Check if litmus namespace exists
    if ! kubectl get namespace litmus &> /dev/null; then
        print_warning "litmus namespace not found - experiments may not work"
    else
        print_success "litmus namespace exists"
    fi
    
    # Check if chaos experiments are installed
    if ! kubectl get chaosexperiments -n litmus &> /dev/null; then
        print_warning "Chaos experiments not installed in litmus namespace"
    else
        print_success "Chaos experiments are available"
    fi
    
    # Check if microservices are running
    print_info "Checking microservice status..."
    for service in "${!SERVICES[@]}"; do
        IFS=':' read -r port name namespace <<< "${SERVICES[$service]}"
        if kubectl get pods -n "$namespace" &> /dev/null; then
            local pod_count=$(kubectl get pods -n "$namespace" --no-headers | wc -l)
            if [ "$pod_count" -gt 0 ]; then
                print_success "$name has $pod_count pod(s) running"
            else
                print_warning "$name has no running pods"
            fi
        else
            print_warning "$name namespace not found"
        fi
    done
    
    if [ "$all_good" = false ]; then
        print_error "Prerequisites check failed. Please fix the issues above."
        exit 1
    fi
    
    print_success "All prerequisites check passed!"
    echo ""
}

# Function to show service status
show_service_status() {
    print_header "📊 Current Service Status"
    echo ""
    
    for service in "${!SERVICES[@]}"; do
        IFS=':' read -r port name namespace <<< "${SERVICES[$service]}"
        echo -e "${BLUE}🔹 $name${NC}"
        
        if kubectl get namespace "$namespace" &> /dev/null; then
            local pods=$(kubectl get pods -n "$namespace" --no-headers 2>/dev/null || echo "")
            if [ -n "$pods" ]; then
                echo "$pods" | while read -r line; do
                    local pod_name=$(echo "$line" | awk '{print $1}')
                    local status=$(echo "$line" | awk '{print $3}')
                    local ready=$(echo "$line" | awk '{print $2}')
                    
                    if [ "$status" = "Running" ] && [[ "$ready" == *"/"* ]]; then
                        print_success "  Pod: $pod_name - $status ($ready)"
                    else
                        print_warning "  Pod: $pod_name - $status ($ready)"
                    fi
                done
            else
                print_warning "  No pods found"
            fi
        else
            print_error "  Namespace not found"
        fi
        echo ""
    done
}

# Function to show experiment menu
show_experiment_menu() {
    print_header "🎭 Available Chaos Experiments"
    echo ""
    
    local index=1
    for exp in "${!EXPERIMENTS[@]}"; do
        IFS=':' read -r name description file <<< "${EXPERIMENTS[$exp]}"
        echo -e "${WHITE}$index.${NC} ${BOLD}$name${NC}"
        echo -e "   ${CYAN}$description${NC}"
        echo -e "   ${YELLOW}File: $file${NC}"
        echo ""
        index=$((index + 1))
    done
    
    echo -e "${WHITE}$index.${NC} ${BOLD}Show Service Status${NC}"
    echo -e "   ${CYAN}Display current status of all microservices${NC}"
    echo ""
    echo -e "${WHITE}$((index + 1)).${NC} ${BOLD}Exit Demo${NC}"
    echo -e "   ${CYAN}Exit the chaos engineering demo${NC}"
    echo ""
}

# Function to get experiment by index
get_experiment_by_index() {
    local index=$1
    local exp_index=1
    
    for exp in "${!EXPERIMENTS[@]}"; do
        if [ "$exp_index" -eq "$index" ]; then
            echo "$exp"
            return 0
        fi
        exp_index=$((exp_index + 1))
    done
    
    return 1
}

# Function to start real-time monitoring
start_monitoring() {
    local experiment_name=$1
    local target_namespace=$2
    
    print_chaos "Starting real-time monitoring for $experiment_name..."
    
    # Start monitoring in background
    (
        while true; do
            clear
            show_banner
            print_header "🎭 Live Experiment Monitoring: $experiment_name"
            echo ""
            
            # Show experiment status
            print_info "Experiment Status:"
            if kubectl get chaosengine "$experiment_name" -n litmus &> /dev/null; then
                local engine_status=$(kubectl get chaosengine "$experiment_name" -n litmus -o jsonpath='{.status.engineStatus}' 2>/dev/null || echo "Unknown")
                local experiment_status=$(kubectl get chaosengine "$experiment_name" -n litmus -o jsonpath='{.status.experiments[0].status}' 2>/dev/null || echo "Unknown")
                
                if [ "$engine_status" = "active" ]; then
                    print_success "Engine Status: $engine_status"
                else
                    print_warning "Engine Status: $engine_status"
                fi
                
                if [ "$experiment_status" = "Running" ]; then
                    print_chaos "Experiment Status: $experiment_status"
                elif [ "$experiment_status" = "Completed" ]; then
                    print_success "Experiment Status: $experiment_status"
                else
                    print_warning "Experiment Status: $experiment_status"
                fi
            else
                print_error "ChaosEngine not found"
            fi
            
            echo ""
            
            # Show target service status
            print_info "Target Service Status:"
            if kubectl get namespace "$target_namespace" &> /dev/null; then
                local pods=$(kubectl get pods -n "$target_namespace" --no-headers 2>/dev/null || echo "")
                if [ -n "$pods" ]; then
                    echo "$pods" | while read -r line; do
                        local pod_name=$(echo "$line" | awk '{print $1}')
                        local status=$(echo "$line" | awk '{print $3}')
                        local ready=$(echo "$line" | awk '{print $2}')
                        local age=$(echo "$line" | awk '{print $5}')
                        
                        if [ "$status" = "Running" ] && [[ "$ready" == *"/"* ]]; then
                            print_success "  Pod: $pod_name - $status ($ready) - Age: $age"
                        else
                            print_warning "  Pod: $pod_name - $status ($ready) - Age: $age"
                        fi
                    done
                else
                    print_warning "  No pods found in target namespace"
                fi
            else
                print_error "  Target namespace not found"
            fi
            
            echo ""
            
            # Show experiment results if available
            print_info "Experiment Results:"
            if kubectl get chaosresult "$experiment_name" -n litmus &> /dev/null; then
                local result_status=$(kubectl get chaosresult "$experiment_name" -n litmus -o jsonpath='{.status.experimentStatus.phase}' 2>/dev/null || echo "Unknown")
                local verdict=$(kubectl get chaosresult "$experiment_name" -n litmus -o jsonpath='{.status.experimentStatus.verdict}' 2>/dev/null || echo "Unknown")
                
                if [ "$result_status" = "Completed" ]; then
                    if [ "$verdict" = "Pass" ]; then
                        print_success "Result: $result_status - Verdict: $verdict"
                    else
                        print_warning "Result: $result_status - Verdict: $verdict"
                    fi
                else
                    print_info "Result: $result_status - Verdict: $verdict"
                fi
            else
                print_info "  Results not yet available"
            fi
            
            echo ""
            print_info "Press Ctrl+C to stop monitoring and return to menu"
            echo ""
            
            sleep $MONITORING_INTERVAL
        done
    ) &
    
    MONITORING_PID=$!
}

# Function to stop monitoring
stop_monitoring() {
    if [ -n "$MONITORING_PID" ]; then
        kill $MONITORING_PID 2>/dev/null || true
        MONITORING_PID=""
    fi
}

# Function to run chaos experiment
run_experiment() {
    local experiment_key=$1
    IFS=':' read -r name description file <<< "${EXPERIMENTS[$experiment_key]}"
    
    print_chaos "Starting Chaos Experiment: $name"
    print_info "Description: $description"
    echo ""
    
    # Check if experiment file exists
    if [ ! -f "$file" ]; then
        print_error "Experiment file not found: $file"
        return 1
    fi
    
    # Apply the experiment
    print_info "Applying chaos experiment..."
    if kubectl apply -f "$file"; then
        print_success "Chaos experiment applied successfully"
    else
        print_error "Failed to apply chaos experiment"
        return 1
    fi
    
    # Wait a moment for the experiment to start
    sleep 3
    
    # Get target namespace from the experiment
    local target_namespace=$(kubectl get chaosengine "$experiment_key" -n litmus -o jsonpath='{.spec.appinfo.appns}' 2>/dev/null || echo "unknown")
    
    # Start monitoring
    start_monitoring "$experiment_key" "$target_namespace"
    
    # Wait for user to stop monitoring
    print_info "Monitoring started. Press Enter to stop monitoring and return to menu..."
    read -r
    
    stop_monitoring
    
    # Show final results
    print_header "📊 Experiment Results Summary"
    echo ""
    
    if kubectl get chaosresult "$experiment_key" -n litmus &> /dev/null; then
        local result_status=$(kubectl get chaosresult "$experiment_key" -n litmus -o jsonpath='{.status.experimentStatus.phase}' 2>/dev/null || echo "Unknown")
        local verdict=$(kubectl get chaosresult "$experiment_key" -n litmus -o jsonpath='{.status.experimentStatus.verdict}' 2>/dev/null || echo "Unknown")
        
        print_info "Final Status: $result_status"
        print_info "Verdict: $verdict"
        
        if [ "$verdict" = "Pass" ]; then
            print_success "✅ Experiment completed successfully - System showed resilience!"
        else
            print_warning "⚠️  Experiment completed with issues - Review system resilience"
        fi
    else
        print_warning "Experiment results not yet available"
    fi
    
    echo ""
    print_info "Press Enter to continue..."
    read -r
}

# Function to cleanup experiments
cleanup_experiments() {
    print_header "🧹 Cleaning up experiments..."
    
    for exp in "${!EXPERIMENTS[@]}"; do
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

# Function to show main menu
show_main_menu() {
    while true; do
        show_banner
        show_experiment_menu
        
        print_demo "Select an option (1-$((${#EXPERIMENTS[@]} + 2))): "
        read -r choice
        
        case $choice in
            [1-${#EXPERIMENTS[@]}])
                local exp_index=$choice
                local experiment_key=$(get_experiment_by_index $exp_index)
                if [ -n "$experiment_key" ]; then
                    run_experiment "$experiment_key"
                else
                    print_error "Invalid experiment selection"
                    sleep 2
                fi
                ;;
            $((${#EXPERIMENTS[@]} + 1)))
                show_banner
                show_service_status
                print_info "Press Enter to return to menu..."
                read -r
                ;;
            $((${#EXPERIMENTS[@]} + 2)))
                print_info "Exiting chaos engineering demo..."
                cleanup_experiments
                exit 0
                ;;
            *)
                print_error "Invalid option. Please select a valid number."
                sleep 2
                ;;
        esac
    done
}

# Function to handle cleanup on exit
cleanup_on_exit() {
    print_info "Cleaning up on exit..."
    stop_monitoring
    cleanup_experiments
}

# Set up signal handlers
trap cleanup_on_exit EXIT INT TERM

# Main execution
main() {
    show_banner
    print_demo "Welcome to the Chaos Engineering Live Demo!"
    print_info "This demo will show you how to test microservice resilience through controlled chaos experiments."
    echo ""
    print_info "Press Enter to start the demo..."
    read -r
    
    check_prerequisites
    show_main_menu
}

# Run the main function
main "$@"
