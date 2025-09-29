#!/bin/bash

# 🎯 Memory-Efficient ML Dataset Generator
# =======================================
# This script generates ML datasets with memory optimization to avoid OutOfMemoryError
# when processing large datasets (50K+ PRs).

set -e

# Configuration
DEFAULT_PR_COUNT=50000
DEFAULT_RUNS_PER_PR=10
DEFAULT_OUTPUT_DIR="./ml-datasets"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# JVM Memory Configuration
JVM_HEAP_SIZE="4g"
JVM_NEW_SIZE="1g"
JVM_GC_OPTIONS="-XX:+UseG1GC -XX:MaxGCPauseMillis=200"

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
    echo -e "${PURPLE}$(printf '=%.0s' {1..50})${NC}"
}

# Function to show usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -p, --pr-count NUM        Number of pull requests to generate (default: $DEFAULT_PR_COUNT)"
    echo "  -r, --runs-per-pr NUM     Number of system runs per PR (default: $DEFAULT_RUNS_PER_PR)"
    echo "  -o, --output-dir DIR      Output directory (default: $DEFAULT_OUTPUT_DIR)"
    echo "  -q, --quick               Quick test with 100 PRs and 3 runs each"
    echo "  -f, --full                Full dataset with 50k PRs and 10 runs each"
    echo "  --heap-size SIZE          JVM heap size (default: $JVM_HEAP_SIZE)"
    echo "  --batch-size SIZE         Batch size for processing (default: 1000)"
    echo "  -h, --help                Show this help message"
    echo ""
    echo "Memory Configuration:"
    echo "  --heap-size 2g            Set JVM heap to 2GB"
    echo "  --heap-size 8g            Set JVM heap to 8GB"
    echo "  --batch-size 500          Process 500 PRs at a time"
    echo "  --batch-size 2000         Process 2000 PRs at a time"
    echo ""
    echo "Examples:"
    echo "  $0 --quick                                    # Quick test (100 PRs)"
    echo "  $0 --full                                     # Full dataset (50k PRs)"
    echo "  $0 -p 10000 -r 5 --heap-size 2g              # Custom with 2GB heap"
    echo "  $0 -p 100000 -r 8 --heap-size 8g --batch-size 2000  # Large dataset with 8GB heap"
}

# Function to check Java and memory
check_java_and_memory() {
    print_info "Checking Java installation and memory configuration..."
    
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
    
    # Check available memory
    if command -v free &> /dev/null; then
        AVAILABLE_MEMORY=$(free -m | awk 'NR==2{printf "%.0f", $7/1024}')
        print_info "Available system memory: ${AVAILABLE_MEMORY}GB"
        
        # Parse heap size
        HEAP_SIZE_GB=$(echo $JVM_HEAP_SIZE | sed 's/g//')
        if [ "$HEAP_SIZE_GB" -gt "$AVAILABLE_MEMORY" ]; then
            print_warning "Requested heap size (${HEAP_SIZE_GB}GB) is larger than available memory (${AVAILABLE_MEMORY}GB)"
            print_warning "Consider reducing heap size or increasing system memory"
        fi
    fi
    
    print_info "JVM Configuration:"
    print_info "  Heap Size: $JVM_HEAP_SIZE"
    print_info "  New Size: $JVM_NEW_SIZE"
    print_info "  GC Options: $JVM_GC_OPTIONS"
}

# Function to compile components
compile_components() {
    print_info "Compiling memory-efficient components..."
    
    # Compile data generator
    javac -cp . src/main/java/com/kb/synthetic/SimpleSyntheticDataGenerator.java
    
    # Compile memory-efficient exporter
    javac -cp . src/main/java/com/kb/synthetic/MemoryEfficientMLDatasetExporter.java
    
    print_success "Memory-efficient components compiled successfully"
}

