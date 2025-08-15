#!/bin/bash

# Microservice Status Check Script
# This script checks the status of all microservices, infrastructure, and testing components

# Don't exit on errors, we want to check all services
# set -e

echo "🔍 Microservice Architecture Status Check"
echo "========================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Function to print colored output
print_header() {
    echo -e "${CYAN}$1${NC}"
}

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

# Function to check if a port is in use
check_port() {
    local port=$1
    local service_name=$2
    local description=$3
    
    if lsof -i :$port > /dev/null 2>&1; then
        local process_info=$(lsof -i :$port | grep LISTEN | head -1)
        local pid=$(echo "$process_info" | awk '{print $2}')
        local process_name=$(echo "$process_info" | awk '{print $1}')
        
        if [ -n "$pid" ]; then
            print_success "$service_name is running on port $port (PID: $pid, Process: $process_name)"
            return 0
        else
            print_warning "$service_name port $port is in use but no process found"
            return 1
        fi
    else
        print_error "$service_name is not running on port $port"
        return 1
    fi
}

# Function to check Docker container status
check_docker_container() {
    local container_name=$1
    local description=$2
    
    if docker ps --format "table {{.Names}}\t{{.Status}}" | grep -q "$container_name"; then
        local status=$(docker ps --format "table {{.Names}}\t{{.Status}}" | grep "$container_name" | awk '{print $2}')
        print_success "$description is running (Status: $status)"
        return 0
    elif docker ps -a --format "table {{.Names}}\t{{.Status}}" | grep -q "$container_name"; then
        local status=$(docker ps -a --format "table {{.Names}}\t{{.Status}}" | grep "$container_name" | awk '{print $2}')
        print_warning "$description is stopped (Status: $status)"
        return 1
    else
        print_error "$description is not found"
        return 1
    fi
}

# Function to check service health endpoint
check_health_endpoint() {
    local url=$1
    local service_name=$2
    
    if curl -s --max-time 5 "$url" > /dev/null 2>&1; then
        print_success "$service_name health check passed"
        return 0
    else
        print_error "$service_name health check failed"
        return 1
    fi
}

# Initialize counters
running_services=0
total_services=0

print_header "1. INFRASTRUCTURE SERVICES"
echo "--------------------------------"

# Check Docker containers
print_status "Checking Docker containers..."

# Check PostgreSQL
check_docker_container "postgres" "PostgreSQL Database"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
fi
total_services=$((total_services + 1))

# Check Kafka
check_docker_container "kafka" "Apache Kafka"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
fi
total_services=$((total_services + 1))

# Check Zookeeper
check_docker_container "zookeeper" "Apache Zookeeper"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
fi
total_services=$((total_services + 1))

echo ""

print_header "2. MICROSERVICES"
echo "-------------------"

# Check microservices by port
print_status "Checking microservice ports..."

# Gateway Service
check_port 8080 "Gateway Service" "API Gateway"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
    check_health_endpoint "http://localhost:8080/actuator/health" "Gateway Service"
fi
total_services=$((total_services + 1))

# User Service
check_port 8081 "User Service" "User Management"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
    check_health_endpoint "http://localhost:8081/actuator/health" "User Service"
fi
total_services=$((total_services + 1))

# Product Service
check_port 8082 "Product Service" "Product Catalog"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
    check_health_endpoint "http://localhost:8082/actuator/health" "Product Service"
fi
total_services=$((total_services + 1))

# Order Service
check_port 8083 "Order Service" "Order Processing"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
    check_health_endpoint "http://localhost:8083/actuator/health" "Order Service"
fi
total_services=$((total_services + 1))

# Notification Service
check_port 8084 "Notification Service" "Notification Management"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
    check_health_endpoint "http://localhost:8084/actuator/health" "Notification Service"
fi
total_services=$((total_services + 1))

echo ""

print_header "3. TESTING LAB COMPONENTS"
echo "-----------------------------"

# Check Jarvis Core
check_port 8085 "Jarvis Core" "AI Test Orchestration"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
    check_health_endpoint "http://localhost:8085/actuator/health" "Jarvis Core"
