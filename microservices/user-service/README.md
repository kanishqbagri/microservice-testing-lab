# User Service

A microservice responsible for user management, authentication, and authorization.

## 🏗️ Architecture

```
user-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kb/user_service/
│   │   │       ├── config/           # Configuration classes
│   │   │       ├── controller/       # REST API controllers
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       ├── entity/           # JPA entities
│   │   │       ├── exception/        # Custom exceptions
│   │   │       ├── repository/       # Data access layer
│   │   │       ├── service/          # Business logic
│   │   │       └── UserServiceApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml
│   └── test/
│       ├── java/
│       │   └── com/kb/user_service/
│       │       ├── config/           # Test configurations
│       │       ├── controller/       # Integration tests
│       │       └── service/          # Unit tests
│       └── resources/
│           └── application.properties
├── tests/                            # External test suites
│   ├── unit/                         # Unit tests
│   ├── integration/                  # Integration tests
│   └── contract/                     # Contract tests
├── Dockerfile                        # Container configuration
├── pom.xml                          # Maven dependencies
└── README.md                        # This file
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL 13+
- Docker (optional)

### Local Development

1. **Start the database**
   ```bash
   docker run -d --name postgres-user \
     -e POSTGRES_DB=user_service \
     -e POSTGRES_USER=user_service \
     -e POSTGRES_PASSWORD=password \
     -p 5432:5432 \
     postgres:13
   ```

2. **Build and run the service**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. **Access the service**
   - API: http://localhost:8081
   - Health Check: http://localhost:8081/actuator/health
   - API Documentation: http://localhost:8081/swagger-ui.html

### Docker Deployment

1. **Build the image**
   ```bash
   docker build -t user-service .
   ```

2. **Run the container**
   ```bash
   docker run -d --name user-service \
     -p 8081:8081 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/user_service \
     -e SPRING_DATASOURCE_USERNAME=user_service \
     -e SPRING_DATASOURCE_PASSWORD=password \
     user-service
   ```

## 📋 API Endpoints

### User Management
- `POST /api/users/register` - Register a new user
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users` - Get all users (paginated)

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh` - Refresh token

### Health & Monitoring
- `GET /actuator/health` - Health check
- `GET /actuator/info` - Service information
- `GET /actuator/metrics` - Metrics endpoint

## 🧪 Testing Strategy

### Testing Pyramid

#### Unit Tests (70%)
- **Location**: `src/test/java/com/kb/user_service/service/`
- **Scope**: Individual service methods
- **Tools**: JUnit 5, Mockito
- **Coverage Target**: >90%

**Example:**
```java
@Test
void shouldCreateUserSuccessfully() {
    // Given
    UserRegistrationRequest request = new UserRegistrationRequest("test@example.com", "password");
    
    // When
    UserResponse response = userService.registerUser(request);
    
    // Then
    assertThat(response.getEmail()).isEqualTo("test@example.com");
    verify(userRepository).save(any(User.class));
}
```

#### Integration Tests (20%)
- **Location**: `src/test/java/com/kb/user_service/controller/`
- **Scope**: API endpoints and database integration
- **Tools**: Spring Boot Test, TestContainers
- **Coverage Target**: Critical business flows

**Example:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserControllerIntegrationTest {
    
    @Test
    void shouldRegisterUserViaApi() {
        // Given
        UserRegistrationRequest request = new UserRegistrationRequest("test@example.com", "password");
        
        // When
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(
            "/api/users/register", request, UserResponse.class);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");
    }
}
```

#### Contract Tests (5%)
- **Location**: `tests/contract/`
- **Scope**: API contracts with other services
- **Tools**: Pact, Spring Cloud Contract
- **Coverage Target**: All service interactions

#### API Tests (3%)
- **Location**: `tests/integration/`
- **Scope**: End-to-end API testing
- **Tools**: RestAssured, Karate
- **Coverage Target**: Complete workflows

#### Chaos Tests (2%)
- **Location**: `tests/chaos/`
- **Scope**: Resilience and fault tolerance
- **Tools**: Custom chaos framework
- **Coverage Target**: Critical failure scenarios

### Test Data Management

#### Test Data Factory
```java
public class UserTestDataFactory {
    
    public static User createUser() {
        return User.builder()
            .email("test@example.com")
            .password("encodedPassword")
            .firstName("John")
            .lastName("Doe")
            .build();
    }
    
    public static UserRegistrationRequest createRegistrationRequest() {
        return new UserRegistrationRequest("test@example.com", "password");
    }
}
```

#### Database Seeding
```java
@TestConfiguration
public class TestDatabaseConfig {
    
    @Bean
    public CommandLineRunner dataLoader(UserRepository userRepository) {
        return args -> {
            userRepository.save(UserTestDataFactory.createUser());
        };
    }
}
```

