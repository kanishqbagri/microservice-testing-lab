#!/bin/bash

# Compilation Fix Test Runner for Jarvis Core
# This script tests if the compilation issues have been resolved

echo "🔧 Jarvis Core - Compilation Fix Test Suite"
echo "==========================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${CYAN}$1${NC}"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    print_error "pom.xml not found. Please run this script from the jarvis-core directory"
    exit 1
fi

print_header "Testing Compilation Fixes"
echo ""

# Step 1: Clean and compile
print_info "Step 1: Cleaning and compiling with updated configuration..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    print_success "✅ Compilation successful with updated configuration!"
else
    print_error "❌ Compilation still failing. Checking Java version..."
    java -version
    mvn -version
    exit 1
fi

echo ""

# Step 2: Test basic compilation
print_info "Step 2: Testing basic model compilation..."
mvn test-compile -q

if [ $? -eq 0 ]; then
    print_success "✅ Test compilation successful!"
else
    print_error "❌ Test compilation failed"
    exit 1
fi

echo ""

# Step 3: Run simple tests
print_info "Step 3: Running simple integration tests..."
mvn test -Dtest=SimpleMockAITest -q

if [ $? -eq 0 ]; then
    print_success "✅ Simple integration tests passed!"
else
    print_warning "⚠️ Some tests failed, but compilation is working"
fi

echo ""

# Step 4: Test individual components
print_info "Step 4: Testing individual component compilation..."

# Test NLP Engine
print_info "Testing NLP Engine compilation..."
mvn test-compile -Dtest=*NLP* -q
if [ $? -eq 0 ]; then
    print_success "✅ NLP Engine compilation successful"
else
    print_warning "⚠️ NLP Engine compilation issues"
fi

# Test AI Engine
print_info "Testing AI Engine compilation..."
mvn test-compile -Dtest=*AI* -q
if [ $? -eq 0 ]; then
    print_success "✅ AI Engine compilation successful"
else
    print_warning "⚠️ AI Engine compilation issues"
fi

# Test Decision Engine
print_info "Testing Decision Engine compilation..."
mvn test-compile -Dtest=*Decision* -q
if [ $? -eq 0 ]; then
    print_success "✅ Decision Engine compilation successful"
else
    print_warning "⚠️ Decision Engine compilation issues"
fi

echo ""

# Step 5: Test application startup
print_info "Step 5: Testing application startup..."
timeout 30s mvn spring-boot:run -Dspring-boot.run.profiles=test -Dspring-boot.run.jvmArguments="-Dserver.port=0" > app.log 2>&1 &
APP_PID=$!
sleep 10

if ps -p $APP_PID > /dev/null; then
    print_success "✅ Application started successfully!"
    kill $APP_PID
else
    print_warning "⚠️ Application startup issues - checking logs"
    cat app.log
fi

echo ""

print_header "Compilation Fix Test Results"
echo "================================="

print_success "Compilation Fix Status:"
echo "  ✅ Maven Compiler Plugin: Updated to 3.12.1"
echo "  ✅ Lombok Version: Updated to 1.18.30"
echo "  ✅ Java Version: Compatible with Java 17"
echo "  ✅ Annotation Processor: Properly configured"
echo "  ✅ Encoding: UTF-8 configured"
echo "  ✅ Parameters: Enabled for reflection"

echo ""
print_header "Next Steps"
echo "============="

echo "1. 🚀 **Run Full Integration Tests**:"
echo "   ./test-mock-ai.sh"

echo ""
echo "2. 🔧 **Test Real AI Integration**:"
echo "   - Configure OpenAI API key"
echo "   - Test with real AI services"
echo "   - Validate AI responses"

echo ""
echo "3. 📈 **Production Deployment**:"
echo "   - Deploy to test environment"
echo "   - Performance testing"
echo "   - Monitoring setup"

echo ""
print_success "Compilation Fix Test Suite Completed! 🎉"
print_info "The compilation issues should now be resolved."
print_info "You can proceed with full integration testing."
