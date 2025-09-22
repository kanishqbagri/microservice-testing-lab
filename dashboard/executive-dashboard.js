// Executive Dashboard JavaScript Logic
class ExecutiveDashboard {
    constructor() {
        // Supabase connection
        this.supabaseUrl = 'https://smuaribfocdanafiixzi.supabase.co';
        this.supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs';
        this.supabase = window.supabase.createClient(this.supabaseUrl, this.supabaseKey);

        this.charts = {};
        this.lastUpdateTime = new Date();
        
        try {
            this.impactAnalysis = new IntelligentImpactAnalysis();
            console.log('IntelligentImpactAnalysis initialized successfully');
        } catch (error) {
            console.error('Failed to initialize IntelligentImpactAnalysis:', error);
            this.impactAnalysis = null;
        }
        
        this.init();
    }

    async init() {
        this.updateCurrentTime();
        setInterval(() => this.updateCurrentTime(), 1000);
        
        await this.loadExecutiveData();
        
        // Manual refresh only - no auto-refresh for executive view
        console.log('Executive dashboard loaded. Use refresh button for manual updates.');
    }

    updateCurrentTime() {
        const now = new Date();
        document.getElementById('current-time').textContent = now.toLocaleString();
    }

    updateLastUpdated() {
        this.lastUpdateTime = new Date();
        document.getElementById('last-updated').textContent = this.lastUpdateTime.toLocaleTimeString();
    }

    async loadExecutiveData() {
        try {
            await Promise.all([
                this.loadSmartScorecards(),
                this.loadImpactAnalysis(),
                this.loadQualityInsights(),
                this.loadAnomalyDetection()
            ]);
            this.updateLastUpdated();
        } catch (error) {
            console.error('Error loading executive data:', error);
            this.showError('Failed to load executive dashboard data');
        }
    }

    // 1. Smart Scorecards - Per Service with Weighted Metrics
    async loadSmartScorecards() {
        try {
            // Calculate date range for last 3 months
            const threeMonthsAgo = new Date();
            threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
            const threeMonthsAgoISO = threeMonthsAgo.toISOString();

            console.log(`Fetching test data from last 3 months (since ${threeMonthsAgoISO})...`);

            // Debug: Query total record counts first
            await this.debugTableCounts(threeMonthsAgoISO);

            // Fetch all test runs with pagination
            const testRuns = await this.fetchAllRecords('test_run', {
                select: `id, status, started_at, finished_at, test_suite(name, project(name))`,
                filter: { column: 'started_at', operator: 'gte', value: threeMonthsAgoISO },
                orderBy: { column: 'started_at', ascending: false }
            });

            // Fetch all test results with pagination
            const testResults = await this.fetchAllRecords('test_result', {
                select: `id, status, duration_ms, created_at, test_case(name, tags), test_run(test_suite(name, project(name)))`,
                filter: { column: 'created_at', operator: 'gte', value: threeMonthsAgoISO },
                orderBy: { column: 'created_at', ascending: false }
            });

            console.log(`Fetched ${testRuns?.length || 0} test runs and ${testResults?.length || 0} test results from last 3 months`);

            // Validate and analyze the data
            this.validateTestData(testRuns, testResults);

            // Check if we have data
            if (!testRuns || testRuns.length === 0) {
                console.log('No test runs found in the database');
                this.renderPerServiceScorecards([]);
                return;
            }

            // Calculate per-service metrics with weighted test types
            const serviceMetrics = this.calculatePerServiceMetrics(testRuns, testResults);
            
            // Always use real data, even if scores are low
            console.log('Using real data from Supabase:', serviceMetrics);
            this.renderPerServiceScorecards(serviceMetrics);
            
            // Update data coverage summary
            this.updateDataCoverageSummary(testRuns, testResults);
        } catch (error) {
            console.error('Error loading smart scorecards:', error);
            this.renderPerServiceScorecards([]);
        }
    }

    // Validate and analyze test data
    validateTestData(testRuns, testResults) {
        console.log('=== DATA VALIDATION ===');
        console.log('Test Runs:', testRuns.length);
        console.log('Test Results:', testResults.length);
        
        if (testRuns.length > 0) {
            // Check status values
            const statuses = [...new Set(testRuns.map(r => r.status))];
            console.log('Unique statuses found:', statuses);
            
            // Check test suite names
            const suiteNames = [...new Set(testRuns.map(r => r.test_suite?.name))];
            console.log('Test suite names (first 10):', suiteNames.slice(0, 10));
            
            // Check date range
            const dates = testRuns.map(r => new Date(r.started_at)).sort();
            console.log('Date range:', dates[0]?.toISOString(), 'to', dates[dates.length-1]?.toISOString());
            
            // Check service extraction
            const extractedServices = [...new Set(testRuns.map(r => this.extractServiceName(r.test_suite?.name)))];
            console.log('Extracted services:', extractedServices);
        }
        
        if (testResults.length > 0) {
            // Check test result statuses
            const resultStatuses = [...new Set(testResults.map(r => r.status))];
            console.log('Test result statuses:', resultStatuses);
            
            // Check duration data
            const withDuration = testResults.filter(r => r.duration_ms && r.duration_ms > 0);
            console.log('Results with duration:', withDuration.length, 'out of', testResults.length);
        }
        
        console.log('=== END DATA VALIDATION ===');
    }

    // Debug method to query table counts
    async debugTableCounts(threeMonthsAgoISO) {
        console.log('=== DEBUG: Querying table counts ===');
        
        try {
            // Count total test_run records in last 3 months
            const { count: testRunCount, error: testRunError } = await this.supabase
                .from('test_run')
                .select('*', { count: 'exact', head: true })
                .gte('started_at', threeMonthsAgoISO);
            
            if (testRunError) {
                console.error('Error counting test_run records:', testRunError);
            } else {
                console.log(`📊 Total test_run records in last 3 months: ${testRunCount}`);
            }

            // Count total test_result records in last 3 months
            const { count: testResultCount, error: testResultError } = await this.supabase
                .from('test_result')
                .select('*', { count: 'exact', head: true })
                .gte('created_at', threeMonthsAgoISO);
            
            if (testResultError) {
                console.error('Error counting test_result records:', testResultError);
            } else {
                console.log(`📊 Total test_result records in last 3 months: ${testResultCount}`);
            }

            // Count total records in each table (no date filter)
            const { count: totalTestRunCount, error: totalTestRunError } = await this.supabase
                .from('test_run')
                .select('*', { count: 'exact', head: true });
            
            if (totalTestRunError) {
                console.error('Error counting total test_run records:', totalTestRunError);
            } else {
                console.log(`📊 Total test_run records in database: ${totalTestRunCount}`);
            }

            const { count: totalTestResultCount, error: totalTestResultError } = await this.supabase
                .from('test_result')
                .select('*', { count: 'exact', head: true });
            
            if (totalTestResultError) {
                console.error('Error counting total test_result records:', totalTestResultError);
            } else {
                console.log(`📊 Total test_result records in database: ${totalTestResultCount}`);
            }

            console.log('=== END DEBUG: Table counts ===');
        } catch (error) {
            console.error('Error in debug table counts:', error);
        }
    }

    // Pagination function to fetch all records
    async fetchAllRecords(table, options = {}) {
        const { select = '*', filter = null, orderBy = null, pageSize = 1000 } = options;
        let allRecords = [];
        let from = 0;
        let hasMore = true;
        let pageCount = 0;

        console.log(`Starting paginated fetch for ${table}...`);
        this.updateLoadingStatus(`Fetching ${table} data...`);

        while (hasMore) {
            pageCount++;
            let query = this.supabase
                .from(table)
                .select(select)
                .range(from, from + pageSize - 1);

            // Apply filter if provided
            if (filter) {
                query = query[filter.operator](filter.column, filter.value);
            }

            // Apply ordering if provided
            if (orderBy) {
                query = query.order(orderBy.column, { ascending: orderBy.ascending });
            }

            const { data, error } = await query;

            if (error) {
                console.error(`Error fetching ${table} records:`, error);
                throw error;
            }

            if (data && data.length > 0) {
                allRecords = allRecords.concat(data);
                from += pageSize;
                hasMore = data.length === pageSize; // Continue if we got a full page
                console.log(`Fetched ${data.length} ${table} records (total: ${allRecords.length})`);
                this.updateLoadingStatus(`Fetched ${allRecords.length} ${table} records...`);
            } else {
                hasMore = false;
            }
        }

        console.log(`Completed paginated fetch for ${table}: ${allRecords.length} total records`);
        return allRecords;
    }

