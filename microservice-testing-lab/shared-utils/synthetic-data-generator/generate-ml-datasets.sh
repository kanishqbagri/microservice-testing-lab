#!/bin/bash

# 🎯 Synthetic Data Generator for AI/ML Model Training
# ===================================================
# This script generates comprehensive synthetic datasets for training AI/ML models
# in microservice environments with realistic patterns and controlled noise injection.

set -e

# Configuration
DEFAULT_PR_COUNT=50000
DEFAULT_RUNS_PER_PR=10
DEFAULT_OUTPUT_DIR="./ml-datasets"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

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
    echo "  -o, --output-dir DIR      Output directory for datasets (default: $DEFAULT_OUTPUT_DIR)"
    echo "  -q, --quick               Quick test with 100 PRs and 3 runs each"
    echo "  -f, --full                Full dataset with 50k PRs and 10 runs each"
    echo "  -h, --help                Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 --quick                                    # Quick test (100 PRs, 3 runs)"
    echo "  $0 --full                                     # Full dataset (50k PRs, 10 runs)"
    echo "  $0 -p 1000 -r 5 -o ./my-datasets             # Custom parameters"
    echo "  $0 --pr-count 10000 --runs-per-pr 8          # 10k PRs with 8 runs each"
    echo ""
    echo "Generated Datasets:"
    echo "  - pr_dataset.json: PR impact prediction features"
    echo "  - system_run_dataset.json: System performance prediction features"
    echo "  - service_metrics_dataset.json: Service health prediction features"
    echo "  - ecommerce_flow_dataset.json: Conversion prediction features"
    echo "  - combined_dataset.json: Multi-entity analysis features"
    echo "  - dataset_statistics.json: Data quality and distribution metrics"
}

# Function to check Java installation
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        print_info "Please install Java 17 or later"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java version $JAVA_VERSION is not supported. Please use Java 17 or later."
        exit 1
    fi
    
    print_success "Java $JAVA_VERSION detected"
}

# Function to compile the generator
compile_generator() {
    print_info "Compiling synthetic data generator..."
    
    if [ ! -f "$SCRIPT_DIR/src/main/java/com/kb/synthetic/SimpleSyntheticDataGenerator.java" ]; then
        print_error "SimpleSyntheticDataGenerator.java not found"
        exit 1
    fi
    
    if [ ! -f "$SCRIPT_DIR/src/main/java/com/kb/synthetic/MLDatasetExporter.java" ]; then
        print_error "MLDatasetExporter.java not found"
        exit 1
    fi
    
    cd "$SCRIPT_DIR"
    javac -cp . src/main/java/com/kb/synthetic/SimpleSyntheticDataGenerator.java src/main/java/com/kb/synthetic/MLDatasetExporter.java
    
    if [ $? -eq 0 ]; then
        print_success "Compilation successful"
    else
        print_error "Compilation failed"
        exit 1
    fi
}

# Function to generate datasets
generate_datasets() {
    local pr_count=$1
    local runs_per_pr=$2
    local output_dir=$3
    
    print_info "Generating datasets..."
    print_info "PR Count: $pr_count"
    print_info "Runs per PR: $runs_per_pr"
    print_info "Output Directory: $output_dir"
    
    # Create output directory
    mkdir -p "$output_dir"
    
    # Estimate total records
    local total_runs=$((pr_count * runs_per_pr))
    local total_metrics=$((total_runs * 5))  # 5 services per run
    local total_flows=$((total_runs * 35))   # ~35 flows per run
    
    print_info "Estimated records:"
    print_info "  - Pull Requests: $pr_count"
    print_info "  - System Runs: $total_runs"
    print_info "  - Service Metrics: $total_metrics"
    print_info "  - E-commerce Flows: $total_flows"
    print_info "  - Total: $((pr_count + total_runs + total_metrics + total_flows))"
    
    # Generate datasets
    cd "$SCRIPT_DIR"
    java -cp src/main/java com.kb.synthetic.MLDatasetExporter "$pr_count" "$runs_per_pr" "$output_dir"
    
    if [ $? -eq 0 ]; then
        print_success "Dataset generation completed successfully!"
    else
        print_error "Dataset generation failed"
        exit 1
    fi
}

