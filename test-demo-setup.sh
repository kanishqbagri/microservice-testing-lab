#!/bin/bash

# Quick Test Script for Demo Setup
# Verifies that all components are ready for the demo

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo "🧪 Testing Demo Setup"
echo "===================="
echo

# Test 1: Check if Jarvis Core is running
echo "1. Testing Jarvis Core connectivity..."
if curl -s http://localhost:8085/health > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Jarvis Core is running${NC}"
else
    echo -e "${RED}❌ Jarvis Core is not running${NC}"
    echo -e "${YELLOW}   Start it with: cd ../jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'${NC}"
    exit 1
fi

# Test 2: Check if demo scripts exist
echo "2. Checking demo scripts..."
if [ -f "demo-jarvis-simple.sh" ]; then
    echo -e "${GREEN}✅ demo-jarvis-simple.sh exists${NC}"
else
    echo -e "${RED}❌ demo-jarvis-simple.sh not found${NC}"
fi

if [ -f "jarvis-cli.sh" ]; then
    echo -e "${GREEN}✅ jarvis-cli.sh exists${NC}"
else
    echo -e "${RED}❌ jarvis-cli.sh not found${NC}"
fi

# Test 3: Check dependencies
echo "3. Checking dependencies..."
if command -v jq &> /dev/null; then
    echo -e "${GREEN}✅ jq is available${NC}"
else
    echo -e "${YELLOW}⚠️  jq not found (JSON parsing will use fallback)${NC}"
fi

if command -v curl &> /dev/null; then
    echo -e "${GREEN}✅ curl is available${NC}"
else
    echo -e "${RED}❌ curl not found${NC}"
    exit 1
fi

# Test 4: Test Jarvis AI command
echo "4. Testing Jarvis AI command..."
response=$(curl -s -X POST http://localhost:8085/api/jarvis/command \
    -H "Content-Type: application/json" \
    -d '{"command": "check health"}')

if [ ! -z "$response" ]; then
    echo -e "${GREEN}✅ Jarvis AI command successful${NC}"
    echo -e "${BLUE}   Response length: ${#response} characters${NC}"
else
    echo -e "${RED}❌ Jarvis AI command failed${NC}"
    exit 1
fi

# Test 5: Test JSON parsing
echo "5. Testing JSON parsing..."
if command -v jq &> /dev/null; then
    message=$(echo "$response" | jq -r '.message // empty' 2>/dev/null)
    if [ ! -z "$message" ] && [ "$message" != "null" ]; then
        echo -e "${GREEN}✅ JSON parsing successful${NC}"
        echo -e "${BLUE}   Message preview: ${message:0:50}...${NC}"
    else
        echo -e "${YELLOW}⚠️  JSON parsing issue (no message found)${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  Skipping JSON parsing test (jq not available)${NC}"
fi

echo
echo "🎉 Demo Setup Test Completed!"
echo
echo "Next steps:"
echo -e "${GREEN}• Run the demo: ./demo-jarvis-simple.sh${NC}"
echo -e "${GREEN}• Try interactive CLI: ./jarvis-cli.sh${NC}"
echo -e "${GREEN}• Read the guide: cat DEMO_GUIDE.md${NC}"
echo
