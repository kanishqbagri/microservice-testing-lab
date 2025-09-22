// Metrics Calculator - Handles all scoring and calculation logic
import { CONFIG, SERVICE_NAMES } from './config.js';

export class MetricsCalculator {
    constructor() {
        this.testTypeWeights = CONFIG.TEST_TYPE_WEIGHTS;
        this.performanceThresholds = CONFIG.PERFORMANCE_THRESHOLDS;
    }

    // Convert percentage (0-100) to 1-10 score scale
    convertPercentageToScore(percentage) {
        if (percentage <= 0) return 1;
        if (percentage >= 100) return 10;
        
        // Linear conversion: 0-100% maps to 1-10
        // Formula: score = 1 + (percentage / 100) * 9
        const score = 1 + (percentage / 100) * 9;
        return Math.round(score);
    }

    // Extract service name from test suite name
    extractServiceName(suiteName) {
        if (!suiteName) return 'Unknown Service';
        
        const name = suiteName.toLowerCase();
        
        // Check for exact matches first
        for (const [key, serviceName] of Object.entries(SERVICE_NAMES)) {
            if (name.includes(key)) {
                return serviceName;
            }
        }
        
        return 'Unknown Service';
    }

    // Categorize test type based on suite name
    categorizeTestType(suiteName) {
        if (!suiteName) return 'unit';
        
        const name = suiteName.toLowerCase();
        if (name.includes('api') || name.includes('rest') || name.includes('endpoint')) return 'api';
        if (name.includes('integration') || name.includes('contract')) return 'integration';
        if (name.includes('ui') || name.includes('e2e') || name.includes('end-to-end')) return 'ui';
        if (name.includes('system') || name.includes('smoke') || name.includes('regression')) return 'system';
        return 'unit'; // Default to unit tests
    }

    // Calculate performance penalty for slow tests
    calculatePerformancePenalty(avgDuration, testType) {
        const threshold = this.performanceThresholds[testType] || 1000;
        
        if (avgDuration <= threshold) return 0;
        
        // Calculate penalty based on how much over threshold
        const overage = avgDuration - threshold;
        const penaltyRate = 0.1; // 10% penalty per 1000ms over threshold
        return Math.min(20, (overage / 1000) * penaltyRate * 100); // Cap at 20% penalty
    }

    // Calculate stability score based on recent failure patterns
    calculateStabilityScore(service) {
        const now = new Date();
        const lastWeek = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
        
        let recentFailures = 0;
        let recentTotal = 0;
        let overallFailures = 0;
        let overallTotal = 0;
        
        Object.values(service.testTypes).forEach(testType => {
            testType.runs.forEach(run => {
                const runDate = new Date(run.started_at);
                overallTotal++;
                if (run.status === 'FAILED') overallFailures++;
                
                if (runDate >= lastWeek) {
                    recentTotal++;
                    if (run.status === 'FAILED') recentFailures++;
                }
            });
        });
        
        const recentFailureRate = recentTotal > 0 ? (recentFailures / recentTotal) * 100 : 0;
        const overallFailureRate = overallTotal > 0 ? (overallFailures / overallTotal) * 100 : 0;
        
        // Weight recent failures more heavily (70% recent, 30% overall)
        const weightedFailureRate = (recentFailureRate * 0.7) + (overallFailureRate * 0.3);
        return Math.max(0, 100 - weightedFailureRate);
    }

    // Calculate coverage score based on test type diversity
    calculateCoverageScore(testTypes, testTypeWeights) {
        const presentTypes = Object.keys(testTypes).filter(type => testTypes[type].total > 0);
        const totalPossibleTypes = Object.keys(testTypeWeights).length;
        
        const coverageRatio = presentTypes.length / totalPossibleTypes;
        const weightedCoverage = presentTypes.reduce((sum, type) => {
            return sum + (testTypeWeights[type] || 0);
        }, 0);
        
        return Math.min(100, (coverageRatio * 50) + (weightedCoverage * 50));
    }

    // Assess service risk level
    assessServiceRisk(service, overallScore) {
        const recentFailureRate = this.calculateRecentFailureRate(service);
        
        if (overallScore >= 80 && recentFailureRate <= 5) return 'LOW';
        if (overallScore >= 60 && recentFailureRate <= 15) return 'MEDIUM';
        return 'HIGH';
    }

    // Calculate recent failure rate
    calculateRecentFailureRate(service) {
        const now = new Date();
        const lastWeek = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
        
        let recentFailures = 0;
        let recentTotal = 0;
        
        Object.values(service.testTypes).forEach(testType => {
            testType.runs.forEach(run => {
                const runDate = new Date(run.started_at);
                if (runDate >= lastWeek) {
                    recentTotal++;
                    if (run.status === 'FAILED') recentFailures++;
                }
            });
        });
        
        return recentTotal > 0 ? (recentFailures / recentTotal) * 100 : 0;
    }

