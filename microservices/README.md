# Microservices Architecture

This directory contains a comprehensive microservices architecture built with Spring Boot, featuring five core services, infrastructure components, and automated management scripts.

## 🏗️ Architecture Overview

```
microservices/
├── gateway-service/          # API Gateway (Port 8080)
├── user-service/            # User Management (Port 8081)
├── product-service/         # Product Catalog (Port 8082)
├── order-service/           # Order Processing (Port 8083)
├── notification-service/    # Notifications (Port 8084)
├── start-all-services.sh    # Start all services
├── stop-all-services.sh     # Stop all services
├── check-service-status.sh  # Comprehensive status check
├── status.sh               # Quick status alias
└── SCRIPTS_README.md       # Detailed script documentation
```

## 🚀 Quick Start

### Prerequisites
- **Java 17+** - Required for all Spring Boot services
- **Maven 3.8+** - Build tool for Java projects
- **Docker & Docker Compose** - Infrastructure services
- **curl** - For health checks and API testing

### Start All Services
```bash
# Start infrastructure and all microservices
./start-all-services.sh

# Check status
./status.sh

# Stop all services
./stop-all-services.sh
```

## 📋 Service Details

### 1. Gateway Service (Port 8080)
**Purpose**: API Gateway and routing service
- **Technology**: Spring Cloud Gateway
- **Dependencies**: Load Balancer, Actuator
- **Features**: Request routing, load balancing, API aggregation
- **Health Check**: http://localhost:8080/actuator/health
- **API Docs**: http://localhost:8080/swagger-ui.html

### 2. User Service (Port 8081)
**Purpose**: User management, authentication, and authorization
- **Technology**: Spring Boot, Spring Security, JPA
- **Database**: PostgreSQL (users-db)
- **Features**: User registration, authentication, JWT tokens
- **Health Check**: http://localhost:8081/actuator/health
- **API Docs**: http://localhost:8081/swagger-ui.html

**Key Endpoints**:
- `POST /api/users/register` - Register new user
- `GET /api/users/{id}` - Get user by ID
- `POST /api/auth/login` - User login
- `GET /api/users` - List all users

### 3. Product Service (Port 8082)
**Purpose**: Product catalog management
- **Technology**: Spring Boot, Spring Security, JPA
- **Database**: PostgreSQL (products-db)
- **Features**: Product CRUD, inventory management
- **Health Check**: http://localhost:8082/actuator/health
- **API Docs**: http://localhost:8082/swagger-ui.html

**Key Endpoints**:
- `GET /api/products` - List products
- `POST /api/products` - Create product
- `GET /api/products/{id}` - Get product by ID
- `PUT /api/products/{id}` - Update product

### 4. Order Service (Port 8083)
**Purpose**: Order processing and management
- **Technology**: Spring Boot, Spring Security, JPA
- **Database**: PostgreSQL (orders-db)
- **Features**: Order creation, status tracking, payment processing
- **Health Check**: http://localhost:8083/actuator/health
- **API Docs**: http://localhost:8083/swagger-ui.html

**Key Endpoints**:
- `POST /api/orders` - Create order
- `GET /api/orders/{id}` - Get order by ID
- `PUT /api/orders/{id}/status` - Update order status
- `GET /api/orders/user/{userId}` - Get user orders

### 5. Notification Service (Port 8084)
**Purpose**: Notification management and delivery
- **Technology**: Spring Boot, Spring Security, JPA
- **Database**: PostgreSQL (notifications-db)
- **Features**: Email notifications, SMS, push notifications
- **Health Check**: http://localhost:8084/actuator/health
- **API Docs**: http://localhost:8084/swagger-ui.html

**Key Endpoints**:
- `POST /api/notifications` - Send notification
- `GET /api/notifications/user/{userId}` - Get user notifications
- `PUT /api/notifications/{id}/read` - Mark as read
- `GET /api/notifications/{id}` - Get notification by ID

## 🗄️ Infrastructure Services

### Database Configuration
Each service has its own PostgreSQL database:

| Service | Database | Port | User | Password |
|---------|----------|------|------|----------|
| User Service | users-db | 5432 | user | pass |
| Product Service | products-db | 5433 | product_user | product_pass |
| Order Service | orders-db | 5434 | order_user | order_pass |
| Notification Service | notifications-db | 5435 | notification_user | notification_pass |

### Message Broker
- **Apache Kafka**: Port 9092
- **Apache Zookeeper**: Port 2181
- **Purpose**: Event-driven communication between services

## 🛠️ Management Scripts

### start-all-services.sh
Comprehensive startup script that:
- ✅ Checks prerequisites (Java, Maven, Docker)
- ✅ Starts infrastructure services (PostgreSQL, Kafka, Zookeeper)
- ✅ Starts microservices in dependency order
- ✅ Performs health checks for each service
- ✅ Provides colored output and status updates
- ✅ Handles errors gracefully

### stop-all-services.sh
Graceful shutdown script that:
- ✅ Stops services in reverse dependency order
- ✅ Graceful shutdown with SIGTERM first, then SIGKILL
- ✅ Cleans up Docker containers
- ✅ Removes orphaned processes
- ✅ Verifies complete shutdown

### check-service-status.sh
Comprehensive status monitoring that:
- ✅ Checks infrastructure services (PostgreSQL, Kafka, Zookeeper)
- ✅ Checks microservices by port
- ✅ Performs health endpoint checks
- ✅ Monitors system resources (memory, disk, Docker)
- ✅ Provides percentage-based status summary
- ✅ Shows service URLs and documentation links

### status.sh
Quick alias for status checking - simply calls `check-service-status.sh`

## 🔧 Configuration

### Environment Variables
The services use the following environment variables:

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/users-db
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=pass

# Server Configuration
SERVER_PORT=8081