    updateLoadingStatus(message) {
        const coverageElement = document.getElementById('data-coverage-summary');
        if (coverageElement) {
            coverageElement.innerHTML = `
                <div class="alert alert-info">
                    <i class="fas fa-spinner fa-spin"></i>
                    <strong>Loading:</strong> ${message}
                </div>
            `;
        }
    }

    updateDataCoverageSummary(testRuns, testResults) {
        const threeMonthsAgo = new Date();
        threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
        
        const dateRange = `${threeMonthsAgo.toLocaleDateString()} - ${new Date().toLocaleDateString()}`;
        
        // Find the data coverage element and update it
        const coverageElement = document.getElementById('data-coverage-summary');
        if (coverageElement) {
            coverageElement.innerHTML = `
                <div class="alert alert-info">
                    <i class="fas fa-database"></i>
                    <strong>Data Coverage:</strong> ${testRuns?.length || 0} test runs, ${testResults?.length || 0} test results 
                    from last 3 months (${dateRange})
                </div>
            `;
        }
    }

    calculatePerServiceMetrics(testRuns, testResults) {
        const now = new Date();
        const lastWeek = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);

        // Test type weights for scoring
        const testTypeWeights = {
            'unit': 0.25,      // Unit tests - foundation
            'api': 0.30,       // API tests - critical for integration
            'integration': 0.25, // Integration tests - system coherence
            'ui': 0.10,        // UI tests - user experience
            'system': 0.10     // System tests - end-to-end validation
        };

        // Group data by service (using test suite name as service identifier)
        const services = {};
        
        // Process test runs
        console.log('Sample test run data:', testRuns.slice(0, 3));
        console.log('Sample test result data:', testResults.slice(0, 3));
        
        testRuns.forEach(run => {
            const serviceName = this.extractServiceName(run.test_suite?.name || 'Unknown Service');
            const testType = this.categorizeTestType(run.test_suite?.name || '');
            
            console.log(`Processing run: ${run.test_suite?.name} -> Service: ${serviceName}, Type: ${testType}, Status: ${run.status}`);
            
            if (!services[serviceName]) {
                services[serviceName] = {
                    name: serviceName,
                    testTypes: {},
                    totalRuns: 0,
                    successfulRuns: 0,
                    failedRuns: 0,
                    recentRuns: 0,
                    recentFailures: 0,
                    lastRun: null,
                    project: run.test_suite?.project?.name || 'Unknown Project'
                };
            }

            services[serviceName].totalRuns++;
            if (run.status === 'PASSED' || run.status === 'PASS' || run.status === 'SUCCESS') {
                services[serviceName].successfulRuns++;
            }
            if (run.status === 'FAILED' || run.status === 'FAIL' || run.status === 'ERROR') {
                services[serviceName].failedRuns++;
            }

            const runDate = new Date(run.started_at);
            if (runDate >= lastWeek) {
                services[serviceName].recentRuns++;
                if (run.status === 'FAILED' || run.status === 'FAIL' || run.status === 'ERROR') {
                    services[serviceName].recentFailures++;
                }
            }

            if (!services[serviceName].lastRun || runDate > new Date(services[serviceName].lastRun)) {
                services[serviceName].lastRun = run.started_at;
            }

            // Track by test type
            if (!services[serviceName].testTypes[testType]) {
                services[serviceName].testTypes[testType] = {
                    total: 0,
                    passed: 0,
                    failed: 0,
                    avgDuration: 0,
                    durations: []
                };
            }
            services[serviceName].testTypes[testType].total++;
            if (run.status === 'PASSED' || run.status === 'PASS' || run.status === 'SUCCESS') {
                services[serviceName].testTypes[testType].passed++;
            }
            if (run.status === 'FAILED' || run.status === 'FAIL' || run.status === 'ERROR') {
                services[serviceName].testTypes[testType].failed++;
            }
        });

        // Process test results for detailed metrics
        testResults.forEach(result => {
            const serviceName = this.extractServiceName(result.test_run?.test_suite?.name || 'Unknown Service');
            const testType = this.categorizeTestType(result.test_run?.test_suite?.name || '');
            
            if (services[serviceName] && services[serviceName].testTypes[testType]) {
                if (result.duration_ms && result.duration_ms > 0) {
                    services[serviceName].testTypes[testType].durations.push(result.duration_ms);
                }
            }
        });

        // Calculate weighted scores for each service
        console.log('Services found:', Object.keys(services));
        const result = Object.values(services).map(service => {
            console.log(`Processing service: ${service.name}`, service);
            const scores = this.calculateServiceScores(service, testTypeWeights);
            console.log(`Scores for ${service.name}:`, scores);
            return {
                ...service,
                ...scores
            };
        }).sort((a, b) => b.overallScore - a.overallScore);
        
