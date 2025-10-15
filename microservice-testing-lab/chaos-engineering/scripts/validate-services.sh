#!/bin/bash

# 🔍 Service Validation Script for Chaos Engineering Demo
# Validates that all required services are running before chaos experiments

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Service configurations
declare -A SERVICES=(
    ["gateway"]="8080:Gateway Service:microservices-gateway-service"
    ["user"]="8081:User Service:microservices-user-service"
    ["product"]="8082:Product Service:microservices-product-service"
    ["order"]="8083:Order Service:microservices-order-service"
    ["notification"]="8084:Notification Service:microservices-notification-service"
)

# Function to print colored output
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

print_header() {
    echo -e "${CYAN}${BOLD}$1${NC}"
}

# Function to check if a service is healthy
check_service_health() {
    local service_key=$1
    IFS=':' read -r port name namespace <<< "${SERVICES[$service_key]}"
    
    print_info "Checking $name..."
    
    # Check if namespace exists
    if ! kubectl get namespace "$namespace" &> /dev/null; then
        print_error "$name namespace '$namespace' not found"
        return 1
    fi
    
    # Check if pods exist and are running
    local pods=$(kubectl get pods -n "$namespace" --no-headers 2>/dev/null || echo "")
    if [ -z "$pods" ]; then
        print_error "$name has no pods in namespace '$namespace'"
        return 1
    fi
    
    local running_pods=0
    local total_pods=0
    
    echo "$pods" | while read -r line; do
        total_pods=$((total_pods + 1))
        local pod_name=$(echo "$line" | awk '{print $1}')
        local status=$(echo "$line" | awk '{print $3}')
        local ready=$(echo "$line" | awk '{print $2}')
        
        if [ "$status" = "Running" ] && [[ "$ready" == *"/"* ]]; then
            running_pods=$((running_pods + 1))
            print_success "  Pod: $pod_name - $status ($ready)"
        else
            print_warning "  Pod: $pod_name - $status ($ready)"
        fi
    done
    
    # Check service endpoints if available
    if kubectl get service -n "$namespace" &> /dev/null; then
        local services=$(kubectl get service -n "$namespace" --no-headers 2>/dev/null || echo "")
        if [ -n "$services" ]; then
            echo "$services" | while read -r line; do
                local svc_name=$(echo "$line" | awk '{print $1}')
                local svc_type=$(echo "$line" | awk '{print $2}')
                local cluster_ip=$(echo "$line" | awk '{print $3}')
                
                if [ "$cluster_ip" != "<none>" ]; then
                    print_success "  Service: $svc_name - $svc_type ($cluster_ip)"
                else
                    print_warning "  Service: $svc_name - $svc_type (no cluster IP)"
                fi
            done
        fi
    fi
    
    return 0
}

# Function to check chaos engineering prerequisites
check_chaos_prerequisites() {
    print_header "🔍 Checking Chaos Engineering Prerequisites"
    echo ""
    
    local all_good=true
    
    # Check kubectl
    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl is not installed or not in PATH"
        all_good=false
    else
        print_success "kubectl is available"
    fi
    
    # Check cluster connectivity
    if ! kubectl cluster-info &> /dev/null; then
        print_error "Cannot connect to Kubernetes cluster"
        all_good=false
    else
        print_success "Connected to Kubernetes cluster"
        local cluster_info=$(kubectl cluster-info | head -1)
        print_info "Cluster: $cluster_info"
    fi
    
    # Check litmus namespace
    if ! kubectl get namespace litmus &> /dev/null; then
        print_warning "litmus namespace not found - chaos experiments may not work"
    else
        print_success "litmus namespace exists"
    fi
    
    # Check chaos experiments
    if ! kubectl get chaosexperiments -n litmus &> /dev/null; then
        print_warning "Chaos experiments not installed in litmus namespace"
    else
        local exp_count=$(kubectl get chaosexperiments -n litmus --no-headers | wc -l)
        print_success "Found $exp_count chaos experiments available"
    fi
    
    # Check chaos service account
    if ! kubectl get serviceaccount k8s-chaos-admin -n litmus &> /dev/null; then
        print_warning "k8s-chaos-admin service account not found in litmus namespace"
    else
        print_success "k8s-chaos-admin service account exists"
    fi
    
    echo ""
    return $([ "$all_good" = true ] && echo 0 || echo 1)
}

