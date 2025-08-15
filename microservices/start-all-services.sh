#!/bin/bash

# Microservice Startup Script
# This script starts all microservices in the correct order

set -e

echo "🚀 Starting Microservice Architecture..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if a service is running
check_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=1
    
    print_status "Checking if $service_name is running on port $port..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
            print_success "$service_name is running on port $port"
            return 0
        fi
        
        print_status "Attempt $attempt/$max_attempts: $service_name not ready yet..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    print_error "$service_name failed to start on port $port"
    return 1
}

# Function to start a service
start_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    
    print_status "Starting $service_name..."
    
    if [ ! -d "$service_dir" ]; then
        print_error "Service directory $service_dir not found"
        return 1
    fi
    
    cd "$service_dir"
    
    # Check if service is already running
    if curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
        print_warning "$service_name is already running on port $port"
        return 0
    fi
    
    # Start the service in background
    print_status "Building and starting $service_name..."
    mvn clean install -DskipTests > /dev/null 2>&1 &
    mvn spring-boot:run > /dev/null 2>&1 &
    
    # Wait for service to start
    sleep 5
    
    # Check if service started successfully
    if check_service "$service_name" "$port"; then
        print_success "$service_name started successfully"
    else
        print_error "Failed to start $service_name"
        return 1
    fi
    
    cd - > /dev/null
}

# Check prerequisites
print_status "Checking prerequisites..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    print_error "Java is not installed. Please install Java 17+"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven 3.8+"
    exit 1
fi

# Check if Docker is running
if ! docker info &> /dev/null; then
    print_error "Docker is not running. Please start Docker"
    exit 1
fi

print_success "Prerequisites check passed"

# Start infrastructure services
print_status "Starting infrastructure services..."

# Check if docker-compose.yml exists in parent directory
if [ -f "../docker-compose.yml" ]; then
    cd ..
    docker-compose up -d
    cd microservices
    print_success "Infrastructure services started"
else
    print_warning "docker-compose.yml not found. Please start infrastructure services manually"
fi

# Wait for infrastructure to be ready
print_status "Waiting for infrastructure services to be ready..."
sleep 10

# Start services in order
print_status "Starting microservices..."

# 1. Start User Service (port 8081)
start_service "User Service" "user-service" 8081

# 2. Start Product Service (port 8082)
start_service "Product Service" "product-service" 8082

# 3. Start Order Service (port 8083)
start_service "Order Service" "order-service" 8083

# 4. Start Notification Service (port 8084)
start_service "Notification Service" "notification-service" 8084

# 5. Start Gateway Service (port 8080)
start_service "Gateway Service" "gateway-service" 8080

# Final status check
print_status "Performing final status check..."

services=(
    "Gateway Service:8080"
    "User Service:8081"
    "Product Service:8082"
    "Order Service:8083"
    "Notification Service:8084"
)

all_services_running=true

for service in "${services[@]}"; do
    IFS=':' read -r service_name port <<< "$service"
    if ! check_service "$service_name" "$port"; then
        all_services_running=false
    fi
done

if [ "$all_services_running" = true ]; then
    print_success "🎉 All microservices are running successfully!"
    echo ""
    echo "📋 Service URLs:"
    echo "  Gateway Service:     http://localhost:8080"
    echo "  User Service:        http://localhost:8081"
    echo "  Product Service:     http://localhost:8082"
    echo "  Order Service:       http://localhost:8083"
    echo "  Notification Service: http://localhost:8084"
    echo ""
    echo "🔍 Health Checks:"
    echo "  Gateway:             http://localhost:8080/actuator/health"
    echo "  User Service:        http://localhost:8081/actuator/health"
    echo "  Product Service:     http://localhost:8082/actuator/health"
    echo "  Order Service:       http://localhost:8083/actuator/health"
    echo "  Notification Service: http://localhost:8084/actuator/health"
    echo ""
    echo "📚 API Documentation:"
    echo "  Gateway:             http://localhost:8080/swagger-ui.html"
    echo "  User Service:        http://localhost:8081/swagger-ui.html"
    echo "  Product Service:     http://localhost:8082/swagger-ui.html"
    echo "  Order Service:       http://localhost:8083/swagger-ui.html"
    echo "  Notification Service: http://localhost:8084/swagger-ui.html"
else
    print_error "❌ Some services failed to start. Please check the logs above."
    exit 1
fi

echo ""
print_status "To stop all services, run: ./stop-all-services.sh"
