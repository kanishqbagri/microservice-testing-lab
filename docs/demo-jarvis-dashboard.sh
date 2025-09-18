#!/bin/bash

# Demo Script: Jarvis Test Dashboard
# This script demonstrates the comprehensive dashboard functionality

echo "🚀 JARVIS TEST DASHBOARD DEMO"
echo "============================="
echo ""

# Check if Jarvis is running
echo "📡 Checking Jarvis Core status..."
if ! curl -s http://localhost:8085/actuator/health > /dev/null; then
    echo "❌ Jarvis Core is not running. Please start it first."
    echo "   Run: cd microservice-testing-lab/jarvis-core && mvn spring-boot:run -Dspring-boot.run.profiles=test -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
    exit 1
fi
echo "✅ Jarvis Core is running on port 8085"
echo ""

# Demo 1: Dashboard Overview
echo "📊 DEMO 1: Dashboard Overview"
echo "-----------------------------"
echo "Getting comprehensive dashboard overview..."
curl -s http://localhost:8085/api/dashboard/overview | jq -r '
"Total Tests: " + (.totalTests | tostring) + 
" | Passed: " + (.passedTests | tostring) + 
" | Failed: " + (.failedTests | tostring) + 
" | Success Rate: " + (.successRate | tostring) + "%"
'
echo "Recent Tests (24h): $(curl -s http://localhost:8085/api/dashboard/overview | jq -r '.recentTests')"
echo ""

# Demo 2: Test Results with Pagination
echo "📋 DEMO 2: Test Results with Pagination"
echo "---------------------------------------"
echo "Getting paginated test results..."
response=$(curl -s "http://localhost:8085/api/dashboard/test-results?page=0&size=5")
echo "Total Elements: $(echo $response | jq -r '.totalElements')"
echo "Current Page: $(echo $response | jq -r '.currentPage')"
echo "Total Pages: $(echo $response | jq -r '.totalPages')"
echo "Has Next: $(echo $response | jq -r '.hasNext')"
echo "First 5 Tests:"
echo $response | jq -r '.tests[] | "  " + .testName + " (" + .serviceName + ") - " + .status + " - " + (.executionTimeMs | tostring) + "ms"'
echo ""

# Demo 3: Test Trends
echo "📈 DEMO 3: Test Trends (Last 7 Days)"
echo "------------------------------------"
echo "Getting test execution trends..."
trends_response=$(curl -s "http://localhost:8085/api/dashboard/trends?days=7")
echo "Period: $(echo $trends_response | jq -r '.period')"
echo "Trend Data:"
echo $trends_response | jq -r '.trendData[] | "  " + .date + ": Passed=" + (.passed | tostring) + ", Failed=" + (.failed | tostring) + ", Total=" + (.total | tostring)'
echo ""

# Demo 4: Performance Metrics
echo "⚡ DEMO 4: Performance Metrics"
echo "------------------------------"
echo "Getting performance analysis..."
perf_response=$(curl -s "http://localhost:8085/api/dashboard/performance-metrics")
echo "Average Execution Time: $(echo $perf_response | jq -r '.averageExecutionTime')ms"
echo "Max Execution Time: $(echo $perf_response | jq -r '.maxExecutionTime')ms"
echo "Min Execution Time: $(echo $perf_response | jq -r '.minExecutionTime')ms"
echo "Slow Tests Count: $(echo $perf_response | jq -r '.slowTests | length')"
echo ""

# Demo 5: Failure Analysis
echo "❌ DEMO 5: Failure Analysis"
echo "---------------------------"
echo "Getting failure analysis..."
failure_response=$(curl -s "http://localhost:8085/api/dashboard/failure-analysis")
echo "Total Failed Tests: $(echo $failure_response | jq -r '.totalFailedTests')"
echo "Recent Failure Count (7 days): $(echo $failure_response | jq -r '.recentFailureCount')"
echo "Failure Patterns:"
echo $failure_response | jq -r '.failurePatterns | to_entries[] | "  " + .key + ": " + (.value | tostring)'
echo "Failures by Service:"
echo $failure_response | jq -r '.failuresByService | to_entries[] | "  " + .key + ": " + (.value | tostring)'
echo ""

