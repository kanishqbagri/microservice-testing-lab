# Contract Test Generator

A powerful tool for automatically generating contract tests from Swagger/OpenAPI specifications. Supports both REST API and CLI interfaces for easy integration into CI/CD pipelines and developer workflows.

## Features

- **Multiple Input Sources**: Parse Swagger specs from URLs, files, or direct content
- **Multiple Test Frameworks**: Support for Pact and Spring Cloud Contract
- **Multiple Languages**: Generate tests in Java, Kotlin, and Groovy
- **Flexible Configuration**: Custom headers, path filtering, and generation options
- **Dual Interface**: REST API and CLI for different use cases
- **Template-Based Generation**: Uses Freemarker templates for customizable output

## Quick Start

### Using Docker Compose

```bash
# Start all services including contract test generator
docker-compose up -d

# The contract test generator will be available at http://localhost:8085
```

### Using CLI

```bash
# Build the application
cd contract-test-generator
./mvnw clean package

# Run in CLI mode
java -jar target/contract-test-generator-1.0.0.jar cli generate \
  --provider user-service \
  --consumer order-service \
  --url http://localhost:8081/v3/api-docs \
  --output generated-contracts
```

## API Usage

### Generate Contract Tests

#### From Swagger URL

```bash
curl -X POST "http://localhost:8085/api/contract-tests/generate-from-url" \
  -H "Content-Type: application/json" \
  -d '{
    "swaggerUrl": "http://localhost:8081/v3/api-docs",
    "providerName": "user-service",
    "consumerName": "order-service",
    "testFramework": "pact",
    "outputDirectory": "generated-contracts"
  }'
```

#### From Swagger Content

```bash
curl -X POST "http://localhost:8085/api/contract-tests/generate-from-content" \
  -H "Content-Type: application/json" \
  -d '{
    "swaggerContent": "{\"openapi\":\"3.0.0\",\"info\":{\"title\":\"User API\",\"version\":\"1.0.0\"},\"paths\":{}}",
    "providerName": "user-service",
    "consumerName": "order-service",
    "testFramework": "pact"
  }'
```

#### Full Request Object

```bash
curl -X POST "http://localhost:8085/api/contract-tests/generate" \
  -H "Content-Type: application/json" \
  -d '{
    "providerName": "user-service",
    "consumerName": "order-service",
    "swaggerSpec": {
      "openapi": "3.0.0",
      "info": {
        "title": "User API",
        "version": "1.0.0"
      },
      "paths": {
        "/api/users/{id}": {
          "get": {
            "operationId": "getUserById",
            "summary": "Get user by ID",
            "responses": {
              "200": {
                "description": "User found",
                "content": {
                  "application/json": {
                    "schema": {
                      "type": "object",
                      "properties": {
                        "id": {"type": "integer"},
                        "name": {"type": "string"},
                        "email": {"type": "string"}
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    },
    "testFramework": "pact",
    "language": "java",
    "outputDirectory": "generated-contracts",
    "port": 8081,
    "includePaths": ["/api/users.*"],
    "excludePaths": ["/api/admin.*"],
    "customHeaders": {
      "Authorization": "Bearer token",
      "X-Request-ID": "uuid"
    }
  }'
```

## CLI Usage

### Basic Commands

```bash
# Generate contract tests
java -jar contract-test-generator.jar cli generate \
  --provider user-service \
  --consumer order-service \
  --url http://localhost:8081/v3/api-docs \
  --output generated-contracts

# Generate from file
java -jar contract-test-generator.jar cli generate \
  --provider user-service \
  --consumer order-service \
  --file swagger.json \
  --output generated-contracts

# Validate Swagger specification
java -jar contract-test-generator.jar cli validate \
  --url http://localhost:8081/v3/api-docs

# List supported frameworks
java -jar contract-test-generator.jar cli list-frameworks

# List supported languages
java -jar contract-test-generator.jar cli list-languages
```

### Advanced CLI Options

```bash
java -jar contract-test-generator.jar cli generate \
  --provider user-service \
  --consumer order-service \
  --url http://localhost:8081/v3/api-docs \
  --framework pact \
  --language java \
  --output generated-contracts \
  --port 8081 \
  --base-url http://localhost:8081 \
  --include-paths "/api/users.*,/api/products.*" \
  --exclude-paths "/api/admin.*,/api/internal.*" \
  --headers "Authorization:Bearer token,X-Request-ID:uuid" \
  --verbose
```

## Configuration Options

### Test Frameworks

- **pact**: Pact Contract Testing Framework
- **spring-cloud-contract**: Spring Cloud Contract

### Programming Languages

- **java**: Java (default)
- **kotlin**: Kotlin
- **groovy**: Groovy

