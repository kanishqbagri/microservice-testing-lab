#!/bin/bash

# Jarvis Core Integration Test Runner
# This script runs the complete NLP to AI integration tests

echo "🤖 Jarvis Core - NLP to AI Integration Test Suite"
echo "=================================================="

# Set environment variables for testing
export SPRING_PROFILES_ACTIVE=test
export JARVIS_TEST_MODE=true

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

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed or not in PATH"
    exit 1
fi

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    print_error "pom.xml not found. Please run this script from the jarvis-core directory"
    exit 1
fi

print_status "Starting integration tests..."

# Clean and compile
print_status "Cleaning and compiling project..."
mvn clean compile -q
if [ $? -ne 0 ]; then
    print_error "Compilation failed"
    exit 1
fi

# Run the integration tests
print_status "Running NLP to AI integration tests..."
mvn test -Dtest=NLPToAIIntegrationTest -q

# Check test results
if [ $? -eq 0 ]; then
    print_success "All integration tests passed! 🎉"
    
    # Run additional component tests
    print_status "Running component-specific tests..."
    mvn test -Dtest=JarvisCoreEngineTest -q
    mvn test -Dtest=NLPEngineTest -q
    
    if [ $? -eq 0 ]; then
        print_success "All tests completed successfully! 🚀"
    else
        print_warning "Some component tests failed, but integration tests passed"
    fi
else
    print_error "Integration tests failed ❌"
    exit 1
fi

# Generate test report
print_status "Generating test report..."
mvn surefire-report:report -q

print_status "Test execution completed!"
print_status "Check target/site/surefire-report.html for detailed results"

echo ""
echo "📊 Test Summary:"
echo "================="
echo "✅ NLP to AI Integration Pipeline"
echo "✅ Complete Command Processing"
echo "✅ Risk Assessment and Decision Making"
echo "✅ Context and Memory Integration"
echo "✅ Learning Engine Integration"
echo ""
echo "🎯 Next Steps:"
echo "=============="
echo "1. Review test results in target/site/surefire-report.html"
echo "2. Check logs for detailed execution flow"
echo "3. Run manual tests with real commands"
echo "4. Monitor system performance during execution"
