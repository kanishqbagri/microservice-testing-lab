#!/bin/bash

# Repository Restructuring Script
# This script automates the separation of the monolithic codebase into focused repositories

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
BASE_DIR="/Users/kanishkabagri/Workspace"
SOURCE_DIR="$BASE_DIR/microservice-testing-lab"
TARGET_BASE="$BASE_DIR"

# Repository configurations
declare -A REPOS=(
    ["jarvis-ai-testing-platform"]="Jarvis AI Testing Platform"
    ["ecommerce-microservices"]="E-commerce Microservices"
    ["executive-analytics-platform"]="Executive Analytics Platform"
    ["chaos-engineering-framework"]="Chaos Engineering Framework"
    ["infrastructure-and-devops"]="Infrastructure and DevOps"
    ["docs-and-demos"]="Documentation and Demos"
)

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."
    
    if [ ! -d "$SOURCE_DIR" ]; then
        log_error "Source directory not found: $SOURCE_DIR"
        exit 1
    fi
    
    if ! command -v git &> /dev/null; then
        log_error "Git is not installed. Please install Git first."
        exit 1
    fi
    
    log_success "Prerequisites check completed"
}

# Create repository structure
create_repository_structure() {
    log_info "Creating repository structure..."
    
    for repo in "${!REPOS[@]}"; do
        local target_dir="$TARGET_BASE/$repo"
        
        if [ -d "$target_dir" ]; then
            log_warning "Repository $repo already exists, skipping creation"
        else
            mkdir -p "$target_dir"
            cd "$target_dir"
            git init
            echo "# ${REPOS[$repo]}" > README.md
            git add README.md
            git commit -m "Initial commit: ${REPOS[$repo]}"
            log_success "Created repository: $repo"
        fi
    done
}

# Extract Jarvis AI Testing Platform
extract_jarvis_platform() {
    log_info "Extracting Jarvis AI Testing Platform..."
    
    local target_dir="$TARGET_BASE/jarvis-ai-testing-platform"
    
    # Copy core components
    cp -r "$SOURCE_DIR/microservice-testing-lab/jarvis-core" "$target_dir/"
    cp -r "$SOURCE_DIR/microservice-testing-lab/agents" "$target_dir/testing-agents"
    cp -r "$SOURCE_DIR/microservice-testing-lab/shared-utils" "$target_dir/shared-testing-utils"
    
    # Copy testing dashboard
    mkdir -p "$target_dir/testing-dashboard"
    cp -r "$SOURCE_DIR/microservice-testing-lab/frontend" "$target_dir/testing-dashboard/"
    
    # Create repository-specific files
    cat > "$target_dir/docker-compose.yml" << 'EOF'
version: '3.8'
services:
  jarvis-core:
    build: ./jarvis-core
    ports:
      - "8085:8085"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - postgres
      - kafka
  
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: jarvis_testing
      POSTGRES_USER: jarvis
      POSTGRES_PASSWORD: jarvis123
    ports:
      - "5432:5432"
  
  kafka:
    image: confluentinc/cp-kafka:latest
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
    ports:
      - "9092:9092"
    depends_on:
      - zookeeper
  
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    ports:
      - "2181:2181"
EOF

    # Create multi-module pom.xml
    cat > "$target_dir/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.kb</groupId>
    <artifactId>jarvis-ai-testing-platform</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>
    <name>Jarvis AI Testing Platform</name>
    <description>AI-powered testing orchestration platform</description>
    
    <modules>
        <module>jarvis-core</module>
        <module>testing-agents</module>
        <module>shared-testing-utils</module>
    </modules>
    
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <spring-boot.version>3.2.0</spring-boot.version>
    </properties>
    
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
EOF

    log_success "Jarvis AI Testing Platform extracted"
}

