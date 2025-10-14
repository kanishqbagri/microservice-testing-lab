# Chaos Engineering Framework

## 🎯 **Overview**

This directory contains the complete chaos engineering framework for testing microservice resilience through intelligent AI-driven experiments.

## 📁 **Directory Structure**

```
chaos-engineering/
├── README.md                           # This file
├── docs/                              # Documentation
│   ├── CHAOS_FRAMEWORK_EXECUTION_FLOW.md
│   ├── CHAOS_FRAMEWORK_FLOW_DIAGRAM.md
│   ├── CHAOS_FRAMEWORK_TROUBLESHOOTING_SUMMARY.md
│   ├── REPOSITORY_RESTRUCTURING_PLAN.md
│   └── installation.md
├── experiments/                       # Chaos experiment definitions
│   ├── network/                      # Network chaos experiments
│   │   └── network-latency.yaml
│   ├── service/                      # Service chaos experiments
│   │   └── pod-failure.yaml
│   ├── infrastructure/               # Infrastructure chaos experiments
│   │   └── node-failure.yaml
│   └── data/                         # Data chaos experiments
│       └── database-connection.yaml
├── k8s/                             # Kubernetes resources
│   └── daemonset/
│       └── chaos-daemon.yaml
├── scripts/                         # Automation scripts
│   ├── setup/
│   │   └── install-litmus.sh
│   └── monitoring/
└── jarvis-core/                     # AI framework (if present)
```

## 🚀 **Quick Start**

### **1. Install Litmus Chaos**
```bash
./scripts/setup/install-litmus.sh
```

### **2. Run a Chaos Experiment**
```bash
# Pod failure experiment
kubectl apply -f experiments/service/pod-failure.yaml

# Network latency experiment
kubectl apply -f experiments/network/network-latency.yaml
```

### **3. Monitor Experiments**
```bash
# Check experiment status
kubectl get chaosengines -n litmus
kubectl get chaosresults -n litmus

# Watch experiment progress
kubectl get chaosengines -n litmus -w
```

## 🧪 **Available Experiments**

### **Service Experiments**
- **Pod Failure**: Terminates pods to test restart resilience
- **Pod CPU Hog**: Simulates CPU stress to test auto-scaling
- **Pod Memory Hog**: Simulates memory pressure to test resource limits

### **Network Experiments**
- **Network Latency**: Injects network latency to test timeout handling
- **Network Partition**: Simulates network partitions between services
- **Network Loss**: Simulates packet loss to test retry mechanisms

### **Infrastructure Experiments**
- **Node Failure**: Drains nodes to test multi-node resilience
- **Node Taint**: Taints nodes to test pod rescheduling

### **Data Experiments**
- **Database Connection**: Simulates database connection failures
- **Data Corruption**: Tests data integrity and recovery mechanisms

## 🔧 **Configuration**

### **Service Account**
All experiments use the `k8s-chaos-admin` service account with appropriate RBAC permissions.

### **Namespaces**
- **Litmus**: `litmus` namespace for chaos framework components
- **Target Services**: 
  - `microservices-product-service`
  - `microservices-user-service`
  - `microservices-order-service`
  - `microservices-notification-service`

### **Safety Controls**
- **Blast Radius**: Limited to single service at a time
- **Duration**: Configurable experiment duration (default: 30-60 seconds)
- **Grace Period**: Graceful pod termination (default: 10 seconds)
- **Auto Rollback**: Automatic rollback on critical failures

## 📊 **Monitoring & Observability**

### **Real-time Monitoring**
```bash
# Monitor experiment execution
kubectl get chaosengines -n litmus -w

# Check experiment results
kubectl describe chaosresult <experiment-name> -n litmus

# View experiment logs
kubectl logs -n litmus -l app=<experiment-name>
```

### **Metrics Collection**
- **Recovery Time**: Time to restore service after disruption
- **Pod Recreation**: Time for new pods to become ready
- **Traffic Redistribution**: Load balancer response to pod failures
- **System Impact**: Performance degradation during experiments

## 🤖 **AI Integration**

The framework integrates with Jarvis AI for:
- **Natural Language Commands**: "Run chaos test on order-service"
- **Intelligent Experiment Selection**: AI chooses appropriate experiments
- **Risk Assessment**: Evaluates potential impact before execution
- **Automated Analysis**: Provides insights and recommendations

## 🛡️ **Safety Guidelines**

### **Before Running Experiments**
1. **Verify Prerequisites**: Ensure monitoring is active
2. **Check System Health**: Run health checks before experiments
3. **Notify Team**: Alert on-call teams before execution
4. **Have Rollback Ready**: Prepare immediate rollback procedures

### **During Experiments**
1. **Monitor Continuously**: Watch system metrics and logs
2. **Be Ready to Stop**: Have emergency stop procedures ready
3. **Document Impact**: Record any unexpected behavior
4. **Validate Recovery**: Ensure complete system recovery

### **After Experiments**
1. **Analyze Results**: Review experiment outcomes and metrics
2. **Update Documentation**: Document lessons learned
3. **Improve Resilience**: Implement improvements based on findings
4. **Share Insights**: Communicate results with the team

## 🔍 **Troubleshooting**

### **Common Issues**
- **Image Pull Errors**: Use `litmus-core` instead of full `litmus`
- **Service Account Issues**: Ensure `k8s-chaos-admin` exists
- **Namespace Problems**: Verify target namespaces contain expected pods
- **Experiment Failures**: Check ChaosExperiment definitions are installed

### **Debug Commands**
```bash
# Check Litmus installation
kubectl get pods -n litmus
kubectl get chaosexperiments -n litmus

# Debug experiment failures
kubectl describe chaosengine <name> -n litmus
kubectl describe chaosresult <name> -n litmus
kubectl logs -n litmus -l app=<experiment-name>
```

## 📚 **Documentation**

- **[Execution Flow](docs/CHAOS_FRAMEWORK_EXECUTION_FLOW.md)**: Detailed technical flow
- **[Flow Diagram](docs/CHAOS_FRAMEWORK_FLOW_DIAGRAM.md)**: Visual architecture
- **[Troubleshooting](docs/CHAOS_FRAMEWORK_TROUBLESHOOTING_SUMMARY.md)**: Common issues and solutions
- **[Installation](docs/installation.md)**: Setup and configuration guide

## 🎯 **Success Metrics**

### **Technical Metrics**
- **Experiment Success Rate**: > 95%
- **Pod Recovery Time**: < 30 seconds
- **System Availability**: > 99.9% during experiments
- **False Positive Rate**: < 5%

### **Business Metrics**
- **Resilience Validation**: Confirmed through real disruptions
- **Recovery Capability**: Validated through pod recreation
- **Service Continuity**: Maintained during chaos experiments
- **User Experience**: Minimal impact during testing

## 🚀 **Next Steps**

1. **Expand Experiment Library**: Add more chaos experiment types
2. **Enhance Monitoring**: Integrate with Prometheus/Grafana
3. **Automate Scheduling**: Implement scheduled chaos experiments
4. **Improve AI Integration**: Enhance natural language processing
5. **Production Deployment**: Deploy to production environments

---

**Note**: This framework is designed for testing microservice resilience in controlled environments. Always follow safety guidelines and ensure proper monitoring before running experiments.
