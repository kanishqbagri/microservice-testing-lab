#!/bin/bash

# 📊 Real-time Chaos Experiment Monitor
# Monitors chaos experiments with live updates and impact analysis

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
BOLD='\033[1m'
NC='\033[0m'

# Configuration
MONITORING_INTERVAL=2
MAX_MONITORING_TIME=300  # 5 minutes max

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

print_metric() {
    echo -e "${WHITE}📊 $1${NC}"
}

# Function to get experiment status
get_experiment_status() {
    local experiment_name=$1
    local namespace=${2:-litmus}
    
    if ! kubectl get chaosengine "$experiment_name" -n "$namespace" &> /dev/null; then
        echo "NOT_FOUND"
        return 1
    fi
    
    local engine_status=$(kubectl get chaosengine "$experiment_name" -n "$namespace" -o jsonpath='{.status.engineStatus}' 2>/dev/null || echo "Unknown")
    local experiment_status=$(kubectl get chaosengine "$experiment_name" -n "$namespace" -o jsonpath='{.status.experiments[0].status}' 2>/dev/null || echo "Unknown")
    
    echo "$engine_status:$experiment_status"
}

# Function to get experiment results
get_experiment_results() {
    local experiment_name=$1
    local namespace=${2:-litmus}
    
    if ! kubectl get chaosresult "$experiment_name" -n "$namespace" &> /dev/null; then
        echo "NOT_FOUND"
        return 1
    fi
    
    local result_status=$(kubectl get chaosresult "$experiment_name" -n "$namespace" -o jsonpath='{.status.experimentStatus.phase}' 2>/dev/null || echo "Unknown")
    local verdict=$(kubectl get chaosresult "$experiment_name" -n "$namespace" -o jsonpath='{.status.experimentStatus.verdict}' 2>/dev/null || echo "Unknown")
    local fail_step=$(kubectl get chaosresult "$experiment_name" -n "$namespace" -o jsonpath='{.status.experimentStatus.failStep}' 2>/dev/null || echo "")
    
    echo "$result_status:$verdict:$fail_step"
}

# Function to get target service metrics
get_service_metrics() {
    local target_namespace=$1
    
    if ! kubectl get namespace "$target_namespace" &> /dev/null; then
        echo "NAMESPACE_NOT_FOUND"
        return 1
    fi
    
    local pod_count=$(kubectl get pods -n "$target_namespace" --no-headers 2>/dev/null | wc -l)
    local running_pods=$(kubectl get pods -n "$target_namespace" --no-headers 2>/dev/null | grep "Running" | wc -l)
    local ready_pods=$(kubectl get pods -n "$target_namespace" --no-headers 2>/dev/null | grep -E "Running.*1/1|Running.*2/2|Running.*3/3" | wc -l)
    
    echo "$pod_count:$running_pods:$ready_pods"
}

# Function to get pod restart counts
get_pod_restarts() {
    local target_namespace=$1
    
    if ! kubectl get namespace "$target_namespace" &> /dev/null; then
        echo "0"
        return 1
    fi
    
    local total_restarts=$(kubectl get pods -n "$target_namespace" --no-headers 2>/dev/null | awk '{sum += $4} END {print sum+0}')
    echo "$total_restarts"
}

# Function to display experiment dashboard
show_experiment_dashboard() {
    local experiment_name=$1
    local target_namespace=$2
    local start_time=$3
    
    clear
    echo -e "${CYAN}${BOLD}"
    echo "╔══════════════════════════════════════════════════════════════════════════════╗"
    echo "║                    🎭 CHAOS EXPERIMENT LIVE MONITOR                         ║"
    echo "╚══════════════════════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
    
    # Calculate elapsed time
    local current_time=$(date +%s)
    local elapsed=$((current_time - start_time))
    local minutes=$((elapsed / 60))
    local seconds=$((elapsed % 60))
    
    print_header "📊 Experiment Dashboard: $experiment_name"
    echo -e "${WHITE}Elapsed Time: ${minutes}m ${seconds}s${NC}"
    echo ""
    
    # Experiment Status
    print_info "Experiment Status:"
    local status=$(get_experiment_status "$experiment_name")
    if [ "$status" != "NOT_FOUND" ]; then
        IFS=':' read -r engine_status experiment_status <<< "$status"
        
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
    
    # Target Service Metrics
    print_info "Target Service Metrics ($target_namespace):"
    local metrics=$(get_service_metrics "$target_namespace")
    if [ "$metrics" != "NAMESPACE_NOT_FOUND" ]; then
        IFS=':' read -r total_pods running_pods ready_pods <<< "$metrics"
        local restart_count=$(get_pod_restarts "$target_namespace")
        
        print_metric "Total Pods: $total_pods"
        print_metric "Running Pods: $running_pods"
        print_metric "Ready Pods: $ready_pods"
        print_metric "Total Restarts: $restart_count"
        
        # Pod details
        echo ""
        print_info "Pod Details:"
        local pods=$(kubectl get pods -n "$target_namespace" --no-headers 2>/dev/null || echo "")
        if [ -n "$pods" ]; then
            echo "$pods" | while read -r line; do
                local pod_name=$(echo "$line" | awk '{print $1}')
                local status=$(echo "$line" | awk '{print $3}')
                local ready=$(echo "$line" | awk '{print $2}')
                local restarts=$(echo "$line" | awk '{print $4}')
                local age=$(echo "$line" | awk '{print $5}')
                
                if [ "$status" = "Running" ] && [[ "$ready" == *"/"* ]]; then
                    print_success "  $pod_name: $status ($ready) - Restarts: $restarts - Age: $age"
                else
                    print_warning "  $pod_name: $status ($ready) - Restarts: $restarts - Age: $age"
                fi
            done
        else
            print_warning "  No pods found"
        fi
    else
        print_error "Target namespace not found"
    fi
    
    echo ""
    
    # Experiment Results
    print_info "Experiment Results:"
    local results=$(get_experiment_results "$experiment_name")
    if [ "$results" != "NOT_FOUND" ]; then
        IFS=':' read -r result_status verdict fail_step <<< "$results"
        
        if [ "$result_status" = "Completed" ]; then
            if [ "$verdict" = "Pass" ]; then
                print_success "Result: $result_status - Verdict: $verdict"
            else
                print_warning "Result: $result_status - Verdict: $verdict"
                if [ -n "$fail_step" ]; then
                    print_warning "Failed at step: $fail_step"
                fi
            fi
        else
            print_info "Result: $result_status - Verdict: $verdict"
        fi
    else
        print_info "  Results not yet available"
    fi
    
    echo ""
    print_info "Press Ctrl+C to stop monitoring"
    echo ""
}

