# order-service with PostgreSQL Sidecar

This Helm chart deploys the order-service with a PostgreSQL database as a sidecar container in the same pod.

## Architecture

The deployment consists of:
- **order-service Container**: Spring Boot application running on port 8081
- **PostgreSQL Sidecar Container**: Database running on port 5432 within the same pod
- **Persistent Volume**: For database data persistence
- **Service**: Exposes the user service to other pods

## Features

- ✅ PostgreSQL sidecar in the same pod
- ✅ Persistent volume for database data
- ✅ Health checks for both containers
- ✅ Resource limits and requests
- ✅ Security contexts
- ✅ Configurable via values.yaml
- ✅ Prometheus monitoring support
- ✅ Ingress support

## Quick Start

### Prerequisites

- Kubernetes cluster (1.19+)
- Helm 3.x
- kubectl configured

### Deploy

```bash
# Deploy using the provided script
./deploy.sh

# Or deploy manually
helm install order-service ./helm-charts/order-service --namespace order-service --create-namespace
```

### Access the Service

```bash
# Port forward to access the service
kubectl port-forward svc/order-service 8081:8081 -n order-service

# Test the service
curl http://localhost:8081/actuator/health
```

## Configuration

Key configuration options in `values.yaml`:

### order-service
```yaml
userService:
  image:
    repository: order-service
    tag: "latest"
  replicas: 1
  resources:
    limits:
      memory: "1Gi"
      cpu: "1000m"
```

### PostgreSQL Sidecar
```yaml
postgresSidecar:
  image:
    repository: postgres
    tag: "14"
  database: users-db
  username: user
  password: pass
  persistence:
    enabled: true
    size: 1Gi
```

## Database Connection

The user service connects to the PostgreSQL sidecar using:
- **Host**: `localhost` (same pod)
- **Port**: `5432`
- **Database**: `users-db`
- **Username**: `user`
- **Password**: `pass`

## Monitoring

### Health Checks
```bash
# Check user service health
kubectl logs -l app=order-service -c order-service -n order-service

# Check database health
kubectl logs -l app=order-service -c postgres-sidecar -n order-service
```

### Prometheus Integration
Enable ServiceMonitor in `values.yaml`:
```yaml
serviceMonitor:
  enabled: true
  labels:
    app: order-service
```

## Scaling

The sidecar pattern means each pod has its own database instance. For horizontal scaling:

1. **Stateless scaling**: Use external database (modify connection string)
2. **Stateful scaling**: Use StatefulSet with persistent volumes
3. **Read replicas**: Deploy additional read-only instances

## Troubleshooting

### Common Issues

1. **Pod not starting**
   ```bash
   kubectl describe pod -l app=order-service -n order-service
   ```

2. **Database connection issues**
   ```bash
   kubectl logs -l app=order-service -c postgres-sidecar -n order-service
   ```

3. **Service not accessible**
   ```bash
   kubectl get svc -n order-service
   kubectl port-forward svc/order-service 8081:8081 -n order-service
   ```

### Logs
```bash
# User service logs
kubectl logs -l app=order-service -c order-service -n order-service -f

# Database logs
kubectl logs -l app=order-service -c postgres-sidecar -n order-service -f
```

## Cleanup

```bash
# Delete the release
helm uninstall order-service -n order-service

# Delete the namespace (optional)
kubectl delete namespace order-service
```

## Development

### Local Testing
```bash
# Build and push the user service image
docker build -t order-service:latest ./microservices/order-service
docker push order-service:latest

# Update the chart
helm upgrade order-service ./helm-charts/order-service -n order-service
```

### Custom Values
```bash
# Deploy with custom values
helm install order-service ./helm-charts/order-service \
  --namespace order-service \
  --set userService.replicas=3 \
  --set postgresSidecar.persistence.size=5Gi
```
