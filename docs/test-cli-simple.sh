#!/bin/bash

# Simple test for Jarvis CLI
echo "Testing Jarvis CLI..."

# Test the send_command function directly
JARVIS_URL="http://localhost:8085"

echo "Sending test command..."
response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
    -H "Content-Type: application/json" \
    -d '{"command": "check health"}')

echo "Response received:"
echo "$response" | jq -r '.message'
echo
echo "Status:"
echo "$response" | jq -r '.status'
echo
echo "Action:"
echo "$response" | jq -r '.action.type'
