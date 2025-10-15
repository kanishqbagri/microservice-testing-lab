# 🎭 Chaos Engineering Live Demo Guide

## Overview

This guide provides comprehensive instructions for running the Chaos Engineering Live Demo, which showcases real-time resilience testing of microservices through controlled chaos experiments.

## 🚀 Quick Start

### Prerequisites

1. **Kubernetes Cluster**: Ensure you have a running Kubernetes cluster
2. **kubectl**: Install and configure kubectl to connect to your cluster
3. **Litmus Chaos**: Install Litmus Chaos framework
4. **Microservices**: Deploy the microservice architecture

### Running the Demo

```bash
# Navigate to the chaos engineering directory
cd microservice-testing-lab/chaos-engineering

# Make the demo script executable (if not already)
chmod +x chaos-demo-live.sh

# Run the live demo
./chaos-demo-live.sh
```

## 📋 Demo Features

### 🎯 Interactive Menu System
- **Experiment Selection**: Choose from available chaos experiments
- **Service Status**: View real-time service health
- **Live Monitoring**: Watch experiments execute in real-time
- **Result Analysis**: Get detailed experiment outcomes

### 🔍 Available Experiments

#### 1. Pod Failure Experiment
- **Purpose**: Test pod restart resilience
- **Target**: Product Service
- **Duration**: 30 seconds
- **What it does**: Terminates pods to test Kubernetes' ability to restart them

#### 2. Network Latency Experiment
- **Purpose**: Test timeout handling under network stress
- **Target**: User Service
- **Duration**: 30 seconds
- **What it does**: Injects 100ms network latency with 10ms jitter

#### 3. Database Connection Experiment
- **Purpose**: Test database failure resilience
- **Target**: Order Service
- **Duration**: 30 seconds
- **What it does**: Simulates database connection failures

#### 4. Node Failure Experiment
- **Purpose**: Test multi-node resilience
- **Target**: Notification Service
- **Duration**: 30 seconds
- **What it does**: Drains nodes to test pod rescheduling

## 🛠️ Supporting Scripts

### Service Validation Script
```bash
# Validate all services before running experiments
./scripts/validate-services.sh

# Check only prerequisites
./scripts/validate-services.sh prerequisites

# Generate detailed health report
./scripts/validate-services.sh report
```

### Experiment Monitoring Script
```bash
# Monitor a specific experiment
./scripts/monitor-experiment.sh <experiment-name> <target-namespace> [interval]

# Example: Monitor pod failure experiment
./scripts/monitor-experiment.sh pod-failure microservices-product-service 2
```

## 📊 Real-time Monitoring Features

### Live Dashboard
- **Experiment Status**: Real-time engine and experiment status
- **Service Metrics**: Pod counts, running status, restart counts
- **Impact Analysis**: Service degradation and recovery metrics
- **Result Tracking**: Verdict and failure step identification

### Metrics Tracked
- **Pod Health**: Running, ready, and restart counts
- **Service Availability**: Endpoint health and response times
- **Recovery Time**: Time to restore service after disruption
- **System Impact**: Performance degradation during experiments

## 🎭 Demo Flow

### 1. Pre-flight Checks
The demo automatically validates:
- Kubernetes cluster connectivity
- Litmus Chaos framework installation
- Microservice deployment status
- Required service accounts and permissions

### 2. Experiment Selection
Choose from available experiments:
- Each experiment shows description and target service
- Real-time service status display
- Safety controls and blast radius information

### 3. Live Execution
- **Real-time Monitoring**: Watch experiment progress
- **Service Impact**: See immediate effects on target services
- **Recovery Tracking**: Monitor automatic recovery processes
- **Interactive Control**: Stop monitoring and return to menu

### 4. Result Analysis
- **Verdict Display**: Pass/Fail with detailed reasoning
- **Impact Summary**: Service degradation and recovery metrics
- **Lessons Learned**: Key insights from the experiment
- **Recommendations**: Suggested improvements

## 🔧 Configuration

### Experiment Parameters
Experiments can be customized by editing the YAML files in the `experiments/` directory:

```yaml
# Example: Pod Failure Experiment
spec:
  experiments:
  - name: pod-delete
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'  # Duration in seconds
        - name: CHAOS_INTERVAL
          value: '10'  # Interval between failures
        - name: GRACE_PERIOD
          value: '-1'  # Grace period for pod termination
```

### Monitoring Configuration
Adjust monitoring parameters in the demo script:

