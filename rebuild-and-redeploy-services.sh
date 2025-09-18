#!/bin/bash

# Rebuild and Redeploy All Services Script
# This script rebuilds and redeploys all microservices with the new actuator dependency

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
SERVICES=(
    "product-service:microservices-product-service"
    "order-service:microservices-order-service"
    "notification-service:microservices-notification-service"
)

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

# Function to build a service
build_service() {
    local service_name=$1
    local service_path="microservices/$service_name"
    
    print_info "Building $service_name..."
    
    if [ ! -d "$service_path" ]; then
        print_error "Service directory $service_path not found"
        return 1
    fi
    
    cd "$service_path"
    
    # Clean and build
    ./mvnw clean package -DskipTests
    
    if [ $? -eq 0 ]; then
        print_status "$service_name built successfully"
    else
        print_error "Failed to build $service_name"
        cd - > /dev/null
        return 1
    fi
    
    cd - > /dev/null
}

# Function to deploy a service
deploy_service() {
    local service_name=$1
    local namespace=$2
    
    print_info "Deploying $service_name to namespace $namespace..."
    
    # Apply the Helm chart
    helm upgrade --install $service_name ./helm-charts/$service_name \
        --namespace $namespace \
        --create-namespace \
        --set database.sidecar.enabled=true \
        --set database.standalone.enabled=false
    
    if [ $? -eq 0 ]; then
        print_status "$service_name deployed successfully"
    else
        print_error "Failed to deploy $service_name"
        return 1
    fi
}

# Function to wait for deployment
wait_for_deployment() {
    local service_name=$1
    local namespace=$2
    
    print_info "Waiting for $service_name deployment to be ready..."
    
    kubectl wait --for=condition=available --timeout=300s deployment/$service_name -n $namespace
    
    if [ $? -eq 0 ]; then
        print_status "$service_name deployment is ready"
    else
        print_warning "$service_name deployment may not be ready yet"
    fi
}

# Function to check service health
check_service_health() {
    local service_name=$1
    local port=$2
    
    print_info "Checking health of $service_name on port $port..."
    
    # Wait a moment for the service to be ready
    sleep 5
    
    # Test health endpoint
    if curl -s -u user:$(grep "$service_name" local-service-credentials.properties | cut -d'=' -f2) \
        http://localhost:$port/actuator/health > /dev/null 2>&1; then
        print_status "$service_name health check passed"
    else
        print_warning "$service_name health check failed - may need more time"
    fi
}

# Main execution
main() {
    echo -e "${BLUE}🚀 Rebuilding and Redeploying All Services${NC}"
    echo ""
    
    # Check if we're in the right directory
    if [ ! -d "microservices" ]; then
        print_error "Please run this script from the project root directory"
        exit 1
    fi
    
    # Build all services
    print_step "Building all services..."
    echo ""
    
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name namespace <<< "$service_info"
        build_service "$service_name"
        echo ""
    done
    
    # Deploy all services
    print_step "Deploying all services..."
    echo ""
    
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name namespace <<< "$service_info"
        deploy_service "$service_name" "$namespace"
        echo ""
    done
    
    # Wait for deployments
    print_step "Waiting for deployments to be ready..."
    echo ""
    
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name namespace <<< "$service_info"
        wait_for_deployment "$service_name" "$namespace"
        echo ""
    done
    
    # Check service health
    print_step "Checking service health..."
    echo ""
    
    # Check if port forwards are running
    if pgrep -f "kubectl port-forward" > /dev/null; then
        print_info "Port forwards are active, checking health endpoints..."
        
        check_service_health "product-service" "8082"
        check_service_health "order-service" "8083"
        check_service_health "notification-service" "8084"
    else
        print_warning "Port forwards are not active. Please run ./port-forward-all-services.sh first"
    fi
    
    echo ""
    print_status "Rebuild and redeploy completed!"
    echo ""
    echo -e "${BLUE}📋 Next steps:${NC}"
    echo "1. Run ./port-forward-all-services.sh to set up port forwarding"
    echo "2. Run ./test-all-services.sh to test all health endpoints"
    echo "3. Check logs with: kubectl logs -l app=<service-name> -c <service-name> -n <namespace> -f"
    echo ""
}

# Run main function
main "$@"
