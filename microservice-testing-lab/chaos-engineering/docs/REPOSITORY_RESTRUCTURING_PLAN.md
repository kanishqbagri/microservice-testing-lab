# Repository Restructuring Plan

## 🎯 **Current Issues**

### **1. Experiment Storage Duplication**
```
❌ Current State:
/experiments/                           (Working experiments)
├── network/network-latency.yaml
└── service/pod-failure.yaml

/microservice-testing-lab/chaos-engineering/experiments/  (Empty)
├── data/           (empty)
├── infrastructure/ (empty) 
├── network/        (empty)
└── service/        (empty)
```

### **2. Inconsistent Configuration**
- `network-latency.yaml` still uses old `litmus-admin` service account
- `network-latency.yaml` targets wrong namespace (`microservices` vs `microservices-user-service`)

### **3. Scattered Documentation**
- Chaos framework docs in root directory
- Framework code in subdirectory
- No clear separation of concerns

---

## 🏗️ **Proposed Restructuring**

### **Option 1: Centralized Chaos Engineering (Recommended)**

```
microservice-testing-lab/
├── README.md
├── docker-compose.yml
├── pom.xml
├── microservices/                    # Application services
│   ├── user-service/
│   ├── product-service/
│   ├── order-service/
│   └── notification-service/
├── chaos-engineering/                # Centralized chaos framework
│   ├── README.md
│   ├── docs/
│   │   ├── CHAOS_FRAMEWORK_EXECUTION_FLOW.md
│   │   ├── CHAOS_FRAMEWORK_FLOW_DIAGRAM.md
│   │   ├── CHAOS_FRAMEWORK_TROUBLESHOOTING_SUMMARY.md
│   │   └── installation.md
│   ├── experiments/                  # All chaos experiments
│   │   ├── network/
│   │   │   ├── network-latency.yaml
│   │   │   ├── network-partition.yaml
│   │   │   └── network-loss.yaml
│   │   ├── service/
│   │   │   ├── pod-failure.yaml
│   │   │   ├── pod-cpu-hog.yaml
│   │   │   └── pod-memory-hog.yaml
│   │   ├── infrastructure/
│   │   │   ├── node-failure.yaml
│   │   │   ├── node-drain.yaml
│   │   │   └── node-taint.yaml
│   │   └── data/
│   │       ├── database-connection.yaml
│   │       └── data-corruption.yaml
│   ├── k8s/                         # Kubernetes resources
│   │   ├── daemonset/
│   │   │   └── chaos-daemon.yaml
│   │   ├── rbac/
│   │   │   └── chaos-rbac.yaml
│   │   └── monitoring/
│   │       └── chaos-monitoring.yaml
│   ├── scripts/
│   │   ├── setup/
│   │   │   ├── install-litmus.sh
│   │   │   └── deploy-chaos-daemon.sh
│   │   ├── experiments/
│   │   │   ├── run-pod-failure.sh
│   │   │   ├── run-network-latency.sh
│   │   │   └── run-all-experiments.sh
│   │   └── monitoring/
│   │       ├── monitor-experiments.sh
│   │       └── collect-results.sh
│   └── jarvis-core/                 # AI framework
│       ├── src/main/java/
│       ├── pom.xml
│       └── README.md
├── helm-charts/                     # Helm charts for services
│   ├── user-service/
│   ├── product-service/
│   ├── order-service/
│   └── notification-service/
├── dashboard/                       # Monitoring dashboard
│   ├── index.html
│   ├── executive-dashboard.html
│   └── js/
├── docs/                           # General documentation
│   ├── DEMO_GUIDE.md
│   ├── installation.md
│   └── demo-*.sh
└── scripts/                        # General scripts
    ├── generate-synthetic-data.sh
    └── test-jarvis-chaos-flow.sh
```

### **Option 2: Flat Structure (Alternative)**

