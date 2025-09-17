#!/bin/bash

# User Service with PostgreSQL Sidecar Deployment Script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
NAMESPACE="notification-service"
RELEASE_NAME="notification-service"
CHART_PATH="./helm-charts/notification-service"

echo -e "${BLUE}🚀 Deploying User Service with PostgreSQL Sidecar${NC}"
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

# Check if kubectl is available
if ! command -v kubectl &> /dev/null; then
    print_error "kubectl is not installed or not in PATH"
    exit 1
fi

# Check if helm is available
if ! command -v helm &> /dev/null; then
    print_error "helm is not installed or not in PATH"
    exit 1
fi

# Check if cluster is accessible
if ! kubectl cluster-info &> /dev/null; then
    print_error "Cannot connect to Kubernetes cluster"
    exit 1
fi

print_status "Kubernetes cluster is accessible"

# Create namespace if it doesn't exist
if ! kubectl get namespace $NAMESPACE &> /dev/null; then
    echo -e "${BLUE}📦 Creating namespace: $NAMESPACE${NC}"
    kubectl create namespace $NAMESPACE
    print_status "Namespace created"
else
    print_status "Namespace already exists"
fi

# Deploy the Helm chart
echo -e "${BLUE}📦 Deploying Helm chart...${NC}"
helm upgrade --install $RELEASE_NAME $CHART_PATH \
    --namespace $NAMESPACE \
    --wait \
    --timeout=5m

print_status "Helm chart deployed successfully"

# Wait for pods to be ready
echo -e "${BLUE}⏳ Waiting for pods to be ready...${NC}"
kubectl wait --for=condition=ready pod -l app=notification-service -n $NAMESPACE --timeout=300s

print_status "Pods are ready"

# Show deployment status
echo -e "${BLUE}📊 Deployment Status:${NC}"
kubectl get pods -n $NAMESPACE -l app=notification-service
echo ""

# Show services
echo -e "${BLUE}🌐 Services:${NC}"
kubectl get svc -n $NAMESPACE
echo ""

# Show PVCs
echo -e "${BLUE}💾 Persistent Volume Claims:${NC}"
kubectl get pvc -n $NAMESPACE
echo ""

# Port forward instructions
echo -e "${BLUE}🔗 To access the service:${NC}"
echo "kubectl port-forward svc/notification-service 8081:8081 -n $NAMESPACE"
echo ""
echo -e "${BLUE}🔍 To check logs:${NC}"
echo "kubectl logs -l app=notification-service -c notification-service -n $NAMESPACE"
echo "kubectl logs -l app=notification-service -c postgres-sidecar -n $NAMESPACE"
echo ""

print_status "Deployment completed successfully!"
