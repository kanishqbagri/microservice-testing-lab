# Chaos Engineering Framework

## 🎯 Overview
A comprehensive chaos engineering framework built with Litmus Chaos for testing microservice resilience and disaster recovery capabilities.

## 🏗️ Architecture
```
┌─────────────────────────────────────────────────────────────────┐
│                    CHAOS ENGINEERING FRAMEWORK                  │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────┐ │
│  │   Chaos     │  │   Chaos     │  │   Chaos     │  │  Chaos  │ │
│  │  Controller │  │  Executor   │  │  Monitor    │  │ Reporter│ │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────┘ │
├─────────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────┐ │
│  │  Network    │  │  Service    │  │  Resource   │  │  Data   │ │
│  │  Chaos      │  │  Chaos      │  │  Chaos      │  │  Chaos  │ │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## 📁 Project Structure
```
chaos-engineering/
├── k8s/                          # Kubernetes manifests
│   ├── litmus/                   # Litmus Chaos installation
│   ├── daemonset/               # Chaos DaemonSet configuration
│   ├── experiments/             # Chaos experiment templates
│   └── monitoring/              # Monitoring setup
├── experiments/                  # Chaos experiment definitions
│   ├── network/                 # Network chaos scenarios
│   ├── service/                 # Service chaos scenarios
│   ├── infrastructure/          # Infrastructure chaos scenarios
│   └── data/                    # Data chaos scenarios
├── scripts/                      # Automation scripts
│   ├── setup/                   # Setup and installation scripts
│   ├── monitoring/              # Monitoring scripts
│   └── cleanup/                 # Cleanup scripts
└── docs/                         # Documentation
```

## 🚀 Quick Start

### Prerequisites
- Kubernetes cluster (v1.19+)
- kubectl configured
- Helm (optional)

### Installation
```bash
# 1. Install Litmus Chaos
./scripts/setup/install-litmus.sh

# 2. Deploy Chaos DaemonSet
kubectl apply -f k8s/daemonset/

# 3. Verify installation
kubectl get pods -n litmus
```

### Running Chaos Experiments
```bash
# Network chaos
kubectl apply -f experiments/network/network-latency.yaml

# Service chaos
kubectl apply -f experiments/service/pod-failure.yaml

# Infrastructure chaos
kubectl apply -f experiments/infrastructure/node-failure.yaml
```

## 🔧 Configuration

### Chaos Categories
1. **Network Chaos**
   - Latency injection
   - Packet loss
   - Bandwidth limitation
   - Connection severing

2. **Service Chaos**
   - Pod failure
   - Service restart
   - Resource exhaustion
   - Dependency failure

3. **Infrastructure Chaos**
   - Node failure
   - Resource scaling
   - Configuration changes

4. **Data Chaos**
   - Data corruption
   - Data loss
   - Data inconsistency

## 📊 Monitoring

### Metrics
- System health during chaos
- Recovery time measurement
- Impact analysis
- Performance degradation

### Dashboards
- Real-time chaos monitoring
- Historical experiment data
- System resilience metrics

## 🛡️ Safety Controls

### Blast Radius Control
- Service-level targeting
- Time-based limits
- Impact level control

### Rollback Mechanisms
- Automatic rollback based on metrics
- Manual emergency stop
- Time-based maximum duration

## 🔗 Integration

### Jarvis AI Integration
- AI-powered chaos decisions
- Intelligent rollback
- Pattern recognition

### CI/CD Integration
- Automated chaos testing
- Quality gates
- Deployment validation

## 📈 Performance

### Resource Requirements
- **Memory**: ~200MB per node
- **CPU**: ~150m per node
- **Storage**: Minimal (configs only)

### Scalability
- Supports 5-50 microservices
- Multi-node cluster support
- Horizontal scaling capability

## 🚨 Safety Guidelines

1. **Start Small**: Begin with non-critical services
2. **Monitor Closely**: Watch system metrics during experiments
3. **Have Rollback Plan**: Always be ready to stop experiments
4. **Test in Staging**: Never run chaos experiments in production first
5. **Document Everything**: Keep detailed records of all experiments

## 📚 Documentation

- [Installation Guide](docs/installation.md)
- [Experiment Templates](docs/experiments.md)
- [Monitoring Setup](docs/monitoring.md)
- [Safety Guidelines](docs/safety.md)
- [Troubleshooting](docs/troubleshooting.md)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add your chaos experiments
4. Test thoroughly
5. Submit a pull request

## 📄 License

MIT License - see LICENSE file for details