# Function to show dataset summary
show_dataset_summary() {
    local output_dir=$1
    
    if [ -f "$output_dir/dataset_statistics.json" ]; then
        print_header "Dataset Summary"
        echo ""
        print_info "Dataset Statistics:"
        cat "$output_dir/dataset_statistics.json" | python3 -m json.tool 2>/dev/null || cat "$output_dir/dataset_statistics.json"
        echo ""
        
        print_info "Generated Files:"
        ls -lh "$output_dir"/*.json | awk '{print "  " $9 " (" $5 ")"}'
        echo ""
        
        print_info "File Sizes:"
        local total_size=$(du -sh "$output_dir" | cut -f1)
        print_success "Total dataset size: $total_size"
    else
        print_warning "Dataset statistics file not found"
    fi
}

# Function to show ML usage examples
show_ml_examples() {
    print_header "ML Model Training Examples"
    echo ""
    print_info "Python/Pandas Example:"
    echo "import pandas as pd"
    echo "import json"
    echo ""
    echo "# Load datasets"
    echo "pr_data = pd.read_json('$DEFAULT_OUTPUT_DIR/pr_dataset.json')"
    echo "system_runs = pd.read_json('$DEFAULT_OUTPUT_DIR/system_run_dataset.json')"
    echo "service_metrics = pd.read_json('$DEFAULT_OUTPUT_DIR/service_metrics_dataset.json')"
    echo "ecommerce_flows = pd.read_json('$DEFAULT_OUTPUT_DIR/ecommerce_flow_dataset.json')"
    echo ""
    echo "# Example: PR Impact Prediction"
    echo "features = ['lines_added', 'lines_deleted', 'test_coverage', 'code_complexity']"
    echo "target = 'has_performance_regression'"
    echo "X = pr_data[features]"
    echo "y = pr_data[target]"
    echo ""
    print_info "R Example:"
    echo "library(jsonlite)"
    echo "library(dplyr)"
    echo ""
    echo "# Load datasets"
    echo "pr_data <- fromJSON('$DEFAULT_OUTPUT_DIR/pr_dataset.json')"
    echo "system_runs <- fromJSON('$DEFAULT_OUTPUT_DIR/system_run_dataset.json')"
    echo ""
    print_info "TensorFlow/PyTorch Example:"
    echo "import tensorflow as tf"
    echo "import numpy as np"
    echo ""
    echo "# Load and preprocess data"
    echo "data = pd.read_json('$DEFAULT_OUTPUT_DIR/combined_dataset.json')"
    echo "X = data.drop(['service_is_anomalous'], axis=1).values"
    echo "y = data['service_is_anomalous'].values"
    echo ""
    echo "# Create model"
    echo "model = tf.keras.Sequential(["
    echo "    tf.keras.layers.Dense(64, activation='relu', input_shape=(X.shape[1],)),"
    echo "    tf.keras.layers.Dense(32, activation='relu'),"
    echo "    tf.keras.layers.Dense(1, activation='sigmoid')"
    echo "])"
    echo ""
}

# Main function
main() {
    local pr_count=$DEFAULT_PR_COUNT
    local runs_per_pr=$DEFAULT_RUNS_PER_PR
    local output_dir=$DEFAULT_OUTPUT_DIR
    local quick_mode=false
    local full_mode=false
    
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
        output_dir="./quick-test-datasets"
    elif [ "$full_mode" = true ]; then
        pr_count=50000
        runs_per_pr=10
        output_dir="./full-datasets"
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
    print_header "Synthetic Data Generator for AI/ML Model Training"
    echo ""
    
    # Check prerequisites
    check_java
    
    # Compile generator
    compile_generator
    
    # Generate datasets
    generate_datasets "$pr_count" "$runs_per_pr" "$output_dir"
    
    # Show summary
    show_dataset_summary "$output_dir"
    
    # Show ML examples
    show_ml_examples
    
    print_success "🎉 Synthetic data generation completed successfully!"
    print_info "Your ML-ready datasets are available in: $output_dir"
    print_info "Ready for AI/ML model training! 🚀"
}

# Run main function
main "$@"
