#!/bin/bash

# Jarvis AI Chaos Flow Demo
# This script demonstrates the AI-powered chaos engineering flow

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

# Wait for Jarvis to be ready
wait_for_jarvis() {
    log_info "Waiting for Jarvis Core to be ready..."
    
    for i in {1..30}; do
        if curl -s "$JARVIS_URL/health" > /dev/null 2>&1; then
            log_success "Jarvis Core is ready!"
            return 0
        fi
        echo -n "."
        sleep 2
    done
    
    log_error "Jarvis Core is not responding. Please start it first:"
    log_info "cd ../jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
    exit 1
}

# Test 1: Basic Jarvis Health
test_jarvis_health() {
    log_jarvis "Testing Jarvis AI health..."
    
    echo "Health Check:"
    curl -s "$JARVIS_URL/health" | jq . 2>/dev/null || curl -s "$JARVIS_URL/health"
    echo
    
    echo "Root Endpoint:"
    curl -s "$JARVIS_URL/" | jq . 2>/dev/null || curl -s "$JARVIS_URL/"
    echo
    
    log_success "Jarvis health check completed"
}

# Test 2: AI Chaos Decision Making
test_ai_chaos_decisions() {
    log_jarvis "Testing AI-powered chaos decision making..."
    
    echo "Test 1: Simple chaos request"
    curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "run chaos test on user service",
            "context": {
                "service": "user-service",
                "environment": "staging",
                "priority": "medium"
            }
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "run chaos test on user service",
            "context": {
                "service": "user-service",
                "environment": "staging",
                "priority": "medium"
            }
        }'
    echo
    
    echo "Test 2: Network chaos request"
    curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "inject network latency into product service for 30 seconds",
            "context": {
                "service": "product-service",
                "chaos_type": "network_latency",
                "duration": "30s",
                "intensity": "medium"
            }
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "inject network latency into product service for 30 seconds",
            "context": {
                "service": "product-service",
                "chaos_type": "network_latency",
                "duration": "30s",
                "intensity": "medium"
            }
        }'
    echo
    
    log_success "AI chaos decision tests completed"
}

# Test 3: Microservice Integration
test_microservice_integration() {
    log_jarvis "Testing microservice integration..."
    
    echo "Test 1: Microservice health check"
    curl -s -X GET "$JARVIS_URL/api/microservices/health" | jq . 2>/dev/null || curl -s -X GET "$JARVIS_URL/api/microservices/health"
    echo
    
    echo "Test 2: Performance analysis"
    curl -s -X POST "$JARVIS_URL/api/microservices/performance-tests" \
        -H "Content-Type: application/json" \
        -d '{
            "services": ["user-service", "product-service"],
            "parameters": {
                "duration": "60s",
                "load": "medium"
            }
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/microservices/performance-tests" \
        -H "Content-Type: application/json" \
        -d '{
            "services": ["user-service", "product-service"],
            "parameters": {
                "duration": "60s",
                "load": "medium"
            }
        }'
    echo
    
    log_success "Microservice integration tests completed"
}

# Test 4: End-to-End Chaos Flow
test_end_to_end_flow() {
    log_jarvis "Testing complete end-to-end chaos flow..."
    
    echo "Step 1: System health assessment"
    curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "assess current system health and identify weak points",
            "context": {
                "assessment_type": "comprehensive",
                "include_metrics": true
            }
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "assess current system health and identify weak points",
            "context": {
                "assessment_type": "comprehensive",
                "include_metrics": true
            }
        }'
    echo
    
    echo "Step 2: AI chaos decision"
    curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "decide on optimal chaos experiment based on system state",
            "context": {
                "target_services": ["user-service"],
                "chaos_intensity": "medium",
                "safety_mode": true
            }
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "decide on optimal chaos experiment based on system state",
            "context": {
                "target_services": ["user-service"],
                "chaos_intensity": "medium",
                "safety_mode": true
            }
        }'
    echo
    
    echo "Step 3: Chaos execution"
    curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "execute recommended chaos experiment with monitoring",
            "context": {
                "experiment_type": "network_latency",
                "duration": "30s",
                "auto_rollback": true
            }
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "execute recommended chaos experiment with monitoring",
            "context": {
                "experiment_type": "network_latency",
                "duration": "30s",
                "auto_rollback": true
            }
        }'
    echo
    
    log_success "End-to-end chaos flow test completed"
}

# Test 5: Safety Mechanisms
test_safety_mechanisms() {
    log_jarvis "Testing safety and rollback mechanisms..."
    
    echo "Test 1: Safety threshold monitoring"
    curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
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
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
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
        }'
    echo
    
    echo "Test 2: Emergency stop"
    curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "emergency stop all chaos activities",
            "context": {
                "emergency_reason": "critical_system_failure",
                "stop_all_experiments": true,
                "restore_normal_state": true
            }
        }' | jq . 2>/dev/null || curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d '{
            "command": "emergency stop all chaos activities",
            "context": {
                "emergency_reason": "critical_system_failure",
                "stop_all_experiments": true,
                "restore_normal_state": true
            }
        }'
    echo
    
    log_success "Safety mechanism tests completed"
}

# Display demo summary
display_demo_summary() {
    echo
    log_info "=========================================="
    log_info "    Jarvis AI Chaos Flow Demo Summary"
    log_info "=========================================="
    echo
    echo "🎯 Demo Completed Successfully!"
    echo
    echo "✅ Capabilities Demonstrated:"
    echo "  • AI-powered chaos decision making"
    echo "  • Natural language command processing"
    echo "  • Microservice health monitoring"
    echo "  • Performance analysis"
    echo "  • End-to-end chaos orchestration"
    echo "  • Safety controls and emergency stops"
    echo
    echo "🚀 Next Steps:"
    echo "  1. Deploy to Kubernetes cluster"
    echo "  2. Install Litmus Chaos: ./scripts/setup/install-litmus.sh"
    echo "  3. Deploy chaos daemon: ./scripts/setup/deploy-chaos-daemon.sh"
    echo "  4. Run real chaos experiments"
    echo "  5. Monitor system resilience"
    echo
    echo "📊 Integration Points:"
    echo "  • Jarvis AI ↔ Chaos DaemonSet"
    echo "  • Chaos DaemonSet ↔ Microservices"
    echo "  • Real-time monitoring and rollback"
    echo "  • AI learning from experiment results"
    echo
}

# Main demo function
main() {
    echo "=========================================="
    echo "    Jarvis AI Chaos Flow Demo"
    echo "=========================================="
    echo
    
    wait_for_jarvis
    
    echo
    log_info "Starting Jarvis AI chaos flow demo..."
    echo
    
    test_jarvis_health
    echo
    
    test_ai_chaos_decisions
    echo
    
    test_microservice_integration
    echo
    
    test_end_to_end_flow
    echo
    
    test_safety_mechanisms
    echo
    
    display_demo_summary
}

# Run main function
main "$@"