# Spring Profiles
SPRING_PROFILES_ACTIVE=docker
```

### Docker Compose
The `docker-compose.yml` in the parent directory defines:
- 4 PostgreSQL databases (one per service)
- Apache Kafka and Zookeeper
- All microservices with proper dependencies
- Volume persistence for databases

## 🧪 Testing Strategy

Each service follows a comprehensive testing approach:

### Testing Pyramid
- **Unit Tests (70%)**: Individual service methods
- **Integration Tests (20%)**: API endpoints and database integration
- **Contract Tests (5%)**: API contracts with other services
- **API Tests (3%)**: End-to-end API testing
- **Chaos Tests (2%)**: Resilience and fault tolerance

### Test Tools
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework
- **Spring Boot Test**: Integration testing
- **TestContainers**: Database testing
- **RestAssured**: API testing

## 📊 Monitoring & Health Checks

### Health Endpoints
All services expose health check endpoints:
- **Gateway**: http://localhost:8080/actuator/health
- **User**: http://localhost:8081/actuator/health
- **Product**: http://localhost:8082/actuator/health
- **Order**: http://localhost:8083/actuator/health
- **Notification**: http://localhost:8084/actuator/health

### Metrics
Each service provides:
- Request count and response times
- Error rates by endpoint
- Database connection status
- Custom business metrics

### Logging
- Structured JSON logging
- Correlation IDs for request tracing
- Sensitive data masking
- Appropriate log levels

## 🔒 Security

### Authentication & Authorization
- **JWT Tokens**: Stateless authentication
- **Spring Security**: Role-based access control
- **Password Hashing**: BCrypt encoding
- **Input Validation**: Request validation

### Data Protection
- **Encryption**: Sensitive data encryption
- **Audit Logging**: User action logging
- **Data Masking**: PII protection
- **SQL Injection Prevention**: Parameterized queries

## 🚀 Deployment

### Docker Deployment
Each service includes a Dockerfile for containerization:

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Kubernetes Ready
Services are configured for Kubernetes deployment with:
- Health checks and readiness probes
- Resource limits and requests
- ConfigMaps and Secrets support
- Service discovery

## 🔄 Service Communication

### Synchronous Communication
- **REST APIs**: HTTP-based service communication
- **API Gateway**: Centralized routing and load balancing
- **Circuit Breakers**: Fault tolerance patterns

### Asynchronous Communication
- **Kafka Events**: Event-driven architecture
- **Event Sourcing**: State change events
- **Saga Pattern**: Distributed transaction management

## 📚 API Documentation

### Swagger UI
Each service provides interactive API documentation:
- **Gateway**: http://localhost:8080/swagger-ui.html
- **User**: http://localhost:8081/swagger-ui.html
- **Product**: http://localhost:8082/swagger-ui.html
- **Order**: http://localhost:8083/swagger-ui.html
- **Notification**: http://localhost:8084/swagger-ui.html

### OpenAPI Specification
- Machine-readable API specifications
- Contract testing support
- Code generation capabilities

## 🛠️ Troubleshooting

### Common Issues

#### Services Won't Start
```bash
# Check prerequisites
java -version
mvn -version
docker --version

# Check Docker is running
docker info

# Check available ports
lsof -i :8080-8085
```

#### Services Won't Stop
```bash
# Force kill all Java processes
pkill -f "spring-boot:run"

# Force kill all Maven processes
pkill -f "mvn"

# Stop Docker containers
docker-compose down
```

#### Health Checks Fail
```bash
# Check individual service health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

### Debug Mode
Run scripts with verbose output:
```bash
bash -x ./start-all-services.sh
bash -x ./stop-all-services.sh
bash -x ./check-service-status.sh
```

## 📈 Performance Considerations

### Database Optimization
- Connection pooling
- Query optimization
- Indexing strategies
- Database-specific configurations

### Caching
- Redis integration ready
- Application-level caching
- Database query caching

### Load Balancing
- Spring Cloud LoadBalancer
- Health check integration
- Circuit breaker patterns

## 🔄 Continuous Integration

### CI/CD Pipeline
Each service supports:
- Automated testing
- Docker image building
- Kubernetes deployment
- Health check validation

### Quality Gates
- Test coverage requirements (>90%)
- Code quality checks
- Security scanning
- Performance testing

## 📝 Development Workflow

### Local Development
1. Start infrastructure: `docker-compose up -d`
2. Start individual services: `mvn spring-boot:run`
3. Run tests: `mvn test`
4. Check status: `./status.sh`

### Code Standards
- Java coding conventions
- Spring Boot best practices
- Test-driven development
- Comprehensive documentation

## 🤝 Contributing

### Development Guidelines
1. Create feature branch from `main`
2. Implement feature with tests
3. Ensure all tests pass
4. Update documentation
5. Create pull request

### Testing Requirements
- Unit tests for all new features
- Integration tests for API endpoints
- Contract tests for service interactions
- Performance tests for critical paths

## 🆘 Support

### Getting Help
- **Documentation**: Check service-specific READMEs
- **Health Checks**: Verify service health endpoints
- **Logs**: Check application logs for errors
- **Status Script**: Use `./check-service-status.sh` for diagnostics

### Common Commands
```bash
# Start all services
./start-all-services.sh

# Check status
./status.sh

# Stop all services
./stop-all-services.sh

# View logs
docker-compose logs -f

# Restart specific service
docker-compose restart user-service
```

## 📄 License

This microservices architecture is part of the microservice testing lab and follows the same license as the main project.

---

**Quick Reference**:
- **Start**: `./start-all-services.sh`
- **Status**: `./status.sh`
- **Stop**: `./stop-all-services.sh`
- **Health**: Check individual service health endpoints
- **Docs**: Access Swagger UI for each service