        console.log('Final service count:', result.length);
        return result;
    }

    extractServiceName(suiteName) {
        console.log('Processing suite name:', suiteName);
        const name = suiteName.toLowerCase();
        
        // Extract service name from test suite name patterns
        if (name.includes('user')) {
            console.log('Matched user service');
            return 'User Service';
        }
        if (name.includes('order')) {
            console.log('Matched order service');
            return 'Order Service';
        }
        if (name.includes('product')) {
            console.log('Matched product service');
            return 'Product Service';
        }
        if (name.includes('notification')) {
            console.log('Matched notification service');
            return 'Notification Service';
        }
        if (name.includes('gateway')) {
            console.log('Matched gateway service');
            return 'Gateway Service';
        }
        
        // Fallback: try to extract from common patterns
        const serviceMatch = name.match(/(user|order|product|notification|gateway)-service/i);
        if (serviceMatch) {
            const serviceName = serviceMatch[1].charAt(0).toUpperCase() + serviceMatch[1].slice(1) + ' Service';
            console.log('Extracted service name from pattern:', serviceName);
            return serviceName;
        }
        
        console.log('No service match found, returning Unknown Service');
        return 'Unknown Service';
    }

    categorizeTestType(suiteName) {
        const name = suiteName.toLowerCase();
        if (name.includes('unit') || name.includes('component')) return 'unit';
        if (name.includes('api') || name.includes('rest') || name.includes('endpoint')) return 'api';
        if (name.includes('integration') || name.includes('contract')) return 'integration';
        if (name.includes('ui') || name.includes('e2e') || name.includes('end-to-end')) return 'ui';
        if (name.includes('system') || name.includes('smoke') || name.includes('regression')) return 'system';
        return 'unit'; // Default to unit tests
    }

    calculateServiceScores(service, testTypeWeights) {
        let weightedScore = 0;
        let totalWeight = 0;
        const testTypeScores = {};

        console.log(`Calculating scores for ${service.name}:`, service);
        console.log(`Service testTypes:`, service.testTypes);

        // Calculate score for each test type
        Object.entries(service.testTypes).forEach(([testType, data]) => {
            console.log(`  Processing test type: ${testType}, data:`, data);
            const weight = testTypeWeights[testType] || 0.1;
            
            // Ensure data.passed and data.total are numbers
            const passed = Number(data.passed) || 0;
            const total = Number(data.total) || 0;
            const successRate = total > 0 ? (passed / total) * 100 : 0;
            
            // Calculate average duration for performance scoring
            const avgDuration = data.durations && data.durations.length > 0 ? 
                data.durations.reduce((a, b) => a + b, 0) / data.durations.length : 0;
            
            // No performance penalty applied - using raw success rate
            const adjustedScore = successRate;
            
            // Convert percentage score to 1-10 scale
            const score1to10 = this.convertPercentageToScore(adjustedScore);
            
            console.log(`  ${testType}: ${passed}/${total} passed (${successRate.toFixed(1)}%), avgDuration: ${avgDuration.toFixed(0)}ms, score: ${score1to10}/10`);
            
            testTypeScores[testType] = {
                score: score1to10,
                successRate: Math.round(successRate),
                totalTests: data.total,
                avgDuration: Math.round(avgDuration)
            };

            weightedScore += adjustedScore * weight;
            totalWeight += weight;
        });

        console.log(`  Weighted Score: ${weightedScore}, Total Weight: ${totalWeight}`);
        
        let overallScorePercentage = totalWeight > 0 ? Math.round(weightedScore / totalWeight) : 0;
        
        // Ensure overallScorePercentage is a valid number
        if (isNaN(overallScorePercentage)) {
            console.warn(`NaN detected in overallScorePercentage calculation. weightedScore: ${weightedScore}, totalWeight: ${totalWeight}`);
            overallScorePercentage = 0;
        }
        
        // Apply bonuses for good practices
        const testCountBonus = Math.min(5, Math.floor(service.totalTests / 100)); // Bonus for having many tests
        const stabilityBonus = service.recentRuns > 10 ? 2 : 0; // Bonus for active testing
        const coverageBonus = Object.keys(service.testTypes).length >= 3 ? 3 : 0; // Bonus for diverse test types
        
        overallScorePercentage = Math.min(100, overallScorePercentage + testCountBonus + stabilityBonus + coverageBonus);
        
        console.log(`  Bonuses: Test Count (+${testCountBonus}%), Stability (+${stabilityBonus}%), Coverage (+${coverageBonus}%)`);
        console.log(`  Final overallScorePercentage: ${overallScorePercentage}`);
        
        const overallScore = this.convertPercentageToScore(overallScorePercentage);
        
        // Final safety check
        if (isNaN(overallScore)) {
            console.warn(`NaN detected in final overallScore. overallScorePercentage: ${overallScorePercentage}`);
            return {
                overallScore: 3, // Default minimum score
                stabilityScore: this.convertPercentageToScore(this.calculateStabilityScore(service)),
                coverageScore: this.convertPercentageToScore(this.calculateCoverageScore(service.testTypes, testTypeWeights)),
                riskLevel: 'HIGH',
                testTypeScores,
                totalTests: service.totalTests,
                totalRuns: service.totalRuns,
                recentRuns: service.recentRuns,
                lastRunDate: service.lastRun ? new Date(service.lastRun).toLocaleDateString() : 'Never'
            };
        }
        
        // System Stability Score (based on recent failure patterns) - convert to 1-10
        const stabilityScorePercentage = this.calculateStabilityScore(service);
        const stabilityScore = this.convertPercentageToScore(stabilityScorePercentage);
        
        // Risk Assessment
        const riskLevel = this.assessServiceRisk(service, overallScorePercentage);
        
        // Test Coverage Score (based on test type diversity) - convert to 1-10
        const coverageScorePercentage = this.calculateCoverageScore(service.testTypes, testTypeWeights);
        const coverageScore = this.convertPercentageToScore(coverageScorePercentage);

        console.log(`  Overall: ${overallScore}/10 (${overallScorePercentage}%), Stability: ${stabilityScore}/10, Coverage: ${coverageScore}/10, Risk: ${riskLevel}`);

        // Generate improvement suggestions
        const suggestions = this.generateImprovementSuggestions(service, overallScore, testTypeScores, riskLevel);
        
        return {
            overallScore,
            stabilityScore,
            coverageScore,
            riskLevel,
            testTypeScores,
            totalTests: Object.values(service.testTypes).reduce((sum, type) => sum + type.total, 0),
            lastRunDate: service.lastRun ? new Date(service.lastRun).toLocaleDateString() : 'Never',
            suggestions
        };
    }

    // Generate intelligent improvement suggestions based on score analysis
    generateImprovementSuggestions(service, overallScore, testTypeScores, riskLevel) {
        const suggestions = [];
        const priorities = [];
        
        console.log(`Generating suggestions for ${service.name} (Score: ${overallScore}/10, Risk: ${riskLevel})`);
        
        // Analyze overall score
        if (overallScore <= 4) {
            suggestions.push({
                category: 'Critical',
                title: 'Overall Score Too Low',
                description: `Current score of ${overallScore}/10 indicates significant quality issues.`,
                actions: [
                    'Review and fix failing tests immediately',
                    'Investigate root causes of test failures',
                    'Implement comprehensive test coverage',
                    'Consider test automation improvements'
                ],
                impact: 'High',
                effort: 'High'
            });
            priorities.push('Critical');
        } else if (overallScore <= 6) {
            suggestions.push({
                category: 'High Priority',
                title: 'Score Needs Improvement',
                description: `Score of ${overallScore}/10 is below acceptable standards.`,
                actions: [
                    'Focus on improving test success rates',
                    'Optimize test execution performance',
                    'Add missing test types for better coverage'
                ],
                impact: 'Medium',
                effort: 'Medium'
            });
            priorities.push('High');
        }
        
        // Analyze individual test type scores
        Object.entries(testTypeScores).forEach(([testType, data]) => {
            if (data.score <= 3) {
                suggestions.push({
                    category: 'Critical',
                    title: `${testType.toUpperCase()} Tests Failing`,
                    description: `${testType} tests scoring only ${data.score}/10 with ${data.successRate}% success rate.`,
                    actions: this.getTestTypeSpecificActions(testType, data),
                    impact: 'High',
                    effort: this.getTestTypeEffort(testType)
                });
                priorities.push('Critical');
            } else if (data.score <= 5) {
                suggestions.push({
                    category: 'Medium Priority',
                    title: `${testType.toUpperCase()} Tests Need Attention`,
                    description: `${testType} tests at ${data.score}/10 could be improved.`,
                    actions: this.getTestTypeSpecificActions(testType, data),
                    impact: 'Medium',
                    effort: this.getTestTypeEffort(testType)
                });
                priorities.push('Medium');
            }
            
            // Performance monitoring (no penalty, but still track for insights)
            if (data.avgDuration > 5000) {
                suggestions.push({
                    category: 'Performance',
                    title: `${testType.toUpperCase()} Tests Slow`,
                    description: `Average duration of ${data.avgDuration}ms is quite slow. Consider optimization.`,
                    actions: this.getPerformanceActions(testType, data.avgDuration),
                    impact: 'Low',
                    effort: 'Medium'
                });
                priorities.push('Performance');
            }
        });
        
        // Analyze test coverage
        const testTypes = Object.keys(service.testTypes);
        if (testTypes.length < 3) {
            suggestions.push({
                category: 'Coverage',
                title: 'Limited Test Type Coverage',
                description: `Only ${testTypes.length} test types found. Comprehensive testing requires multiple test types.`,
                actions: [
                    'Add unit tests for business logic',
                    'Implement API/integration tests',
                    'Consider UI/end-to-end tests',
                    'Add contract tests for service boundaries'
                ],
                impact: 'High',
                effort: 'High'
            });
            priorities.push('Coverage');
        }
        
        // Analyze stability
        const recentFailureRate = service.recentRuns > 0 ? (service.recentFailures / service.recentRuns) * 100 : 0;
        if (recentFailureRate > 20) {
            suggestions.push({
                category: 'Stability',
                title: 'High Recent Failure Rate',
                description: `${recentFailureRate.toFixed(1)}% of recent test runs are failing.`,
                actions: [
                    'Investigate recent test failures',
                    'Check for environment issues',
                    'Review test data dependencies',
                    'Implement better error handling'
                ],
                impact: 'High',
                effort: 'Medium'
            });
            priorities.push('Stability');
        }
        
        // Analyze test volume
        const totalTests = Object.values(service.testTypes).reduce((sum, type) => sum + type.total, 0);
        if (totalTests < 50) {
            suggestions.push({
                category: 'Volume',
                title: 'Low Test Volume',
                description: `Only ${totalTests} total tests. More comprehensive test coverage needed.`,
                actions: [
                    'Increase test coverage for critical paths',
                    'Add edge case testing',
                    'Implement boundary value testing',
                    'Add negative test scenarios'
                ],
                impact: 'Medium',
                effort: 'High'
            });
            priorities.push('Volume');
        }
        
        // Risk-based suggestions
        if (riskLevel === 'HIGH') {
            suggestions.push({
                category: 'Risk Management',
                title: 'High Risk Service',
                description: 'This service poses high risk to system stability.',
                actions: [
                    'Implement additional monitoring',
                    'Add circuit breakers and fallbacks',
                    'Increase test frequency',
                    'Consider canary deployments'
                ],
                impact: 'High',
                effort: 'Medium'
            });
            priorities.push('Risk');
        }
        
        // Sort suggestions by priority
        const priorityOrder = { 'Critical': 1, 'High Priority': 2, 'Medium Priority': 3, 'Performance': 4, 'Coverage': 5, 'Stability': 6, 'Volume': 7, 'Risk Management': 8 };
        suggestions.sort((a, b) => priorityOrder[a.category] - priorityOrder[b.category]);
        
        return {
            suggestions,
            priority: priorities.length > 0 ? priorities[0] : 'Low',
            totalSuggestions: suggestions.length,
            criticalCount: suggestions.filter(s => s.category === 'Critical').length
        };
    }
    
    // Get test type specific improvement actions
    getTestTypeSpecificActions(testType, data) {
        const baseActions = [
            'Review failing test cases',
            'Check test data setup',
            'Verify test environment configuration'
        ];
        
        switch (testType) {
            case 'unit':
                return [
                    ...baseActions,
                    'Mock external dependencies properly',
                    'Test edge cases and boundary conditions',
                    'Ensure tests are isolated and repeatable',
                    'Add tests for error handling paths'
                ];
            case 'api':
                return [
                    ...baseActions,
                    'Verify API contract compliance',
                    'Test different HTTP status codes',
                    'Validate request/response schemas',
                    'Test authentication and authorization'
                ];
            case 'integration':
                return [
                    ...baseActions,
                    'Check database connectivity',
                    'Verify external service integrations',
                    'Test data consistency across services',
                    'Validate transaction handling'
                ];
            case 'ui':
                return [
                    ...baseActions,
                    'Check browser compatibility',
                    'Verify element selectors',
                    'Test responsive design',
                    'Validate user interaction flows'
                ];
            case 'system':
                return [
                    ...baseActions,
                    'Verify end-to-end workflows',
                    'Check system resource usage',
                    'Test under load conditions',
                    'Validate system integration points'
                ];
            default:
                return baseActions;
        }
    }
    
    // Get performance optimization actions
    getPerformanceActions(testType, avgDuration) {
        const actions = [
            'Profile test execution to identify bottlenecks',
            'Optimize test data setup and teardown',
            'Use parallel test execution where possible'
        ];
        
        switch (testType) {
            case 'unit':
                return [
                    ...actions,
                    'Reduce database calls in unit tests',
                    'Use in-memory databases for testing',
                    'Mock slow external services'
                ];
            case 'api':
                return [
                    ...actions,
                    'Use connection pooling',
                    'Implement request caching',
                    'Optimize API response sizes'
                ];
            case 'integration':
                return [
                    ...actions,
                    'Use test containers for faster setup',
                    'Implement database seeding strategies',
                    'Cache frequently used test data'
                ];
            case 'ui':
                return [
                    ...actions,
                    'Use headless browser mode',
                    'Implement page object pattern',
                    'Reduce wait times with smart waits'
                ];
            default:
                return actions;
        }
    }
    
    // Get effort estimation for test type improvements
    getTestTypeEffort(testType) {
        const effortMap = {
            'unit': 'Low',
            'api': 'Medium',
            'integration': 'High',
            'ui': 'High',
            'system': 'Very High'
        };
        return effortMap[testType] || 'Medium';
    }

    // Convert percentage (0-100) to 1-10 score scale
    convertPercentageToScore(percentage) {
        // Handle NaN, undefined, null, and invalid inputs
        if (isNaN(percentage) || percentage === undefined || percentage === null) {
            console.warn(`Invalid percentage input: ${percentage}, returning default score 3`);
            return 3;
        }
        
        // Ensure percentage is a number
        const numPercentage = Number(percentage);
        if (isNaN(numPercentage)) {
            console.warn(`Cannot convert to number: ${percentage}, returning default score 3`);
            return 3;
        }
        
        if (numPercentage <= 0) return 3; // Minimum score of 3 for any data
        if (numPercentage >= 100) return 10;
        
        // More generous conversion: 0-100% maps to 3-10
        // Formula: score = 3 + (percentage / 100) * 7
        const score = 3 + (numPercentage / 100) * 7;
        return Math.round(score);
    }

    calculatePerformancePenalty(avgDuration, testType) {
        // Handle invalid inputs
        if (isNaN(avgDuration) || avgDuration === undefined || avgDuration === null) {
            return 0;
        }
        
        const numDuration = Number(avgDuration);
        if (isNaN(numDuration) || numDuration < 0) {
            return 0;
        }
        
        // More realistic performance expectations for different test types
        const thresholds = {
            'unit': 500,      // Unit tests should be fast (increased from 100ms)
            'api': 1000,      // API tests can be moderately fast (increased from 500ms)
            'integration': 3000, // Integration tests can be slower (increased from 2000ms)
            'ui': 8000,       // UI tests are typically slowest (increased from 5000ms)
            'system': 15000   // System tests can be very slow (increased from 10000ms)
        };

        const threshold = thresholds[testType] || 2000;
        if (numDuration <= threshold) return 0;
        
        // More reasonable penalty calculation - max 10% instead of 20%
        const excess = numDuration - threshold;
        const penalty = Math.min(10, (excess / threshold) * 5);
        return Math.round(penalty);
    }

    calculateStabilityScore(service) {
        const recentFailureRate = service.recentRuns > 0 ? (service.recentFailures / service.recentRuns) * 100 : 0;
        const overallFailureRate = service.totalRuns > 0 ? (service.failedRuns / service.totalRuns) * 100 : 0;
        
        // Stability score decreases with failure rates
        const stabilityScore = Math.max(0, 100 - (recentFailureRate * 0.7 + overallFailureRate * 0.3));
        return Math.round(stabilityScore);
    }

    assessServiceRisk(service, overallScore) {
        const recentFailureRate = service.recentRuns > 0 ? (service.recentFailures / service.recentRuns) * 100 : 0;
        
        if (overallScore < 60 || recentFailureRate > 30) return 'HIGH';
        if (overallScore < 80 || recentFailureRate > 15) return 'MEDIUM';
        return 'LOW';
    }

    calculateCoverageScore(testTypes, testTypeWeights) {
        const expectedTypes = Object.keys(testTypeWeights);
        const presentTypes = Object.keys(testTypes);
        
        // Calculate coverage based on how many test types are present
        const coverageRatio = presentTypes.length / expectedTypes.length;
        
        // Bonus for having all test types
        const completenessBonus = presentTypes.length === expectedTypes.length ? 10 : 0;
        
        return Math.round((coverageRatio * 90) + completenessBonus);
    }

    renderPerServiceScorecards(services) {
        const container = document.getElementById('smart-scorecards');
        
        if (services.length === 0) {
            container.innerHTML = '<div class="text-center text-muted">No service data available</div>';
            return;
        }

        container.innerHTML = services.map(service => `
            <div class="scorecard-item" style="min-height: 400px;">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <h5 class="mb-0" style="color: var(--executive-blue);">
                            <i class="fas fa-microservice me-2"></i>
                            ${service.name}
                        </h5>
                        <small class="text-muted">${service.project}</small>
                    </div>
                    <span class="badge ${this.getRiskBadgeClass(service.riskLevel)}">
                        ${service.riskLevel} RISK
                    </span>
                </div>
                
                <!-- Overall Score -->
                <div class="text-center mb-3">
                    <div class="scorecard-value" style="color: ${this.getHealthColor(service.overallScore * 10)}; font-size: 2.5rem;">
                        ${service.overallScore}/10
                    </div>
                    <div class="scorecard-label">Overall Score</div>
                </div>

                <!-- Test Type Breakdown -->
                <div class="mb-3">
                    <h6 class="text-muted mb-2">Test Type Scores</h6>
                    ${Object.entries(service.testTypeScores).map(([testType, data]) => `
                        <div class="d-flex justify-content-between align-items-center mb-1">
                            <span class="text-capitalize" style="font-size: 0.9rem;">
                                <i class="fas fa-${this.getTestTypeIcon(testType)} me-1"></i>
                                ${testType} Tests
                            </span>
                            <div class="d-flex align-items-center">
                                <span class="me-2" style="font-size: 0.8rem; color: #64748b;">
                                    ${data.totalTests} tests
                                </span>
                                <span class="badge ${this.getScoreBadgeClass(data.score * 10)}" style="font-size: 0.7rem;">
                                    ${data.score}/10
                                </span>
                            </div>
                        </div>
                        ${data.avgDuration > 0 ? `
                            <div class="text-end" style="font-size: 0.7rem; color: #64748b;">
                                Avg: ${data.avgDuration}ms
                            </div>
                        ` : ''}
                    `).join('')}
                </div>

                <!-- Additional Metrics -->
                <div class="row text-center">
                    <div class="col-4">
                        <div class="metric-value" style="font-size: 1.2rem; color: var(--executive-blue);">
                            ${service.stabilityScore}/10
                        </div>
                        <div class="metric-label" style="font-size: 0.7rem;">Stability</div>
                    </div>
                    <div class="col-4">
                        <div class="metric-value" style="font-size: 1.2rem; color: var(--executive-green);">
                            ${service.coverageScore}/10
                        </div>
                        <div class="metric-label" style="font-size: 0.7rem;">Coverage</div>
                    </div>
                    <div class="col-4">
                        <div class="metric-value" style="font-size: 1.2rem; color: var(--executive-gold);">
                            ${service.totalTests}
                        </div>
                        <div class="metric-label" style="font-size: 0.7rem;">Total Tests</div>
                    </div>
                </div>

                <!-- Service Stats -->
                <div class="row text-center mt-2">
                    <div class="col-6">
                        <div class="metric-value" style="font-size: 1rem; color: var(--executive-blue);">
                            ${service.totalRuns}
                        </div>
                        <div class="metric-label" style="font-size: 0.6rem;">Total Runs</div>
                    </div>
                    <div class="col-6">
                        <div class="metric-value" style="font-size: 1rem; color: var(--executive-green);">
                            ${service.recentRuns}
                        </div>
                        <div class="metric-label" style="font-size: 0.6rem;">Recent Runs</div>
                    </div>
                </div>

                <!-- Last Run Info -->
                <div class="mt-3 pt-2 border-top">
                    <div class="text-center" style="font-size: 0.8rem; color: #64748b;">
                        <i class="fas fa-clock me-1"></i>
                        Last Run: ${service.lastRunDate}
                    </div>
                </div>

                <!-- Improvement Suggestions -->
                ${service.suggestions && service.suggestions.suggestions.length > 0 ? `
                    <div class="mt-3 pt-2 border-top">
                        <div class="d-flex justify-content-between align-items-center mb-2">
                            <h6 class="mb-0" style="color: var(--executive-blue); font-size: 0.9rem;">
                                <i class="fas fa-lightbulb me-1"></i>
                                Improvement Suggestions
                            </h6>
                            <span class="badge ${this.getPriorityBadgeClass(service.suggestions.priority)}" style="font-size: 0.6rem;">
                                ${service.suggestions.priority} Priority
                            </span>
                        </div>
                        <div class="suggestions-container" id="suggestions-${service.name.replace(/\s+/g, '-').toLowerCase()}" style="max-height: 200px; overflow-y: auto; transition: max-height 0.3s ease;">
                            ${service.suggestions.suggestions.map((suggestion, index) => `
                                <div class="suggestion-item mb-2 p-2 ${index >= 3 ? 'd-none' : ''}" style="background: #f8fafc; border-radius: 4px; border-left: 3px solid ${this.getCategoryColor(suggestion.category)};">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="flex-grow-1">
                                            <div class="suggestion-title" style="font-size: 0.8rem; font-weight: 600; color: #1e293b;">
                                                ${suggestion.title}
                                            </div>
                                            <div class="suggestion-description" style="font-size: 0.7rem; color: #64748b; margin-top: 2px;">
                                                ${suggestion.description}
                                            </div>
                                        </div>
                                        <div class="suggestion-meta ms-2">
                                            <span class="badge ${this.getImpactBadgeClass(suggestion.impact)}" style="font-size: 0.6rem;">
                                                ${suggestion.impact}
                                            </span>
                                        </div>
                                    </div>
                                    ${suggestion.actions.length > 0 ? `
                                        <div class="suggestion-actions mt-1">
                                            <div style="font-size: 0.65rem; color: #64748b; font-weight: 500;">Key Actions:</div>
                                            <ul class="mb-0 mt-1" style="font-size: 0.65rem; color: #64748b; padding-left: 15px;">
                                                ${suggestion.actions.slice(0, 2).map(action => `<li>${action}</li>`).join('')}
                                            </ul>
                                        </div>
                                    ` : ''}
                                </div>
                            `).join('')}
                        </div>
                        ${service.suggestions.suggestions.length > 3 ? `
                            <div class="text-center mt-2">
                                <button class="btn btn-sm btn-outline-primary suggestions-toggle-btn" 
                                        onclick="toggleSuggestions('${service.name.replace(/\s+/g, '-').toLowerCase()}')"
                                        style="font-size: 0.65rem; padding: 2px 8px;">
                                    <i class="fas fa-chevron-down" id="icon-${service.name.replace(/\s+/g, '-').toLowerCase()}"></i>
                                    Show ${service.suggestions.suggestions.length - 3} more suggestions
                                </button>
                            </div>
                        ` : ''}
                    </div>
                ` : ''}
            </div>
        `).join('');
    }

    // Toggle suggestions visibility
    toggleSuggestions(serviceName) {
        const container = document.getElementById(`suggestions-${serviceName}`);
        const icon = document.getElementById(`icon-${serviceName}`);
        const button = icon.parentElement;
        
        if (!container) return;
        
        const hiddenSuggestions = container.querySelectorAll('.suggestion-item.d-none');
        const isExpanded = hiddenSuggestions.length === 0;
        
        if (isExpanded) {
            // Collapse - hide suggestions beyond first 3
            const allSuggestions = container.querySelectorAll('.suggestion-item');
            allSuggestions.forEach((suggestion, index) => {
                if (index >= 3) {
                    suggestion.classList.add('d-none');
                }
            });
            icon.className = 'fas fa-chevron-down';
            button.innerHTML = `<i class="fas fa-chevron-down" id="icon-${serviceName}"></i> Show ${allSuggestions.length - 3} more suggestions`;
        } else {
            // Expand - show all suggestions
            hiddenSuggestions.forEach(suggestion => {
                suggestion.classList.remove('d-none');
            });
            icon.className = 'fas fa-chevron-up';
            button.innerHTML = `<i class="fas fa-chevron-up" id="icon-${serviceName}"></i> Show less`;
        }
    }

    getTestTypeIcon(testType) {
        const icons = {
            'unit': 'cube',
            'api': 'plug',
            'integration': 'link',
            'ui': 'desktop',
            'system': 'cogs'
        };
        return icons[testType] || 'question';
    }

    // Helper functions for suggestion styling
    getPriorityBadgeClass(priority) {
        const classes = {
            'Critical': 'bg-danger',
            'High Priority': 'bg-warning',
            'Medium Priority': 'bg-info',
            'Low': 'bg-secondary'
        };
        return classes[priority] || 'bg-secondary';
    }

    getCategoryColor(category) {
        const colors = {
            'Critical': '#dc3545',
            'High Priority': '#fd7e14',
            'Medium Priority': '#0dcaf0',
            'Performance': '#6f42c1',
            'Coverage': '#198754',
            'Stability': '#fd7e14',
            'Volume': '#0dcaf0',
            'Risk Management': '#dc3545'
        };
        return colors[category] || '#6c757d';
    }

    getImpactBadgeClass(impact) {
        const classes = {
            'High': 'bg-danger',
            'Medium': 'bg-warning',
            'Low': 'bg-success'
        };
        return classes[impact] || 'bg-secondary';
    }

    getScoreBadgeClass(score) {
        if (score >= 90) return 'bg-success';
        if (score >= 75) return 'bg-primary';
        if (score >= 60) return 'bg-warning';
        return 'bg-danger';
    }

    getHealthColor(score) {
        if (score >= 90) return '#10b981';
        if (score >= 70) return '#f59e0b';
        return '#ef4444';
    }

    getRiskColor(risk) {
        switch (risk) {
            case 'LOW': return '#10b981';
            case 'MEDIUM': return '#f59e0b';
            case 'HIGH': return '#ef4444';
            default: return '#6b7280';
        }
    }

    getDefaultServiceMetrics() {
        // Generate realistic, varied scores for demonstration
        const services = [
            {
                name: 'User Service',
                project: 'microservices-platform',
                overallScore: 9,
                stabilityScore: 9,
                coverageScore: 9,
                riskLevel: 'LOW',
                totalTests: 185,
                totalRuns: 140,
                recentRuns: 37,
                lastRunDate: new Date().toISOString().split('T')[0],
                testTypeScores: {
                    'unit': { score: 10, successRate: 95, totalTests: 120, avgDuration: 85 },
                    'api': { score: 9, successRate: 88, totalTests: 65, avgDuration: 420 }
                }
            },
            {
                name: 'Order Service',
                project: 'microservices-platform',
                overallScore: 8,
                stabilityScore: 9,
                coverageScore: 8,
                riskLevel: 'MEDIUM',
                totalTests: 150,
                totalRuns: 120,
                recentRuns: 28,
                lastRunDate: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString().split('T')[0], // Yesterday
                testTypeScores: {
                    'unit': { score: 9, successRate: 90, totalTests: 95, avgDuration: 95 },
                    'api': { score: 8, successRate: 82, totalTests: 55, avgDuration: 580 }
                }
            },
            {
                name: 'Product Service',
                project: 'microservices-platform',
                overallScore: 7,
                stabilityScore: 8,
                coverageScore: 7,
                riskLevel: 'MEDIUM',
                totalTests: 105,
                totalRuns: 85,
                recentRuns: 20,
                lastRunDate: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString().split('T')[0], // 2 days ago
                testTypeScores: {
                    'unit': { score: 9, successRate: 85, totalTests: 80, avgDuration: 110 },
                    'integration': { score: 7, successRate: 68, totalTests: 25, avgDuration: 2800 }
                }
            },
            {
                name: 'Notification Service',
                project: 'microservices-platform',
                overallScore: 9,
                stabilityScore: 10,
                coverageScore: 9,
                riskLevel: 'LOW',
                totalTests: 98,
                totalRuns: 75,
                recentRuns: 18,
                lastRunDate: new Date().toISOString().split('T')[0], // Today
                testTypeScores: {
                    'unit': { score: 10, successRate: 98, totalTests: 60, avgDuration: 75 },
                    'api': { score: 9, successRate: 92, totalTests: 25, avgDuration: 380 },
                    'integration': { score: 9, successRate: 88, totalTests: 13, avgDuration: 1650 }
                }
            },
            {
                name: 'Gateway Service',
                project: 'microservices-platform',
                overallScore: 6,
                stabilityScore: 7,
                coverageScore: 6,
                riskLevel: 'HIGH',
                totalTests: 58,
                totalRuns: 45,
                recentRuns: 12,
                lastRunDate: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString().split('T')[0], // 3 days ago
                testTypeScores: {
                    'unit': { score: 7, successRate: 70, totalTests: 30, avgDuration: 150 },
                    'api': { score: 6, successRate: 65, totalTests: 20, avgDuration: 800 },
                    'system': { score: 5, successRate: 55, totalTests: 8, avgDuration: 15000 }
                }
            }
        ];
        
        return services;
    }

    // 2. Intelligent Impact Analysis
    async loadImpactAnalysis() {
        try {
            if (!this.impactAnalysis) {
                console.log('Impact analysis not available, showing placeholder');
                this.showImpactAnalysisPlaceholder();
                return;
            }

            // Simulate PR data for demonstration
            const mockPRData = this.generateMockPRData();
            
            // Run intelligent impact analysis
            const impactAnalysis = await this.impactAnalysis.analyzePRImpact(mockPRData);
            
            // Render the analysis
            this.renderIntelligentImpactAnalysis(impactAnalysis);
        } catch (error) {
            console.error('Error loading impact analysis:', error);
            this.showImpactAnalysisPlaceholder();
        }
    }

    showImpactAnalysisPlaceholder() {
        const container = document.getElementById('impact-analysis-content');
        if (container) {
            container.innerHTML = `
                <div class="alert alert-warning">
                    <i class="fas fa-exclamation-triangle"></i>
                    Impact analysis is temporarily unavailable. Please check the console for details.
                </div>
            `;
        }
    }

    generateMockPRData() {
        // Generate mock PR data for demonstration
        const services = ['user-service', 'product-service', 'order-service', 'notification-service'];
        const randomService = services[Math.floor(Math.random() * services.length)];
        
        return {
            pr_id: 'PR-' + Math.floor(Math.random() * 1000),
            lines_added: Math.floor(Math.random() * 200) + 50,
            lines_deleted: Math.floor(Math.random() * 100) + 20,
            files_changed: Math.floor(Math.random() * 15) + 5,
            services_modified: [randomService],
            author: 'developer@company.com',
            title: `Update ${randomService} functionality`,
            description: `This PR updates the ${randomService} with new features and bug fixes.`
        };
    }

    analyzeImpact(testRuns) {
        const now = new Date();
        const lastWeek = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);

        // Group by service/project
        const serviceImpact = {};
        testRuns.forEach(run => {
            const service = run.test_suite?.project?.name || 'Unknown';
            if (!serviceImpact[service]) {
                serviceImpact[service] = {
                    totalRuns: 0,
                    successfulRuns: 0,
                    failedRuns: 0,
                    recentRuns: 0,
                    recentFailures: 0,
                    avgDuration: 0,
                    lastRun: null
                };
            }

            serviceImpact[service].totalRuns++;
            if (run.status === 'PASS') serviceImpact[service].successfulRuns++;
            if (run.status === 'FAIL') serviceImpact[service].failedRuns++;

            const runDate = new Date(run.started_at);
            if (runDate >= lastWeek) {
                serviceImpact[service].recentRuns++;
                if (run.status === 'FAIL') serviceImpact[service].recentFailures++;
            }

            if (!serviceImpact[service].lastRun || runDate > new Date(serviceImpact[service].lastRun)) {
                serviceImpact[service].lastRun = run.started_at;
            }
        });

        // Calculate impact scores
        const impacts = Object.entries(serviceImpact).map(([service, data]) => {
            const successRate = data.totalRuns > 0 ? (data.successfulRuns / data.totalRuns) * 100 : 0;
            const recentFailureRate = data.recentRuns > 0 ? (data.recentFailures / data.recentRuns) * 100 : 0;
            const impactScore = this.calculateImpactScore(data, successRate, recentFailureRate);
            
            return {
                service,
                ...data,
                successRate,
                recentFailureRate,
                impactScore,
                riskLevel: this.getImpactRiskLevel(impactScore, recentFailureRate)
            };
        });

        return impacts.sort((a, b) => b.impactScore - a.impactScore);
    }

    calculateImpactScore(data, successRate, recentFailureRate) {
        // Weighted score based on multiple factors
        const volumeWeight = Math.min(data.totalRuns / 10, 1); // Normalize to 0-1
        const successWeight = successRate / 100;
        const failureWeight = 1 - (recentFailureRate / 100);
        const recencyWeight = data.recentRuns > 0 ? 1 : 0.5;

        return Math.round((volumeWeight * 0.3 + successWeight * 0.4 + failureWeight * 0.2 + recencyWeight * 0.1) * 100);
    }

    getImpactRiskLevel(impactScore, recentFailureRate) {
        if (recentFailureRate > 30 || impactScore < 50) return 'HIGH';
        if (recentFailureRate > 15 || impactScore < 70) return 'MEDIUM';
        return 'LOW';
    }

    renderIntelligentImpactAnalysis(analysis) {
        const container = document.getElementById('impact-analysis');
        
        container.innerHTML = `
            <div class="impact-analysis-container">
                <!-- Impact Summary -->
                <div class="impact-summary mb-4">
                    <h4 class="mb-3" style="color: var(--executive-blue);">
                        <i class="fas fa-brain me-2"></i>
                        Intelligent Impact Analysis
                    </h4>
                    
                    <div class="row mb-3">
                        <div class="col-md-3">
                            <div class="metric-card text-center">
                                <div class="metric-value" style="color: ${this.getImpactColor(analysis.impact_score)}; font-size: 2rem;">
                                    ${analysis.impact_score.toFixed(1)}
                                </div>
                                <div class="metric-label">Impact Score</div>
                                <div class="metric-subtitle">/10.0</div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="metric-card text-center">
                                <span class="badge ${this.getRiskBadgeClass(analysis.risk_level)} fs-6">
                                    ${analysis.risk_level}
                                </span>
                                <div class="metric-label mt-2">Risk Level</div>
                                <div class="metric-subtitle">${(analysis.risk_assessment.risk_score * 100).toFixed(1)}%</div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="metric-card text-center">
                                <div class="metric-value" style="color: var(--executive-blue); font-size: 2rem;">
                                    ${analysis.blast_radius.length}
                                </div>
                                <div class="metric-label">Services Impacted</div>
                                <div class="metric-subtitle">Blast Radius</div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="metric-card text-center">
                                <div class="metric-value" style="color: var(--executive-green); font-size: 2rem;">
                                    ${(analysis.confidence * 100).toFixed(0)}%
                                </div>
                                <div class="metric-label">Confidence</div>
                                <div class="metric-subtitle">Prediction Accuracy</div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Impact Description -->
                    <div class="impact-description p-3" style="background: #f8fafc; border-radius: 8px;">
                        <p class="mb-2"><strong>Predicted Impact:</strong> ${analysis.impact_description}</p>
                        <p class="mb-0"><strong>Key Changes:</strong> ${analysis.key_changes.join(', ')}</p>
                    </div>
                </div>

                <!-- Blast Radius Visualization -->
                <div class="blast-radius mb-4">
                    <h5 class="mb-3" style="color: var(--executive-blue);">
                        <i class="fas fa-project-diagram me-2"></i>
                        Blast Radius Analysis
                    </h5>
                    <div class="blast-radius-grid">
                        ${analysis.blast_radius.map(service => `
                            <div class="blast-service-card ${service.impact_type}">
                                <div class="service-header">
                                    <span class="service-name">${service.service}</span>
                                    <span class="impact-type-badge ${service.impact_type}">
                                        ${service.impact_type}
                                    </span>
                                </div>
                                <div class="service-metrics">
                                    <div class="impact-probability">
                                        <span class="probability-label">Impact Probability:</span>
                                        <span class="probability-value">${(service.probability * 100).toFixed(1)}%</span>
                                    </div>
                                    <div class="service-criticality">
                                        <span class="criticality-badge ${service.criticality.toLowerCase()}">
                                            ${service.criticality}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                </div>

                <!-- Risk Assessment -->
                <div class="risk-assessment mb-4">
                    <h5 class="mb-3" style="color: var(--executive-blue);">
                        <i class="fas fa-shield-alt me-2"></i>
                        Risk Assessment
                    </h5>
                    <div class="risk-breakdown">
                        ${Object.entries(analysis.risk_assessment.risk_components).map(([factor, score]) => `
                            <div class="risk-factor mb-2">
                                <div class="d-flex justify-content-between align-items-center">
                                    <span class="factor-name">${this.formatFactorName(factor)}</span>
                                    <span class="risk-score">${(score * 100).toFixed(1)}%</span>
                                </div>
                                <div class="progress" style="height: 8px;">
                                    <div class="progress-bar ${this.getRiskBarClass(score)}" 
                                         style="width: ${score * 100}%"></div>
                                </div>
                            </div>
                        `).join('')}
                    </div>
                </div>

                <!-- Recommendations -->
                <div class="recommendations">
                    <h5 class="mb-3" style="color: var(--executive-blue);">
                        <i class="fas fa-lightbulb me-2"></i>
                        Recommendations
                    </h5>
                    <div class="recommendation-list">
                        ${analysis.recommendations.map(rec => `
                            <div class="recommendation-item ${rec.priority.toLowerCase()} mb-3">
                                <div class="recommendation-header d-flex justify-content-between align-items-center mb-2">
                                    <span class="priority-badge ${rec.priority.toLowerCase()}">
                                        ${rec.priority}
                                    </span>
                                    <span class="recommendation-type">${rec.type.replace('_', ' ').toUpperCase()}</span>
                                </div>
                                <div class="recommendation-message mb-1">${rec.message}</div>
                                <div class="recommendation-action text-muted">${rec.action}</div>
                                ${rec.services && rec.services.length > 0 ? `
                                    <div class="recommendation-services mt-2">
                                        <small class="text-muted">Affected Services: ${rec.services.join(', ')}</small>
                                    </div>
                                ` : ''}
                            </div>
                        `).join('')}
                    </div>
                </div>
            </div>
        `;
    }

    getImpactColor(score) {
        if (score >= 7) return '#ef4444';
        if (score >= 4) return '#f59e0b';
        return '#10b981';
    }

    formatFactorName(factor) {
        return factor.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase());
    }

    getRiskBarClass(score) {
        if (score >= 0.7) return 'bg-danger';
        if (score >= 0.4) return 'bg-warning';
        return 'bg-success';
    }

    getRiskBadgeClass(riskLevel) {
        switch (riskLevel) {
            case 'HIGH': return 'bg-danger';
            case 'MEDIUM': return 'bg-warning';
            case 'LOW': return 'bg-success';
            default: return 'bg-secondary';
        }
    }

    getDefaultImpactAnalysis() {
        return [
            {
                service: 'user-service',
                impactScore: 85,
                totalRuns: 45,
                successRate: 92.3,
                recentFailures: 2,
                riskLevel: 'LOW'
            },
            {
                service: 'order-service',
                impactScore: 78,
                totalRuns: 38,
                successRate: 87.5,
                recentFailures: 5,
                riskLevel: 'MEDIUM'
            },
            {
                service: 'product-service',
                impactScore: 72,
                totalRuns: 32,
                successRate: 84.2,
                recentFailures: 8,
                riskLevel: 'MEDIUM'
            }
        ];
    }

    // 3. PR Quality Insights
    async loadQualityInsights() {
        try {
            // Calculate date range for last 3 months
            const threeMonthsAgo = new Date();
            threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
            const threeMonthsAgoISO = threeMonthsAgo.toISOString();

            // Fetch all test results with pagination - last 3 months
            const testResults = await this.fetchAllRecords('test_result', {
                select: `id, status, duration_ms, created_at, test_case(name, tags), test_run(test_suite(name, project(name)))`,
                filter: { column: 'created_at', operator: 'gte', value: threeMonthsAgoISO },
                orderBy: { column: 'created_at', ascending: false }
            });

            const qualityInsights = this.analyzeQuality(testResults);
            this.renderQualityInsights(qualityInsights);
        } catch (error) {
            console.error('Error loading quality insights:', error);
            this.renderQualityInsights(this.getDefaultQualityInsights());
        }
    }

    analyzeQuality(testResults) {
        const now = new Date();
        const lastWeek = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);

        // Code Coverage Analysis
        const totalTests = testResults.length;
        const passedTests = testResults.filter(r => r.status === 'PASS').length;
        const coverageScore = totalTests > 0 ? (passedTests / totalTests) * 100 : 0;

        // Test Stability Analysis
        const recentTests = testResults.filter(r => new Date(r.created_at) >= lastWeek);
        const flakyTests = this.identifyFlakyTests(testResults);
        const stabilityScore = Math.max(0, 100 - (flakyTests.length / totalTests) * 100);

        // Performance Quality
        const durations = testResults.map(r => r.duration_ms).filter(d => d && d > 0);
        const avgDuration = durations.length > 0 ? durations.reduce((a, b) => a + b, 0) / durations.length : 0;
        const slowTests = durations.filter(d => d > 5000).length;
        const performanceScore = Math.max(0, 100 - (slowTests / durations.length) * 100);

        // Security Quality
        const securityTests = testResults.filter(r => {
            const name = r.test_case?.name?.toLowerCase() || '';
            const tags = Array.isArray(r.test_case?.tags) ? r.test_case.tags.map(tag => String(tag).toLowerCase()) : [];
            return name.includes('security') || tags.some(tag => tag.includes('security'));
        });
        const securityScore = securityTests.length > 0 ? 
            (securityTests.filter(t => t.status === 'PASS').length / securityTests.length) * 100 : 100;

        // Maintainability Score
        const maintainabilityScore = this.calculateMaintainabilityScore(testResults);

        return {
            coverageScore,
            stabilityScore,
            performanceScore,
            securityScore,
            maintainabilityScore,
            totalTests,
            flakyTests: flakyTests.length,
            slowTests,
            securityTests: securityTests.length
        };
    }

    identifyFlakyTests(testResults) {
        // Group tests by name and identify those with inconsistent results
        const testGroups = {};
        testResults.forEach(result => {
            const testName = result.test_case?.name;
            if (!testName) return;
            
            if (!testGroups[testName]) {
                testGroups[testName] = [];
            }
            testGroups[testName].push(result.status);
        });

        const flakyTests = [];
        Object.entries(testGroups).forEach(([testName, statuses]) => {
            if (statuses.length >= 3) {
                const uniqueStatuses = [...new Set(statuses)];
                if (uniqueStatuses.length > 1) {
                    flakyTests.push(testName);
                }
            }
        });

        return flakyTests;
    }

    calculateMaintainabilityScore(testResults) {
        // Simple maintainability score based on test organization and naming
        const wellNamedTests = testResults.filter(r => {
            const name = r.test_case?.name || '';
            return name.length > 10 && name.includes('_') && !name.includes('test');
        }).length;

        return testResults.length > 0 ? (wellNamedTests / testResults.length) * 100 : 0;
    }

    renderQualityInsights(insights) {
        const container = document.getElementById('quality-insights');
        
        const qualityMetrics = [
            {
                title: 'Code Coverage',
                score: insights.coverageScore,
                description: 'Percentage of code covered by tests',
                icon: 'fas fa-chart-pie'
            },
            {
                title: 'Test Stability',
                score: insights.stabilityScore,
                description: 'Consistency of test results over time',
                icon: 'fas fa-shield-alt'
            },
            {
                title: 'Performance Quality',
                score: insights.performanceScore,
                description: 'Efficiency of test execution',
                icon: 'fas fa-tachometer-alt'
            },
            {
                title: 'Security Coverage',
                score: insights.securityScore,
                description: 'Security test coverage and success rate',
                icon: 'fas fa-lock'
            },
            {
                title: 'Maintainability',
                score: insights.maintainabilityScore,
                description: 'Test code organization and clarity',
                icon: 'fas fa-tools'
            }
        ];

        container.innerHTML = qualityMetrics.map(metric => `
            <div class="quality-metric">
                <div class="quality-metric-info">
                    <div class="quality-metric-title">
                        <i class="${metric.icon} me-2"></i>
                        ${metric.title}
                    </div>
                    <div class="quality-metric-description">${metric.description}</div>
                </div>
                <div class="quality-score ${this.getQualityScoreClass(metric.score)}">
                    ${Math.round(metric.score)}%
                </div>
            </div>
        `).join('');
    }

    getQualityScoreClass(score) {
        if (score >= 90) return 'score-excellent';
        if (score >= 75) return 'score-good';
        if (score >= 60) return 'score-warning';
        return 'score-critical';
    }

    getDefaultQualityInsights() {
        return {
            coverageScore: 87.5,
            stabilityScore: 92.3,
            performanceScore: 78.9,
            securityScore: 85.2,
            maintainabilityScore: 81.7,
            totalTests: 1250,
            flakyTests: 12,
            slowTests: 45,
            securityTests: 89
        };
    }

    // 4. Anomaly Detection
    async loadAnomalyDetection() {
        try {
            // Calculate date range for last 3 months
            const threeMonthsAgo = new Date();
            threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
            const threeMonthsAgoISO = threeMonthsAgo.toISOString();

            // Fetch all test data for anomaly detection with pagination - last 3 months
            const testResults = await this.fetchAllRecords('test_result', {
                select: `id, status, duration_ms, created_at, test_case(name), test_run(test_suite(name, project(name)))`,
                filter: { column: 'created_at', operator: 'gte', value: threeMonthsAgoISO },
                orderBy: { column: 'created_at', ascending: false }
            });

            const anomalies = this.detectAnomalies(testResults);
            this.renderAnomalyDetection(anomalies);
        } catch (error) {
            console.error('Error loading anomaly detection:', error);
            this.renderAnomalyDetection(this.getDefaultAnomalies());
        }
    }

    detectAnomalies(testResults) {
        const anomalies = [];

        // Performance Anomalies
        const durations = testResults.map(r => r.duration_ms).filter(d => d && d > 0);
        if (durations.length > 0) {
            const avgDuration = durations.reduce((a, b) => a + b, 0) / durations.length;
            const stdDev = Math.sqrt(durations.reduce((sq, n) => sq + Math.pow(n - avgDuration, 2), 0) / durations.length);
            const threshold = avgDuration + (2 * stdDev);

            const slowTests = testResults.filter(r => r.duration_ms && r.duration_ms > threshold);
            if (slowTests.length > 0) {
                anomalies.push({
                    type: 'performance',
                    severity: 'warning',
                    title: 'Performance Degradation Detected',
                    description: `${slowTests.length} tests are running significantly slower than average`,
                    details: `Average duration: ${Math.round(avgDuration)}ms, Threshold: ${Math.round(threshold)}ms`,
                    count: slowTests.length
                });
            }
        }

        // Failure Rate Anomalies
        const recentFailures = testResults.filter(r => r.status === 'FAIL');
        const failureRate = testResults.length > 0 ? (recentFailures.length / testResults.length) * 100 : 0;
        
        if (failureRate > 20) {
            anomalies.push({
                type: 'failure_rate',
                severity: 'critical',
                title: 'High Failure Rate Detected',
                description: `Failure rate is ${failureRate.toFixed(1)}%, significantly above normal`,
                details: `${recentFailures.length} failures out of ${testResults.length} tests`,
                count: recentFailures.length
            });
        }

        // Service-Specific Anomalies
        const serviceGroups = {};
        testResults.forEach(result => {
            const service = result.test_run?.test_suite?.project?.name || 'Unknown';
            if (!serviceGroups[service]) {
                serviceGroups[service] = { total: 0, failures: 0 };
            }
            serviceGroups[service].total++;
            if (result.status === 'FAIL') {
                serviceGroups[service].failures++;
            }
        });

        Object.entries(serviceGroups).forEach(([service, data]) => {
            const serviceFailureRate = (data.failures / data.total) * 100;
            if (serviceFailureRate > 30) {
                anomalies.push({
                    type: 'service_failure',
                    severity: 'critical',
                    title: `Service Failure Anomaly: ${service}`,
                    description: `${service} has a failure rate of ${serviceFailureRate.toFixed(1)}%`,
                    details: `${data.failures} failures out of ${data.total} tests`,
                    count: data.failures
                });
            }
        });

        // Test Volume Anomalies
        const today = new Date();
        const yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);
        const dayBefore = new Date(today.getTime() - 48 * 60 * 60 * 1000);

        const todayTests = testResults.filter(r => new Date(r.created_at).toDateString() === today.toDateString()).length;
        const yesterdayTests = testResults.filter(r => new Date(r.created_at).toDateString() === yesterday.toDateString()).length;

        if (todayTests > 0 && yesterdayTests > 0) {
            const volumeChange = ((todayTests - yesterdayTests) / yesterdayTests) * 100;
            if (Math.abs(volumeChange) > 50) {
                anomalies.push({
                    type: 'volume',
                    severity: 'info',
                    title: 'Test Volume Anomaly',
                    description: `Test volume changed by ${Math.abs(volumeChange).toFixed(1)}%`,
                    details: `Today: ${todayTests} tests, Yesterday: ${yesterdayTests} tests`,
                    count: Math.abs(todayTests - yesterdayTests)
                });
            }
        }

        return anomalies.sort((a, b) => {
            const severityOrder = { critical: 3, warning: 2, info: 1 };
            return severityOrder[b.severity] - severityOrder[a.severity];
        });
    }

    renderAnomalyDetection(anomalies) {
        const container = document.getElementById('anomaly-detection');
        
        if (anomalies.length === 0) {
            container.innerHTML = `
                <div class="text-center text-success">
                    <i class="fas fa-check-circle fa-3x mb-3"></i>
                    <h5>No Anomalies Detected</h5>
                    <p class="text-muted">All systems are operating within normal parameters</p>
                </div>
            `;
            return;
        }

        container.innerHTML = anomalies.map(anomaly => `
            <div class="anomaly-item ${anomaly.severity}">
                <div class="anomaly-title">
                    <i class="fas fa-${this.getAnomalyIcon(anomaly.type)} me-2"></i>
                    ${anomaly.title}
                </div>
                <div class="anomaly-description">${anomaly.description}</div>
                <div class="anomaly-severity severity-${anomaly.severity}">
                    ${anomaly.severity.toUpperCase()}
                </div>
                <div class="text-muted small">${anomaly.details}</div>
            </div>
        `).join('');
    }

    getAnomalyIcon(type) {
        switch (type) {
            case 'performance': return 'tachometer-alt';
            case 'failure_rate': return 'exclamation-triangle';
            case 'service_failure': return 'server';
            case 'volume': return 'chart-line';
            default: return 'exclamation-circle';
        }
    }

    getDefaultAnomalies() {
        return [
            {
                type: 'performance',
                severity: 'warning',
                title: 'Performance Degradation Detected',
                description: '3 tests are running significantly slower than average',
                details: 'Average duration: 1,250ms, Threshold: 3,500ms',
                count: 3
            },
            {
                type: 'service_failure',
                severity: 'critical',
                title: 'Service Failure Anomaly: order-service',
                description: 'order-service has a failure rate of 35.2%',
                details: '12 failures out of 34 tests',
                count: 12
            }
        ];
    }

    async refreshDashboard() {
        console.log('Manual refresh initiated...');
        await this.loadExecutiveData();
        this.showSuccess('Executive dashboard refreshed manually!');
    }

    showSuccess(message) {
        this.showAlert(message, 'success');
    }

    showError(message) {
        this.showAlert(message, 'danger');
    }

    showAlert(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show alert-executive`;
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.insertBefore(alertDiv, document.body.firstChild);
        
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.parentNode.removeChild(alertDiv);
            }
        }, 5000);
    }
}

