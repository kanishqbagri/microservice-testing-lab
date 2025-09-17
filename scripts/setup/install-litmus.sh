#!/bin/bash

# Litmus Chaos Installation Script
# This script installs Litmus Chaos on a Kubernetes cluster

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
LITMUS_VERSION="2.6.0"
LITMUS_NAMESPACE="litmus"
LITMUS_CHART_NAME="litmuschaos"

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
    
    # Check if kubectl is installed
    if ! command -v kubectl &> /dev/null; then
        log_error "kubectl is not installed. Please install kubectl first."
        exit 1
    fi
    
    # Check if kubectl can connect to cluster
    if ! kubectl cluster-info &> /dev/null; then
        log_error "Cannot connect to Kubernetes cluster. Please check your kubectl configuration."
        exit 1
    fi
    
    # Check if Helm is installed (optional)
    if command -v helm &> /dev/null; then
        log_info "Helm is available - will use Helm for installation"
        USE_HELM=true
    else
        log_warning "Helm is not installed - will use kubectl for installation"
        USE_HELM=false
    fi
    
    log_success "Prerequisites check completed"
}

# Create namespace
create_namespace() {
    log_info "Creating namespace: $LITMUS_NAMESPACE"
    
    if kubectl get namespace $LITMUS_NAMESPACE &> /dev/null; then
        log_warning "Namespace $LITMUS_NAMESPACE already exists"
    else
        kubectl create namespace $LITMUS_NAMESPACE
        log_success "Namespace $LITMUS_NAMESPACE created"
    fi
}

# Install using Helm (preferred method)
install_with_helm() {
    log_info "Installing Litmus Chaos using Helm..."
    
    # Add Litmus Helm repository
    helm repo add litmuschaos https://litmuschaos.github.io/litmus-helm/
    helm repo update
    
    # Install Litmus Chaos
    helm install $LITMUS_CHART_NAME litmuschaos/litmus-2-0-0 \
        --namespace $LITMUS_NAMESPACE \
        --set litmusCRDs.enabled=true \
        --set litmusOperator.enabled=true \
        --set litmusPortal.enabled=true \
        --set litmusPortal.frontendService.type=NodePort \
        --set litmusPortal.frontendService.nodePort=30000 \
        --wait --timeout=300s
    
    log_success "Litmus Chaos installed using Helm"
}

# Install using kubectl (fallback method)
install_with_kubectl() {
    log_info "Installing Litmus Chaos using kubectl..."
    
    # Install CRDs
    kubectl apply -f https://litmuschaos.github.io/litmus/2.6.0/crds.yaml
    
    # Install RBAC
    kubectl apply -f https://litmuschaos.github.io/litmus/2.6.0/rbac.yaml
    
    # Install Litmus Chaos
    kubectl apply -f https://litmuschaos.github.io/litmus/2.6.0/namespaced-scope/chaos-resources.yaml
    
    log_success "Litmus Chaos installed using kubectl"
}

# Verify installation
verify_installation() {
    log_info "Verifying Litmus Chaos installation..."
    
    # Wait for pods to be ready
    log_info "Waiting for pods to be ready..."
    kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=litmus-2-0-0 --namespace $LITMUS_NAMESPACE --timeout=300s
    
    # Check if all pods are running
    local pod_count=$(kubectl get pods -n $LITMUS_NAMESPACE --no-headers | wc -l)
    local running_pods=$(kubectl get pods -n $LITMUS_NAMESPACE --no-headers | grep -c "Running")
    
    if [ "$pod_count" -eq "$running_pods" ] && [ "$pod_count" -gt 0 ]; then
        log_success "All Litmus Chaos pods are running"
    else
        log_error "Some pods are not running. Please check the pod status:"
        kubectl get pods -n $LITMUS_NAMESPACE
        exit 1
    fi
    
    # Check if CRDs are installed
    if kubectl get crd | grep -q "chaosengines.litmuschaos.io"; then
        log_success "Litmus Chaos CRDs are installed"
    else
        log_error "Litmus Chaos CRDs are not installed properly"
        exit 1
    fi
}

# Display access information
display_access_info() {
    log_info "Litmus Chaos installation completed successfully!"
    echo
    echo "=== Access Information ==="
    echo "Namespace: $LITMUS_NAMESPACE"
    echo "Pods: $(kubectl get pods -n $LITMUS_NAMESPACE --no-headers | wc -l)"
    echo
    echo "=== Next Steps ==="
    echo "1. Check pod status: kubectl get pods -n $LITMUS_NAMESPACE"
    echo "2. View logs: kubectl logs -n $LITMUS_NAMESPACE -l app.kubernetes.io/name=litmus-2-0-0"
    echo "3. Access portal: kubectl port-forward -n $LITMUS_NAMESPACE svc/litmus-2-0-0-frontend-service 9091:9091"
    echo "4. Run your first experiment: kubectl apply -f experiments/network/network-latency.yaml"
    echo
}

# Main installation function
main() {
    echo "=========================================="
    echo "    Litmus Chaos Installation Script"
    echo "=========================================="
    echo
    
    check_prerequisites
    create_namespace
    
    if [ "$USE_HELM" = true ]; then
        install_with_helm
    else
        install_with_kubectl
    fi
    
    verify_installation
    display_access_info
}

# Run main function
main "$@"
