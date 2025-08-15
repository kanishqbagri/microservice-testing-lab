# Microservice Architecture with Testing Lab

This repository contains a comprehensive microservice architecture with an advanced testing infrastructure designed to validate microservice systems through multiple testing layers and automated orchestration.

## 🏗️ Repository Structure

```
microservice-testing-lab/          # Monorepo for all test orchestration
├── jarvis-core/                   # AI-powered test orchestration engine
├── agents/                        # Specialized testing agents
│   ├── api-testing/              # End-to-end API testing
│   ├── contract-testing/         # Service contract validation
│   ├── chaos-testing/            # Resilience and fault tolerance testing
│   ├── penetration-testing/      # Security testing and vulnerability assessment
│   └── reporting/                # Test reporting and analytics
├── shared-utils/                  # Reusable testing utilities
│   ├── test-data-factory/        # Test data generation and management
│   ├── kafka-event-simulator/    # Event-driven testing utilities
│   ├── api-client-lib/           # Standardized API client library
│   └── contract-test-generator/  # Contract test generation from OpenAPI specs
├── ci-cd/                        # CI/CD pipeline configurations
│   └── github-actions/           # GitHub Actions workflows
├── frontend/                     # Web-based testing dashboard
└── docs/                         # Documentation
    ├── test-strategy.md          # Comprehensive testing strategy
    ├── architecture.md           # System architecture documentation
    └── scorecard-template.md     # Quality assessment framework

microservices/                    # Individual microservice repositories
├── user-service/                 # User management and authentication
├── product-service/              # Product catalog and management
├── order-service/                # Order processing and management
├── notification-service/         # Notification and messaging
└── gateway-service/              # API gateway and routing
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- Node.js 18+ (for frontend)
- PostgreSQL 13+
- Apache Kafka

### 1. Start Infrastructure Services

```bash
# Start all infrastructure services
docker-compose up -d

# Verify services are running
docker-compose ps
```

### 2. Start Microservices

```bash
# Start all microservices
cd microservices
./start-all-services.sh

# Or start individual services
cd user-service && mvn spring-boot:run
cd product-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
cd gateway-service && mvn spring-boot:run
```

### 3. Start Testing Lab

```bash
# Start Jarvis Core (AI-powered test orchestration)
cd microservice-testing-lab/jarvis-core
mvn spring-boot:run

# Start the frontend dashboard
cd microservice-testing-lab/frontend
npm install
npm start
```

### 4. Access Services

- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Product Service**: http://localhost:8082
- **Order Service**: http://localhost:8083
- **Notification Service**: http://localhost:8084
- **Testing Dashboard**: http://localhost:3000
- **Jarvis Core API**: http://localhost:8085

## 🧪 Testing Infrastructure

### Testing Lab Components

#### Jarvis Core
The central AI-powered orchestration engine that coordinates all testing activities.

**Features:**
- AI-powered test generation
- Intelligent test orchestration
- Automated test analysis
- Plugin-based architecture

#### Testing Agents
Specialized agents for different types of testing:

- **API Testing Agent**: End-to-end API testing across microservices
- **Contract Testing Agent**: Service contract validation
- **Chaos Testing Agent**: Resilience and fault tolerance testing
- **Penetration Testing Agent**: Security testing and vulnerability assessment
- **Reporting Agent**: Test reporting and analytics

#### Shared Utilities
Reusable components for testing:

- **Test Data Factory**: Generate consistent test data
- **Kafka Event Simulator**: Event-driven testing utilities
- **API Client Library**: Standardized API client
- **Contract Test Generator**: Generate tests from OpenAPI specs

### Running Tests

#### Unit and Integration Tests
```bash
# Run tests for a specific service
cd microservices/user-service
mvn clean test

# Run all service tests
cd microservices
mvn clean test
```

#### Contract Tests
```bash
# Run contract tests
cd microservice-testing-lab/agents/contract-testing
mvn clean test
```

#### API Tests
```bash
# Run API tests
cd microservice-testing-lab/agents/api-testing
mvn clean test
```

#### Chaos Tests
```bash
# Run chaos tests
cd microservice-testing-lab/agents/chaos-testing
mvn clean test
```

#### Security Tests
```bash
# Run security tests
cd microservice-testing-lab/agents/penetration-testing
mvn clean test
```

## 📊 Monitoring and Observability

### Health Checks
- **Service Health**: Each service exposes health endpoints
- **Database Health**: Connection status and performance
- **Infrastructure Health**: Docker containers and network status

### Metrics
- **Application Metrics**: Business and technical metrics
- **Infrastructure Metrics**: System and resource metrics
- **Test Metrics**: Test execution and quality metrics

### Logging
- **Structured Logging**: JSON-formatted logs
- **Centralized Logging**: ELK stack integration
- **Correlation IDs**: Request tracing across services

## 🔄 CI/CD Pipeline

### GitHub Actions
Automated testing pipeline with multiple stages:

1. **Build**: Compile and package services
2. **Unit Tests**: Execute unit test suites
3. **Integration Tests**: Run integration tests
4. **Contract Tests**: Validate service contracts
5. **API Tests**: Execute end-to-end API tests
6. **Chaos Tests**: Run resilience tests
7. **Security Scans**: Perform security testing
8. **Deploy**: Deploy to staging/production

### Pipeline Configuration
```yaml
# .github/workflows/microservice-testing.yml
name: Microservice Testing Pipeline
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run All Tests
        run: |
          # Run microservice tests
          cd microservices
          mvn clean test
          
          # Run testing lab tests
          cd ../microservice-testing-lab
          mvn clean test
