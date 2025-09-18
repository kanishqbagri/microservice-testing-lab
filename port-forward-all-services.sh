#!/bin/bash

# Port Forward All Microservices Script
# This script sets up port forwarding for all deployed microservices with PostgreSQL sidecar

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
    "user-service:8081:microservices-user-service"
    "product-service:8082:microservices-product-service"
    "order-service:8083:microservices-order-service"
    "notification-service:8084:microservices-notification-service"
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

# Function to check if kubectl is available
check_prerequisites() {
    print_step "Checking prerequisites..."
    
    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl is not installed or not in PATH"
        exit 1
    fi
    
    if ! kubectl cluster-info &> /dev/null; then
        print_error "Cannot connect to Kubernetes cluster"
        exit 1
    fi
    
    print_status "Prerequisites check passed"
}

# Function to kill existing port forwards
kill_existing_port_forwards() {
    print_step "Stopping existing port forwards..."
    
    # Kill any existing kubectl port-forward processes
    pkill -f "kubectl port-forward" 2>/dev/null || true
    
    print_status "Existing port forwards stopped"
}

# Function to start port forwarding for a service
start_port_forward() {
    local service_name=$1
    local port=$2
    local namespace=$3
    
    print_info "Starting port forward for $service_name on port $port..."
    
    # Start port forward in background
    kubectl port-forward svc/$service_name $port:$port -n $namespace > /dev/null 2>&1 &
    local pid=$!
    
    # Wait a moment to check if it started successfully
    sleep 2
    
    if kill -0 $pid 2>/dev/null; then
        print_status "$service_name port forward started (PID: $pid)"
        echo $pid >> /tmp/port-forward-pids.txt
    else
        print_error "Failed to start port forward for $service_name"
        return 1
    fi
}

# Function to test service connectivity
test_service_connectivity() {
    local service_name=$1
    local port=$2
    
    print_info "Testing connectivity to $service_name on port $port..."
    
    # Wait a moment for the port forward to be ready
    sleep 3
    
    # Test if the service is responding
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:$port/actuator/health | grep -q "401\|404\|500"; then
        print_status "$service_name is responding on port $port"
    else
        print_warning "$service_name may not be ready yet on port $port"
    fi
}

# Function to show service information
show_service_info() {
    print_step "Service Access Information"
    echo ""
    
    echo -e "${BLUE}🌐 Services are now accessible at:${NC}"
    echo ""
    
    echo -e "${GREEN}User Service:${NC}"
    echo "  URL: http://localhost:8081"
    echo "  Health: http://localhost:8081/actuator/health"
    echo "  Credentials: user / 60c080e0-b20a-4d21-91f2-ffdf317f00cc"
    echo ""
    
    echo -e "${GREEN}Product Service:${NC}"
    echo "  URL: http://localhost:8082"
    echo "  Health: http://localhost:8082/actuator/health"
    echo "  Credentials: user / 0051ae03-e74d-457f-84d9-c9bcefa25d8a"
    echo ""
    
    echo -e "${GREEN}Order Service:${NC}"
    echo "  URL: http://localhost:8083"
    echo "  Health: http://localhost:8083/actuator/health"
    echo "  Credentials: user / 53b1e600-5cb3-4f3d-86d1-b10825468b92"
    echo ""
    
    echo -e "${GREEN}Notification Service:${NC}"
    echo "  URL: http://localhost:8084"
    echo "  Health: http://localhost:8084/actuator/health"
    echo "  Credentials: user / 17d9e18c-7990-4663-8c4b-4be96d893e70"
    echo ""
}