# Function to monitor experiment
monitor_experiment() {
    local experiment_name=$1
    local target_namespace=$2
    local start_time=$(date +%s)
    local monitoring_time=0
    
    print_chaos "Starting real-time monitoring for experiment: $experiment_name"
    print_info "Target namespace: $target_namespace"
    print_info "Monitoring interval: ${MONITORING_INTERVAL}s"
    echo ""
    
    # Wait for user to start monitoring
    print_info "Press Enter to start monitoring..."
    read -r
    
    while [ $monitoring_time -lt $MAX_MONITORING_TIME ]; do
        show_experiment_dashboard "$experiment_name" "$target_namespace" "$start_time"
        
        # Check if experiment is completed
        local status=$(get_experiment_status "$experiment_name")
        if [ "$status" != "NOT_FOUND" ]; then
            IFS=':' read -r engine_status experiment_status <<< "$status"
            if [ "$experiment_status" = "Completed" ]; then
                print_success "Experiment completed! Final results:"
                echo ""
                show_experiment_dashboard "$experiment_name" "$target_namespace" "$start_time"
                break
            fi
        fi
        
        sleep $MONITORING_INTERVAL
        monitoring_time=$((monitoring_time + MONITORING_INTERVAL))
    done
    
    if [ $monitoring_time -ge $MAX_MONITORING_TIME ]; then
        print_warning "Maximum monitoring time reached. Stopping monitoring."
    fi
}

# Function to show experiment summary
show_experiment_summary() {
    local experiment_name=$1
    local target_namespace=$2
    
    print_header "📋 Experiment Summary: $experiment_name"
    echo ""
    
    # Final status
    local status=$(get_experiment_status "$experiment_name")
    if [ "$status" != "NOT_FOUND" ]; then
        IFS=':' read -r engine_status experiment_status <<< "$status"
        print_info "Final Engine Status: $engine_status"
        print_info "Final Experiment Status: $experiment_status"
    fi
    
    # Final results
    local results=$(get_experiment_results "$experiment_name")
    if [ "$results" != "NOT_FOUND" ]; then
        IFS=':' read -r result_status verdict fail_step <<< "$results"
        print_info "Final Result Status: $result_status"
        print_info "Final Verdict: $verdict"
        
        if [ -n "$fail_step" ]; then
            print_warning "Failed at step: $fail_step"
        fi
    fi
    
    # Final service state
    local metrics=$(get_service_metrics "$target_namespace")
    if [ "$metrics" != "NAMESPACE_NOT_FOUND" ]; then
        IFS=':' read -r total_pods running_pods ready_pods <<< "$metrics"
        local restart_count=$(get_pod_restarts "$target_namespace")
        
        echo ""
        print_info "Final Service State:"
        print_metric "Total Pods: $total_pods"
        print_metric "Running Pods: $running_pods"
        print_metric "Ready Pods: $ready_pods"
        print_metric "Total Restarts: $restart_count"
    fi
    
    echo ""
}

# Main function
main() {
    if [ $# -lt 2 ]; then
        echo "Usage: $0 <experiment_name> <target_namespace> [monitoring_interval]"
        echo ""
        echo "Example: $0 pod-failure microservices-product-service 2"
        exit 1
    fi
    
    local experiment_name=$1
    local target_namespace=$2
    local interval=${3:-$MONITORING_INTERVAL}
    
    MONITORING_INTERVAL=$interval
    
    monitor_experiment "$experiment_name" "$target_namespace"
    show_experiment_summary "$experiment_name" "$target_namespace"
}

# Run main function
main "$@"
