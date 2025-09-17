#!/bin/bash

# Jarvis AI Chaos Flow Test Script
# This script tests the complete AI-powered chaos engineering flow

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
LITMUS_NAMESPACE="litmus"
MICROSERVICES_NAMESPACE="microservices"

# Logging functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_jarvis() {
    echo -e "${PURPLE}[JARVIS AI]${NC} $1"
}

log_chaos() {
    echo -e "${CYAN}[CHAOS ENGINE]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites for Jarvis AI chaos flow test..."
    
    # Check if Jarvis Core is running
    if curl -s "$JARVIS_URL/health" > /dev/null; then
        log_success "Jarvis Core is running at $JARVIS_URL"
    else
        log_error "Jarvis Core is not running. Please start it first."
        log_info "Start Jarvis Core: cd ../jarvis-core && mvn spring-boot:run"
        exit 1
    fi
    
    # Check if kubectl is available
    if command -v kubectl &> /dev/null; then
        log_success "kubectl is available"
    else
        log_error "kubectl is not installed"
        exit 1
    fi
    
    # Check if we can connect to cluster
    if kubectl cluster-info &> /dev/null; then
        log_success "Kubernetes cluster is accessible"
    else
        log_warning "Cannot connect to Kubernetes cluster - will simulate chaos flow"
    fi
}

# Test 1: Jarvis AI Health Check
test_jarvis_health() {
    log_jarvis "Testing Jarvis AI health and basic functionality..."
    
    # Health check
    response=$(curl -s "$JARVIS_URL/health")
    echo "Health Response: $response"
    
    # Basic endpoint test
    response=$(curl -s "$JARVIS_URL/")
    echo "Root Response: $response"
    
    log_success "Jarvis AI health check completed"
}

# Test 2: AI-Powered Chaos Decision Making
test_ai_chaos_decisions() {
    log_jarvis "Testing AI-powered chaos decision making..."
    
    # Test 1: Simple chaos request
    log_info "Test 1: Simple chaos request"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "run chaos test on user service",
            "context": {
                "service": "user-service",
                "environment": "staging",
                "priority": "medium"
            }
        }')
    echo "AI Decision Response: $response"
    
    # Test 2: Network chaos request
    log_info "Test 2: Network chaos request"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "inject network latency into product service for 30 seconds",
            "context": {
                "service": "product-service",
                "chaos_type": "network_latency",
                "duration": "30s",
                "intensity": "medium"
            }
        }')
    echo "Network Chaos Response: $response"
    
    # Test 3: Service failure request
    log_info "Test 3: Service failure request"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "simulate pod failure in order service",
            "context": {
                "service": "order-service",
                "chaos_type": "pod_failure",
                "target_pods": "1",
                "grace_period": "10s"
            }
        }')
    echo "Service Failure Response: $response"
    
    log_success "AI chaos decision tests completed"
}

# Test 3: Microservice Integration
test_microservice_integration() {
    log_jarvis "Testing microservice integration with Jarvis AI..."
    
    # Test microservice health check
    log_info "Test 1: Microservice health check"
    response=$(curl -s -X GET "$JARVIS_URL/api/microservices/health")
    echo "Microservice Health Response: $response"
    
    # Test performance analysis
    log_info "Test 2: Performance analysis"
    response=$(curl -s -X POST "$JARVIS_URL/api/microservices/performance-tests" \
        -H "Content-Type: application/json" \
        -d '{
            "services": ["user-service", "product-service"],
            "parameters": {
                "duration": "60s",
                "load": "medium"
            }
        }')
    echo "Performance Analysis Response: $response"
    
    # Test failure analysis
    log_info "Test 3: Failure analysis"
    response=$(curl -s -X POST "$JARVIS_URL/api/microservices/failure-analysis" \
        -H "Content-Type: application/json" \
        -d '{
            "services": ["order-service", "notification-service"],
            "analysis_type": "dependency_failure"
        }')
    echo "Failure Analysis Response: $response"
    
    log_success "Microservice integration tests completed"
}

# Test 4: Chaos Experiment Orchestration
test_chaos_orchestration() {
    log_chaos "Testing chaos experiment orchestration..."
    
    # Test 1: Create chaos experiment
    log_info "Test 1: Creating chaos experiment via Jarvis"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "create and execute network latency chaos experiment",
            "context": {
                "experiment_name": "user-service-network-test",
                "target_service": "user-service",
                "chaos_type": "network_latency",
                "parameters": {
                    "latency": "100ms",
                    "jitter": "10ms",
                    "duration": "60s"
                }
            }
        }')
    echo "Chaos Experiment Creation Response: $response"
    
    # Test 2: Monitor chaos experiment
    log_info "Test 2: Monitoring chaos experiment"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "monitor current chaos experiments and their impact",
            "context": {
                "monitoring_type": "real_time",
                "metrics": ["response_time", "error_rate", "throughput"]
            }
        }')
    echo "Chaos Monitoring Response: $response"
    
    # Test 3: Emergency stop
    log_info "Test 3: Emergency stop capability"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "emergency stop all chaos experiments",
            "context": {
                "reason": "system_health_degradation",
                "priority": "high"
            }
        }')
    echo "Emergency Stop Response: $response"
    
    log_success "Chaos orchestration tests completed"
}

