#!/bin/bash

# 🎯 Complete ML Pipeline: Data Generation → Model Training → Model Persistence → Deployment
# ========================================================================================
# This script orchestrates the complete ML pipeline from synthetic data generation
# to model training, persistence, and deployment.

set -e

# Configuration
DEFAULT_PR_COUNT=50000
DEFAULT_RUNS_PER_PR=10
DEFAULT_DATASETS_DIR="./ml-datasets"
DEFAULT_MODELS_DIR="./trained-models"
DEFAULT_DEPLOYMENT_DIR="./deployed-models"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_header() {
    echo -e "${PURPLE}🎯 $1${NC}"
    echo -e "${PURPLE}$(printf '=%.0s' {1..60})${NC}"
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -p, --pr-count NUM        Number of pull requests to generate (default: $DEFAULT_PR_COUNT)"
    echo "  -r, --runs-per-pr NUM     Number of system runs per PR (default: $DEFAULT_RUNS_PER_PR)"
    echo "  -d, --datasets-dir DIR    Datasets directory (default: $DEFAULT_DATASETS_DIR)"
    echo "  -m, --models-dir DIR      Models directory (default: $DEFAULT_MODELS_DIR)"
    echo "  -o, --deploy-dir DIR      Deployment directory (default: $DEFAULT_DEPLOYMENT_DIR)"
    echo "  -q, --quick               Quick test with 100 PRs and 3 runs each"
    echo "  -f, --full                Full pipeline with 50k PRs and 10 runs each"
    echo "  --skip-data-generation    Skip data generation step"
    echo "  --skip-training           Skip model training step"
    echo "  --skip-deployment         Skip deployment step"
    echo "  -h, --help                Show this help message"
    echo ""
    echo "Pipeline Steps:"
    echo "  1. Generate synthetic datasets"
    echo "  2. Train ML models on the datasets"
    echo "  3. Persist trained models with metadata"
    echo "  4. Deploy models for inference"
    echo "  5. Test model inference"
    echo ""
    echo "Examples:"
    echo "  $0 --quick                                    # Quick test pipeline"
    echo "  $0 --full                                     # Full production pipeline"
    echo "  $0 -p 1000 -r 5 --skip-deployment            # Generate data and train models only"
    echo "  $0 --skip-data-generation --skip-training     # Deploy existing models only"
}

# Function to check prerequisites
check_prerequisites() {
    print_info "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java version $JAVA_VERSION is not supported. Please use Java 17 or later."
        exit 1
    fi
    
    print_success "Java $JAVA_VERSION detected"
    
    # Check Docker (optional)
    if command -v docker &> /dev/null; then
        print_success "Docker detected (optional for containerized deployment)"
    else
        print_warning "Docker not found (containerized deployment will be skipped)"
    fi
    
    # Check kubectl (optional)
    if command -v kubectl &> /dev/null; then
        print_success "kubectl detected (optional for Kubernetes deployment)"
    else
        print_warning "kubectl not found (Kubernetes deployment will be skipped)"
    fi
}

# Function to compile all components
compile_components() {
    print_info "Compiling ML pipeline components..."
    
    # Compile data generator
    javac -cp . src/main/java/com/kb/synthetic/SimpleSyntheticDataGenerator.java src/main/java/com/kb/synthetic/MLDatasetExporter.java
    
    # Compile model trainer
    javac -cp . src/main/java/com/kb/synthetic/MLModelTrainer.java
    
    # Compile model inference
    javac -cp . src/main/java/com/kb/synthetic/MLModelInference.java
    
    print_success "All components compiled successfully"
}