### Generation Options

- `providerName`: Name of the service providing the API
- `consumerName`: Name of the service consuming the API
- `testFramework`: Framework to use for contract tests
- `language`: Programming language for generated tests
- `outputDirectory`: Directory to output generated tests
- `port`: Port for the service in tests
- `baseUrl`: Base URL for the service
- `includePaths`: Regex patterns for paths to include
- `excludePaths`: Regex patterns for paths to exclude
- `customHeaders`: Custom headers to include in tests

## Generated Output

### Pact Framework

Generates two types of tests:

1. **Consumer Tests**: Test the consumer's expectations
2. **Provider Tests**: Verify the provider meets the contract

Example consumer test:
```java
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "user-service", port = "8081")
public class GetUserByIdConsumerTest {

    @Pact(consumer = "order-service")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .given("A request to GET /api/users/{id}")
            .uponReceiving("A request to get user by ID")
                .path("/api/users/1")
                .method("GET")
            .willRespondWith()
                .status(200)
                .headers(headers)
                .body("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}")
            .toPact();
    }

    @Test
    void testContract(MockServer mockServer) throws IOException {
        String response = Request.get(mockServer.getUrl() + "/api/users/1")
            .execute().returnContent().asString();
        assertThat(response).isNotNull();
    }
}
```

### Spring Cloud Contract

Generates Groovy contract files:

```groovy
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Contract for getUserById"
    
    request {
        method 'GET'
        url '/api/users/1'
    }
    
    response {
        status 200
        body('{"id":1,"name":"John Doe","email":"john@example.com"}')
        headers {
            contentType(applicationJson())
        }
    }
}
```

## API Endpoints

### Health Check
```bash
GET /api/contract-tests/health
```

### Supported Frameworks
```bash
GET /api/contract-tests/supported-frameworks
```

### Supported Languages
```bash
GET /api/contract-tests/supported-languages
```

### Generate Tests
```bash
POST /api/contract-tests/generate
POST /api/contract-tests/generate-from-url
POST /api/contract-tests/generate-from-content
```

## Integration Examples

### CI/CD Pipeline (GitHub Actions)

```yaml
name: Generate Contract Tests
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  generate-contracts:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Start Services
        run: docker-compose up -d
      
      - name: Wait for Services
        run: sleep 30
      
      - name: Generate Contract Tests
        run: |
          curl -X POST "http://localhost:8085/api/contract-tests/generate-from-url" \
            -H "Content-Type: application/json" \
            -d '{
              "swaggerUrl": "http://localhost:8081/v3/api-docs",
              "providerName": "user-service",
              "consumerName": "order-service",
              "testFramework": "pact",
              "outputDirectory": "generated-contracts"
            }'
      
      - name: Run Contract Tests
        run: |
          cd generated-contracts
          mvn test
```

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    
    stages {
        stage('Generate Contract Tests') {
            steps {
                sh '''
                    docker-compose up -d
                    sleep 30
                    curl -X POST "http://localhost:8085/api/contract-tests/generate-from-url" \
                      -H "Content-Type: application/json" \
                      -d '{
                        "swaggerUrl": "http://localhost:8081/v3/api-docs",
                        "providerName": "user-service",
                        "consumerName": "order-service",
                        "testFramework": "pact"
                      }'
                '''
            }
        }
        
        stage('Run Contract Tests') {
            steps {
                dir('generated-contracts') {
                    sh 'mvn test'
                }
            }
        }
    }
}
```

## Development

### Building from Source

```bash
git clone <repository>
cd contract-test-generator
./mvnw clean package
```

### Running Tests

```bash
./mvnw test
```

### Adding Custom Templates

1. Create new template files in `src/main/resources/templates/`
2. Update `ContractTestGeneratorService` to use new templates
3. Add template processing logic

### Extending Functionality

- Add new test frameworks by implementing template generators
- Add new languages by creating language-specific templates
- Add new input formats by extending `SwaggerParserService`

## Troubleshooting

### Common Issues

1. **Swagger URL not accessible**: Ensure the service is running and the URL is correct
2. **Template processing errors**: Check template syntax and data model
3. **File permission issues**: Ensure output directory is writable
4. **Memory issues**: Increase JVM heap size with `-Xmx2g`

### Debug Mode

```bash
# Enable verbose output
java -jar contract-test-generator.jar cli generate \
  --provider user-service \
  --consumer order-service \
  --url http://localhost:8081/v3/api-docs \
  --verbose

# Enable debug logging
java -Dlogging.level.com.kb.contractgenerator=DEBUG \
  -jar contract-test-generator.jar cli generate \
  --provider user-service \
  --consumer order-service \
  --url http://localhost:8081/v3/api-docs
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 