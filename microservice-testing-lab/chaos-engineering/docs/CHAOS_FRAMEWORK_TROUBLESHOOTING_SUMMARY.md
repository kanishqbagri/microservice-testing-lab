# Chaos Framework Troubleshooting Summary

## 🚨 **Installation Errors Encountered & Solutions**

### **Error 1: Chart Not Found**
```
Error: INSTALLATION FAILED: chart "litmus-2-0-0" matching not found in litmuschaos index
```

**Root Cause**: Outdated chart name in installation script
- Script was using `litmus-2-0-0` (non-existent)
- Current chart name is `litmus`

**Solution**:
```bash
# Fixed in install-litmus.sh
helm install $LITMUS_CHART_NAME litmuschaos/litmus \
    --namespace $LITMUS_NAMESPACE \
    --set litmusCRDs.enabled=true \
    --set litmusOperator.enabled=true \
    --set litmusPortal.enabled=true
```

---

### **Error 2: Release Name Conflict**
```
Error: INSTALLATION FAILED: cannot re-use a name that is still in use
```

**Root Cause**: Previous failed Helm release still existed
- Failed `litmuschaos` release in `litmus` namespace
- Helm couldn't reuse the same release name

**Solution**:
```bash
# Clean up existing installation
helm uninstall litmuschaos -n litmus
kubectl delete namespace litmus

# Fresh installation
helm install litmuschaos litmuschaos/litmus \
    --namespace litmus --create-namespace
```

---

### **Error 3: Context Deadline Exceeded**
```
Error: INSTALLATION FAILED: context deadline exceeded
```

**Root Cause**: MongoDB image pull failures causing installation timeout
- `docker.io/bitnami/os-shell:12-debian-12-r47` not found
- `docker.io/bitnami/mongodb:5.0.8-debian-10-r24` pull issues
- Installation waited 300s timeout for pods that couldn't start

**Solution**:
```bash
# Install core components only (no MongoDB/Portal)
helm install litmuschaos litmuschaos/litmus-core \
    --namespace litmus --create-namespace \
    --set litmusCRDs.enabled=true \
    --set litmusOperator.enabled=true
```

---

### **Error 4: Chaos Resources Not Found**
```
Warning: ChaosResourcesOperationFailed (chaos start) Unable to get chaos resources
```

**Root Cause**: Chaos experiments not installed
- ChaosEngine created but no ChaosExperiment definitions available
- `pod-delete` experiment not found

**Solution**:
```bash
# Install chaos experiments
helm install litmuschaos-experiments litmuschaos/kubernetes-chaos \
    --namespace litmus
```

---

### **Error 5: Service Account Mismatch**
```
Warning: ChaosResourcesOperationFailed (chaos start) Unable to get chaos resources
```

**Root Cause**: Wrong service account reference
- ChaosEngine looking for `litmus-admin`
- Actual service account was `k8s-chaos-admin`

**Solution**:
```yaml
# Fixed in pod-failure.yaml
spec:
  chaosServiceAccount: k8s-chaos-admin  # Changed from litmus-admin
```

---

### **Error 6: Wrong Target Namespace**
```
Warning: ChaosResourcesOperationFailed (chaos start) Unable to get chaos resources
```

**Root Cause**: Experiment targeting non-existent namespace
- ChaosEngine targeting `microservices` namespace
- Actual pods in `microservices-product-service` namespace

**Solution**:
```yaml
# Fixed in pod-failure.yaml
spec:
  appinfo:
    appns: 'microservices-product-service'  # Changed from microservices
    applabel: 'app=product-service'
    appkind: 'deployment'
```

---

### **Error 7: Target Pod Selection Error**
```
Error Code: TARGET_SELECTION_ERROR
Reason: "pod doesn't exist set by TARGET_PODS ENV"
```

**Root Cause**: Incorrect TARGET_PODS configuration
- TARGET_PODS set to `'1'` (looking for pod named "1")
- Should be empty for label-based selection

**Solution**:
```yaml
# Fixed in pod-failure.yaml
env:
- name: TARGET_PODS
  value: ''  # Changed from '1' to empty string
```

---

## 🔧 **Final Working Configuration**

### **Step 1: Install Litmus Core**
```bash
helm install litmuschaos litmuschaos/litmus-core \
    --namespace litmus --create-namespace \
    --set litmusCRDs.enabled=true \
    --set litmusOperator.enabled=true \
    --wait --timeout=300s
```

### **Step 2: Install Chaos Experiments**
```bash
helm install litmuschaos-experiments litmuschaos/kubernetes-chaos \
    --namespace litmus
```

### **Step 3: Verify Installation**
```bash
# Check CRDs
kubectl get crd | grep litmus

# Check experiments
kubectl get chaosexperiments -n litmus

# Check operator
kubectl get pods -n litmus
```

### **Step 4: Run Chaos Experiment**
```bash
kubectl apply -f experiments/service/pod-failure.yaml
```

---

## 📊 **Verification Results**

### **Successful Pod Disruption**
```bash
# Before experiment
NAME                              READY   STATUS    RESTARTS      AGE
product-service-9879b496f-8mk68   2/2     Running   3 (19d ago)   25d

# After experiment
NAME                              READY   STATUS    RESTARTS   AGE
product-service-9879b496f-ctmvl   2/2     Running   0          2m11s
```

### **Chaos Result Status**
```yaml
Status:
  Experiment Status:
    Phase:                     Completed
    Probe Success Percentage:  100
    Verdict:                   Pass
  History:
    Failed Runs:   0
    Passed Runs:   1
    Stopped Runs:  0
```

---

## 🎯 **Key Lessons Learned**

1. **Use Core Components**: Avoid MongoDB dependencies for initial setup
2. **Install Experiments Separately**: Chaos experiments are separate from core operator
3. **Verify Service Accounts**: Ensure correct RBAC permissions
4. **Check Namespaces**: Verify target namespaces exist and contain expected pods
5. **Configure TARGET_PODS**: Use empty string for label-based pod selection
6. **Monitor Installation**: Watch for image pull errors and resource constraints
7. **Clean Failed Installations**: Remove failed releases before retrying

---

## 🚀 **Production Recommendations**

1. **Pre-flight Checks**: Verify all prerequisites before installation
2. **Staged Installation**: Install core first, then experiments
3. **Resource Monitoring**: Monitor cluster resources during installation
4. **Backup Strategy**: Have rollback procedures ready
5. **Testing Environment**: Test in non-production first
6. **Documentation**: Keep configuration changes documented
7. **Monitoring**: Set up alerts for chaos experiment failures
