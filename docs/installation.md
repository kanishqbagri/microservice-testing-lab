# Chaos Engineering Framework - Installation Guide

## 🎯 Overview
This guide provides step-by-step instructions for installing and configuring the Chaos Engineering Framework with Litmus Chaos and DaemonSet deployment.

## 📋 Prerequisites

### System Requirements
- **Kubernetes Cluster**: v1.19 or higher
- **kubectl**: Configured to access your cluster
- **Helm**: v3.0 or higher (optional, but recommended)
- **Docker**: For building custom images (if needed)

### Cluster Requirements
- **Nodes**: At least 2 worker nodes
- **Resources**: 
  - CPU: 2 cores per node
  - Memory: 4GB per node
  - Storage: 20GB per node

## 🚀 Installation Steps

### Step 1: Verify Prerequisites

```bash
# Check kubectl version
kubectl version --client

# Check cluster connectivity
kubectl cluster-info

# Check if Helm is available (optional)
helm version
```

### Step 2: Install Litmus Chaos

```bash
# Navigate to the chaos-engineering directory
cd chaos-engineering

# Run the installation script
./scripts/setup/install-litmus.sh
```

**Alternative Manual Installation:**

```bash
# Create namespace
kubectl create namespace litmus

# Install using Helm (recommended)
helm repo add litmuschaos https://litmuschaos.github.io/litmus-helm/
helm repo update
helm install litmuschaos litmuschaos/litmus-2-0-0 \
  --namespace litmus \
  --set litmusCRDs.enabled=true \
  --set litmusOperator.enabled=true \
  --set litmusPortal.enabled=true

# Or install using kubectl
kubectl apply -f https://litmuschaos.github.io/litmus/2.6.0/crds.yaml
kubectl apply -f https://litmuschaos.github.io/litmus/2.6.0/rbac.yaml
kubectl apply -f https://litmuschaos.github.io/litmus/2.6.0/namespaced-scope/chaos-resources.yaml
```

### Step 3: Deploy Chaos DaemonSet

```bash
# Deploy the chaos daemon
./scripts/setup/deploy-chaos-daemon.sh
```

**Manual Deployment:**

```bash
# Create microservices namespace
kubectl create namespace microservices

# Label nodes for chaos daemon
kubectl label node <node-name> chaos-enabled=true

# Deploy chaos daemon
kubectl apply -f k8s/daemonset/chaos-daemon.yaml
```

### Step 4: Verify Installation

```bash
# Check Litmus Chaos pods
kubectl get pods -n litmus

# Check chaos daemon pods
kubectl get pods -n litmus -l app=chaos-daemon

# Check service accounts
kubectl get serviceaccount -n litmus

# Check cluster role bindings
kubectl get clusterrolebinding | grep chaos
```

## 🔧 Configuration

### Chaos Daemon Configuration

The chaos daemon is configured via a ConfigMap. Key configuration options:

```yaml
chaos:
  daemon:
    enabled: true
    mode: "daemon"
    blast-radius: "service"
    max-duration: 300
    safety-mode: true
    target-namespace: "microservices"
```

### Safety Controls

```yaml
safety:
  enabled: true
  emergency-stop:
    enabled: true
    grace-period: 30
  rollback:
    enabled: true
    auto-rollback: true
    rollback-threshold: 0.8
  limits:
    max-concurrent-experiments: 2
    max-experiments-per-hour: 10
    max-experiments-per-day: 50
```

### Experiment Categories

```yaml
experiments:
  network:
    enabled: true
    max-latency: 1000
    max-packet-loss: 50
  service:
    enabled: true
    max-pod-failures: 1
    max-service-restarts: 2
  infrastructure:
    enabled: true
    max-node-failures: 0
  data:
    enabled: false
```

## 🧪 Running Your First Experiment

### Network Latency Experiment

```bash
# Apply the network latency experiment
kubectl apply -f experiments/network/network-latency.yaml

# Monitor the experiment
kubectl get chaosengines -n litmus
kubectl get chaosresults -n litmus

# View experiment logs
kubectl logs -n litmus -l app=network-latency
```

### Pod Failure Experiment

```bash
# Apply the pod failure experiment
kubectl apply -f experiments/service/pod-failure.yaml

# Monitor the experiment
kubectl get chaosengines -n litmus
kubectl get chaosresults -n litmus
```

## 📊 Monitoring and Observability

### Accessing the Litmus Portal

```bash
# Port forward to access the portal
kubectl port-forward -n litmus svc/litmus-2-0-0-frontend-service 9091:9091

# Access the portal at: http://localhost:9091
```

### Metrics and Logs

```bash
# View chaos daemon logs
kubectl logs -n litmus -l app=chaos-daemon

# View experiment logs
kubectl logs -n litmus -l job-name=<experiment-job-name>

# Get metrics
kubectl get chaosresults -n litmus -o yaml
```

## 🛡️ Safety Guidelines

### Before Running Experiments

1. **Test in Staging**: Never run chaos experiments in production first
2. **Backup Data**: Ensure all critical data is backed up
3. **Monitor Closely**: Watch system metrics during experiments
4. **Have Rollback Plan**: Be ready to stop experiments immediately

### During Experiments

1. **Monitor System Health**: Watch for critical failures
2. **Check Metrics**: Monitor performance degradation
3. **Be Ready to Stop**: Have emergency stop procedures ready
4. **Document Everything**: Keep detailed records

### After Experiments

1. **Analyze Results**: Review experiment outcomes
2. **Update Documentation**: Document lessons learned
3. **Improve Resilience**: Implement improvements based on findings
4. **Plan Next Experiments**: Schedule follow-up tests

## 🔧 Troubleshooting

### Common Issues

#### Litmus Chaos Installation Fails

```bash
# Check cluster resources
kubectl describe nodes

# Check namespace
kubectl get namespace litmus

# Check CRDs
kubectl get crd | grep litmus
```

#### Chaos Daemon Not Starting

```bash
# Check pod status
kubectl get pods -n litmus -l app=chaos-daemon

# Check pod logs
kubectl logs -n litmus -l app=chaos-daemon

# Check service account
kubectl get serviceaccount chaos-daemon-sa -n litmus
```

#### Experiments Not Running

```bash
# Check chaos engine status
kubectl get chaosengines -n litmus

# Check chaos results
kubectl get chaosresults -n litmus

# Check experiment logs
kubectl logs -n litmus -l job-name=<experiment-job-name>
```

### Getting Help

1. **Check Logs**: Review pod and experiment logs
2. **Verify Configuration**: Check ConfigMaps and environment variables
3. **Test Connectivity**: Ensure network connectivity between components
4. **Review Documentation**: Check Litmus Chaos documentation
5. **Community Support**: Join Litmus Chaos community channels

## 🚀 Next Steps

After successful installation:

1. **Deploy Microservices**: Deploy your applications to the `microservices` namespace
2. **Create Custom Experiments**: Design experiments specific to your use cases
3. **Set Up Monitoring**: Configure Prometheus and Grafana for metrics
4. **Integrate with CI/CD**: Add chaos testing to your deployment pipeline
5. **Train Your Team**: Educate team members on chaos engineering practices

## 📚 Additional Resources

- [Litmus Chaos Documentation](https://docs.litmuschaos.io/)
- [Chaos Engineering Principles](https://principlesofchaos.org/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Helm Documentation](https://helm.sh/docs/)