```
microservice-testing-lab/
├── README.md
├── docker-compose.yml
├── pom.xml
├── microservices/                  # Application services
├── chaos-experiments/              # All chaos experiments
│   ├── network/
│   ├── service/
│   ├── infrastructure/
│   └── data/
├── chaos-k8s/                     # Chaos Kubernetes resources
├── chaos-scripts/                 # Chaos automation scripts
├── jarvis-core/                   # AI framework
├── helm-charts/                   # Service Helm charts
├── dashboard/                     # Monitoring dashboard
├── docs/                          # Documentation
└── scripts/                       # General scripts
```

---

## 🔧 **Implementation Steps**

### **Phase 1: Consolidate Experiments**

1. **Move working experiments to proper location**
   ```bash
   # Move from root to chaos-engineering
   mv experiments/* microservice-testing-lab/chaos-engineering/experiments/
   rmdir experiments
   ```

2. **Fix configuration inconsistencies**
   ```bash
   # Update network-latency.yaml
   - chaosServiceAccount: k8s-chaos-admin  # Fix service account
   - appns: 'microservices-user-service'   # Fix namespace
   ```

3. **Create missing experiment categories**
   ```bash
   # Create infrastructure experiments
   touch microservice-testing-lab/chaos-engineering/experiments/infrastructure/node-failure.yaml
   touch microservice-testing-lab/chaos-engineering/experiments/infrastructure/node-drain.yaml
   
   # Create data experiments  
   touch microservice-testing-lab/chaos-engineering/experiments/data/database-connection.yaml
   ```

### **Phase 2: Reorganize Documentation**

1. **Move chaos framework docs**
   ```bash
   mv CHAOS_FRAMEWORK_*.md microservice-testing-lab/chaos-engineering/docs/
   ```

2. **Update documentation references**
   ```bash
   # Update all script references to new paths
   # Update README files with new structure
   ```

### **Phase 3: Update Scripts and References**

1. **Update experiment paths in scripts**
   ```bash
   # Update install-litmus.sh
   # Update demo scripts
   # Update test scripts
   ```

2. **Update documentation**
   ```bash
   # Update README files
   # Update installation guides
   # Update demo guides
   ```

---

## 📋 **Migration Checklist**

### **Experiments**
- [ ] Move `experiments/network/network-latency.yaml` to `chaos-engineering/experiments/network/`
- [ ] Move `experiments/service/pod-failure.yaml` to `chaos-engineering/experiments/service/`
- [ ] Fix `network-latency.yaml` service account and namespace
- [ ] Create infrastructure experiments
- [ ] Create data experiments
- [ ] Remove empty root `experiments/` directory

### **Documentation**
- [ ] Move `CHAOS_FRAMEWORK_*.md` to `chaos-engineering/docs/`
- [ ] Update all documentation references
- [ ] Update README files
- [ ] Update installation guides

### **Scripts**
- [ ] Update `install-litmus.sh` experiment paths
- [ ] Update demo scripts experiment paths
- [ ] Update test scripts experiment paths
- [ ] Create experiment runner scripts

### **Kubernetes Resources**
- [ ] Move `k8s/daemonset/chaos-daemon.yaml` to `chaos-engineering/k8s/daemonset/`
- [ ] Create RBAC resources
- [ ] Create monitoring resources

---

## 🎯 **Benefits of Restructuring**

### **1. Clear Separation of Concerns**
- Chaos engineering isolated in dedicated directory
- Application services separate from testing framework
- Documentation co-located with code

### **2. Improved Maintainability**
- Single source of truth for experiments
- Consistent configuration across experiments
- Clear directory structure

### **3. Better Developer Experience**
- Intuitive directory layout
- Easy to find experiments and documentation
- Consistent naming conventions

### **4. Production Readiness**
- Proper RBAC configuration
- Monitoring and observability
- Automated experiment execution

---

## 🚀 **Next Steps**

1. **Choose restructuring approach** (Option 1 recommended)
2. **Execute Phase 1** (Consolidate experiments)
3. **Execute Phase 2** (Reorganize documentation)
4. **Execute Phase 3** (Update scripts and references)
5. **Test all functionality** after restructuring
6. **Update CI/CD pipelines** if needed

This restructuring will create a clean, maintainable, and production-ready chaos engineering framework.