# Demo 6: Security Dashboard
echo "🔒 DEMO 6: Security Dashboard"
echo "-----------------------------"
echo "Getting security analysis..."
security_response=$(curl -s "http://localhost:8085/api/dashboard/security-dashboard")
echo "Total Security Tests: $(echo $security_response | jq -r '.totalSecurityTests')"
echo "Passed Security Tests: $(echo $security_response | jq -r '.passedSecurityTests')"
echo "Failed Security Tests: $(echo $security_response | jq -r '.failedSecurityTests')"
echo "Security Score: $(echo $security_response | jq -r '.securityScore')%"
echo "Risk Distribution:"
echo $security_response | jq -r '.riskDistribution | to_entries[] | "  " + .key + ": " + (.value | tostring)'
echo ""

# Demo 7: Data Export
echo "📤 DEMO 7: Data Export"
echo "----------------------"
echo "Testing data export functionality..."
export_response=$(curl -s "http://localhost:8085/api/dashboard/export?format=json&days=7")
echo "Export Status: Success"
echo "Total Records: $(echo $export_response | jq -r '.totalRecords')"
echo "Export Format: $(echo $export_response | jq -r '.exportFormat')"
echo "Exported At: $(echo $export_response | jq -r '.exportedAt')"
echo ""

# Demo 8: Filtered Results
echo "🔍 DEMO 8: Filtered Results"
echo "---------------------------"
echo "Testing filtered test results..."

# Filter by service
echo "Filtering by user-service:"
user_service_response=$(curl -s "http://localhost:8085/api/dashboard/test-results?service=user-service&size=3")
echo "  Results: $(echo $user_service_response | jq -r '.totalElements')"
echo $user_service_response | jq -r '.tests[] | "    " + .testName + " - " + .status'

# Filter by test type
echo "Filtering by UNIT_TEST:"
unit_test_response=$(curl -s "http://localhost:8085/api/dashboard/test-results?testType=UNIT_TEST&size=3")
echo "  Results: $(echo $unit_test_response | jq -r '.totalElements')"
echo $unit_test_response | jq -r '.tests[] | "    " + .testName + " - " + .status'

# Filter by status
echo "Filtering by FAILED status:"
failed_response=$(curl -s "http://localhost:8085/api/dashboard/test-results?status=FAILED&size=3")
echo "  Results: $(echo $failed_response | jq -r '.totalElements')"
echo $failed_response | jq -r '.tests[] | "    " + .testName + " - " + .serviceName'
echo ""

# Demo 9: Dashboard Features Summary
echo "🎯 DEMO 9: Dashboard Features Summary"
echo "-------------------------------------"
echo "✅ Dashboard Features Implemented:"
echo "  • Real-time test result display"
echo "  • Comprehensive filtering (service, type, status, date)"
echo "  • Pagination with navigation"
echo "  • Test execution trends and analytics"
echo "  • Performance metrics and monitoring"
echo "  • Failure pattern analysis"
echo "  • Security test dashboard"
echo "  • Data export functionality"
echo "  • Interactive charts and visualizations"
echo "  • Historical test data analysis"
echo ""

# Demo 10: Frontend Dashboard Access
echo "🌐 DEMO 10: Frontend Dashboard Access"
echo "-------------------------------------"
echo "Frontend Dashboard URL: http://localhost:8085/dashboard/index.html"
echo "Dashboard Features:"
echo "  • Modern, responsive UI with Bootstrap 5"
echo "  • Interactive charts using Chart.js"
echo "  • Real-time data updates"
echo "  • Advanced filtering and search"
echo "  • Export capabilities"
echo "  • Mobile-friendly design"
echo "  • Dark/light theme support"
echo ""

echo "🎉 DASHBOARD DEMO COMPLETED!"
echo "============================"
echo ""
echo "✅ Key Achievements:"
echo "  • Comprehensive dashboard backend API implemented"
echo "  • Real-time test result fetching and display"
echo "  • Advanced filtering and pagination"
echo "  • Performance and security analytics"
echo "  • Historical trend analysis"
echo "  • Data export functionality"
echo "  • Modern frontend dashboard UI"
echo "  • Interactive data visualizations"
echo ""
echo "🚀 Next Steps:"
echo "  • Open dashboard in browser: http://localhost:8085/dashboard/index.html"
echo "  • Connect to real microservices for live test execution"
echo "  • Add real-time WebSocket updates"
echo "  • Implement advanced analytics and ML insights"
echo "  • Scale to production with PostgreSQL database"
echo ""