# Function to generate datasets
generate_datasets() {
    local pr_count=$1
    local runs_per_pr=$2
    local datasets_dir=$3
    
    print_header "Step 1: Generate Synthetic Datasets"
    
    print_info "Generating datasets..."
    print_info "PR Count: $pr_count"
    print_info "Runs per PR: $runs_per_pr"
    print_info "Output Directory: $datasets_dir"
    
    # Create output directory
    mkdir -p "$datasets_dir"
    
    # Generate datasets
    java -cp src/main/java com.kb.synthetic.MLDatasetExporter "$pr_count" "$runs_per_pr" "$datasets_dir"
    
    if [ $? -eq 0 ]; then
        print_success "Dataset generation completed successfully!"
        
        # Show dataset summary
        if [ -f "$datasets_dir/dataset_statistics.json" ]; then
            print_info "Dataset Statistics:"
            cat "$datasets_dir/dataset_statistics.json" | python3 -m json.tool 2>/dev/null || cat "$datasets_dir/dataset_statistics.json"
        fi
    else
        print_error "Dataset generation failed"
        exit 1
    fi
}

# Function to train models
train_models() {
    local datasets_dir=$1
    local models_dir=$2
    
    print_header "Step 2: Train ML Models"
    
    print_info "Training models on datasets from: $datasets_dir"
    print_info "Models will be saved to: $models_dir"
    
    # Create models directory
    mkdir -p "$models_dir"
    
    # Train models
    java -cp src/main/java com.kb.synthetic.MLModelTrainer "$datasets_dir" "$models_dir"
    
    if [ $? -eq 0 ]; then
        print_success "Model training completed successfully!"
        
        # Show model registry
        if [ -f "$models_dir/model_registry.json" ]; then
            print_info "Model Registry:"
            cat "$models_dir/model_registry.json" | python3 -m json.tool 2>/dev/null || cat "$models_dir/model_registry.json"
        fi
    else
        print_error "Model training failed"
        exit 1
    fi
}

# Function to test model inference
test_inference() {
    local models_dir=$1
    
    print_header "Step 3: Test Model Inference"
    
    print_info "Testing model inference with models from: $models_dir"
    
    # Test inference
    java -cp src/main/java com.kb.synthetic.MLModelInference "$models_dir"
    
    if [ $? -eq 0 ]; then
        print_success "Model inference testing completed successfully!"
    else
        print_error "Model inference testing failed"
        exit 1
    fi
}