# Function to validate all services
validate_all_services() {
    print_header "📊 Validating All Microservices"
    echo ""
    
    local all_healthy=true
    
    for service in "${!SERVICES[@]}"; do
        if ! check_service_health "$service"; then
            all_healthy=false
        fi
        echo ""
    done
    
    return $([ "$all_healthy" = true ] && echo 0 || echo 1)
}

# Function to show detailed service status
show_detailed_status() {
    print_header "📋 Detailed Service Status Report"
    echo ""
    
    for service in "${!SERVICES[@]}"; do
        IFS=':' read -r port name namespace <<< "${SERVICES[$service]}"
        echo -e "${BLUE}🔹 $name${NC}"
        echo -e "   Port: $port"
        echo -e "   Namespace: $namespace"
        
        if kubectl get namespace "$namespace" &> /dev/null; then
            # Pod details
            local pods=$(kubectl get pods -n "$namespace" --no-headers 2>/dev/null || echo "")
            if [ -n "$pods" ]; then
                echo "   Pods:"
                echo "$pods" | while read -r line; do
                    local pod_name=$(echo "$line" | awk '{print $1}')
                    local status=$(echo "$line" | awk '{print $3}')
                    local ready=$(echo "$line" | awk '{print $2}')
                    local restarts=$(echo "$line" | awk '{print $4}')
                    local age=$(echo "$line" | awk '{print $5}')
                    
                    echo -e "     - $pod_name: $status ($ready) - Restarts: $restarts - Age: $age"
                done
            else
                echo "   Pods: None found"
            fi
            
            # Service details
            local services=$(kubectl get service -n "$namespace" --no-headers 2>/dev/null || echo "")
            if [ -n "$services" ]; then
                echo "   Services:"
                echo "$services" | while read -r line; do
                    local svc_name=$(echo "$line" | awk '{print $1}')
                    local svc_type=$(echo "$line" | awk '{print $2}')
                    local cluster_ip=$(echo "$line" | awk '{print $3}')
                    local external_ip=$(echo "$line" | awk '{print $4}')
                    
                    echo -e "     - $svc_name: $svc_type ($cluster_ip)"
                    if [ "$external_ip" != "<none>" ]; then
                        echo -e "       External IP: $external_ip"
                    fi
                done
            else
                echo "   Services: None found"
            fi
        else
            echo "   Status: Namespace not found"
        fi
        echo ""
    done
}

# Function to generate health report
generate_health_report() {
    local report_file="/tmp/chaos-demo-health-report-$(date +%Y%m%d-%H%M%S).txt"
    
    print_header "📄 Generating Health Report"
    echo ""
    
    {
        echo "Chaos Engineering Demo - Health Report"
        echo "Generated: $(date)"
        echo "======================================"
        echo ""
        
        echo "Prerequisites Check:"
        if check_chaos_prerequisites; then
            echo "✅ All prerequisites met"
        else
            echo "❌ Some prerequisites missing"
        fi
        echo ""
        
        echo "Service Validation:"
        if validate_all_services; then
            echo "✅ All services healthy"
        else
            echo "❌ Some services have issues"
        fi
        echo ""
        
        echo "Detailed Status:"
        show_detailed_status
        
    } > "$report_file"
    
    print_success "Health report generated: $report_file"
    print_info "Report contents:"
    cat "$report_file"
}

# Main function
main() {
    case "${1:-validate}" in
        "prerequisites")
            check_chaos_prerequisites
            ;;
        "services")
            validate_all_services
            ;;
        "status")
            show_detailed_status
            ;;
        "report")
            generate_health_report
            ;;
        "validate"|*)
            check_chaos_prerequisites
            echo ""
            validate_all_services
            ;;
    esac
}

# Run main function
main "$@"