# Function to generate datasets with memory optimization
generate_datasets() {
    local pr_count=$1
    local runs_per_pr=$2
    local output_dir=$3
    local batch_size=$4
    
    print_header "Step 1: Generate Memory-Efficient Datasets"
    
    print_info "Configuration:"
    print_info "  PR Count: $pr_count"
    print_info "  Runs per PR: $runs_per_pr"
    print_info "  Output Directory: $output_dir"
    print_info "  Batch Size: $batch_size"
    print_info "  JVM Heap: $JVM_HEAP_SIZE"
    print_info ""
    
    # Create output directory
    mkdir -p "$output_dir"
    
    # Build JVM options
    JVM_OPTS="-Xmx$JVM_HEAP_SIZE -Xms$JVM_HEAP_SIZE -Xmn$JVM_NEW_SIZE $JVM_GC_OPTIONS"
    
    print_info "Starting dataset generation with JVM options: $JVM_OPTS"
    
    # Generate datasets with memory-efficient exporter
    java $JVM_OPTS -cp src/main/java com.kb.synthetic.MemoryEfficientMLDatasetExporter "$pr_count" "$runs_per_pr" "$output_dir"
    
    if [ $? -eq 0 ]; then
        print_success "Memory-efficient dataset generation completed successfully!"
        
        # Show dataset summary
        if [ -f "$output_dir/dataset_statistics.json" ]; then
            print_info "Dataset Statistics:"
            cat "$output_dir/dataset_statistics.json" | python3 -m json.tool 2>/dev/null || cat "$output_dir/dataset_statistics.json"
        fi
        
        # Show file sizes
        print_info "Generated Files:"
        ls -lh "$output_dir"/*.json | awk '{print "  " $9 ": " $5}'
        
    else
        print_error "Dataset generation failed"
        exit 1
    fi
}

# Function to show dataset examples
show_dataset_examples() {
    local output_dir=$1
    
    print_header "Dataset Examples for ML Training"
    
    if [ -f "$output_dir/pr_dataset.json" ]; then
        print_info "PR Dataset Example (first 3 records):"
        head -n 20 "$output_dir/pr_dataset.json" | tail -n 17
        echo ""
    fi
    
    if [ -f "$output_dir/system_run_dataset.json" ]; then
        print_info "System Run Dataset Example (first 3 records):"
        head -n 20 "$output_dir/system_run_dataset.json" | tail -n 17
        echo ""
    fi
    
    if [ -f "$output_dir/service_metrics_dataset.json" ]; then
        print_info "Service Metrics Dataset Example (first 3 records):"
        head -n 20 "$output_dir/service_metrics_dataset.json" | tail -n 17
        echo ""
    fi
    
    if [ -f "$output_dir/ecommerce_flow_dataset.json" ]; then
        print_info "E-commerce Flow Dataset Example (first 3 records):"
        head -n 20 "$output_dir/ecommerce_flow_dataset.json" | tail -n 17
        echo ""
    fi
}

# Function to show ML training examples
show_ml_examples() {
    local output_dir=$1
    
    print_header "ML Training Examples"
    
    print_info "Python/Pandas Example:"
    cat << 'EOF'
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report

# Load datasets
pr_data = pd.read_json('./ml-datasets/pr_dataset.json')
system_runs = pd.read_json('./ml-datasets/system_run_dataset.json')
service_metrics = pd.read_json('./ml-datasets/service_metrics_dataset.json')

# PR Impact Prediction Model
features = ['lines_added', 'lines_deleted', 'test_coverage', 'code_complexity']
target = 'has_performance_regression'
X = pr_data[features]
y = pr_data[target]

# Train model
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

# Evaluate
y_pred = model.predict(X_test)
print("PR Impact Prediction Model Performance:")
print(classification_report(y_test, y_pred))
EOF
    
    print_info "TensorFlow Example:"
    cat << 'EOF'
import tensorflow as tf
import pandas as pd
from sklearn.preprocessing import StandardScaler

# Load service metrics data
data = pd.read_json('./ml-datasets/service_metrics_dataset.json')
X = data[['cpu_usage_percent', 'memory_usage_percent', 'error_rate', 'response_time_ms']]
y = data['is_anomalous']

# Scale features
scaler = StandardScaler()
X_scaled = scaler.fit_transform(X)

# Create neural network
model = tf.keras.Sequential([
    tf.keras.layers.Dense(64, activation='relu', input_shape=(X_scaled.shape[1],)),
    tf.keras.layers.Dropout(0.3),
    tf.keras.layers.Dense(32, activation='relu'),
    tf.keras.layers.Dropout(0.3),
    tf.keras.layers.Dense(1, activation='sigmoid')
])

# Compile and train
model.compile(optimizer='adam', loss='binary_crossentropy', metrics=['accuracy'])
model.fit(X_scaled, y, epochs=50, batch_size=32, validation_split=0.2)
EOF
}

# Main function
main() {
    local pr_count=$DEFAULT_PR_COUNT
    local runs_per_pr=$DEFAULT_RUNS_PER_PR
    local output_dir=$DEFAULT_OUTPUT_DIR
    local quick_mode=false
    local full_mode=false
    local batch_size=1000
    
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
            -o|--output-dir)
                output_dir="$2"
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
            --heap-size)
                JVM_HEAP_SIZE="$2"
                shift 2
                ;;
            --batch-size)
                batch_size="$2"
                shift 2
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
        output_dir="./quick-datasets"
        JVM_HEAP_SIZE="1g"
        batch_size=50
    elif [ "$full_mode" = true ]; then
        pr_count=50000
        runs_per_pr=10
        output_dir="./full-datasets"
        JVM_HEAP_SIZE="8g"
        batch_size=2000
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
    
    if ! [[ "$batch_size" =~ ^[0-9]+$ ]] || [ "$batch_size" -lt 1 ]; then
        print_error "Batch size must be a positive integer"
        exit 1
    fi
    
    # Show header
    print_header "Memory-Efficient ML Dataset Generator"
    echo ""
    
    # Check prerequisites
    check_java_and_memory
    
    # Compile components
    compile_components
    
    # Generate datasets
    generate_datasets "$pr_count" "$runs_per_pr" "$output_dir" "$batch_size"
    
    # Show examples
    show_dataset_examples "$output_dir"
    show_ml_examples "$output_dir"
    
    print_success "🎉 Memory-efficient dataset generation completed successfully!"
    print_info "📁 Datasets are available in: $output_dir"
    print_info "🤖 Ready for ML model training!"
    print_info "💾 Memory usage optimized with batch processing"
}

# Run main function
main "$@"