## 🔧 Configuration

### Application Properties
```yaml
# Server Configuration
server:
  port: 8081
  servlet:
    context-path: /api

# Database Configuration
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/user_service
    username: user_service
    password: password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

# Security Configuration
security:
  jwt:
    secret: your-jwt-secret-key
    expiration: 86400000 # 24 hours

# Actuator Configuration
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always
```

### Environment Variables
```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/user_service
SPRING_DATASOURCE_USERNAME=user_service
SPRING_DATASOURCE_PASSWORD=password

# Security
JWT_SECRET=your-jwt-secret-key
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8081
```

## 📊 Monitoring

### Health Checks
- **Database Connectivity**: Verifies PostgreSQL connection
- **Service Health**: Overall service status
- **Custom Health Indicators**: Business logic health checks

### Metrics
- **Request Count**: API endpoint request counts
- **Response Time**: Average response times
- **Error Rate**: Error percentage by endpoint
- **User Registration Rate**: Business metrics

### Logging
- **Structured Logging**: JSON-formatted logs
- **Log Levels**: Appropriate level usage
- **Correlation IDs**: Request tracing
- **Sensitive Data Masking**: Password and token masking

## 🔒 Security

### Authentication
- **JWT Tokens**: Stateless authentication
- **Password Hashing**: BCrypt password encoding
- **Token Refresh**: Automatic token renewal
- **Session Management**: Secure session handling

### Authorization
- **Role-Based Access**: User roles and permissions
- **API Security**: Endpoint protection
- **Input Validation**: Request validation
- **SQL Injection Prevention**: Parameterized queries

### Data Protection
- **Encryption**: Sensitive data encryption
- **Audit Logging**: User action logging
- **Data Masking**: PII protection
- **Compliance**: GDPR compliance measures

## 🚀 Deployment

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/user-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: user-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: user-service
  template:
    metadata:
      labels:
        app: user-service
    spec:
      containers:
      - name: user-service
        image: user-service:latest
        ports:
        - containerPort: 8081
        env:
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
```

### CI/CD Pipeline
```yaml
# .github/workflows/user-service.yml
name: User Service CI/CD
on:
  push:
    paths:
      - 'microservices/user-service/**'

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run Tests
        run: |
          cd microservices/user-service
          mvn clean test
      
      - name: Build Image
        run: |
          docker build -t user-service .
      
      - name: Deploy
        run: |
          # Deployment steps
```

## 🔄 Integration

### Service Dependencies
- **Database**: PostgreSQL for data persistence
- **Message Broker**: Kafka for event publishing
- **API Gateway**: Gateway service for routing
- **Monitoring**: Prometheus for metrics collection

### Event Publishing
```java
@Service
public class UserService {
    
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        kafkaTemplate.send("user-events", event);
    }
}
```

### API Contracts
- **OpenAPI Specification**: API documentation
- **Contract Tests**: Service compatibility validation
- **Versioning**: API version management
- **Breaking Changes**: Contract evolution strategy

## 📚 Documentation

### API Documentation
- **Swagger UI**: Interactive API documentation
- **OpenAPI Spec**: Machine-readable API specification
- **Postman Collection**: API testing collection
- **Examples**: Request/response examples

### Code Documentation
- **JavaDoc**: Method and class documentation
- **Architecture Decisions**: ADR documentation
- **Setup Guide**: Development environment setup
- **Troubleshooting**: Common issues and solutions

## 🤝 Contributing

### Development Workflow
1. Create feature branch from `main`
2. Implement feature with tests
3. Ensure all tests pass
4. Update documentation
5. Create pull request

### Code Standards
- **Java Conventions**: Follow Java coding standards
- **Test Coverage**: Maintain >90% coverage
- **Documentation**: Comprehensive documentation
- **Code Review**: Mandatory code review

### Testing Requirements
- **Unit Tests**: All new features
- **Integration Tests**: API endpoints
- **Contract Tests**: Service interactions
- **Performance Tests**: Load testing for critical paths

## 🆘 Support

### Common Issues
- **Database Connection**: Check PostgreSQL status and credentials
- **Authentication Failures**: Verify JWT configuration
- **Performance Issues**: Monitor database queries and indexes
- **Integration Problems**: Check service dependencies

### Debugging
- **Logs**: Check application logs for errors
- **Health Checks**: Verify service health endpoints
- **Metrics**: Monitor performance metrics
- **Tracing**: Use correlation IDs for request tracing

### Getting Help
- **Documentation**: Check this README and API docs
- **Issues**: Create GitHub issue with details
- **Team Chat**: Use team communication channels
- **Escalation**: Contact team lead for urgent issues
