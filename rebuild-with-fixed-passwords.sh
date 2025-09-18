#!/bin/bash

# Rebuild Services with Fixed Passwords Script
# This script rebuilds all services with the new fixed password configuration

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
    "user-service:microservices-user-service"
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
    
    print_info "Building $service_name with fixed password configuration..."
    
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

# Function to test service with fixed password
test_service_fixed_password() {
    local service_name=$1
    local port=$2
    local password=$3
    
    print_info "Testing $service_name with fixed password on port $port..."
    
    # Wait a moment for the service to be ready
    sleep 5
    
    # Test health endpoint with fixed password
    if curl -s -u user:$password http://localhost:$port/actuator/health > /dev/null 2>&1; then
        print_status "$service_name health check passed with fixed password"
    else
        print_warning "$service_name health check failed - may need more time"
    fi
}

# Main execution
main() {
    echo -e "${BLUE}🚀 Rebuilding Services with Fixed Passwords${NC}"
    echo ""
    
    # Check if we're in the right directory
    if [ ! -d "microservices" ]; then
        print_error "Please run this script from the project root directory"
        exit 1
    fi
    
    # Build all services
    print_step "Building all services with fixed password configuration..."
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
    
    # Test services with fixed passwords
    print_step "Testing services with fixed passwords..."
    echo ""
    
    # Check if port forwards are running
    if pgrep -f "kubectl port-forward" > /dev/null; then
        print_info "Port forwards are active, testing with fixed passwords..."
        
        test_service_fixed_password "user-service" "8081" "user-service-password"
        test_service_fixed_password "product-service" "8082" "product-service-password"
        test_service_fixed_password "order-service" "8083" "order-service-password"
        test_service_fixed_password "notification-service" "8084" "notification-service-password"
    else
        print_warning "Port forwards are not active. Please run ./port-forward-all-services.sh first"
    fi
    
    echo ""
    print_status "Rebuild with fixed passwords completed!"
    echo ""
    echo -e "${BLUE}📋 Fixed Password Configuration:${NC}"
    echo ""
    echo -e "${GREEN}User Service:${NC} user:user-service-password"
    echo -e "${GREEN}Product Service:${NC} user:product-service-password"
    echo -e "${GREEN}Order Service:${NC} user:order-service-password"
    echo -e "${GREEN}Notification Service:${NC} user:notification-service-password"
    echo ""
    echo -e "${BLUE}📋 Next steps:${NC}"
    echo "1. Run ./test-all-services.sh to test all health endpoints"
    echo "2. Passwords will remain consistent across service restarts"
    echo "3. Check logs with: kubectl logs -l app=<service-name> -c <service-name> -n <namespace> -f"
    echo ""
}

# Run main function
main "$@"
