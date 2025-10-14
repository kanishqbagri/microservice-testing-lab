# Phase 2 Implementation Plan - Chaos Experiments Enhancement

## 🎯 **Current State vs. Target State**

### **Current Coverage (Phase 1)**
- ✅ **4 experiments** implemented (13.3% of available)
- ✅ **4 categories** covered (Service, Network, Infrastructure, Data)
- ✅ **3 services** covered (product, user, order)
- ✅ **Basic functionality** validated and working

### **Phase 2 Target**
- 🎯 **18 experiments** total (60% of available)
- 🎯 **All 4 services** covered (including notification-service)
- 🎯 **Production-ready** chaos testing framework
- 🎯 **Comprehensive resilience** validation

---

## 🚀 **Phase 2 Implementation Strategy**

### **Approach: Incremental Enhancement**
1. **Start with High-Impact Experiments**: Focus on critical service resilience
2. **Template-Based Development**: Create reusable experiment templates
3. **Service-by-Service Coverage**: Ensure all services have comprehensive testing
4. **Automated Validation**: Build testing framework for all experiments

---

## 📋 **Detailed Implementation Plan**

### **Week 1: Service Resilience Experiments (5 experiments)**

#### **1.1 Pod CPU Stress Testing**
```yaml
# File: experiments/service/pod-cpu-hog.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: pod-cpu-hog
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-product-service'
    applabel: 'app=product-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-cpu-hog
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: CPU_CORES
          value: '2'
        - name: TARGET_PODS
          value: ''
```

#### **1.2 Pod Memory Stress Testing**
```yaml
# File: experiments/service/pod-memory-hog.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: pod-memory-hog
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-order-service'
    applabel: 'app=order-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-memory-hog
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: MEMORY_CONSUMPTION
          value: '500'
        - name: TARGET_PODS
          value: ''
```

#### **1.3 Container Kill Testing**
```yaml
# File: experiments/service/container-kill.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: container-kill
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-user-service'
    applabel: 'app=user-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: container-kill
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: TARGET_CONTAINER
          value: 'user-service'
        - name: CONTAINER_RUNTIME
          value: 'docker'
```

#### **1.4 Pod I/O Stress Testing**
```yaml
# File: experiments/service/pod-io-stress.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: pod-io-stress
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-product-service'
    applabel: 'app=product-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-io-stress
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: FILESYSTEM_UTILIZATION_PERCENTAGE
          value: '80'
        - name: TARGET_PODS
          value: ''
```

#### **1.5 Pod DNS Spoof Testing**
```yaml
# File: experiments/service/pod-dns-spoof.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: pod-dns-spoof
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-user-service'
    applabel: 'app=user-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-dns-spoof
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: SPOOF_MAP
          value: 'database-service:127.0.0.1'
        - name: TARGET_PODS
          value: ''
```

### **Week 2: Network Resilience Experiments (4 experiments)**

#### **2.1 Network Packet Loss**
```yaml
# File: experiments/network/network-loss.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: network-loss
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-order-service'
    applabel: 'app=order-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-network-loss
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: NETWORK_PACKET_LOSS_PERCENTAGE
          value: '10'
        - name: TARGET_PODS
          value: ''
```

#### **2.2 Network Partition**
```yaml
# File: experiments/network/network-partition.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: network-partition
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-gateway-service'
    applabel: 'app=gateway-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-network-partition
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: PARTITION_ACTION
          value: 'REJECT'
        - name: TARGET_PODS
          value: ''
```

#### **2.3 HTTP Latency Testing**
```yaml
# File: experiments/network/http-latency.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: http-latency
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-gateway-service'
    applabel: 'app=gateway-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-http-latency
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: LATENCY
          value: '2000'
        - name: TARGET_PODS
          value: ''
```

#### **2.4 Network Corruption**
```yaml
# File: experiments/network/network-corruption.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: network-corruption
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-product-service'
    applabel: 'app=product-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-network-corruption
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: NETWORK_CORRUPTION_PERCENTAGE
          value: '5'
        - name: TARGET_PODS
          value: ''
```

### **Week 3: Infrastructure Resilience Experiments (5 experiments)**

#### **3.1 Node CPU Stress**
```yaml
# File: experiments/infrastructure/node-cpu-hog.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: node-cpu-hog
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-user-service'
    applabel: 'app=user-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: node-cpu-hog
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: CPU_CORES
          value: '2'
        - name: NODE_LABEL
          value: 'chaos-enabled=true'
```

#### **3.2 Node Memory Stress**
```yaml
# File: experiments/infrastructure/node-memory-hog.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: node-memory-hog
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-order-service'
    applabel: 'app=order-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: node-memory-hog
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: MEMORY_CONSUMPTION
          value: '1000'
        - name: NODE_LABEL
          value: 'chaos-enabled=true'
```

#### **3.3 Node Taint Testing**
```yaml
# File: experiments/infrastructure/node-taint.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: node-taint
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-product-service'
    applabel: 'app=product-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: node-taint
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: TAINT_LABEL
          value: 'chaos=inject'
        - name: NODE_LABEL
          value: 'chaos-enabled=true'
```

