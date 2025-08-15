# Service Management Scripts

This directory contains scripts for managing the microservice architecture.

## 📋 Available Scripts

### 1. `start-all-services.sh`
**Purpose**: Start all microservices and infrastructure services in the correct order.

**Usage**:
```bash
./start-all-services.sh
```

**Features**:
- ✅ Checks prerequisites (Java, Maven, Docker)
- ✅ Starts infrastructure services (PostgreSQL, Kafka, Zookeeper)
- ✅ Starts microservices in dependency order
- ✅ Performs health checks for each service
- ✅ Provides colored output and status updates
- ✅ Handles errors gracefully

**Output**:
```
🚀 Starting Microservice Architecture...
[INFO] Checking prerequisites...
[SUCCESS] Prerequisites check passed
[INFO] Starting infrastructure services...
[SUCCESS] Infrastructure services started
[INFO] Starting microservices...
[SUCCESS] User Service started successfully
[SUCCESS] Product Service started successfully
[SUCCESS] Order Service started successfully
[SUCCESS] Notification Service started successfully
[SUCCESS] Gateway Service started successfully
🎉 All microservices are running successfully!
```

### 2. `stop-all-services.sh`
**Purpose**: Stop all microservices and infrastructure services gracefully.

**Usage**:
```bash
./stop-all-services.sh
```

**Features**:
- ✅ Stops services in reverse dependency order
- ✅ Graceful shutdown with SIGTERM first, then SIGKILL
- ✅ Cleans up Docker containers
- ✅ Removes orphaned processes
- ✅ Verifies complete shutdown

**Output**:
```
🛑 Stopping Microservice Architecture...
[INFO] Stopping microservices...
[INFO] Stopping Gateway Service on port 8080...
[SUCCESS] Gateway Service stopped gracefully
[INFO] Stopping infrastructure services...
[SUCCESS] Infrastructure services stopped
🎉 All microservices have been stopped successfully!
```

### 3. `check-service-status.sh`
**Purpose**: Comprehensive status check of all services and system resources.

**Usage**:
```bash
./check-service-status.sh
```

**Features**:
- ✅ Checks infrastructure services (PostgreSQL, Kafka, Zookeeper)
- ✅ Checks microservices by port
- ✅ Checks testing lab components (Jarvis Core, Frontend)
- ✅ Performs health endpoint checks
- ✅ Monitors system resources (memory, disk, Docker)
- ✅ Provides percentage-based status summary
- ✅ Shows service URLs and documentation links

**Output**:
```
🔍 Microservice Architecture Status Check
========================================

1. INFRASTRUCTURE SERVICES
--------------------------------
[SUCCESS] PostgreSQL Database is running (Status: Up 2 minutes)
[SUCCESS] Apache Kafka is running (Status: Up 2 minutes)
[SUCCESS] Apache Zookeeper is running (Status: Up 2 minutes)

2. MICROSERVICES
-------------------
[SUCCESS] Gateway Service is running on port 8080 (PID: 12345, Process: java)
[SUCCESS] Gateway Service health check passed
[SUCCESS] User Service is running on port 8081 (PID: 12346, Process: java)
[SUCCESS] User Service health check passed

5. SUMMARY
-------------
📊 Service Status Summary:
   Running: 10/10 services
   Status: 100% operational
🎉 All services are running!
```

### 4. `status.sh`
**Purpose**: Quick alias for status checking.

**Usage**:
```bash
./status.sh
```

**Features**:
- ✅ Simple alias for `check-service-status.sh`
- ✅ Quick status overview

## 🚀 Quick Start

### Start All Services
```bash
./start-all-services.sh
```

### Check Status
```bash
./status.sh
```

### Stop All Services
```bash
./stop-all-services.sh
```

## 📊 Exit Codes

The scripts use the following exit codes:

- **0**: Success (all services running)
- **1**: Partial success (some services running)
- **2**: Failure (most services stopped)

## 🔧 Configuration

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- curl (for health checks)

### Environment Variables
The scripts automatically detect and use:
- `JAVA_HOME` - Java installation path
- `MAVEN_HOME` - Maven installation path
- `DOCKER_HOST` - Docker daemon address

### Port Configuration
Default service ports:
- **8080**: Gateway Service
- **8081**: User Service
- **8082**: Product Service
- **8083**: Order Service
- **8084**: Notification Service
- **8085**: Jarvis Core
- **3000**: Frontend Dashboard

## 🛠️ Troubleshooting

### Common Issues

#### Services Won't Start
```bash
# Check prerequisites
java -version
mvn -version
docker --version

# Check Docker is running
docker info

# Check available ports
lsof -i :8080-8085
```

#### Services Won't Stop
```bash
# Force kill all Java processes
pkill -f "spring-boot:run"

# Force kill all Maven processes
pkill -f "mvn"

# Stop Docker containers
docker-compose down
```

#### Health Checks Fail
```bash
# Check individual service health
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
curl http://localhost:8084/actuator/health
```

### Debug Mode
Run scripts with verbose output:
```bash
bash -x ./start-all-services.sh
bash -x ./stop-all-services.sh
bash -x ./check-service-status.sh
```

## 📚 Service URLs

When services are running:

### API Endpoints
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Product Service**: http://localhost:8082
- **Order Service**: http://localhost:8083
- **Notification Service**: http://localhost:8084
- **Jarvis Core**: http://localhost:8085
- **Frontend Dashboard**: http://localhost:3000

### Health Checks
- **Gateway**: http://localhost:8080/actuator/health
- **User**: http://localhost:8081/actuator/health
- **Product**: http://localhost:8082/actuator/health
- **Order**: http://localhost:8083/actuator/health
- **Notification**: http://localhost:8084/actuator/health
- **Jarvis**: http://localhost:8085/actuator/health

### API Documentation
- **Gateway**: http://localhost:8080/swagger-ui.html
- **User**: http://localhost:8081/swagger-ui.html
- **Product**: http://localhost:8082/swagger-ui.html
- **Order**: http://localhost:8083/swagger-ui.html
- **Notification**: http://localhost:8084/swagger-ui.html
- **Jarvis**: http://localhost:8085/swagger-ui.html

## 🔄 Continuous Monitoring

For continuous monitoring, you can:

### Set up a monitoring loop
```bash
# Check status every 30 seconds
watch -n 30 ./status.sh
```

### Create a monitoring script
```bash
#!/bin/bash
while true; do
    ./status.sh
    sleep 60
done
```

### Use with cron
```bash
# Add to crontab for hourly checks
0 * * * * cd /path/to/microservices && ./status.sh >> status.log 2>&1
```

## 📝 Logs

### Service Logs
Each service generates logs in its respective directory:
```bash
# View service logs
tail -f user-service/logs/application.log
tail -f product-service/logs/application.log
tail -f order-service/logs/application.log
tail -f notification-service/logs/application.log
tail -f gateway-service/logs/application.log
```

### Docker Logs
```bash
# View Docker container logs
docker-compose logs -f
docker-compose logs -f user-service
docker-compose logs -f postgres
```

## 🤝 Contributing

When modifying these scripts:

1. **Test thoroughly** - Test with services running and stopped
2. **Handle errors gracefully** - Don't let one failure stop the entire script
3. **Provide clear output** - Use colored output and descriptive messages
4. **Document changes** - Update this README when adding new features
5. **Maintain compatibility** - Ensure scripts work on macOS and Linux

## 📄 License

These scripts are part of the microservice testing lab and follow the same license as the main project.
