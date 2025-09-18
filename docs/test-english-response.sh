#!/bin/bash

# Test English Response Formatting
echo "🧪 Testing English Response Formatting"
echo "======================================"
echo

# Test the response parsing
response='{"message":"I understand you want to perform system health check.\n\nBased on my analysis:\n• System health issues detected - may impact test reliability\n\nI\'m Performing system health check.\nThis should take approximately Less than 1 minute.\n","status":"PROCESSING","action":{"type":"HEALTH_CHECK","description":"Performing system health check","estimatedTime":"Less than 1 minute","priority":"MEDIUM","confidence":0.87}}'

echo "Original JSON Response:"
echo "$response"
echo

echo "Extracted English Response:"
echo "----------------------------------------"

# Extract message
if echo "$response" | grep -q "message"; then
    message=$(echo "$response" | grep -o '"message":"[^"]*"' | sed 's/"message":"//;s/"$//' | sed 's/\\n/\n/g')
    echo -e "\033[33m💬 $message\033[0m"
    echo
fi

# Extract status
if echo "$response" | grep -q "status"; then
    status=$(echo "$response" | grep -o '"status":"[^"]*"' | sed 's/"status":"//;s/"$//')
    echo -e "\033[34m📊 Status: $status\033[0m"
    echo
fi

# Extract action details
if echo "$response" | grep -q "action"; then
    action_type=$(echo "$response" | grep -o '"type":"[^"]*"' | sed 's/"type":"//;s/"$//')
    description=$(echo "$response" | grep -o '"description":"[^"]*"' | sed 's/"description":"//;s/"$//')
    
    echo -e "\033[35m🎯 Action: $action_type\033[0m"
    if [ ! -z "$description" ]; then
        echo -e "\033[36m   Description: $description\033[0m"
    fi
    
    # Extract estimated time
    estimated_time=$(echo "$response" | grep -o '"estimatedTime":"[^"]*"' | sed 's/"estimatedTime":"//;s/"$//')
    if [ ! -z "$estimated_time" ]; then
        echo -e "\033[32m   ⏱️  Estimated Time: $estimated_time\033[0m"
    fi
    
    # Extract priority
    priority=$(echo "$response" | grep -o '"priority":"[^"]*"' | sed 's/"priority":"//;s/"$//')
    if [ ! -z "$priority" ]; then
        echo -e "\033[33m   🎯 Priority: $priority\033[0m"
    fi
    
    # Extract confidence
    confidence=$(echo "$response" | grep -o '"confidence":[0-9.]*' | sed 's/"confidence"://')
    if [ ! -z "$confidence" ]; then
        confidence_percent=$(echo "$confidence * 100" | awk '{printf "%.0f", $1}')
        echo -e "\033[34m   🎯 Confidence: ${confidence_percent}%\033[0m"
    fi
    
    echo
fi

echo "----------------------------------------"
echo "✅ English response formatting test completed!"