#### **3.4 Kubelet Service Kill**
```yaml
# File: experiments/infrastructure/kubelet-service-kill.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: kubelet-service-kill
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-user-service'
    applabel: 'app=user-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: kubelet-service-kill
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: TARGET_NODE
          value: ''
        - name: NODE_LABEL
          value: 'chaos-enabled=true'
```

#### **3.5 Disk Fill Testing**
```yaml
# File: experiments/infrastructure/disk-fill.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: disk-fill
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-product-service'
    applabel: 'app=product-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: disk-fill
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: FILL_PERCENTAGE
          value: '80'
        - name: TARGET_PODS
          value: ''
```

### **Week 4: Data & Storage Resilience Experiments (4 experiments)**

#### **4.1 Disk Loss Testing**
```yaml
# File: experiments/data/disk-loss.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: disk-loss
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-product-service'
    applabel: 'app=product-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: disk-loss
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: TARGET_PODS
          value: ''
        - name: VOLUME_MOUNT_PATH
          value: '/var/lib/data'
```

#### **4.2 HTTP Status Code Testing**
```yaml
# File: experiments/data/http-status-code.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: http-status-code
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-gateway-service'
    applabel: 'app=gateway-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-http-status-code
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: STATUS_CODE
          value: '500'
        - name: TARGET_PODS
          value: ''
```

#### **4.3 Network Duplication**
```yaml
# File: experiments/data/network-duplication.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: network-duplication
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-user-service'
    applabel: 'app=user-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-network-duplication
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '30'
        - name: NETWORK_DUPLICATION_PERCENTAGE
          value: '10'
        - name: TARGET_PODS
          value: ''
```

#### **4.4 Notification Service Testing**
```yaml
# File: experiments/service/notification-memory-hog.yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: notification-memory-hog
  namespace: litmus
spec:
  appinfo:
    appns: 'microservices-notification-service'
    applabel: 'app=notification-service'
    appkind: 'deployment'
  chaosServiceAccount: k8s-chaos-admin
  experiments:
  - name: pod-memory-hog
    spec:
      components:
        env:
        - name: TOTAL_CHAOS_DURATION
          value: '60'
        - name: MEMORY_CONSUMPTION
          value: '300'
        - name: TARGET_PODS
          value: ''
```

---

## 🔧 **Implementation Tools & Scripts**

### **1. Experiment Generator Script**
```bash
#!/bin/bash
# File: scripts/generate-experiments.sh

EXPERIMENT_TYPES=("service" "network" "infrastructure" "data")
SERVICES=("product-service" "user-service" "order-service" "notification-service")

for type in "${EXPERIMENT_TYPES[@]}"; do
    for service in "${SERVICES[@]}"; do
        echo "Generating $type experiments for $service..."
        # Generate experiment YAML files
    done
done
```

### **2. Experiment Validator Script**
```bash
#!/bin/bash
# File: scripts/validate-experiments.sh

EXPERIMENTS_DIR="microservice-testing-lab/chaos-engineering/experiments"

for experiment in $(find $EXPERIMENTS_DIR -name "*.yaml"); do
    echo "Validating $experiment..."
    kubectl apply --dry-run=client -f $experiment
    if [ $? -eq 0 ]; then
        echo "✅ $experiment is valid"
    else
        echo "❌ $experiment has errors"
    fi
done
```

### **3. Experiment Runner Script**
```bash
#!/bin/bash
# File: scripts/run-experiment-suite.sh

EXPERIMENTS=(
    "service/pod-cpu-hog"
    "service/pod-memory-hog"
    "network/network-loss"
    "infrastructure/node-cpu-hog"
)

for experiment in "${EXPERIMENTS[@]}"; do
    echo "Running experiment: $experiment"
    kubectl apply -f "microservice-testing-lab/chaos-engineering/experiments/$experiment.yaml"
    sleep 30
    kubectl get chaosresults -n litmus
done
```

---

## 📊 **Success Metrics & Validation**

### **Coverage Targets**
- **Week 1**: 9/30 experiments (30%)
- **Week 2**: 13/30 experiments (43%)
- **Week 3**: 18/30 experiments (60%)
- **Week 4**: 22/30 experiments (73%)

### **Service Coverage**
- **Week 1**: 3/4 services (75%)
- **Week 2**: 4/4 services (100%)
- **Week 3**: 4/4 services (100%)
- **Week 4**: 4/4 services (100%)

### **Validation Criteria**
- ✅ All experiments apply successfully
- ✅ All experiments execute without errors
- ✅ All services recover within 30 seconds
- ✅ All experiments produce valid ChaosResults
- ✅ Documentation updated for all experiments

---

## 🚀 **Next Steps**

1. **Start Week 1**: Implement 5 service resilience experiments
2. **Create Templates**: Develop reusable experiment templates
3. **Build Automation**: Create validation and testing scripts
4. **Document Everything**: Update documentation for each experiment
5. **Test Thoroughly**: Validate all experiments work correctly

This implementation plan will systematically expand the chaos engineering framework from 4 to 22 experiments, providing comprehensive resilience testing across all services and failure scenarios.