fi
total_services=$((total_services + 1))

# Check Frontend Dashboard
check_port 3000 "Frontend Dashboard" "Web Interface"
if [ $? -eq 0 ]; then
    running_services=$((running_services + 1))
fi
total_services=$((total_services + 1))

echo ""

print_header "4. SYSTEM RESOURCES"
echo "----------------------"

# Check system resources
print_status "Checking system resources..."

# Check available memory (macOS specific)
if command -v vm_stat > /dev/null 2>&1; then
    memory_available=$(vm_stat | grep "Pages free:" | awk '{print $3}' | sed 's/\.//')
    memory_available_mb=$((memory_available * 4096 / 1024 / 1024))
    if [ $memory_available_mb -gt 1000 ]; then
        print_success "Available memory: ${memory_available_mb}MB"
    else
        print_warning "Available memory: ${memory_available_mb}MB (low)"
    fi
else
    # Fallback for other systems
    memory_available_mb=$(free -m 2>/dev/null | grep Mem | awk '{print $7}' || echo "0")
    if [ "$memory_available_mb" -gt 1000 ] 2>/dev/null; then
        print_success "Available memory: ${memory_available_mb}MB"
    else
        print_warning "Available memory: ${memory_available_mb}MB (low)"
    fi
fi

# Check disk space
disk_usage=$(df -h . | tail -1 | awk '{print $5}' | sed 's/%//')
if [ $disk_usage -lt 80 ]; then
    print_success "Disk usage: ${disk_usage}%"
else
    print_warning "Disk usage: ${disk_usage}% (high)"
fi

# Check Docker status
if docker info > /dev/null 2>&1; then
    print_success "Docker is running"
else
    print_error "Docker is not running"
fi

echo ""

print_header "5. SUMMARY"
echo "-------------"

# Calculate percentage
if [ $total_services -gt 0 ]; then
    percentage=$((running_services * 100 / total_services))
else
    percentage=0
fi

echo "📊 Service Status Summary:"
echo "   Running: $running_services/$total_services services"
echo "   Status: $percentage% operational"

if [ $percentage -eq 100 ]; then
    print_success "🎉 All services are running!"
elif [ $percentage -ge 80 ]; then
    print_success "✅ Most services are running"
elif [ $percentage -ge 50 ]; then
    print_warning "⚠️  Some services are running"
else
    print_error "❌ Most services are stopped"
fi

echo ""
echo "🔗 Service URLs (when running):"
echo "   API Gateway:     http://localhost:8080"
echo "   User Service:    http://localhost:8081"
echo "   Product Service: http://localhost:8082"
echo "   Order Service:   http://localhost:8083"
echo "   Notification:    http://localhost:8084"
echo "   Jarvis Core:     http://localhost:8085"
echo "   Frontend:        http://localhost:3000"

echo ""
echo "🛠️  Quick Actions:"
echo "   Start all services:     ./start-all-services.sh"
echo "   Stop all services:      ./stop-all-services.sh"
echo "   Check status again:     ./check-service-status.sh"

echo ""
echo "📋 Health Check Endpoints:"
echo "   Gateway:     http://localhost:8080/actuator/health"
echo "   User:        http://localhost:8081/actuator/health"
echo "   Product:     http://localhost:8082/actuator/health"
echo "   Order:       http://localhost:8083/actuator/health"
echo "   Notification: http://localhost:8084/actuator/health"
echo "   Jarvis:      http://localhost:8085/actuator/health"

echo ""
echo "📚 API Documentation:"
echo "   Gateway:     http://localhost:8080/swagger-ui.html"
echo "   User:        http://localhost:8081/swagger-ui.html"
echo "   Product:     http://localhost:8082/swagger-ui.html"
echo "   Order:       http://localhost:8083/swagger-ui.html"
echo "   Notification: http://localhost:8084/swagger-ui.html"
echo "   Jarvis:      http://localhost:8085/swagger-ui.html"

# Exit with appropriate code
if [ $percentage -eq 100 ]; then
    exit 0
elif [ $percentage -ge 50 ]; then
    exit 1
else
    exit 2
fi