```bash
# Monitoring interval (seconds)
MONITORING_INTERVAL=2

# Maximum monitoring time (seconds)
MAX_MONITORING_TIME=300

# Experiment timeout (seconds)
EXPERIMENT_TIMEOUT=60
```

## 🛡️ Safety Features

### Built-in Safety Controls
- **Blast Radius**: Limited to single service at a time
- **Duration Limits**: Configurable experiment duration
- **Grace Periods**: Graceful pod termination
- **Auto Rollback**: Automatic cleanup on critical failures

### Pre-flight Validation
- **Service Health**: Ensures target services are healthy
- **Resource Availability**: Checks cluster resources
- **Permission Validation**: Verifies required RBAC permissions
- **Namespace Verification**: Confirms target namespaces exist

## 📈 Success Metrics

### Technical Metrics
- **Experiment Success Rate**: > 95%
- **Pod Recovery Time**: < 30 seconds
- **System Availability**: > 99.9% during experiments
- **False Positive Rate**: < 5%

### Business Metrics
- **Resilience Validation**: Confirmed through real disruptions
- **Recovery Capability**: Validated through pod recreation
- **Service Continuity**: Maintained during chaos experiments
- **User Experience**: Minimal impact during testing

## 🚨 Troubleshooting

### Common Issues

#### 1. Prerequisites Not Met
```bash
# Check cluster connectivity
kubectl cluster-info

# Verify litmus installation
kubectl get pods -n litmus

# Check service accounts
kubectl get serviceaccount -n litmus
```

#### 2. Experiment Failures
```bash
# Check experiment status
kubectl get chaosengines -n litmus

# View experiment logs
kubectl logs -n litmus -l app=<experiment-name>

# Check experiment results
kubectl describe chaosresult <experiment-name> -n litmus
```

#### 3. Service Issues
```bash
# Check service status
kubectl get pods -n <service-namespace>

# View service logs
kubectl logs -n <service-namespace> <pod-name>

# Check service endpoints
kubectl get endpoints -n <service-namespace>
```

### Debug Commands
```bash
# Validate services
./scripts/validate-services.sh report

# Monitor specific experiment
./scripts/monitor-experiment.sh <experiment-name> <namespace>

# Check chaos framework
kubectl get chaosexperiments -n litmus
kubectl get chaosengines -n litmus
kubectl get chaosresults -n litmus
```

## 📚 Additional Resources

### Documentation
- [Chaos Engineering Framework README](README.md)
- [Installation Guide](docs/installation.md)
- [Execution Flow](docs/CHAOS_FRAMEWORK_EXECUTION_FLOW.md)
- [Flow Diagram](docs/CHAOS_FRAMEWORK_FLOW_DIAGRAM.md)

### Scripts
- `chaos-demo-live.sh` - Main demo script
- `scripts/validate-services.sh` - Service validation
- `scripts/monitor-experiment.sh` - Experiment monitoring
- `scripts/setup/install-litmus.sh` - Litmus installation

### Experiment Files
- `experiments/service/pod-failure.yaml` - Pod failure experiment
- `experiments/network/network-latency.yaml` - Network latency experiment
- `experiments/data/database-connection.yaml` - Database failure experiment
- `experiments/infrastructure/node-failure.yaml` - Node failure experiment

## 🎯 Best Practices

### Before Running Experiments
1. **Verify Prerequisites**: Ensure all services are healthy
2. **Check System Health**: Run health checks before experiments
3. **Notify Team**: Alert on-call teams before execution
4. **Have Rollback Ready**: Prepare immediate rollback procedures

### During Experiments
1. **Monitor Continuously**: Watch system metrics and logs
2. **Be Ready to Stop**: Have emergency stop procedures ready
3. **Document Impact**: Record any unexpected behavior
4. **Validate Recovery**: Ensure complete system recovery

### After Experiments
1. **Analyze Results**: Review experiment outcomes and metrics
2. **Update Documentation**: Document lessons learned
3. **Improve Resilience**: Implement improvements based on findings
4. **Share Insights**: Communicate results with the team

## 🚀 Next Steps

1. **Expand Experiment Library**: Add more chaos experiment types
2. **Enhance Monitoring**: Integrate with Prometheus/Grafana
3. **Automate Scheduling**: Implement scheduled chaos experiments
4. **Improve AI Integration**: Enhance natural language processing
5. **Production Deployment**: Deploy to production environments

---

**Note**: This demo is designed for testing microservice resilience in controlled environments. Always follow safety guidelines and ensure proper monitoring before running experiments.
