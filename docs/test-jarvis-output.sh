#!/bin/bash

# Test Jarvis CLI Output
# This script demonstrates the improved output formatting

echo "🧪 Testing Jarvis AI CLI Output"
echo "================================"
echo

# Test 1: Health Check
echo "Test 1: Health Check"
echo "-------------------"
curl -s -X POST http://localhost:8085/api/jarvis/command \
  -H "Content-Type: application/json" \
  -d '{"command": "check health"}' | jq .
echo

# Test 2: Chaos Test
echo "Test 2: Chaos Test for Order Service"
echo "-----------------------------------"
curl -s -X POST http://localhost:8085/api/jarvis/command \
  -H "Content-Type: application/json" \
  -d '{"command": "run chaos test on order-service"}' | jq .
echo

# Test 3: Performance Analysis
echo "Test 3: Performance Analysis"
echo "---------------------------"
curl -s -X POST http://localhost:8085/api/jarvis/command \
  -H "Content-Type: application/json" \
  -d '{"command": "run performance analysis"}' | jq .
echo

echo "✅ All tests completed!"
echo
echo "Now try the interactive CLI: ./jarvis-cli.sh"
