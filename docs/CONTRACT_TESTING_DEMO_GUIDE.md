# 🤖 Jarvis Core Contract Testing Demo Guide

## 🎯 Demo Overview

This demo showcases Jarvis Core's intelligent contract testing capabilities through three key scenarios:

1. **Contract Test Execution** - Run contract tests for multiple services
2. **Failure Analysis** - Analyze when contract tests last failed
3. **Coverage Analysis** - Identify which service has the most test coverage

## 🚀 Quick Start

### Prerequisites
- Jarvis Core running on port 8085
- `jq` installed for JSON formatting (optional but recommended)

### Step 1: Start Jarvis Core
```bash
cd /Users/kanishkabagri/Workspace/microservice-testing-lab/microservice-testing-lab/jarvis-core
SPRING_PROFILES_ACTIVE=test mvn spring-boot:run -Dserver.port=8085
```

### Step 2: Run the Demo
```bash
cd /Users/kanishkabagri/Workspace/microservice-testing-lab
./demo-jarvis-contract-testing.sh
```

## 📋 Demo Scenarios

### Scenario 1: Contract Test Execution
**Goal**: Run contract tests for 2 services

**What it demonstrates**:
- Real-time contract test execution
- Service-specific test results
- Pass/fail analysis with execution times
- Detailed test information including error messages

**API Endpoints**:
- `GET /api/demo/contract-tests/{serviceName}` - Run contract tests for a specific service
- `GET /api/demo/services` - Get list of available services

### Scenario 2: Failure Analysis
**Goal**: Answer "When was the last time contract tests for an app failed?"

**What it demonstrates**:
- Historical failure analysis
- Time-based failure tracking
- Root cause identification
- Service-specific failure insights

**API Endpoints**:
- `GET /api/demo/contract-tests/failure-analysis` - Analyze contract test failures

**Sample Response**:
```json
{
  "lastFailureFound": true,
  "service": "user-service",
  "testName": "UserServiceContractTest.testUpdateUser",
  "failureTime": "2025-09-10T19:23:13.459741",
  "errorMessage": "Contract mismatch: expected field 'email' but received 'emailAddress'",
  "daysSinceFailure": 3
}
```

### Scenario 3: Coverage Analysis
**Goal**: Answer "Which application seems to have the most coverage?"

**What it demonstrates**:
- Test coverage analysis by service
- Test type breakdown (Unit, Integration, Contract, Security, Performance)
- Service comparison based on total test counts
- Data-driven coverage insights

**API Endpoints**:
- `GET /api/demo/coverage-analysis` - Analyze test coverage across services
- `GET /api/demo/test-summary` - Get overall test summary

**Sample Response**:
```json
{
  "serviceWithMostTests": "product-service",
  "totalServices": 5,
  "serviceCoverage": {
    "product-service": {
      "CONTRACT_TEST": 3,
      "UNIT_TEST": 2,
      "INTEGRATION_TEST": 1,
      "SECURITY_TEST": 1
    }
  }
}
```

## 🎭 Demo Features

### Interactive Experience
- **Color-coded output** for better readability
- **Step-by-step progression** with user prompts
- **Real-time API calls** with response formatting
- **Comprehensive analysis** with insights and recommendations

### Mock Data
The demo uses realistic mock data including:
- **5 microservices**: user-service, product-service, order-service, notification-service, gateway-service
- **Multiple test types**: Unit, Integration, Contract, Security, Performance
- **Historical data** with realistic timestamps and execution times
- **Failure scenarios** with detailed error messages

### API Endpoints Available
- `GET /api/demo/contract-tests/{serviceName}` - Run contract tests
- `GET /api/demo/contract-tests/failure-analysis` - Failure analysis
- `GET /api/demo/coverage-analysis` - Coverage analysis
- `GET /api/demo/services` - Available services
- `GET /api/demo/test-summary` - Test summary

## 🔧 Technical Details

### Architecture
- **Spring Boot REST API** with mock data
- **JSON responses** with comprehensive test information
- **Cross-origin support** for frontend integration
- **Error handling** with graceful fallbacks

### Data Model
Each test result includes:
- Service name and test name
- Test type (Unit, Integration, Contract, Security, Performance)
- Status (PASSED, FAILED)
- Execution time in milliseconds
- Timestamp
- Error message (for failures)

## 🎯 Demo Outcomes

After running the demo, you'll understand:

1. **Contract Testing Capabilities**
   - How to execute contract tests for multiple services
   - Real-time test result analysis
   - Service-specific test insights

2. **Failure Analysis Intelligence**
   - Historical failure tracking
   - Root cause identification
   - Time-based failure analysis

3. **Coverage Analysis Insights**
   - Service comparison based on test coverage
   - Test type distribution analysis
   - Data-driven coverage decisions

## 🚀 Next Steps

After the demo, you can:
- **Extend the mock data** with more realistic scenarios
- **Integrate with real test frameworks** (JUnit, TestNG, etc.)
- **Add database persistence** for historical data
- **Build a web dashboard** for visual analysis
- **Implement real-time monitoring** with WebSocket support

## 🎉 Conclusion

This demo showcases Jarvis Core's ability to provide intelligent insights into contract testing, failure analysis, and coverage optimization - key capabilities for modern microservice testing strategies.