# Function to create deployment artifacts
create_deployment_artifacts() {
    local models_dir=$1
    local deploy_dir=$2
    
    print_header "Step 4: Create Deployment Artifacts"
    
    print_info "Creating deployment artifacts in: $deploy_dir"
    
    # Create deployment directory
    mkdir -p "$deploy_dir"
    
    # Create Dockerfile for model serving
    cat > "$deploy_dir/Dockerfile" << 'EOF'
FROM openjdk:17-jdk-slim

# Install Python for ML inference
RUN apt-get update && apt-get install -y python3 python3-pip && rm -rf /var/lib/apt/lists/*

# Install Python ML libraries
RUN pip3 install scikit-learn pandas numpy joblib

# Copy application
COPY target/synthetic-data-generator-1.0.0.jar app.jar
COPY trained-models/ /app/models/
COPY deployment/ /app/deployment/

# Set environment
ENV JAVA_OPTS="-Xmx2g -Xms1g"
ENV MODELS_DIR="/app/models"

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "/app.jar"]
EOF

    # Create Kubernetes deployment
    cat > "$deploy_dir/k8s-deployment.yaml" << EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ml-model-service
  labels:
    app: ml-model-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: ml-model-service
  template:
    metadata:
      labels:
        app: ml-model-service
    spec:
      containers:
      - name: ml-model-service
        image: ml-model-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: MODELS_DIR
          value: "/app/models"
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        volumeMounts:
        - name: models-volume
          mountPath: /app/models
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
      volumes:
      - name: models-volume
        persistentVolumeClaim:
          claimName: ml-models-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: ml-model-service
spec:
  selector:
    app: ml-model-service
  ports:
  - port: 8080
    targetPort: 8080
  type: LoadBalancer
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: ml-models-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
EOF

    # Create model serving configuration
    cat > "$deploy_dir/model-serving-config.json" << EOF
{
  "model_serving": {
    "endpoints": {
      "pr_impact": {
        "path": "/api/v1/predict/pr-impact",
        "method": "POST",
        "model": "pr_impact_model",
        "input_schema": {
          "lines_added": "integer",
          "test_coverage": "float",
          "code_complexity": "float",
          "risk_score": "float"
        }
      },
      "system_performance": {
        "path": "/api/v1/predict/system-performance",
        "method": "POST",
        "model": "system_performance_model",
        "input_schema": {
          "load_intensity": "float",
          "cpu_usage": "float",
          "memory_usage": "float",
          "response_time": "float"
        }
      },
      "service_health": {
        "path": "/api/v1/predict/service-health",
        "method": "POST",
        "model": "service_health_model",
        "input_schema": {
          "cpu_usage_percent": "float",
          "memory_usage_percent": "float",
          "error_rate": "float",
          "response_time_ms": "float"
        }
      },
      "ecommerce_conversion": {
        "path": "/api/v1/predict/ecommerce-conversion",
        "method": "POST",
        "model": "ecommerce_conversion_model",
        "input_schema": {
          "cart_value": "float",
          "session_duration_minutes": "float",
          "items_in_cart": "integer",
          "success_rate": "float"
        }
      },
      "anomaly_detection": {
        "path": "/api/v1/predict/anomaly-detection",
        "method": "POST",
        "model": "anomaly_detection_model",
        "input_schema": {
          "cpu_usage_percent": "float",
          "memory_usage_percent": "float",
          "error_rate": "float",
          "response_time_ms": "float"
        }
      }
    },
    "monitoring": {
      "metrics_enabled": true,
      "logging_enabled": true,
      "health_check_interval": 30
    },
    "scaling": {
      "min_replicas": 2,
      "max_replicas": 10,
      "target_cpu_utilization": 70
    }
  }
}
EOF

    # Copy models to deployment directory
    cp -r "$models_dir" "$deploy_dir/"
    
    print_success "Deployment artifacts created successfully!"
    print_info "Deployment directory: $deploy_dir"
    print_info "Files created:"
    print_info "  - Dockerfile (for containerized deployment)"
    print_info "  - k8s-deployment.yaml (for Kubernetes deployment)"
    print_info "  - model-serving-config.json (for API configuration)"
    print_info "  - trained-models/ (model files and metadata)"
}

# Function to show deployment instructions
show_deployment_instructions() {
    local deploy_dir=$1
    
    print_header "Deployment Instructions"
    
    echo ""
    print_info "🐳 Docker Deployment:"
    echo "  cd $deploy_dir"
    echo "  docker build -t ml-model-service:latest ."
    echo "  docker run -p 8080:8080 -v \$(pwd)/trained-models:/app/models ml-model-service:latest"
    echo ""
    
    print_info "☸️  Kubernetes Deployment:"
    echo "  kubectl apply -f $deploy_dir/k8s-deployment.yaml"
    echo "  kubectl get pods -l app=ml-model-service"
    echo "  kubectl port-forward svc/ml-model-service 8080:8080"
    echo ""
    
    print_info "🧪 Test API Endpoints:"
    echo "  # PR Impact Prediction"
    echo "  curl -X POST http://localhost:8080/api/v1/predict/pr-impact \\"
    echo "    -H 'Content-Type: application/json' \\"
    echo "    -d '{\"lines_added\": 150, \"test_coverage\": 0.75, \"code_complexity\": 8.5, \"risk_score\": 0.6}'"
    echo ""
    echo "  # Service Health Prediction"
    echo "  curl -X POST http://localhost:8080/api/v1/predict/service-health \\"
    echo "    -H 'Content-Type: application/json' \\"
    echo "    -d '{\"cpu_usage_percent\": 85.0, \"memory_usage_percent\": 70.0, \"error_rate\": 0.02, \"response_time_ms\": 500.0}'"
    echo ""
    
    print_info "📊 Model Persistence Location:"
    echo "  Models are persisted in: $deploy_dir/trained-models/"
    echo "  Each model includes:"
    echo "    - model_metadata.json (model info, accuracy, version)"
    echo "    - model_parameters.json (training parameters)"
    echo "    - feature_importance.json (feature weights)"
    echo "    - training_metrics.json (performance metrics)"
    echo "    - model.bin (serialized model)"
    echo "    - version.txt (version information)"
    echo ""
    print_info "🔄 Model Registry:"
    echo "  Central registry: $deploy_dir/trained-models/model_registry.json"
    echo "  Contains all model metadata and deployment information"
}

# Main function
main() {
    local pr_count=$DEFAULT_PR_COUNT
    local runs_per_pr=$DEFAULT_RUNS_PER_PR
    local datasets_dir=$DEFAULT_DATASETS_DIR
    local models_dir=$DEFAULT_MODELS_DIR
    local deploy_dir=$DEFAULT_DEPLOYMENT_DIR
    local quick_mode=false
    local full_mode=false
    local skip_data_generation=false
    local skip_training=false
    local skip_deployment=false
    
    # Parse command line arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            -p|--pr-count)
                pr_count="$2"
                shift 2
                ;;
            -r|--runs-per-pr)
                runs_per_pr="$2"
                shift 2
                ;;
            -d|--datasets-dir)
                datasets_dir="$2"
                shift 2
                ;;
            -m|--models-dir)
                models_dir="$2"
                shift 2
                ;;
            -o|--deploy-dir)
                deploy_dir="$2"
                shift 2
                ;;
            -q|--quick)
                quick_mode=true
                shift
                ;;
            -f|--full)
                full_mode=true
                shift
                ;;
            --skip-data-generation)
                skip_data_generation=true
                shift
                ;;
            --skip-training)
                skip_training=true
                shift
                ;;
            --skip-deployment)
                skip_deployment=true
                shift
                ;;
            -h|--help)
                show_usage
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                show_usage
                exit 1
                ;;
        esac
    done
    
    # Handle quick and full modes
    if [ "$quick_mode" = true ]; then
        pr_count=100
        runs_per_pr=3
        datasets_dir="./quick-datasets"
        models_dir="./quick-models"
        deploy_dir="./quick-deployment"
    elif [ "$full_mode" = true ]; then
        pr_count=50000
        runs_per_pr=10
        datasets_dir="./full-datasets"
        models_dir="./full-models"
        deploy_dir="./full-deployment"
    fi
    
    # Validate parameters
    if ! [[ "$pr_count" =~ ^[0-9]+$ ]] || [ "$pr_count" -lt 1 ]; then
        print_error "PR count must be a positive integer"
        exit 1
    fi
    
    if ! [[ "$runs_per_pr" =~ ^[0-9]+$ ]] || [ "$runs_per_pr" -lt 1 ]; then
        print_error "Runs per PR must be a positive integer"
        exit 1
    fi
    
    # Show header
    print_header "Complete ML Pipeline: Data Generation → Model Training → Model Persistence → Deployment"
    echo ""
    
    # Check prerequisites
    check_prerequisites
    
    # Compile components
    compile_components
    
    # Step 1: Generate datasets
    if [ "$skip_data_generation" = false ]; then
        generate_datasets "$pr_count" "$runs_per_pr" "$datasets_dir"
    else
        print_warning "Skipping data generation step"
    fi
    
    # Step 2: Train models
    if [ "$skip_training" = false ]; then
        train_models "$datasets_dir" "$models_dir"
    else
        print_warning "Skipping model training step"
    fi
    
    # Step 3: Test inference
    test_inference "$models_dir"
    
    # Step 4: Create deployment artifacts
    if [ "$skip_deployment" = false ]; then
        create_deployment_artifacts "$models_dir" "$deploy_dir"
        show_deployment_instructions "$deploy_dir"
    else
        print_warning "Skipping deployment step"
    fi
    
    print_success "🎉 Complete ML pipeline executed successfully!"
    print_info "📁 Generated datasets: $datasets_dir"
    print_info "🤖 Trained models: $models_dir"
    print_info "🚀 Deployment artifacts: $deploy_dir"
    print_info "📊 Models are persisted and ready for production use!"
}

# Run main function
main "$@"