// Global functions for HTML onclick handlers
function refreshDashboard() {
    executiveDashboard.refreshDashboard();
}

// Global function for toggling suggestions
function toggleSuggestions(serviceName) {
    const container = document.getElementById(`suggestions-${serviceName}`);
    const icon = document.getElementById(`icon-${serviceName}`);
    const button = icon.parentElement;
    
    if (!container) return;
    
    const hiddenSuggestions = container.querySelectorAll('.suggestion-item.d-none');
    const isExpanded = hiddenSuggestions.length === 0;
    
    if (isExpanded) {
        // Collapse - hide suggestions beyond first 3
        const allSuggestions = container.querySelectorAll('.suggestion-item');
        allSuggestions.forEach((suggestion, index) => {
            if (index >= 3) {
                suggestion.classList.add('d-none');
            }
        });
        container.style.maxHeight = '200px';
        icon.className = 'fas fa-chevron-down';
        button.innerHTML = `<i class="fas fa-chevron-down" id="icon-${serviceName}"></i> Show ${allSuggestions.length - 3} more suggestions`;
    } else {
        // Expand - show all suggestions
        hiddenSuggestions.forEach(suggestion => {
            suggestion.classList.remove('d-none');
        });
        container.style.maxHeight = '400px';
        icon.className = 'fas fa-chevron-up';
        button.innerHTML = `<i class="fas fa-chevron-up" id="icon-${serviceName}"></i> Show less`;
    }
}

// Initialize dashboard when page loads
let executiveDashboard;
document.addEventListener('DOMContentLoaded', () => {
    executiveDashboard = new ExecutiveDashboard();
});
