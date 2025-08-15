# Microservice Testing Lab

A comprehensive testing infrastructure for validating microservice architectures through multiple testing layers and automated orchestration.

## 🏗️ Architecture Overview

```
microservice-testing-lab/
├── jarvis-core/                    # AI-powered test orchestration engine
├── agents/                         # Specialized testing agents
│   ├── api-testing/               # End-to-end API testing
│   ├── contract-testing/          # Service contract validation
│   ├── chaos-testing/             # Resilience and fault tolerance testing
│   ├── penetration-testing/       # Security testing and vulnerability assessment
│   └── reporting/                 # Test reporting and analytics
├── shared-utils/                   # Reusable testing utilities
│   ├── test-data-factory/         # Test data generation and management
│   ├── kafka-event-simulator/     # Event-driven testing utilities
│   ├── api-client-lib/            # Standardized API client library
│   └── contract-test-generator/   # Contract test generation from OpenAPI specs
├── ci-cd/                         # CI/CD pipeline configurations
│   └── github-actions/            # GitHub Actions workflows
├── frontend/                      # Web-based testing dashboard
└── docs/                          # Documentation
    ├── test-strategy.md           # Comprehensive testing strategy
    ├── architecture.md            # System architecture documentation
    └── scorecard-template.md      # Quality assessment framework
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Node.js 18+ (for frontend)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd microservice-testing-lab
   ```

2. **Start the testing infrastructure**
   ```bash
   docker-compose up -d
   ```

3. **Build and start Jarvis Core**
   ```bash
   cd jarvis-core
   mvn clean install
   mvn spring-boot:run
   ```

4. **Start the frontend dashboard**
   ```bash
   cd frontend
   npm install
   npm start
   ```

5. **Access the dashboard**
   - Frontend: http://localhost:3000
   - Jarvis Core API: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

## 🧪 Testing Components

### Jarvis Core
The central orchestration engine that coordinates all testing activities.

**Features:**
- AI-powered test generation
- Intelligent test orchestration
- Automated test analysis
- Plugin-based architecture

**Usage:**
```bash
# Start Jarvis Core
cd jarvis-core
mvn spring-boot:run

# Generate tests for a service
curl -X POST http://localhost:8080/api/tests/generate \
  -H "Content-Type: application/json" \
  -d '{"serviceName": "user-service", "testType": "api"}'
```

### Testing Agents

#### API Testing Agent
End-to-end API testing across microservices.

**Features:**
- Automated API workflow testing
- Response validation and schema checking
- Performance benchmarking
- Error scenario testing

**Usage:**
```bash
cd agents/api-testing
mvn test -Dtest=UserServiceApiTest
```

#### Contract Testing Agent
Validate service contracts and API compatibility.

**Features:**
- Consumer-driven contract testing
- Provider contract validation
- Contract versioning and evolution
- Breaking change detection

**Usage:**
```bash
cd agents/contract-testing
mvn test -Dtest=UserServiceContractTest
```

#### Chaos Testing Agent
Test system resilience and fault tolerance.

**Features:**
- Network partition simulation
- Service failure injection
- Resource exhaustion testing
- Recovery time measurement

**Usage:**
```bash
cd agents/chaos-testing
mvn test -Dtest=ResilienceTest
```

#### Penetration Testing Agent
Security testing and vulnerability assessment.

**Features:**
- Automated security scanning
- Authentication/authorization testing
- Input validation testing
- Security compliance checking

**Usage:**
```bash
cd agents/penetration-testing
mvn test -Dtest=SecurityTest
```

### Shared Utilities

#### Test Data Factory
Generate consistent test data across all test types.

**Features:**
- Factory pattern implementation
- Faker integration for realistic data
- Database seeding utilities
- Test data versioning

**Usage:**
```java
// Generate test user data
User testUser = TestDataFactory.createUser();
Order testOrder = TestDataFactory.createOrder(testUser);
```

#### Kafka Event Simulator
Simulate event-driven scenarios for testing.

**Features:**
- Event generation and publishing
- Event consumption simulation
- Event schema validation
- Event flow testing

**Usage:**
```java
// Simulate user registration event
KafkaEventSimulator.publishEvent("user-registered", userEvent);
```

#### API Client Library
Reusable API client for testing.

**Features:**
- Standardized API interactions
- Authentication handling
- Request/response logging
- Error handling utilities

**Usage:**
```java
// Create API client
ApiClient client = ApiClient.builder()
    .baseUrl("http://localhost:8080")
    .authToken("test-token")
    .build();

// Make API call
ApiResponse response = client.get("/api/users/1");
```

#### Contract Test Generator
Generate contract tests from OpenAPI specifications.

**Features:**
- OpenAPI/Swagger parsing
- Multiple test framework support
- Customizable test templates
- CI/CD integration

**Usage:**
```bash
cd shared-utils/contract-test-generator
mvn exec:java -Dexec.args="--input swagger.json --output tests/"
```

## 🔄 CI/CD Integration

### GitHub Actions
Automated testing pipeline with multiple stages.