```

## 🛡️ Security

### Security Testing
- **OWASP ZAP**: Security vulnerability scanning
- **Dependency Scanning**: Automated vulnerability detection
- **Code Scanning**: Static analysis for security issues
- **Container Scanning**: Docker image security analysis

### Security Features
- **JWT Authentication**: Stateless authentication
- **Role-Based Access Control**: User permissions
- **API Security**: Endpoint protection
- **Data Encryption**: Sensitive data protection

## 📈 Performance Testing

### Load Testing
- **JMeter**: Load testing scenarios
- **Gatling**: Performance testing
- **Artillery**: API performance testing

### Chaos Engineering
- **Network Partition Simulation**: Test network failures
- **Service Failure Injection**: Test service resilience
- **Resource Exhaustion Testing**: Test resource limits
- **Recovery Time Measurement**: Test recovery capabilities

## 📚 Documentation

### Architecture Documentation
- **System Architecture**: Overall system design
- **Service Architecture**: Individual service designs
- **Testing Strategy**: Comprehensive testing approach
- **Deployment Guide**: Deployment procedures

### API Documentation
- **OpenAPI Specifications**: Machine-readable API docs
- **Swagger UI**: Interactive API documentation
- **Postman Collections**: API testing collections

### Quality Assessment
- **Quality Scorecard**: Testing quality evaluation
- **Best Practices**: Development and testing guidelines
- **Troubleshooting**: Common issues and solutions

## 🔧 Configuration

### Environment Variables
```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=microservices
DB_USERNAME=postgres
DB_PASSWORD=password

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
KAFKA_TOPIC_PREFIX=microservice

# API Configuration
API_BASE_URL=http://localhost:8080
API_TIMEOUT=30000

# Security Configuration
JWT_SECRET=your-jwt-secret
OAUTH_CLIENT_ID=your-client-id
OAUTH_CLIENT_SECRET=your-client-secret
```

### Docker Compose
```yaml
# docker-compose.yml
version: '3.8'
services:
  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: microservices
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
  
  kafka:
    image: confluentinc/cp-kafka:latest
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
    ports:
      - "9092:9092"
  
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
```

## 🤝 Contributing

### Development Workflow
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
- **Documentation**: Check the `docs/` directories
- **Issues**: Create an issue on GitHub
- **Discussions**: Use GitHub Discussions
- **Wiki**: Check the project wiki

### Common Issues
- **Service Startup**: Check Docker containers and dependencies
- **Test Failures**: Verify test environment and data
- **Performance Issues**: Monitor resource usage and configuration
- **Integration Problems**: Check service connectivity and contracts

## 🔮 Roadmap

### Phase 1: Foundation (Completed)
- [x] Set up microservice architecture
- [x] Implement basic testing framework
- [x] Create CI/CD pipeline
- [x] Establish monitoring and logging

### Phase 2: Advanced Testing (In Progress)
- [ ] Implement AI-powered test generation
- [ ] Set up chaos engineering framework
- [ ] Create comprehensive security testing
- [ ] Develop advanced reporting and analytics

### Phase 3: Optimization (Planned)
- [ ] Optimize test execution performance
- [ ] Implement parallel testing strategies
- [ ] Create quality gates and metrics
- [ ] Establish predictive analytics

### Future Enhancements
- **Machine Learning Integration**: AI-powered test optimization
- **Advanced Analytics**: Predictive testing insights
- **Mobile Testing**: Mobile application testing support
- **Cloud Integration**: Multi-cloud testing capabilities
- **Service Mesh**: Istio integration for advanced networking
- **GraphQL**: Modern API query language support

## 📊 Project Status

### Microservices
- [x] User Service - User management and authentication
- [x] Product Service - Product catalog and management
- [x] Order Service - Order processing and management
- [x] Notification Service - Notification and messaging
- [x] Gateway Service - API gateway and routing

### Testing Infrastructure
- [x] Jarvis Core - AI-powered test orchestration
- [x] API Testing Agent - End-to-end API testing
- [x] Contract Testing Agent - Service contract validation
- [x] Chaos Testing Agent - Resilience testing
- [x] Penetration Testing Agent - Security testing
- [x] Reporting Agent - Test reporting and analytics

### Documentation
- [x] Architecture documentation
- [x] Testing strategy
- [x] Quality scorecard template
- [x] Service-specific READMEs
- [x] API documentation

### CI/CD
- [x] GitHub Actions workflows
- [x] Automated testing pipeline
- [x] Docker containerization
- [x] Kubernetes deployment manifests