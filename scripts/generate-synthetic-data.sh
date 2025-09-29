#!/bin/bash

# Synthetic Data Generator Script
# Usage: ./generate-synthetic-data.sh [pr_count] [runs_per_pr] [output_path]

set -e

# Default values
PR_COUNT=${1:-1000}
RUNS_PER_PR=${2:-5}
OUTPUT_PATH=${3:-"./ml-datasets"}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🎯 Synthetic Data Generator for AI/ML Model Training${NC}"
echo -e "${BLUE}===================================================${NC}"
echo ""

# Check prerequisites
echo -e "${YELLOW}📋 Checking prerequisites...${NC}"

# Check Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java is not installed. Please install Java 17+${NC}"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo -e "${RED}❌ Java version $JAVA_VERSION is not supported. Please install Java 17+${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Java $JAVA_VERSION found${NC}"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}❌ Maven is not installed. Please install Maven 3.8+${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Maven found${NC}"

# Check Docker (for TestContainers)
if ! command -v docker &> /dev/null; then
    echo -e "${YELLOW}⚠️  Docker not found. TestContainers tests will be skipped${NC}"
else
    echo -e "${GREEN}✅ Docker found${NC}"
fi

echo ""

# Navigate to the synthetic data generator directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SYNTHETIC_DIR="$PROJECT_ROOT/microservice-testing-lab/shared-utils/synthetic-data-generator"

if [ ! -d "$SYNTHETIC_DIR" ]; then
    echo -e "${RED}❌ Synthetic data generator directory not found: $SYNTHETIC_DIR${NC}"
    exit 1
fi

cd "$SYNTHETIC_DIR"

echo -e "${YELLOW}📁 Working directory: $(pwd)${NC}"
echo ""

# Build the project
echo -e "${YELLOW}🔨 Building the project...${NC}"
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Project built successfully${NC}"
else
    echo -e "${RED}❌ Project build failed${NC}"
    exit 1
fi

echo ""

# Run tests (if Docker is available)
if command -v docker &> /dev/null; then
    echo -e "${YELLOW}🧪 Running tests...${NC}"
    mvn test -q
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ All tests passed${NC}"
    else
        echo -e "${YELLOW}⚠️  Some tests failed, but continuing with data generation${NC}"
    fi
    echo ""
fi

# Create output directory
mkdir -p "$OUTPUT_PATH"

# Generate synthetic data
echo -e "${YELLOW}🚀 Generating synthetic data...${NC}"
echo -e "${BLUE}   PR Count: $PR_COUNT${NC}"
echo -e "${BLUE}   Runs per PR: $RUNS_PER_PR${NC}"
echo -e "${BLUE}   Output Path: $OUTPUT_PATH${NC}"
echo ""

# Calculate expected data volumes
TOTAL_RUNS=$((PR_COUNT * RUNS_PER_PR))
TOTAL_FLOWS=$((TOTAL_RUNS * 30))  # 30 flows per run
TOTAL_STEPS=$((TOTAL_FLOWS * 8))  # 8 steps per flow

echo -e "${BLUE}📊 Expected data volumes:${NC}"
echo -e "${BLUE}   Pull Requests: $PR_COUNT${NC}"
echo -e "${BLUE}   System Runs: $TOTAL_RUNS${NC}"
echo -e "${BLUE}   E-commerce Flows: $TOTAL_FLOWS${NC}"
echo -e "${BLUE}   Flow Steps: $TOTAL_STEPS${NC}"
echo ""

# Run the application
echo -e "${YELLOW}⏳ Starting data generation (this may take several minutes)...${NC}"
mvn spring-boot:run -Dspring-boot.run.arguments="generate $PR_COUNT $RUNS_PER_PR $OUTPUT_PATH" -q

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}🎉 Data generation completed successfully!${NC}"
    echo ""
    
    # Display generated files
    echo -e "${BLUE}📁 Generated files in $OUTPUT_PATH:${NC}"
    ls -la "$OUTPUT_PATH" | grep -E '\.(json|csv)$' | while read line; do
        echo -e "${GREEN}   $line${NC}"
    done
    
    echo ""
    echo -e "${BLUE}📊 Dataset statistics:${NC}"
    if [ -f "$OUTPUT_PATH/dataset_statistics.json" ]; then
        echo -e "${GREEN}   Dataset statistics available in dataset_statistics.json${NC}"
    fi
    
    echo ""
    echo -e "${YELLOW}💡 Next steps:${NC}"
    echo -e "${YELLOW}   1. Review the generated datasets in $OUTPUT_PATH${NC}"
    echo -e "${YELLOW}   2. Use the JSON files for ML model training${NC}"
    echo -e "${YELLOW}   3. Check dataset_statistics.json for data quality metrics${NC}"
    echo ""
    
else
    echo -e "${RED}❌ Data generation failed${NC}"
    exit 1
fi

echo -e "${BLUE}🎯 Synthetic Data Generation Complete!${NC}"
