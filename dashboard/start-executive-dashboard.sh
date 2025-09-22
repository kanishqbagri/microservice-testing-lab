#!/bin/bash

# Executive Dashboard Deployment Script
# This script starts the executive dashboard on port 8087

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 Starting Jarvis Executive Dashboard${NC}"
echo -e "${BLUE}=====================================${NC}"
echo

# Check if port 8087 is already in use
if lsof -i :8087 > /dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  Port 8087 is already in use. Stopping existing process...${NC}"
    pkill -f "python3 -m http.server 8087" || true
    sleep 2
fi

# Start the HTTP server
echo -e "${GREEN}📡 Starting HTTP server on port 8087...${NC}"
cd "$(dirname "$0")"
python3 -m http.server 8087 &

# Wait a moment for the server to start
sleep 3

# Check if the server is running
if curl -s -o /dev/null -w "%{http_code}" http://localhost:8087 | grep -q "200"; then
    echo -e "${GREEN}✅ Executive Dashboard is now running!${NC}"
    echo
    echo -e "${BLUE}📊 Dashboard URLs:${NC}"
    echo -e "   Executive Dashboard: ${GREEN}http://localhost:8087/executive-dashboard.html${NC}"
    echo -e "   Standard Dashboard:  ${GREEN}http://localhost:8087/index.html${NC}"
    echo -e "   Run Details:         ${GREEN}http://localhost:8087/run-details.html${NC}"
    echo
    echo -e "${BLUE}🎯 Executive Dashboard Features:${NC}"
    echo -e "   • Smart Scorecards - Overall health, velocity, quality metrics"
    echo -e "   • Intelligent Impact Analysis - PR-based impact assessment"
    echo -e "   • PR Quality Insights - Code coverage, stability, performance"
    echo -e "   • Anomaly Detection - Performance and failure pattern analysis"
    echo
    echo -e "${YELLOW}💡 To stop the dashboard, run: pkill -f 'python3 -m http.server 8087'${NC}"
    echo
    echo -e "${GREEN}🎉 Dashboard is ready for executive review!${NC}"
else
    echo -e "${RED}❌ Failed to start the dashboard. Please check the logs.${NC}"
    exit 1
fi
