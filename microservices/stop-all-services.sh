#!/bin/bash

# Microservice Stop Script
# This script stops all microservices gracefully

set -e

echo "🛑 Stopping Microservice Architecture..."

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

# Function to stop a service
stop_service() {
    local service_name=$1
    local port=$2
    
    print_status "Stopping $service_name on port $port..."
    
    # Find and kill the process
    local pid=$(lsof -ti:$port 2>/dev/null || echo "")
    
    if [ -n "$pid" ]; then
        print_status "Found process $pid for $service_name"
        
        # Send SIGTERM first
        kill -TERM "$pid" 2>/dev/null || true
        
        # Wait for graceful shutdown
        local wait_count=0
        local max_wait=30
        
        while [ $wait_count -lt $max_wait ]; do
            if ! lsof -ti:$port > /dev/null 2>&1; then
                print_success "$service_name stopped gracefully"
                return 0
            fi
            
            print_status "Waiting for $service_name to stop... ($((wait_count + 1))/$max_wait)"
            sleep 1
            wait_count=$((wait_count + 1))
        done
        
        # Force kill if still running
        print_warning "Force killing $service_name"
        kill -KILL "$pid" 2>/dev/null || true
        
        # Final check
        if ! lsof -ti:$port > /dev/null 2>&1; then
            print_success "$service_name stopped"
        else
            print_error "Failed to stop $service_name"
            return 1
        fi
    else
        print_warning "$service_name is not running on port $port"
    fi
}

# Stop services in reverse order
print_status "Stopping microservices..."

# 1. Stop Gateway Service (port 8080)
stop_service "Gateway Service" 8080

# 2. Stop Notification Service (port 8084)
stop_service "Notification Service" 8084

# 3. Stop Order Service (port 8083)
stop_service "Order Service" 8083

# 4. Stop Product Service (port 8082)
stop_service "Product Service" 8082

# 5. Stop User Service (port 8081)
stop_service "User Service" 8081

# Stop infrastructure services
print_status "Stopping infrastructure services..."

# Check if docker-compose.yml exists in parent directory
if [ -f "../docker-compose.yml" ]; then
    cd ..
    docker-compose down
    cd microservices
    print_success "Infrastructure services stopped"
else
    print_warning "docker-compose.yml not found. Please stop infrastructure services manually"
fi

# Clean up any remaining processes
print_status "Cleaning up remaining processes..."

# Kill any remaining Java processes related to our services
pids=$(pgrep -f "spring-boot:run" 2>/dev/null || echo "")
if [ -n "$pids" ]; then
    print_status "Found remaining Spring Boot processes: $pids"
    echo "$pids" | xargs kill -TERM 2>/dev/null || true
    sleep 2
    echo "$pids" | xargs kill -KILL 2>/dev/null || true
fi

# Kill any remaining Maven processes
pids=$(pgrep -f "mvn" 2>/dev/null || echo "")
if [ -n "$pids" ]; then
    print_status "Found remaining Maven processes: $pids"
    echo "$pids" | xargs kill -TERM 2>/dev/null || true
    sleep 2
    echo "$pids" | xargs kill -KILL 2>/dev/null || true
fi

# Final verification
print_status "Verifying all services are stopped..."

services=(
    "Gateway Service:8080"
    "User Service:8081"
    "Product Service:8082"
    "Order Service:8083"
    "Notification Service:8084"
)

all_services_stopped=true

for service in "${services[@]}"; do
    IFS=':' read -r service_name port <<< "$service"
    
    if lsof -ti:$port > /dev/null 2>&1; then
        print_error "$service_name is still running on port $port"
        all_services_stopped=false
    else
        print_success "$service_name is stopped"
    fi
done

if [ "$all_services_stopped" = true ]; then
    print_success "🎉 All microservices have been stopped successfully!"
else
    print_warning "⚠️  Some services may still be running. Please check manually."
fi

echo ""
print_status "To start all services again, run: ./start-all-services.sh"
