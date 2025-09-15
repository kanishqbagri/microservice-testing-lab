🚀 Complete Command Sequence
Step 1: Start Jarvis Core in Background
# Navigate to jarvis-core directory
cd /Users/kanishkabagri/Workspace/microservice-testing-lab/microservice-testing-lab/jarvis-core

# Start Jarvis Core in background
SPRING_PROFILES_ACTIVE=test mvn spring-boot:run -Dserver.port=8085 &

Step 2: Wait for Application to Start
# Wait for application to start (20-30 seconds)
sleep 25

# Verify Jarvis Core is running
curl -s http://localhost:8085/actuator/health

Step 3: Run the Integration Test Demo
# Navigate to project root
cd /Users/kanishkabagri/Workspace/microservice-testing-lab

# Run the demo script
./demo-jarvis-contract-testing.sh

🎯 One-Line Command Sequence
Here's the complete sequence as a single command block:
sh
🔧 Alternative: Quick Test Commands
If you want to test individual endpoints without the full demo:

# Test contract tests for user-service
curl -s http://localhost:8085/api/demo/contract-tests/user-service | jq .

# Test contract tests for product-service  
curl -s http://localhost:8085/api/demo/contract-tests/product-service | jq .

# Test failure analysis
curl -s http://localhost:8085/api/demo/contract-tests/failure-analysis | jq .

# Test coverage analysis
curl -s http://localhost:8085/api/demo/coverage-analysis | jq .

📋 Expected Output Sequence
Jarvis Core Startup: You'll see Spring Boot startup logs
Health Check: {"status":"UP","components":{"diskSpace":{"status":"UP"},"ping":{"status":"UP"}}}
Demo Execution: Interactive demo with color-coded output showing:
Contract test execution for 2 services
Failure analysis results
Coverage analysis results
🛠️ Troubleshooting Commands
If you need to check or restart:
8085
🎉 Ready to Execute!