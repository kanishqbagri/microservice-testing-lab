# Microservices with PostgreSQL Sidecar - Helm Charts

This directory contains Helm charts for deploying microservices with PostgreSQL databases as sidecar containers in the same pod.

## Architecture Overview

Each microservice is deployed with:
- **Application Container**: Spring Boot service
- **PostgreSQL Sidecar Container**: Database running in the same pod
- **Persistent Volume**: For database data persistence
- **Service**: Exposes the application to other pods
- **Health Checks**: For both application and database
- **Resource Limits**: CPU and memory constraints
- **Security Contexts**: Non-root user execution

## Services

| Service | Port | Database | Namespace |
|---------|------|----------|-----------|
| user-service | 8081 | users-db | microservices-user-service |
| product-service | 8082 | products-db | microservices-product-service |
| order-service | 8083 | orders-db | microservices-order-service |
| notification-service | 8084 | notifications-db | microservices-notification-service |

## Quick Start

### Prerequisites

- Kubernetes cluster (1.19+)
- Helm 3.x
- kubectl configured
- Docker images built and available

### Deploy All Services

```bash
# Deploy all services at once
./deploy-all-services.sh
```

### Deploy Individual Service

```bash
# Deploy a specific service
cd user-service && ./deploy.sh
cd product-service && ./deploy.sh
cd order-service && ./deploy.sh
cd notification-service && ./deploy.sh
```

## Directory Structure

```
helm-charts/
├── README.md                           # This file
├── deploy-all-services.sh              # Deploy all services script
├── create-sidecar-charts.sh            # Generate charts for all services
├── user-service/                       # User service chart
│   ├── Chart.yaml
│   ├── values.yaml
│   ├── deploy.sh
│   ├── README.md
│   └── templates/
│       ├── deployment.yaml
│       ├── service.yaml
│       ├── pvc.yaml
│       ├── ingress.yaml
│       ├── servicemonitor.yaml
│       └── NOTES.txt
├── product-service/                    # Product service chart
├── order-service/                      # Order service chart
└── notification-service/               # Notification service chart
```

## Configuration

### Key Features

- **Sidecar Database**: PostgreSQL runs in the same pod as the application
- **Persistent Storage**: Database data persists across pod restarts
- **Health Checks**: Both application and database health monitoring
- **Resource Management**: CPU and memory limits/requests
- **Security**: Non-root user execution and security contexts
- **Monitoring**: Prometheus ServiceMonitor support
- **Ingress**: Configurable ingress for external access

### Environment Variables

Each service is configured with:
- `SPRING_PROFILES_ACTIVE=kubernetes`
- `SERVER_PORT=<service-port>`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/<database>`
- `SPRING_DATASOURCE_USERNAME=<username>`
- `SPRING_DATASOURCE_PASSWORD=<password>`

### Database Configuration

| Service | Database | Username | Password |
|---------|----------|----------|----------|
| user-service | users-db | user | pass |
| product-service | products-db | product_user | product_pass |
| order-service | orders-db | order_user | order_pass |
| notification-service | notifications-db | notification_user | notification_pass |

## Accessing Services

### Port Forward

```bash
# User Service
kubectl port-forward svc/user-service 8081:8081 -n microservices-user-service

# Product Service
kubectl port-forward svc/product-service 8082:8082 -n microservices-product-service

# Order Service
kubectl port-forward svc/order-service 8083:8083 -n microservices-order-service

# Notification Service
kubectl port-forward svc/notification-service 8084:8084 -n microservices-notification-service
```

### Health Checks

```bash
# Test health endpoints
curl http://localhost:8081/actuator/health  # User Service
curl http://localhost:8082/actuator/health  # Product Service
curl http://localhost:8083/actuator/health  # Order Service
curl http://localhost:8084/actuator/health  # Notification Service
```

## Monitoring

### Logs

```bash
# Application logs
kubectl logs -l app=user-service -c user-service -n microservices-user-service -f

# Database logs
kubectl logs -l app=user-service -c postgres-sidecar -n microservices-user-service -f
```

### Prometheus Integration

Enable ServiceMonitor in `values.yaml`:
```yaml
serviceMonitor:
  enabled: true
  labels:
    app: user-service
```

### Health Checks

```bash
# Check pod status
kubectl get pods -l app=user-service -n microservices-user-service

# Check service status
kubectl get svc -n microservices-user-service

# Check PVC status
kubectl get pvc -n microservices-user-service
```

## Customization

### Resource Limits

Modify `values.yaml`:
```yaml
userService:
  resources:
    limits:
      memory: "2Gi"
      cpu: "2000m"
    requests:
      memory: "1Gi"
      cpu: "1000m"

postgresSidecar:
  resources:
    limits:
      memory: "1Gi"
      cpu: "1000m"
    requests:
      memory: "512Mi"
      cpu: "500m"
```

### Storage

Modify `values.yaml`:
```yaml
postgresSidecar:
  persistence:
    enabled: true
    size: 5Gi
    storageClass: "fast-ssd"
```

### Scaling

For horizontal scaling, consider:
1. **External Database**: Use managed PostgreSQL service
2. **StatefulSet**: For stateful scaling with persistent volumes
3. **Read Replicas**: Deploy additional read-only instances

## Troubleshooting

### Common Issues

1. **Pod not starting**
   ```bash
   kubectl describe pod -l app=user-service -n microservices-user-service
   ```

2. **Database connection issues**
   ```bash
   kubectl logs -l app=user-service -c postgres-sidecar -n microservices-user-service
   ```

3. **Service not accessible**
   ```bash
   kubectl get svc -n microservices-user-service
   kubectl port-forward svc/user-service 8081:8081 -n microservices-user-service
   ```

4. **Storage issues**
   ```bash
   kubectl get pvc -n microservices-user-service
   kubectl describe pvc postgres-sidecar-pvc -n microservices-user-service
   ```

### Debug Commands

```bash
# Get all resources
kubectl get all -n microservices-user-service

# Describe deployment
kubectl describe deployment user-service -n microservices-user-service

# Check events
kubectl get events -n microservices-user-service --sort-by='.lastTimestamp'

# Exec into pod
kubectl exec -it <pod-name> -c user-service -n microservices-user-service -- /bin/bash
kubectl exec -it <pod-name> -c postgres-sidecar -n microservices-user-service -- /bin/bash
```

## Cleanup

### Remove All Services

```bash
# Remove all deployments
for service in user-service product-service order-service notification-service; do
  helm uninstall $service -n microservices-$service
  kubectl delete namespace microservices-$service
done
```

### Remove Individual Service

```bash
# Remove specific service
helm uninstall user-service -n microservices-user-service
kubectl delete namespace microservices-user-service
```

## Development

### Building Images

```bash
# Build all service images
docker build -t user-service:latest ./microservices/user-service
docker build -t product-service:latest ./microservices/product-service
docker build -t order-service:latest ./microservices/order-service
docker build -t notification-service:latest ./microservices/notification-service
```

### Updating Charts

```bash
# Update specific service
helm upgrade user-service ./helm-charts/user-service -n microservices-user-service

# Update all services
./deploy-all-services.sh
```

## Best Practices

1. **Resource Management**: Set appropriate CPU and memory limits
2. **Security**: Use non-root users and security contexts
3. **Monitoring**: Enable health checks and Prometheus monitoring
4. **Storage**: Use appropriate storage classes for your environment
5. **Backup**: Implement database backup strategies
6. **Scaling**: Plan for horizontal scaling with external databases
7. **Networking**: Use proper service discovery and ingress

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review pod logs and events
3. Verify resource availability
4. Check network connectivity
5. Validate configuration files