    // Calculate service scores
    calculateServiceScores(service, testTypeWeights) {
        let weightedScore = 0;
        let totalWeight = 0;
        const testTypeScores = {};

        // Calculate score for each test type
        Object.entries(service.testTypes).forEach(([testType, data]) => {
            const weight = testTypeWeights[testType] || 0.1;
            const successRate = data.total > 0 ? (data.passed / data.total) * 100 : 0;
            
            // Calculate average duration for performance scoring
            const avgDuration = data.durations.length > 0 ? 
                data.durations.reduce((a, b) => a + b, 0) / data.durations.length : 0;
            
            // Performance penalty for slow tests
            const performancePenalty = this.calculatePerformancePenalty(avgDuration, testType);
            const adjustedScore = Math.max(0, successRate - performancePenalty);
            
            // Convert percentage score to 1-10 scale
            const score1to10 = this.convertPercentageToScore(adjustedScore);
            
            testTypeScores[testType] = {
                score: score1to10,
                successRate: Math.round(successRate),
                totalTests: data.total,
                avgDuration: Math.round(avgDuration),
                performancePenalty: Math.round(performancePenalty)
            };

            weightedScore += adjustedScore * weight;
            totalWeight += weight;
        });

        const overallScorePercentage = totalWeight > 0 ? Math.round(weightedScore / totalWeight) : 0;
        const overallScore = this.convertPercentageToScore(overallScorePercentage);
        
        // System Stability Score (based on recent failure patterns) - convert to 1-10
        const stabilityScorePercentage = this.calculateStabilityScore(service);
        const stabilityScore = this.convertPercentageToScore(stabilityScorePercentage);
        
        // Risk Assessment
        const riskLevel = this.assessServiceRisk(service, overallScorePercentage);
        
        // Test Coverage Score (based on test type diversity) - convert to 1-10
        const coverageScorePercentage = this.calculateCoverageScore(service.testTypes, testTypeWeights);
        const coverageScore = this.convertPercentageToScore(coverageScorePercentage);

        return {
            overallScore,
            stabilityScore,
            coverageScore,
            riskLevel,
            testTypeScores,
            totalTests: Object.values(service.testTypes).reduce((sum, type) => sum + type.total, 0),
            lastRunDate: service.lastRun ? new Date(service.lastRun).toLocaleDateString() : 'Never'
        };
    }

    // Calculate per-service metrics
    calculatePerServiceMetrics(testRuns, testResults) {
        const now = new Date();
        const lastWeek = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);

        // Group data by service
        const services = {};

        // Process test runs
        testRuns.forEach(run => {
            const suiteName = run.test_suite?.name || 'Unknown Suite';
            const serviceName = this.extractServiceName(suiteName);
            const testType = this.categorizeTestType(suiteName);

            if (!services[serviceName]) {
                services[serviceName] = {
                    name: serviceName,
                    project: run.test_suite?.project?.name || 'Unknown Project',
                    testTypes: {},
                    lastRun: null
                };
            }

            if (!services[serviceName].testTypes[testType]) {
                services[serviceName].testTypes[testType] = {
                    total: 0,
                    passed: 0,
                    failed: 0,
                    durations: [],
                    runs: []
                };
            }

            services[serviceName].testTypes[testType].total++;
            if (run.status === 'PASSED') {
                services[serviceName].testTypes[testType].passed++;
            } else {
                services[serviceName].testTypes[testType].failed++;
            }

            services[serviceName].testTypes[testType].runs.push(run);
            
            // Track last run date
            const runDate = new Date(run.started_at);
            if (!services[serviceName].lastRun || runDate > new Date(services[serviceName].lastRun)) {
                services[serviceName].lastRun = run.started_at;
            }
        });

        // Process test results for durations
        testResults.forEach(result => {
            const suiteName = result.test_run?.test_suite?.name || 'Unknown Suite';
            const serviceName = this.extractServiceName(suiteName);
            const testType = this.categorizeTestType(suiteName);

            if (services[serviceName] && services[serviceName].testTypes[testType]) {
                if (result.duration_ms) {
                    services[serviceName].testTypes[testType].durations.push(result.duration_ms);
                }
            }
        });

        // Calculate scores for each service
        const result = Object.values(services).map(service => {
            const scores = this.calculateServiceScores(service, this.testTypeWeights);
            
            // Calculate additional metrics
            const totalRuns = Object.values(service.testTypes).reduce((sum, type) => sum + type.runs.length, 0);
            const recentRuns = Object.values(service.testTypes).reduce((sum, type) => {
                return sum + type.runs.filter(run => new Date(run.started_at) >= lastWeek).length;
            }, 0);

            return {
                ...service,
                ...scores,
                totalRuns,
                recentRuns
            };
        });

        return result;
    }
}
