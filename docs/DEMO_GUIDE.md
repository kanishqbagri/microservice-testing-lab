# Jarvis AI Demo Guide

## Overview

This guide provides comprehensive instructions for running the Jarvis AI demo scripts that showcase the full capabilities of our AI-powered testing framework.

## Demo Scripts Available

### 1. `demo-jarvis-simple.sh` - Simple Demo (Recommended)
- **Purpose**: Showcases AI capabilities assuming Jarvis Core is already running
- **Duration**: ~5-8 minutes
- **Requirements**: Jarvis Core running on port 8085

### 2. `demo-jarvis-showcase.sh` - Full Demo
- **Purpose**: Complete end-to-end demonstration including starting Jarvis Core
- **Duration**: ~10-15 minutes
- **Requirements**: Maven, Java 17, microservices environment

### 3. `jarvis-cli.sh` - Interactive CLI
- **Purpose**: Interactive natural language interface for Jarvis AI
- **Duration**: Continuous
- **Requirements**: Jarvis Core running

## Prerequisites

### Required Software
- Java 17 or higher
- Maven 3.6+
- curl
- jq (for JSON parsing)
- bash shell

### Required Services
- Jarvis Core running on `http://localhost:8085`
- Optional: Microservices running for full integration

## Quick Start

### Step 1: Start Jarvis Core

```bash
# Navigate to jarvis-core directory
cd ../jarvis-core

# Start Jarvis Core
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8085" -Dmaven.test.skip=true

# Wait for startup (check logs for "Started JarvisTestFrameworkApplication")
```

### Step 2: Run the Demo

```bash
# Navigate to chaos-engineering directory
cd ../chaos-engineering

# Run the simple demo
./demo-jarvis-simple.sh
```

## Demo Scenarios Covered

### 1. AI-Powered System Health Assessment
- **Command**: `assess current system health and identify weak points`
- **Demonstrates**: 
  - Natural language processing
  - Intelligent system analysis
  - AI-powered health checks
  - Risk assessment

### 2. Performance Testing with AI Analysis
- **Command**: `run performance analysis on user-service`
- **Demonstrates**:
  - Service-specific testing
  - Performance metrics collection
  - AI-driven test orchestration
  - Real-time analysis

### 3. Contract Testing Demonstration
- **Command**: `run contract tests for order-service`
- **Demonstrates**:
  - API contract validation
  - Schema validation
  - Response format verification
  - Automated contract testing

### 4. Contract Test Failure Scenario
- **Simulation**: Data model changes causing contract failures
- **Demonstrates**:
  - Breaking change detection
  - Failure analysis
  - Impact assessment
  - Root cause identification

### 5. AI-Powered Failure Analysis
- **Command**: `analyze the contract test failure and provide recommendations`
- **Demonstrates**:
  - Intelligent failure analysis
  - Pattern recognition
  - AI-generated recommendations
  - Actionable insights

### 6. Chaos Engineering with AI Orchestration
- **Command**: `run chaos test on product-service with network latency`
- **Demonstrates**:
  - Chaos engineering integration
  - Network failure simulation
  - Resilience testing
  - AI-driven chaos orchestration

### 7. AI-Powered Analysis and Recommendations
- **Features**:
  - Pattern recognition
  - Risk assessment
  - Trend analysis
  - Root cause analysis
  - Actionable recommendations

### 8. Comprehensive System Assessment
- **Command**: `provide a comprehensive summary of all test results and system health`
- **Demonstrates**:
  - Holistic system analysis
  - Cross-service correlation
  - AI-powered insights
  - Strategic recommendations

## Expected Output

The demo will show:

1. **Colored Output**: Different colors for different types of information
2. **English Responses**: AI responses in natural language, not JSON
3. **Test Simulations**: Realistic test execution and results
4. **Failure Scenarios**: Contract test failures with detailed analysis
5. **AI Insights**: Intelligent analysis and recommendations

## Sample Output

```
================================================
    🤖 Jarvis AI Capabilities Demo
================================================

[DEMO] Welcome to the Jarvis AI Demo!
[DEMO] This showcase demonstrates the AI-powered testing framework capabilities.

[SUCCESS] Jarvis Core is running and ready!

[DEMO] === System Health Assessment ===

[JARVIS AI] Sending command: assess current system health and identify weak points
⏳ Processing...

🤖 Jarvis AI Response:
----------------------------------------
💬 I understand you want to perform system health check.

Based on my analysis:
• System health issues detected - may impact test reliability

I'm Performing system health check.
This should take approximately Less than 1 minute.

📊 Status: PROCESSING

🎯 Action: HEALTH_CHECK
   Description: Performing system health check
   ⏱️  Estimated Time: Less than 1 minute
   🎯 Priority: MEDIUM
   🎯 Confidence: 87%
----------------------------------------
```

## Troubleshooting

### Jarvis Core Not Starting
```bash
# Check Java version
java -version

# Check Maven
mvn -version

# Check port availability
lsof -i :8085

# Start with debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8085 -Ddebug=true"
```

### Demo Script Issues
```bash
# Check script permissions
chmod +x demo-jarvis-simple.sh

# Check dependencies
which jq
which curl

# Test Jarvis Core manually
curl -X POST http://localhost:8085/api/jarvis/command \
  -H "Content-Type: application/json" \
  -d '{"command": "check health"}'
```

### Common Issues
1. **Port 8085 in use**: Change port in script or kill existing process
2. **Java version mismatch**: Ensure Java 17+ is being used
3. **Maven dependencies**: Run `mvn clean install` in jarvis-core
4. **Database issues**: Check PostgreSQL connection in application.yml

## Advanced Usage

### Custom Demo Scenarios
You can modify the demo scripts to test different scenarios:

```bash
# Test specific service
curl -X POST http://localhost:8085/api/jarvis/command \
  -H "Content-Type: application/json" \
  -d '{"command": "run performance test on notification-service"}'

# Test chaos scenarios
curl -X POST http://localhost:8085/api/jarvis/command \
  -H "Content-Type: application/json" \
  -d '{"command": "inject network latency into gateway-service"}'
```

### Interactive Testing
Use the interactive CLI for custom commands:

```bash
./jarvis-cli.sh
```

Then type commands like:
- "check health of user-service"
- "run chaos test on order-service"
- "analyze performance patterns"

## Next Steps

After running the demo:

1. **Explore the Interactive CLI**: Try `./jarvis-cli.sh`
2. **Review Test Results**: Check the database for stored results
3. **Explore Chaos Engineering**: Set up Litmus Chaos with `./scripts/setup/install-litmus.sh`
4. **Integrate with CI/CD**: Add Jarvis AI to your pipeline
5. **Customize for Your Services**: Adapt the framework for your microservices

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Review the logs in `jarvis-core/jarvis.log`
3. Verify all prerequisites are met
4. Test individual components manually

## Architecture Overview

The demo showcases these key components:

- **NLP Engine**: Natural language command processing
- **AI Engine**: Intelligent analysis and decision making
- **Decision Engine**: Action planning and execution
- **Context Manager**: System state management
- **Memory Manager**: Historical data storage
- **Learning Engine**: Pattern recognition and improvement

This creates a comprehensive AI-powered testing framework that can understand natural language commands, execute tests intelligently, and provide actionable insights.