# Function to show management commands
show_management_commands() {
    print_step "Management Commands"
    echo ""
    
    echo -e "${BLUE}📋 To stop all port forwards:${NC}"
    echo "  pkill -f 'kubectl port-forward'"
    echo "  or"
    echo "  ./stop-port-forwards.sh"
    echo ""
    
    echo -e "${BLUE}📋 To check running port forwards:${NC}"
    echo "  ps aux | grep 'kubectl port-forward'"
    echo ""
    
    echo -e "${BLUE}📋 To test all services:${NC}"
    echo "  ./test-all-services.sh"
    echo ""
    
    echo -e "${BLUE}📋 To view logs:${NC}"
    echo "  kubectl logs -l app=user-service -c user-service -n microservices-user-service -f"
    echo "  kubectl logs -l app=product-service -c product-service -n microservices-product-service -f"
    echo "  kubectl logs -l app=order-service -c order-service -n microservices-order-service -f"
    echo "  kubectl logs -l app=notification-service -c notification-service -n microservices-notification-service -f"
    echo ""
}

# Function to create stop script
create_stop_script() {
    cat > stop-port-forwards.sh << 'EOF'
#!/bin/bash
# Stop All Port Forwards Script

echo "🛑 Stopping all kubectl port forwards..."

# Kill all kubectl port-forward processes
pkill -f "kubectl port-forward"

# Remove PID file if it exists
rm -f /tmp/port-forward-pids.txt

echo "✅ All port forwards stopped"
EOF
    
    chmod +x stop-port-forwards.sh
    print_status "Created stop-port-forwards.sh script"
}

# Function to create test script
create_test_script() {
    cat > test-all-services.sh << 'EOF'
#!/bin/bash
# Test All Services Script

echo "🧪 Testing all microservices..."

# Test User Service
echo "Testing User Service..."
curl -s -u user:60c080e0-b20a-4d21-91f2-ffdf317f00cc http://localhost:8081/actuator/health && echo " ✅" || echo " ❌"

# Test Product Service
echo "Testing Product Service..."
curl -s -u user:0051ae03-e74d-457f-84d9-c9bcefa25d8a http://localhost:8082/actuator/health && echo " ✅" || echo " ❌"

# Test Order Service
echo "Testing Order Service..."
curl -s -u user:53b1e600-5cb3-4f3d-86d1-b10825468b92 http://localhost:8083/actuator/health && echo " ✅" || echo " ❌"

# Test Notification Service
echo "Testing Notification Service..."
curl -s -u user:17d9e18c-7990-4663-8c4b-4be96d893e70 http://localhost:8084/actuator/health && echo " ✅" || echo " ❌"

echo "🎉 Service testing completed!"
EOF
    
    chmod +x test-all-services.sh
    print_status "Created test-all-services.sh script"
}

# Main execution
main() {
    echo -e "${BLUE}🚀 Starting Port Forward for All Microservices${NC}"
    echo ""
    
    # Check prerequisites
    check_prerequisites
    
    # Kill existing port forwards
    kill_existing_port_forwards
    
    # Initialize PID file
    > /tmp/port-forward-pids.txt
    
    # Start port forwards for all services
    print_step "Starting port forwards for all services..."
    echo ""
    
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name port namespace <<< "$service_info"
        start_port_forward "$service_name" "$port" "$namespace"
    done
    
    echo ""
    
    # Test connectivity
    print_step "Testing service connectivity..."
    echo ""
    
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_name port namespace <<< "$service_info"
        test_service_connectivity "$service_name" "$port"
    done
    
    echo ""
    
    # Create helper scripts
    create_stop_script
    create_test_script
    
    # Show service information
    show_service_info
    
    # Show management commands
    show_management_commands
    
    print_status "All port forwards are now active!"
    echo ""
    echo -e "${YELLOW}💡 Tip: Keep this terminal open to maintain port forwards${NC}"
    echo -e "${YELLOW}💡 Tip: Use Ctrl+C to stop this script and all port forwards${NC}"
    echo ""
    
    # Wait for user interrupt
    trap 'echo ""; print_warning "Stopping all port forwards..."; pkill -f "kubectl port-forward"; rm -f /tmp/port-forward-pids.txt; print_status "All port forwards stopped"; exit 0' INT
    
    echo -e "${GREEN}🎉 Port forwarding is active! Press Ctrl+C to stop.${NC}"
    
    # Keep script running
    while true; do
        sleep 60
    done
}

# Run main function
main "$@"

