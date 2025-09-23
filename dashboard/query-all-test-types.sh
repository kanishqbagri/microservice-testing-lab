#!/bin/bash

# Script to query all test types from Supabase
# This will help identify all test types in your database

SUPABASE_URL="https://smuaribfocdanafiixzi.supabase.co"
API_KEY="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs"

echo "🔍 Querying all test types from Supabase..."
echo "================================================"

echo ""
echo "📊 Method 1: All unique test suite names with counts"
echo "----------------------------------------------------"
curl -s -X GET "${SUPABASE_URL}/rest/v1/test_run?select=test_suite(name)" \
  -H "apikey: ${API_KEY}" \
  -H "Authorization: Bearer ${API_KEY}" \
  -H "Content-Type: application/json" | \
  jq -r '.[].test_suite.name' | sort | uniq -c | sort -nr

echo ""
echo "📋 Method 2: Test types by service and category"
echo "-----------------------------------------------"
curl -s -X GET "${SUPABASE_URL}/rest/v1/test_run?select=test_suite(name)" \
  -H "apikey: ${API_KEY}" \
  -H "Authorization: Bearer ${API_KEY}" \
  -H "Content-Type: application/json" | \
  jq -r '.[].test_suite.name' | sort | uniq | while read suite_name; do
    service=$(echo "$suite_name" | grep -oE "(User|Order|Product|Notification|Gateway)" | head -1)
    test_type=$(echo "$suite_name" | grep -oE "(API|UI|Integration|Contract|Chaos|Unit|System|Performance|Security|E2E|Functional|Acceptance)" | head -1)
    echo "$suite_name | $service | $test_type"
  done | column -t -s '|'

echo ""
echo "📈 Method 3: Summary by test type"
echo "---------------------------------"
curl -s -X GET "${SUPABASE_URL}/rest/v1/test_run?select=test_suite(name)" \
  -H "apikey: ${API_KEY}" \
  -H "Authorization: Bearer ${API_KEY}" \
  -H "Content-Type: application/json" | \
  jq -r '.[].test_suite.name' | sort | uniq | while read suite_name; do
    echo "$suite_name" | grep -oE "(API|UI|Integration|Contract|Chaos|Unit|System|Performance|Security|E2E|Functional|Acceptance)" | head -1
  done | sort | uniq -c | sort -nr

echo ""
echo "✅ Query completed!"
echo "Use this information to verify that the categorization function handles all your test types correctly."