**Pipeline Stages:**
1. **Build**: Compile and package services
2. **Unit Tests**: Execute unit test suites
3. **Integration Tests**: Run integration tests
4. **Contract Tests**: Validate service contracts
5. **API Tests**: Execute end-to-end API tests
6. **Chaos Tests**: Run resilience tests
7. **Security Scans**: Perform security testing
8. **Deploy**: Deploy to staging/production

**Usage:**
```yaml
# .github/workflows/test.yml
name: Microservice Testing
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run Tests
        run: |
          cd microservice-testing-lab
          mvn clean test
```

## 📊 Monitoring and Reporting

### Test Metrics
- **Coverage Reports**: Code coverage by service
- **Test Results**: Pass/fail rates and trends
- **Performance Metrics**: Response times and throughput
- **Quality Gates**: Automated quality checks

### Dashboard Features
- Real-time test execution monitoring
- Historical test data analysis
- Performance trend visualization
- Quality scorecard tracking

## 🛡️ Security Testing

### Automated Security Scans
- **OWASP ZAP**: Security vulnerability scanning
- **Dependency Scanning**: Automated vulnerability detection
- **Code Scanning**: Static analysis for security issues
- **Container Scanning**: Docker image security analysis

### Security Test Types
- Authentication testing
- Authorization testing
- Input validation testing
- API security testing
- Compliance checking

## 📈 Performance Testing

### Load Testing
- **JMeter**: Load testing scenarios
- **Gatling**: Performance testing
- **Artillery**: API performance testing

### Chaos Engineering
- Network partition simulation
- Service failure injection
- Resource exhaustion testing
- Recovery time measurement

## 📚 Documentation

### Test Strategy
Comprehensive testing strategy covering all testing layers from unit to end-to-end testing.

**Key Topics:**
- Testing pyramid implementation
- Test data management
- CI/CD integration
- Monitoring and reporting
- Security testing
- Performance testing

### Architecture Documentation
Detailed system architecture and design decisions.

**Key Topics:**
- System architecture overview
- Component interactions
- Technology stack
- Security architecture
- Scalability considerations
- Deployment strategy

### Quality Scorecard
Framework for evaluating testing quality and effectiveness.

**Key Topics:**
- Scoring methodology
- Quality metrics
- Action items by score range
- Review schedules
- Continuous improvement

## 🔧 Configuration

### Environment Variables
```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=testing_lab
DB_USERNAME=test_user
DB_PASSWORD=test_password

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
KAFKA_TOPIC_PREFIX=test

# API Configuration
API_BASE_URL=http://localhost:8080
API_TIMEOUT=30000

# Security Configuration
JWT_SECRET=your-jwt-secret
OAUTH_CLIENT_ID=your-client-id
OAUTH_CLIENT_SECRET=your-client-secret
```

### Application Properties
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS}
    topic-prefix: ${KAFKA_TOPIC_PREFIX}

api:
  base-url: ${API_BASE_URL}
  timeout: ${API_TIMEOUT}

security:
  jwt:
    secret: ${JWT_SECRET}
  oauth:
    client-id: ${OAUTH_CLIENT_ID}
    client-secret: ${OAUTH_CLIENT_SECRET}
```

## 🤝 Contributing

### Development Setup
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

### Code Standards
- Follow Java coding conventions
- Maintain test coverage above 80%
- Write comprehensive documentation
- Use meaningful commit messages

### Testing Requirements
- All new features must include tests
- Maintain or improve test coverage
- Ensure tests are reliable and fast
- Follow testing best practices

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

### Getting Help
- **Documentation**: Check the `docs/` directory
- **Issues**: Create an issue on GitHub
- **Discussions**: Use GitHub Discussions
- **Wiki**: Check the project wiki

### Common Issues
- **Test Failures**: Check test logs and environment setup
- **Performance Issues**: Review resource allocation and configuration
- **Security Alerts**: Update dependencies and review security scans
- **Integration Problems**: Verify service connectivity and configuration

## 🔮 Roadmap

### Phase 1: Foundation (Weeks 1-4)
- [x] Set up testing infrastructure
- [x] Implement unit test framework
- [x] Create test data factory
- [x] Establish CI/CD pipeline

### Phase 2: Integration (Weeks 5-8)
- [x] Implement integration tests
- [x] Set up contract testing
- [x] Create API test suite
- [x] Implement basic reporting

### Phase 3: Advanced Testing (Weeks 9-12)
- [ ] Implement chaos testing
- [ ] Set up security testing
- [ ] Create performance test suite
- [ ] Implement advanced reporting

### Phase 4: Optimization (Weeks 13-16)
- [ ] Optimize test execution
- [ ] Implement parallel testing
- [ ] Create quality gates
- [ ] Establish monitoring and alerting

### Future Enhancements
- **Machine Learning Integration**: AI-powered test optimization
- **Advanced Analytics**: Predictive testing insights
- **Mobile Testing**: Mobile application testing support
- **Cloud Integration**: Multi-cloud testing capabilities
