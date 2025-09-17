#!/bin/bash

# Script to create Helm charts for all microservices with PostgreSQL sidecar

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
SERVICES=("product-service" "order-service" "notification-service")
BASE_CHART_PATH="./helm-charts/user-service"

echo -e "${BLUE}🚀 Creating Helm charts for all microservices with PostgreSQL sidecar${NC}"
echo ""

# Function to print status
print_status() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Function to create chart for a service
create_service_chart() {
    local service_name=$1
    local service_port=$2
    local db_name=$3
    local db_user=$4
    local db_password=$5
    
    echo -e "${BLUE}📦 Creating chart for $service_name${NC}"
    
    # Create directory structure
    mkdir -p "helm-charts/$service_name/templates"
    
    # Copy base files
    cp "$BASE_CHART_PATH/Chart.yaml" "helm-charts/$service_name/"
    cp "$BASE_CHART_PATH/templates/"*.yaml "helm-charts/$service_name/templates/"
    cp "$BASE_CHART_PATH/templates/NOTES.txt" "helm-charts/$service_name/templates/"
    cp "$BASE_CHART_PATH/deploy.sh" "helm-charts/$service_name/"
    cp "$BASE_CHART_PATH/README.md" "helm-charts/$service_name/"
    
    # Update Chart.yaml
    sed -i.bak "s/user-service/$service_name/g" "helm-charts/$service_name/Chart.yaml"
    sed -i.bak "s/User Service/$service_name/g" "helm-charts/$service_name/Chart.yaml"
    rm "helm-charts/$service_name/Chart.yaml.bak"
    
    # Update values.yaml
    cat > "helm-charts/$service_name/values.yaml" << EOF
# Default values for $service_name with PostgreSQL sidecar
# This is a YAML-formatted file.

# $service_name configuration
${service_name//-/_}:
  image:
    repository: $service_name
    tag: "latest"
    pullPolicy: IfNotPresent
  
  name: $service_name
  port: $service_port
  
  replicas: 1
  
  resources:
    limits:
      memory: "1Gi"
      cpu: "1000m"
    requests:
      memory: "512Mi"
      cpu: "500m"
  
  service:
    type: ClusterIP
    port: $service_port
    targetPort: $service_port
  
  # Environment variables
  env:
    SPRING_PROFILES_ACTIVE: "kubernetes"
    SERVER_PORT: "$service_port"
    SPRING_DATASOURCE_URL: "jdbc:postgresql://localhost:5432/$db_name"
    SPRING_DATASOURCE_USERNAME: "$db_user"
    SPRING_DATASOURCE_PASSWORD: "$db_password"
  
  # Health checks
  healthCheck:
    enabled: true
    initialDelaySeconds: 60
    periodSeconds: 10
    timeoutSeconds: 5
    failureThreshold: 3
    livenessProbe:
      httpGet:
        path: /actuator/health
        port: $service_port
    readinessProbe:
      httpGet:
        path: /actuator/health/readiness
        port: $service_port

# PostgreSQL Sidecar Database configuration
postgresSidecar:
  image:
    repository: postgres
    tag: "14"
    pullPolicy: IfNotPresent
  
  name: postgres-sidecar
  port: 5432
  database: $db_name
  username: $db_user
  password: $db_password
  
  persistence:
    enabled: true
    size: 1Gi
    storageClass: ""
  
  resources:
    limits:
      memory: "512Mi"
      cpu: "500m"
    requests:
      memory: "256Mi"
      cpu: "250m"
  
  healthCheck:
    enabled: true
    initialDelaySeconds: 30
    periodSeconds: 10
    timeoutSeconds: 5
    failureThreshold: 3

# Ingress configuration
ingress:
  enabled: false
  className: ""
  annotations: {}
  hosts:
    - host: $service_name.local
      paths:
        - path: /
          pathType: Prefix
  tls: []

# ServiceMonitor for Prometheus (if using Prometheus Operator)
serviceMonitor:
  enabled: false
  labels: {}
  interval: 30s
  scrapeTimeout: 10s

# Pod Security Context
podSecurityContext:
  fsGroup: 2000

# Security Context
securityContext:
  capabilities:
    drop:
    - ALL
  readOnlyRootFilesystem: false
  runAsNonRoot: true
  runAsUser: 1000

# Node selector
nodeSelector: {}

# Tolerations
tolerations: []

# Affinity
affinity: {}

# Annotations
annotations: {}

# Labels
labels: {}
EOF

    # Update deployment.yaml to use the correct service name
    sed -i.bak "s/userService/${service_name//-/_}/g" "helm-charts/$service_name/templates/deployment.yaml"
    sed -i.bak "s/user-service/$service_name/g" "helm-charts/$service_name/templates/deployment.yaml"
    rm "helm-charts/$service_name/templates/deployment.yaml.bak"
    
    # Update service.yaml
    sed -i.bak "s/userService/${service_name//-/_}/g" "helm-charts/$service_name/templates/service.yaml"
    sed -i.bak "s/user-service/$service_name/g" "helm-charts/$service_name/templates/service.yaml"
    rm "helm-charts/$service_name/templates/service.yaml.bak"
    
    # Update other templates
    for file in "helm-charts/$service_name/templates/"*.yaml; do
        if [ -f "$file" ]; then
            sed -i.bak "s/userService/${service_name//-/_}/g" "$file"
            sed -i.bak "s/user-service/$service_name/g" "$file"
            rm "${file}.bak"
        fi
    done
    
    # Update NOTES.txt
    sed -i.bak "s/userService/${service_name//-/_}/g" "helm-charts/$service_name/templates/NOTES.txt"
    sed -i.bak "s/user-service/$service_name/g" "helm-charts/$service_name/templates/NOTES.txt"
    rm "helm-charts/$service_name/templates/NOTES.txt.bak"
    
    # Update deploy.sh
    sed -i.bak "s/user-service/$service_name/g" "helm-charts/$service_name/deploy.sh"
    sed -i.bak "s/user-service/$service_name/g" "helm-charts/$service_name/deploy.sh"
    rm "helm-charts/$service_name/deploy.sh.bak"
    
    # Update README.md
    sed -i.bak "s/user-service/$service_name/g" "helm-charts/$service_name/README.md"
    sed -i.bak "s/User Service/$service_name/g" "helm-charts/$service_name/README.md"
    rm "helm-charts/$service_name/README.md.bak"
    
    # Make deploy.sh executable
    chmod +x "helm-charts/$service_name/deploy.sh"
    
    print_status "Chart created for $service_name"
}

# Create charts for all services
create_service_chart "product-service" "8082" "products-db" "product_user" "product_pass"
create_service_chart "order-service" "8083" "orders-db" "order_user" "order_pass"
create_service_chart "notification-service" "8084" "notifications-db" "notification_user" "notification_pass"

echo ""
print_status "All Helm charts created successfully!"
echo ""
echo -e "${BLUE}📋 Created charts:${NC}"
echo "  • user-service (already existed)"
echo "  • product-service"
echo "  • order-service"
echo "  • notification-service"
echo ""
echo -e "${BLUE}🚀 To deploy a service:${NC}"
echo "  cd helm-charts/product-service && ./deploy.sh"
echo "  cd helm-charts/order-service && ./deploy.sh"
echo "  cd helm-charts/notification-service && ./deploy.sh"
echo ""
echo -e "${BLUE}📖 Each chart includes:${NC}"
echo "  • PostgreSQL sidecar container"
echo "  • Persistent volume for database"
echo "  • Health checks"
echo "  • Resource limits"
echo "  • Security contexts"
echo "  • Deployment script"
echo "  • README documentation"
