#!/bin/bash

# Jarvis PR Analysis Demo Script
# Demonstrates intelligent PR analysis and testing recommendations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
JARVIS_URL="http://localhost:8085"
DEMO_DELAY=2

# Logging functions
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

log_demo() {
    echo -e "${PURPLE}🎯 $1${NC}"
}

log_ai() {
    echo -e "${CYAN}🤖 $1${NC}"
}

# Check if Jarvis is running
check_jarvis_health() {
    log_info "Checking Jarvis Core health..."
    if curl -s "$JARVIS_URL/api/pr-analysis/health" > /dev/null; then
        log_success "Jarvis Core is running and ready!"
        return 0
    else
        log_error "Jarvis Core is not running at $JARVIS_URL"
        return 1
    fi
}

# Wait for service to be ready
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    log_info "Waiting for $service_name to be ready..."
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            log_success "$service_name is ready!"
            return 0
        fi
        
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    log_error "$service_name failed to start within expected time"
    return 1
}

# Demo 1: Analyze Sample Security PR
demo_security_pr_analysis() {
    log_demo "Demo 1: Analyzing Security PR (JWT Authentication)"
    echo
    
    log_info "Analyzing security-focused PR with JWT authentication changes..."
    
    response=$(curl -s -X GET "$JARVIS_URL/api/pr-demo/analyze-sample/security")
    
    if [ $? -eq 0 ]; then
        log_success "Security PR analysis completed!"
        echo
        log_ai "AI Analysis Results:"
        echo "$response" | jq -r '
            "📊 Analysis Summary:",
            "   PR ID: " + .prInfo.prId,
            "   Title: " + .prInfo.title,
            "   Risk Level: " + .riskAssessment.overallRiskLevel,
            "   Impact Level: " + .impactAnalysis.overallImpact,
            "   Test Recommendations: " + (.testRecommendations | length | tostring),
            "   AI Confidence: " + (.confidenceScore * 100 | floor | tostring) + "%",
            "",
            "🔒 Security Insights:",
            (.insights[] | select(contains("Security") or contains("🔒") or contains("🔐") or contains("🛡️"))),
            "",
            "🧪 High Priority Test Recommendations:",
            (.testRecommendations[] | select(.priority >= 0.8) | "   • " + .description + " (Priority: " + (.priority * 100 | floor | tostring) + "%)")
        '
    else
        log_error "Failed to analyze security PR"
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Demo 2: Analyze Breaking Change PR
demo_breaking_change_analysis() {
    log_demo "Demo 2: Analyzing Breaking Change PR (API Refactor)"
    echo
    
    log_info "Analyzing PR with breaking API changes..."
    
    response=$(curl -s -X GET "$JARVIS_URL/api/pr-demo/analyze-sample/breaking")
    
    if [ $? -eq 0 ]; then
        log_success "Breaking change PR analysis completed!"
        echo
        log_ai "AI Analysis Results:"
        echo "$response" | jq -r '
            "📊 Analysis Summary:",
            "   PR ID: " + .prInfo.prId,
            "   Title: " + .prInfo.title,
            "   Risk Level: " + .riskAssessment.overallRiskLevel,
            "   Impact Level: " + .impactAnalysis.overallImpact,
            "   Breaking Changes: " + (.impactAnalysis.breakingChanges | length | tostring),
            "   Test Recommendations: " + (.testRecommendations | length | tostring),
            "",
            "⚠️ Breaking Change Insights:",
            (.insights[] | select(contains("Breaking") or contains("⚠️") or contains("🔄"))),
            "",
            "🧪 Critical Test Recommendations:",
            (.testRecommendations[] | select(.critical == true) | "   • " + .description + " (" + .testType + ")")
        '
    else
        log_error "Failed to analyze breaking change PR"
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Demo 3: Analyze New Feature PR
demo_new_feature_analysis() {
    log_demo "Demo 3: Analyzing New Feature PR (AI Recommendations)"
    echo
    
    log_info "Analyzing PR with new AI-powered recommendation feature..."
    
    response=$(curl -s -X GET "$JARVIS_URL/api/pr-demo/analyze-sample/feature")
    
    if [ $? -eq 0 ]; then
        log_success "New feature PR analysis completed!"
        echo
        log_ai "AI Analysis Results:"
        echo "$response" | jq -r '
            "📊 Analysis Summary:",
            "   PR ID: " + .prInfo.prId,
            "   Title: " + .prInfo.title,
            "   Risk Level: " + .riskAssessment.overallRiskLevel,
            "   Impact Level: " + .impactAnalysis.overallImpact,
            "   New Features: " + (.impactAnalysis.newFeatures | length | tostring),
            "   Test Recommendations: " + (.testRecommendations | length | tostring),
            "",
            "✨ Feature Insights:",
            (.insights[] | select(contains("feature") or contains("✨") or contains("AI") or contains("recommendation"))),
            "",
            "🧪 Recommended Test Types:",
            (.testRecommendations | group_by(.testType) | .[] | "   • " + .[0].testType + ": " + (length | tostring) + " recommendations")
        '
    else
        log_error "Failed to analyze new feature PR"
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Demo 4: Comprehensive Analysis Comparison
demo_comprehensive_analysis() {
    log_demo "Demo 4: Comprehensive PR Analysis Comparison"
    echo
    
    log_info "Running comprehensive analysis comparing all PR types..."
    
    response=$(curl -s -X GET "$JARVIS_URL/api/pr-demo/comprehensive-demo")
    
    if [ $? -eq 0 ]; then
        log_success "Comprehensive analysis completed!"
        echo
        log_ai "AI-Powered Comparison Analysis:"
        echo "$response" | jq -r '
            "📊 PR Analysis Comparison:",
            "",
            "🔒 Security PR:",
            "   Risk Level: " + .securityPR.summary.riskLevel,
            "   Impact Level: " + .securityPR.summary.impactLevel,
            "   Test Recommendations: " + (.securityPR.summary.testRecommendations | tostring),
            "   Security Concerns: " + (.securityPR.summary.securityConcerns | length | tostring),
            "",
            "⚠️ Breaking Change PR:",
            "   Risk Level: " + .breakingChangePR.summary.riskLevel,
            "   Impact Level: " + .breakingChangePR.summary.impactLevel,
            "   Test Recommendations: " + (.breakingChangePR.summary.testRecommendations | tostring),
            "   Breaking Changes: " + (.breakingChangePR.summary.breakingChanges | length | tostring),
            "",
            "✨ New Feature PR:",
            "   Risk Level: " + .newFeaturePR.summary.riskLevel,
            "   Impact Level: " + .newFeaturePR.summary.impactLevel,
            "   Test Recommendations: " + (.newFeaturePR.summary.testRecommendations | tostring),
            "   New Features: " + (.newFeaturePR.summary.newFeatures | length | tostring),
            "",
            "🎯 AI Insights:",
            "   Highest Risk: " + .comparison.highestRisk,
            "   Most Test Recommendations: " + .comparison.mostTestRecommendations,
            "",
            "💡 Key Insights:",
            (.comparison.insights[] | "   • " + .)
        '
    else
        log_error "Failed to run comprehensive analysis"
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Demo 5: Test Recommendations by Type
demo_test_recommendations() {
    log_demo "Demo 5: Test Recommendations by Type"
    echo
    
    log_info "Getting test recommendations for different test types..."
    
    # Get unit test recommendations
    log_info "Unit Test Recommendations:"
    response=$(curl -s -X POST "$JARVIS_URL/api/pr-analysis/recommendations-by-type" \
        -H "Content-Type: application/json" \
        -d '{
            "prId": "PR-001",
            "title": "Sample PR",
            "description": "Sample description",
            "author": "demo-user",
            "sourceBranch": "feature/sample",
            "targetBranch": "main",
            "repository": "demo-repo"
        }' \
        -G -d "testType=UNIT_TEST")
    
    if [ $? -eq 0 ]; then
        echo "$response" | jq -r '.[] | "   • " + .description + " (Priority: " + (.priority * 100 | floor | tostring) + "%)"'
    fi
    
    echo
    
    # Get integration test recommendations
    log_info "Integration Test Recommendations:"
    response=$(curl -s -X POST "$JARVIS_URL/api/pr-analysis/recommendations-by-type" \
        -H "Content-Type: application/json" \
        -d '{
            "prId": "PR-001",
            "title": "Sample PR",
            "description": "Sample description",
            "author": "demo-user",
            "sourceBranch": "feature/sample",
            "targetBranch": "main",
            "repository": "demo-repo"
        }' \
        -G -d "testType=INTEGRATION_TEST")
    
    if [ $? -eq 0 ]; then
        echo "$response" | jq -r '.[] | "   • " + .description + " (Priority: " + (.priority * 100 | floor | tostring) + "%)"'
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Demo 6: Risk Assessment
demo_risk_assessment() {
    log_demo "Demo 6: AI-Powered Risk Assessment"
    echo
    
    log_info "Performing risk assessment for security PR..."
    
    response=$(curl -s -X POST "$JARVIS_URL/api/pr-analysis/risk-assessment" \
        -H "Content-Type: application/json" \
        -d '{
            "prId": "PR-001",
            "title": "Add JWT authentication and authorization",
            "description": "Implement JWT-based authentication with role-based authorization",
            "author": "john.doe",
            "sourceBranch": "feature/jwt-auth",
            "targetBranch": "main",
            "repository": "microservice-testing-lab",
            "fileChanges": [
                {
                    "filePath": "user-service/src/main/java/com/kb/user/controller/AuthController.java",
                    "changeType": "MODIFIED",
                    "linesAdded": 45,
                    "linesDeleted": 12,
                    "keywords": ["security", "jwt", "auth"]
                }
            ]
        }')
    
    if [ $? -eq 0 ]; then
        log_success "Risk assessment completed!"
        echo
        log_ai "Risk Assessment Results:"
        echo "$response" | jq -r '
            "🚨 Risk Assessment:",
            "   Overall Risk Level: " + .overallRiskLevel,
            "   Risk Score: " + (.riskMetrics.risk_score * 100 | floor | tostring) + "%",
            "   Total Risk Factors: " + (.riskMetrics.total_factors | tostring),
            "   High Risk Factors: " + (.riskMetrics.high_risk_factors | tostring),
            "",
            "⚠️ Risk Factors:",
            (.riskFactors[] | "   • " + .),
            "",
            "🛡️ Mitigation Strategies:",
            (.mitigationStrategies[] | "   • " + .),
            "",
            "⚠️ Warnings:",
            (.warnings[] | "   • " + .)
        '
    else
        log_error "Failed to perform risk assessment"
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Main demo function
main() {
    echo "================================================"
    echo "    🤖 Jarvis PR Analysis Demo"
    echo "    Intelligent Testing Recommendations"
    echo "================================================"
    echo
    log_demo "Welcome to the Jarvis PR Analysis Demo!"
    log_demo "This demo showcases AI-powered PR analysis and intelligent testing recommendations."
    echo
    
    # Check if Jarvis is running
    if ! check_jarvis_health; then
        log_error "Please start Jarvis Core first:"
        log_info "cd microservice-testing-lab/jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
        exit 1
    fi
    
    echo
    log_demo "Starting PR Analysis Demonstrations..."
    echo
    
    # Run all demos
    demo_security_pr_analysis
    demo_breaking_change_analysis
    demo_new_feature_analysis
    demo_comprehensive_analysis
    demo_test_recommendations
    demo_risk_assessment
    
    echo
    echo "================================================"
    echo -e "${GREEN}🎉 PR Analysis Demo Completed!${NC}"
    echo "================================================"
    echo
    echo -e "${BLUE}Demo Summary:${NC}"
    echo "• ✅ Security PR Analysis with JWT authentication"
    echo "• ✅ Breaking Change PR Analysis with API refactoring"
    echo "• ✅ New Feature PR Analysis with AI recommendations"
    echo "• ✅ Comprehensive Analysis Comparison"
    echo "• ✅ Test Recommendations by Type"
    echo "• ✅ AI-Powered Risk Assessment"
    echo
    echo -e "${YELLOW}Key Features Demonstrated:${NC}"
    echo "• 🤖 AI-powered code pattern analysis"
    echo "• 🔍 Intelligent impact assessment"
    echo "• 🚨 Risk scoring and mitigation strategies"
    echo "• 🧪 Smart test recommendations"
    echo "• 📊 Comprehensive analysis metrics"
    echo "• 🎯 Priority-based testing guidance"
    echo
    echo -e "${PURPLE}Next Steps:${NC}"
    echo "• Integrate with your CI/CD pipeline"
    echo "• Customize risk factors for your organization"
    echo "• Add custom test patterns and recommendations"
    echo "• Connect with your version control system"
    echo
}

# Run the demo
main "$@"