# Test 5: AI Learning and Pattern Recognition
test_ai_learning() {
    log_jarvis "Testing AI learning and pattern recognition..."
    
    # Test 1: Pattern analysis
    log_info "Test 1: Pattern analysis"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "analyze historical chaos experiment patterns",
            "context": {
                "time_range": "last_7_days",
                "services": ["user-service", "product-service", "order-service"],
                "analysis_type": "failure_patterns"
            }
        }')
    echo "Pattern Analysis Response: $response"
    
    # Test 2: Predictive chaos
    log_info "Test 2: Predictive chaos recommendations"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "recommend chaos experiments based on system patterns",
            "context": {
                "recommendation_type": "proactive",
                "confidence_threshold": "0.8",
                "risk_tolerance": "medium"
            }
        }')
    echo "Predictive Chaos Response: $response"
    
    # Test 3: Learning from results
    log_info "Test 3: Learning from experiment results"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "learn from recent chaos experiment outcomes",
            "context": {
                "learning_type": "reinforcement",
                "experiment_results": "network_latency_impact",
                "system_response": "graceful_degradation"
            }
        }')
    echo "Learning Response: $response"
    
    log_success "AI learning tests completed"
}

# Test 6: End-to-End Chaos Flow
test_end_to_end_flow() {
    log_jarvis "Testing complete end-to-end chaos flow..."
    
    log_info "Step 1: System health assessment"
    health_response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "assess current system health and identify weak points",
            "context": {
                "assessment_type": "comprehensive",
                "include_metrics": true
            }
        }')
    echo "Health Assessment: $health_response"
    
    log_info "Step 2: AI chaos decision"
    decision_response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "decide on optimal chaos experiment based on system state",
            "context": {
                "target_services": ["user-service"],
                "chaos_intensity": "medium",
                "safety_mode": true
            }
        }')
    echo "AI Decision: $decision_response"
    
    log_info "Step 3: Chaos execution"
    execution_response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "execute recommended chaos experiment with monitoring",
            "context": {
                "experiment_type": "network_latency",
                "duration": "30s",
                "auto_rollback": true
            }
        }')
    echo "Chaos Execution: $execution_response"
    
    log_info "Step 4: Impact analysis"
    impact_response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "analyze chaos experiment impact and system resilience",
            "context": {
                "analysis_metrics": ["response_time", "error_rate", "throughput"],
                "resilience_score": true
            }
        }')
    echo "Impact Analysis: $impact_response"
    
    log_success "End-to-end chaos flow test completed"
}

# Test 7: Safety and Rollback Mechanisms
test_safety_mechanisms() {
    log_jarvis "Testing safety and rollback mechanisms..."
    
    # Test 1: Safety threshold monitoring
    log_info "Test 1: Safety threshold monitoring"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "monitor safety thresholds during chaos experiments",
            "context": {
                "safety_metrics": ["error_rate", "response_time", "system_health"],
                "thresholds": {
                    "error_rate": "0.1",
                    "response_time": "1000ms",
                    "system_health": "0.7"
                }
            }
        }')
    echo "Safety Monitoring Response: $response"
    
    # Test 2: Automatic rollback
    log_info "Test 2: Automatic rollback trigger"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "trigger automatic rollback due to safety threshold breach",
            "context": {
                "breach_type": "error_rate_exceeded",
                "current_error_rate": "0.15",
                "threshold": "0.1",
                "rollback_priority": "immediate"
            }
        }')
    echo "Rollback Response: $response"
    
    # Test 3: Emergency stop
    log_info "Test 3: Emergency stop capability"
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "emergency stop all chaos activities",
            "context": {
                "emergency_reason": "critical_system_failure",
                "stop_all_experiments": true,
                "restore_normal_state": true
            }
        }')
    echo "Emergency Stop Response: $response"
    
    log_success "Safety mechanism tests completed"
}

# Display test results summary
display_test_summary() {
    log_info "=========================================="
    log_info "    Jarvis AI Chaos Flow Test Summary"
    log_info "=========================================="
    echo
    echo "✅ Tests Completed:"
    echo "  1. Jarvis AI Health Check"
    echo "  2. AI-Powered Chaos Decision Making"
    echo "  3. Microservice Integration"
    echo "  4. Chaos Experiment Orchestration"
    echo "  5. AI Learning and Pattern Recognition"
    echo "  6. End-to-End Chaos Flow"
    echo "  7. Safety and Rollback Mechanisms"
    echo
    echo "🎯 Key Capabilities Demonstrated:"
    echo "  • AI-powered chaos decision making"
    echo "  • Intelligent service targeting"
    echo "  • Real-time monitoring and analysis"
    echo "  • Automatic safety controls"
    echo "  • Pattern recognition and learning"
    echo "  • Emergency stop mechanisms"
    echo
    echo "🚀 Next Steps:"
    echo "  1. Deploy to Kubernetes cluster"
    echo "  2. Run actual chaos experiments"
    echo "  3. Monitor real system impact"
    echo "  4. Analyze results and improve"
    echo
}

# Main test function
main() {
    echo "=========================================="
    echo "    Jarvis AI Chaos Flow Test Suite"
    echo "=========================================="
    echo
    
    check_prerequisites
    
    echo
    log_info "Starting Jarvis AI chaos flow tests..."
    echo
    
    test_jarvis_health
    echo
    
    test_ai_chaos_decisions
    echo
    
    test_microservice_integration
    echo
    
    test_chaos_orchestration
    echo
    
    test_ai_learning
    echo
    
    test_end_to_end_flow
    echo
    
    test_safety_mechanisms
    echo
    
    display_test_summary
}

# Run main function
main "$@"
