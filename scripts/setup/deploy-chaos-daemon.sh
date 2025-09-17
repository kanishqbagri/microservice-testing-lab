#!/bin/bash

# Chaos Daemon Deployment Script
# This script deploys the Chaos DaemonSet and related resources

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
LITMUS_NAMESPACE="litmus"
MICROSERVICES_NAMESPACE="microservices"

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
    
    # Check if Litmus Chaos is installed
    if ! kubectl get namespace $LITMUS_NAMESPACE &> /dev/null; then
        log_error "Litmus Chaos namespace not found. Please install Litmus Chaos first."
        exit 1
    fi
    
    log_success "Prerequisites check completed"
}

# Create microservices namespace
create_microservices_namespace() {
    log_info "Creating microservices namespace: $MICROSERVICES_NAMESPACE"
    
    if kubectl get namespace $MICROSERVICES_NAMESPACE &> /dev/null; then
        log_warning "Namespace $MICROSERVICES_NAMESPACE already exists"
    else
        kubectl create namespace $MICROSERVICES_NAMESPACE
        log_success "Namespace $MICROSERVICES_NAMESPACE created"
    fi
}

# Label nodes for chaos daemon
label_nodes() {
    log_info "Labeling nodes for chaos daemon..."
    
    # Get all worker nodes
    local nodes=$(kubectl get nodes --no-headers -o custom-columns=":metadata.name" | grep -v "control-plane\|master")
    
    for node in $nodes; do
        log_info "Labeling node: $node"
        kubectl label node $node chaos-enabled=true --overwrite
    done
    
    log_success "Nodes labeled for chaos daemon"
}

# Deploy chaos daemon
deploy_chaos_daemon() {
    log_info "Deploying chaos daemon..."
    
    # Apply the chaos daemon configuration
    kubectl apply -f k8s/daemonset/chaos-daemon.yaml
    
    log_success "Chaos daemon deployed"
}

# Verify deployment
verify_deployment() {
    log_info "Verifying chaos daemon deployment..."
    
    # Wait for daemon set to be ready
    log_info "Waiting for chaos daemon to be ready..."
    kubectl wait --for=condition=available daemonset/chaos-daemon -n $LITMUS_NAMESPACE --timeout=300s
    
    # Check if all pods are running
    local desired_pods=$(kubectl get daemonset chaos-daemon -n $LITMUS_NAMESPACE -o jsonpath='{.status.desiredNumberScheduled}')
    local ready_pods=$(kubectl get daemonset chaos-daemon -n $LITMUS_NAMESPACE -o jsonpath='{.status.numberReady}')
    
    if [ "$desired_pods" -eq "$ready_pods" ] && [ "$desired_pods" -gt 0 ]; then
        log_success "All chaos daemon pods are running"
    else
        log_error "Some chaos daemon pods are not running. Please check the pod status:"
        kubectl get pods -n $LITMUS_NAMESPACE -l app=chaos-daemon
        exit 1
    fi
    
    # Check service account
    if kubectl get serviceaccount chaos-daemon-sa -n $LITMUS_NAMESPACE &> /dev/null; then
        log_success "Chaos daemon service account created"
    else
        log_error "Chaos daemon service account not found"
        exit 1
    fi
    
    # Check cluster role binding
    if kubectl get clusterrolebinding chaos-daemon-role-binding &> /dev/null; then
        log_success "Chaos daemon cluster role binding created"
    else
        log_error "Chaos daemon cluster role binding not found"
        exit 1
    fi
}

# Display deployment information
display_deployment_info() {
    log_info "Chaos daemon deployment completed successfully!"
    echo
    echo "=== Deployment Information ==="
    echo "Namespace: $LITMUS_NAMESPACE"
    echo "DaemonSet: chaos-daemon"
    echo "Service Account: chaos-daemon-sa"
    echo "Target Namespace: $MICROSERVICES_NAMESPACE"
    echo
    echo "=== Pod Status ==="
    kubectl get pods -n $LITMUS_NAMESPACE -l app=chaos-daemon
    echo
    echo "=== Next Steps ==="
    echo "1. Deploy your microservices to the $MICROSERVICES_NAMESPACE namespace"
    echo "2. Run chaos experiments: kubectl apply -f experiments/network/network-latency.yaml"
    echo "3. Monitor chaos experiments: kubectl get chaosengines -n $LITMUS_NAMESPACE"
    echo "4. View chaos results: kubectl get chaosresults -n $LITMUS_NAMESPACE"
    echo
}

# Main deployment function
main() {
    echo "=========================================="
    echo "    Chaos Daemon Deployment Script"
    echo "=========================================="
    echo
    
    check_prerequisites
    create_microservices_namespace
    label_nodes
    deploy_chaos_daemon
    verify_deployment
    display_deployment_info
}

# Run main function
main "$@"
