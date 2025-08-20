#!/bin/bash

echo "🚀 Starting Real Microservices for End-to-End Testing"
echo "=================================================="
echo ""

# Set colors for output
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

# Check if we're in the right directory
if [ ! -d "../microservices" ]; then
    print_error "Microservices directory not found. Please run this script from the jarvis-core directory."
    exit 1
fi

print_status "Navigating to microservices directory..."
cd ../microservices

print_status "Starting all microservices..."
./start-all-services.sh

print_status "Waiting for services to start..."
sleep 30

print_status "Checking service status..."
./check-service-status.sh

print_success "Microservices startup initiated!"
echo ""
echo "🎯 Next Steps:"
echo "1. Wait for all services to be fully started (check with ./check-service-status.sh)"
echo "2. Run the end-to-end integration demo: ./demo-end-to-end-integration.sh"
echo "3. Test with real microservice endpoints"
echo ""
echo "📊 Service URLs (when running):"
echo "   API Gateway:     http://localhost:8080"
echo "   User Service:    http://localhost:8081"
echo "   Product Service: http://localhost:8082"
echo "   Order Service:   http://localhost:8083"
echo "   Notification:    http://localhost:8084"
echo "   Jarvis Core:     http://localhost:8085"
echo ""
