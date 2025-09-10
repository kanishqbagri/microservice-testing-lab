#!/bin/bash

# Simple Jarvis CLI that works
JARVIS_URL="http://localhost:8085"
USER_NAME="Kanishq"

echo "=========================================="
echo "    🤖 Jarvis AI Natural Language CLI"
echo "=========================================="
echo
echo "�� Welcome back $USER_NAME!"
echo
echo "Type English commands directly. Type 'quit' to leave."
echo

# Check if Jarvis is running
if ! curl -s "$JARVIS_URL/health" > /dev/null 2>&1; then
    echo "❌ Jarvis Core is not running at $JARVIS_URL"
    exit 1
fi

echo "✅ Connected to Jarvis AI at $JARVIS_URL"
echo

while true; do
    echo -e "\033[35mJarvis>\033[0m "
    read -r user_input
    
    if [ -z "$user_input" ]; then
        continue
    fi
    
    if [ "$user_input" = "quit" ] || [ "$user_input" = "exit" ]; then
        echo "👋 Goodbye $USER_NAME! Have a great day!"
        exit 0
    fi
    
    echo
    echo "🤖 Sending command: $user_input"
    echo "⏳ Processing..."
    
    # Send command to Jarvis AI
    response=$(curl -s -X POST "$JARVIS_URL/api/jarvis/command" \
        -H "Content-Type: application/json" \
        -d "{\"command\": \"$user_input\"}")
    
    echo "🤖 Jarvis AI Response:"
    echo "----------------------------------------"
    
    # Extract and display message using jq
    if command -v jq &> /dev/null; then
        message=$(echo "$response" | jq -r '.message // empty' 2>/dev/null)
        if [ ! -z "$message" ] && [ "$message" != "null" ]; then
            echo -e "\033[33m💬 $message\033[0m"
            echo
        fi
        
        status=$(echo "$response" | jq -r '.status // empty' 2>/dev/null)
        if [ ! -z "$status" ] && [ "$status" != "null" ]; then
            echo -e "\033[34m📊 Status: $status\033[0m"
            echo
        fi
        
        action_type=$(echo "$response" | jq -r '.action.type // empty' 2>/dev/null)
        if [ ! -z "$action_type" ] && [ "$action_type" != "null" ]; then
            echo -e "\033[35m🎯 Action: $action_type\033[0m"
            echo
        fi
    else
        echo "$response"
    fi
    
    echo "----------------------------------------"
    echo
done