# Extract E-commerce Microservices
extract_ecommerce_services() {
    log_info "Extracting E-commerce Microservices..."
    
    local target_dir="$TARGET_BASE/ecommerce-microservices"
    
    # Copy services
    cp -r "$SOURCE_DIR/microservices" "$target_dir/services"
    
    # Copy infrastructure
    mkdir -p "$target_dir/infrastructure"
    cp -r "$SOURCE_DIR/helm-charts" "$target_dir/infrastructure/"
    cp -r "$SOURCE_DIR/k8s" "$target_dir/infrastructure/"
    
    # Copy scripts
    cp -r "$SOURCE_DIR/microservices"/*.sh "$target_dir/scripts/" 2>/dev/null || true
    
    # Create service-specific docker-compose
    cat > "$target_dir/docker-compose.yml" << 'EOF'
version: '3.8'
services:
  gateway-service:
    build: ./services/gateway-service
    ports:
      - "8080:8080"
    depends_on:
      - user-service
      - product-service
      - order-service
      - notification-service
  
  user-service:
    build: ./services/user-service
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - users-db
  
  product-service:
    build: ./services/product-service
    ports:
      - "8082:8082"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - products-db
  
  order-service:
    build: ./services/order-service
    ports:
      - "8083:8083"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - orders-db
  
  notification-service:
    build: ./services/notification-service
    ports:
      - "8084:8084"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - notifications-db
  
  # Databases
  users-db:
    image: postgres:15
    environment:
      POSTGRES_DB: users_db
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
    ports:
      - "5433:5432"
  
  products-db:
    image: postgres:15
    environment:
      POSTGRES_DB: products_db
      POSTGRES_USER: product
      POSTGRES_PASSWORD: password
    ports:
      - "5434:5432"
  
  orders-db:
    image: postgres:15
    environment:
      POSTGRES_DB: orders_db
      POSTGRES_USER: order
      POSTGRES_PASSWORD: password
    ports:
      - "5435:5432"
  
  notifications-db:
    image: postgres:15
    environment:
      POSTGRES_DB: notifications_db
      POSTGRES_USER: notification
      POSTGRES_PASSWORD: password
    ports:
      - "5436:5432"
EOF

    log_success "E-commerce Microservices extracted"
}

# Extract Executive Analytics Platform
extract_analytics_platform() {
    log_info "Extracting Executive Analytics Platform..."
    
    local target_dir="$TARGET_BASE/executive-analytics-platform"
    
    # Copy dashboard components
    cp -r "$SOURCE_DIR/dashboard" "$target_dir/executive-dashboard"
    
    # Copy synthetic data generator
    cp -r "$SOURCE_DIR/microservice-testing-lab/shared-utils/synthetic-data-generator" "$target_dir/"
    
    # Create analytics engine structure
    mkdir -p "$target_dir/analytics-engine/src/main/java/com/kb/analytics"
    
    # Create analytics engine pom.xml
    cat > "$target_dir/analytics-engine/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.kb</groupId>
    <artifactId>analytics-engine</artifactId>
    <version>1.0.0</version>
    <name>Analytics Engine</name>
    <description>AI/ML-powered analytics engine for executive insights</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
EOF

    log_success "Executive Analytics Platform extracted"
}

# Extract Chaos Engineering Framework
extract_chaos_framework() {
    log_info "Extracting Chaos Engineering Framework..."
    
    local target_dir="$TARGET_BASE/chaos-engineering-framework"
    
    # Copy chaos engineering components
    cp -r "$SOURCE_DIR/microservice-testing-lab/chaos-engineering" "$target_dir/"
    
    # Copy experiments
    cp -r "$SOURCE_DIR/experiments" "$target_dir/litmus-integration/"
    
    # Create chaos core structure
    mkdir -p "$target_dir/chaos-core/src/main/java/com/kb/chaos"
    
    # Create chaos core pom.xml
    cat > "$target_dir/chaos-core/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    
    <groupId>com.kb</groupId>
    <artifactId>chaos-core</artifactId>
    <version>1.0.0</version>
    <name>Chaos Core</name>
    <description>Core chaos engineering framework</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>io.kubernetes</groupId>
            <artifactId>client-java</artifactId>
            <version>18.0.1</version>
        </dependency>
    </dependencies>
</project>
EOF

    log_success "Chaos Engineering Framework extracted"
}

# Extract Infrastructure and DevOps
extract_infrastructure() {
    log_info "Extracting Infrastructure and DevOps..."
    
    local target_dir="$TARGET_BASE/infrastructure-and-devops"
    
    # Copy CI/CD components
    cp -r "$SOURCE_DIR/microservice-testing-lab/ci-cd" "$target_dir/"
    
    # Copy scripts
    cp -r "$SOURCE_DIR/scripts" "$target_dir/"
    
    # Create monitoring structure
    mkdir -p "$target_dir/monitoring/{prometheus,grafana,jaeger,elasticsearch}"
    
    # Create deployment structure
    mkdir -p "$target_dir/deployment/{kubernetes,helm,terraform,ansible}"
    
    log_success "Infrastructure and DevOps extracted"
}

# Extract Documentation and Demos
extract_docs_and_demos() {
    log_info "Extracting Documentation and Demos..."
    
    local target_dir="$TARGET_BASE/docs-and-demos"
    
    # Copy documentation
    cp -r "$SOURCE_DIR/docs" "$target_dir/comprehensive-docs"
    
    # Copy demo scripts
    mkdir -p "$target_dir/demo-scripts"
    cp "$SOURCE_DIR/docs"/demo-*.sh "$target_dir/demo-scripts/" 2>/dev/null || true
    
    # Create examples structure
    mkdir -p "$target_dir/examples/{testing-examples,chaos-examples,analytics-examples}"
    
    # Create tutorials structure
    mkdir -p "$target_dir/tutorials/{getting-started,advanced-topics,best-practices}"
    
    log_success "Documentation and Demos extracted"
}

# Create cross-repository configuration
create_cross_repo_config() {
    log_info "Creating cross-repository configuration..."
    
    # Create shared configuration directory
    mkdir -p "$TARGET_BASE/shared-config"
    
    # Create shared models library
    cat > "$TARGET_BASE/shared-config/shared-models/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.kb</groupId>
    <artifactId>shared-models</artifactId>
    <version>1.0.0</version>
    <name>Shared Models</name>
    <description>Shared data models across repositories</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
        </dependency>
    </dependencies>
</project>
EOF

    # Create API client library
    cat > "$TARGET_BASE/shared-config/api-client/pom.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.kb</groupId>
    <artifactId>api-client</artifactId>
    <version>1.0.0</version>
    <name>API Client</name>
    <description>Standardized API client for cross-repository communication</description>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
    </dependencies>
</project>
EOF

    log_success "Cross-repository configuration created"
}

# Main execution function
main() {
    echo "=========================================="
    echo "    Repository Restructuring Script"
    echo "=========================================="
    echo
    
    check_prerequisites
    create_repository_structure
    extract_jarvis_platform
    extract_ecommerce_services
    extract_analytics_platform
    extract_chaos_framework
    extract_infrastructure
    extract_docs_and_demos
    create_cross_repo_config
    
    echo
    log_success "Repository restructuring completed successfully!"
    echo
    echo "=== Created Repositories ==="
    for repo in "${!REPOS[@]}"; do
        echo "• $repo: ${REPOS[$repo]}"
    done
    echo
    echo "=== Next Steps ==="
    echo "1. Review and test each repository"
    echo "2. Update dependencies and imports"
    echo "3. Set up CI/CD pipelines"
    echo "4. Configure cross-repository communication"
    echo "5. Update documentation"
    echo
}

# Run main function
main "$@"
