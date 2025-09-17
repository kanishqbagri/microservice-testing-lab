#!/bin/bash

# Deploy All Microservices with PostgreSQL Sidecar

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
SERVICES=("user-service" "product-service" "order-service" "notification-service")
NAMESPACE_PREFIX="microservices"

echo -e "${BLUE}🚀 Deploying All Microservices with PostgreSQL Sidecar${NC}"
echo ""

# Function to print status
print_status() {
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

print_step() {
    echo -e "${PURPLE}📦 $1${NC}"
}

# Check prerequisites
check_prerequisites() {
    print_step "Checking prerequisites..."
    
    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl is not installed or not in PATH"
        exit 1
    fi
    
    if ! command -v helm &> /dev/null; then
        print_error "helm is not installed or not in PATH"
        exit 1
    fi
    
    if ! kubectl cluster-info &> /dev/null; then
        print_error "Cannot connect to Kubernetes cluster"
        exit 1
    fi
    
    print_status "Prerequisites check passed"
}

# Deploy a single service
deploy_service() {
    local service_name=$1
    local namespace="${NAMESPACE_PREFIX}-${service_name}"
    
    print_step "Deploying $service_name..."
    
    # Create namespace if it doesn't exist
    if ! kubectl get namespace $namespace &> /dev/null; then
        kubectl create namespace $namespace
        print_info "Created namespace: $namespace"
    else
        print_info "Namespace already exists: $namespace"
    fi
    
    # Deploy the Helm chart
    helm upgrade --install $service_name "./$service_name" \
        --namespace $namespace \
        --wait \
        --timeout=5m
    
    print_status "$service_name deployed successfully"
}

# Wait for all services to be ready
wait_for_services() {
    print_step "Waiting for all services to be ready..."
    
    for service in "${SERVICES[@]}"; do
        local namespace="${NAMESPACE_PREFIX}-${service}"
        print_info "Waiting for $service pods to be ready..."
        
        kubectl wait --for=condition=ready pod -l app=$service -n $namespace --timeout=300s
        print_status "$service pods are ready"
    done
}

# Show deployment status
show_status() {
    print_step "Deployment Status Summary"
    echo ""
    
    for service in "${SERVICES[@]}"; do
        local namespace="${NAMESPACE_PREFIX}-${service}"
        echo -e "${BLUE}📊 $service (namespace: $namespace):${NC}"
        
        # Show pods
        kubectl get pods -n $namespace -l app=$service --no-headers | while read line; do
            echo "  Pod: $line"
        done
        
        # Show services
        kubectl get svc -n $namespace --no-headers | while read line; do
            echo "  Service: $line"
        done
        
        # Show PVCs
        kubectl get pvc -n $namespace --no-headers | while read line; do
            echo "  PVC: $line"
        done
        
        echo ""
    done
}

# Show access instructions
show_access_instructions() {
    print_step "Access Instructions"
    echo ""
    
    for service in "${SERVICES[@]}"; do
        local namespace="${NAMESPACE_PREFIX}-${service}"
        local port=""
        
        case $service in
            "user-service") port="8081" ;;
            "product-service") port="8082" ;;
            "order-service") port="8083" ;;
            "notification-service") port="8084" ;;
        esac
        
        echo -e "${BLUE}🔗 $service:${NC}"
        echo "  kubectl port-forward svc/$service $port:$port -n $namespace"
        echo "  curl http://localhost:$port/actuator/health"
        echo ""
    done
}

# Show monitoring commands
show_monitoring_commands() {
    print_step "Monitoring Commands"
    echo ""
    
    for service in "${SERVICES[@]}"; do
        local namespace="${NAMESPACE_PREFIX}-${service}"
        
        echo -e "${BLUE}📋 $service logs:${NC}"
        echo "  kubectl logs -l app=$service -c $service -n $namespace -f"
        echo "  kubectl logs -l app=$service -c postgres-sidecar -n $namespace -f"
        echo ""
    done
}

# Main execution
main() {
    check_prerequisites
    
    echo -e "${BLUE}🎯 Deploying ${#SERVICES[@]} microservices with PostgreSQL sidecar...${NC}"
    echo ""
    
    # Deploy all services
    for service in "${SERVICES[@]}"; do
        deploy_service $service
        echo ""
    done
    
    # Wait for all services to be ready
    wait_for_services
    echo ""
    
    # Show status
    show_status
    
    # Show access instructions
    show_access_instructions
    
    # Show monitoring commands
    show_monitoring_commands
    
    print_status "All microservices deployed successfully!"
    echo ""
    echo -e "${GREEN}🎉 Deployment Complete!${NC}"
    echo -e "${BLUE}📖 Each service has:${NC}"
    echo "  • PostgreSQL sidecar container"
    echo "  • Persistent volume for database"
    echo "  • Health checks"
    echo "  • Resource limits"
    echo "  • Security contexts"
    echo ""
    echo -e "${YELLOW}💡 Next steps:${NC}"
    echo "  1. Port forward to access services"
    echo "  2. Test health endpoints"
    echo "  3. Monitor logs for any issues"
    echo "  4. Deploy gateway service to route traffic"
}

# Run main function
main "$@"
