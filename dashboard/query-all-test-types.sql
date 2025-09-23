-- Comprehensive SQL Query to Find All Test Types in Supabase
-- This query will return all unique test suite names with their counts

-- Method 1: Get all unique test suite names with counts
SELECT 
    ts.name as test_suite_name,
    COUNT(*) as test_run_count,
    COUNT(CASE WHEN tr.status = 'PASS' THEN 1 END) as passed_runs,
    COUNT(CASE WHEN tr.status = 'FAIL' THEN 1 END) as failed_runs,
    COUNT(CASE WHEN tr.status = 'FLAKE' THEN 1 END) as flaky_runs,
    ROUND(COUNT(CASE WHEN tr.status = 'PASS' THEN 1 END) * 100.0 / COUNT(*), 2) as success_rate
FROM test_run tr
JOIN test_suite ts ON tr.suite_id = ts.id
WHERE ts.name IS NOT NULL
GROUP BY ts.name
ORDER BY test_run_count DESC, ts.name;

-- Method 2: Get test types categorized by service
SELECT 
    ts.name as test_suite_name,
    CASE 
        WHEN LOWER(ts.name) LIKE '%user%' THEN 'User Service'
        WHEN LOWER(ts.name) LIKE '%order%' THEN 'Order Service'
        WHEN LOWER(ts.name) LIKE '%product%' THEN 'Product Service'
        WHEN LOWER(ts.name) LIKE '%notification%' THEN 'Notification Service'
        WHEN LOWER(ts.name) LIKE '%gateway%' THEN 'Gateway Service'
        ELSE 'Other'
    END as service_name,
    CASE 
        WHEN LOWER(ts.name) LIKE '%api%' THEN 'API'
        WHEN LOWER(ts.name) LIKE '%ui%' THEN 'UI'
        WHEN LOWER(ts.name) LIKE '%integration%' THEN 'Integration'
        WHEN LOWER(ts.name) LIKE '%contract%' THEN 'Contract'
        WHEN LOWER(ts.name) LIKE '%chaos%' THEN 'Chaos'
        WHEN LOWER(ts.name) LIKE '%unit%' THEN 'Unit'
        WHEN LOWER(ts.name) LIKE '%system%' THEN 'System'
        WHEN LOWER(ts.name) LIKE '%performance%' THEN 'Performance'
        WHEN LOWER(ts.name) LIKE '%security%' THEN 'Security'
        WHEN LOWER(ts.name) LIKE '%e2e%' OR LOWER(ts.name) LIKE '%end-to-end%' THEN 'E2E'
        WHEN LOWER(ts.name) LIKE '%functional%' THEN 'Functional'
        WHEN LOWER(ts.name) LIKE '%acceptance%' THEN 'Acceptance'
        ELSE 'Unknown'
    END as test_type,
    COUNT(*) as test_run_count
FROM test_run tr
JOIN test_suite ts ON tr.suite_id = ts.id
WHERE ts.name IS NOT NULL
GROUP BY ts.name, service_name, test_type
ORDER BY service_name, test_type, test_run_count DESC;

-- Method 3: Get summary by test type
SELECT 
    CASE 
        WHEN LOWER(ts.name) LIKE '%api%' THEN 'API'
        WHEN LOWER(ts.name) LIKE '%ui%' THEN 'UI'
        WHEN LOWER(ts.name) LIKE '%integration%' THEN 'Integration'
        WHEN LOWER(ts.name) LIKE '%contract%' THEN 'Contract'
        WHEN LOWER(ts.name) LIKE '%chaos%' THEN 'Chaos'
        WHEN LOWER(ts.name) LIKE '%unit%' THEN 'Unit'
        WHEN LOWER(ts.name) LIKE '%system%' THEN 'System'
        WHEN LOWER(ts.name) LIKE '%performance%' THEN 'Performance'
        WHEN LOWER(ts.name) LIKE '%security%' THEN 'Security'
        WHEN LOWER(ts.name) LIKE '%e2e%' OR LOWER(ts.name) LIKE '%end-to-end%' THEN 'E2E'
        WHEN LOWER(ts.name) LIKE '%functional%' THEN 'Functional'
        WHEN LOWER(ts.name) LIKE '%acceptance%' THEN 'Acceptance'
        ELSE 'Unknown'
    END as test_type,
    COUNT(DISTINCT ts.name) as unique_test_suites,
    COUNT(*) as total_test_runs,
    COUNT(CASE WHEN tr.status = 'PASS' THEN 1 END) as passed_runs,
    COUNT(CASE WHEN tr.status = 'FAIL' THEN 1 END) as failed_runs,
    ROUND(COUNT(CASE WHEN tr.status = 'PASS' THEN 1 END) * 100.0 / COUNT(*), 2) as success_rate
FROM test_run tr
JOIN test_suite ts ON tr.suite_id = ts.id
WHERE ts.name IS NOT NULL
GROUP BY test_type
ORDER BY total_test_runs DESC;

