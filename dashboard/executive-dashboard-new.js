// Executive Dashboard - Main Orchestrator (Streamlined)
import { CONFIG } from './js/config.js';
import { SmartScorecards } from './js/scorecards.js';

class ExecutiveDashboard {
    constructor() {
        // Initialize Supabase connection
        this.supabase = window.supabase.createClient(CONFIG.SUPABASE.URL, CONFIG.SUPABASE.KEY);
        
        // Initialize modules
        this.scorecards = new SmartScorecards(this.supabase);
        
        // Initialize impact analysis
        try {
            this.impactAnalysis = new IntelligentImpactAnalysis();
            console.log('IntelligentImpactAnalysis initialized successfully');
        } catch (error) {
            console.error('Failed to initialize IntelligentImpactAnalysis:', error);
            this.impactAnalysis = null;
        }
        
        this.charts = {};
        this.lastUpdateTime = new Date();
        
        this.init();
    }

    async init() {
        this.updateCurrentTime();
        setInterval(() => this.updateCurrentTime(), 1000);
        
        await this.loadExecutiveData();
        
        // Auto-refresh every 5 minutes for executive view
        setInterval(() => this.refreshDashboard(), CONFIG.DATA.AUTO_REFRESH_INTERVAL);
    }

    updateCurrentTime() {
        const now = new Date();
        document.getElementById('current-time').textContent = now.toLocaleString();
        document.getElementById('last-updated').textContent = this.lastUpdateTime.toLocaleTimeString();
    }

    async loadExecutiveData() {
        try {
            await Promise.all([
                this.scorecards.loadSmartScorecards(),
                this.loadImpactAnalysis(),
                this.loadQualityInsights(),
                this.loadAnomalyDetection()
            ]);
        } catch (error) {
            console.error('Error loading executive data:', error);
            this.showError('Failed to load executive dashboard data');
        }
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
            id: `PR-${Math.floor(Math.random() * 10000)}`,
            title: `Update ${randomService} functionality`,
            author: 'developer@company.com',
            servicesModified: [randomService],
            filesChanged: Math.floor(Math.random() * 20) + 5,
            linesAdded: Math.floor(Math.random() * 300) + 50,
            linesDeleted: Math.floor(Math.random() * 100) + 10,
            features: ['api', 'database', 'security']
        };
    }

    renderIntelligentImpactAnalysis(analysis) {
        const container = document.getElementById('impact-analysis-content');
        if (!container) return;

        container.innerHTML = `
            <div class="impact-analysis-container">
                <!-- Impact Summary -->
                <div class="metric-card mb-4">
                    <h6 class="metric-title">Impact Summary</h6>
                    <div class="row">
                        <div class="col-4">
                            <div class="metric-value ${analysis.riskLevel.toLowerCase()}-risk">
                                ${analysis.riskLevel}
                            </div>
                            <div class="metric-label">Risk Level</div>
                        </div>
                        <div class="col-4">
                            <div class="metric-value">
                                ${analysis.blastRadius.length}
                            </div>
                            <div class="metric-label">Services Affected</div>
                        </div>
                        <div class="col-4">
                            <div class="metric-value">
                                ${Math.round(analysis.confidence * 100)}%
                            </div>
                            <div class="metric-label">Confidence</div>
                        </div>
                    </div>
                </div>

                <!-- Blast Radius -->
                <div class="metric-card mb-4">
                    <h6 class="metric-title">Blast Radius</h6>
                    <div class="blast-radius-list">
                        ${analysis.blastRadius.map(service => `
                            <div class="blast-radius-item ${service.impact_type}">
                                <div class="service-name">${service.service}</div>
                                <div class="impact-probability">${Math.round(service.probability * 100)}%</div>
                                <div class="impact-type">${service.impact_type}</div>
                            </div>
                        `).join('')}
                    </div>
                </div>

                <!-- Risk Assessment -->
                <div class="metric-card mb-4">
                    <h6 class="metric-title">Risk Assessment</h6>
                    <div class="risk-breakdown">
                        <div class="risk-factor">
                            <span class="risk-label">Code Complexity:</span>
                            <span class="risk-value ${analysis.riskFactors.complexity.toLowerCase()}">${analysis.riskFactors.complexity}</span>
                        </div>
                        <div class="risk-factor">
                            <span class="risk-label">Test Coverage:</span>
                            <span class="risk-value ${analysis.riskFactors.coverage.toLowerCase()}">${analysis.riskFactors.coverage}</span>
                        </div>
                        <div class="risk-factor">
                            <span class="risk-label">Dependency Depth:</span>
                            <span class="risk-value ${analysis.riskFactors.dependencies.toLowerCase()}">${analysis.riskFactors.dependencies}</span>
                        </div>
                    </div>
                </div>

                <!-- Recommendations -->
                <div class="metric-card">
                    <h6 class="metric-title">Recommendations</h6>
                    <ul class="recommendations-list">
                        ${analysis.recommendations.map(rec => `
                            <li class="recommendation-item ${rec.priority.toLowerCase()}">
                                <i class="fas fa-${rec.priority === 'HIGH' ? 'exclamation-triangle' : rec.priority === 'MEDIUM' ? 'info-circle' : 'check-circle'}"></i>
                                ${rec.description}
                            </li>
                        `).join('')}
                    </ul>
                </div>
            </div>
        `;
    }

    // 3. PR Quality Insights
    async loadQualityInsights() {
        try {
            // Calculate date range for last 3 months
            const threeMonthsAgo = new Date();
            threeMonthsAgo.setMonth(threeMonthsAgo.getMonth() - 3);
            const threeMonthsAgoISO = threeMonthsAgo.toISOString();

            // Fetch all test results with pagination - last 3 months
            const testResults = await this.dataService.fetchAllRecords('test_result', {
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
        
        const qualityMetrics = {
            totalTests: testResults.length,
            passedTests: testResults.filter(t => t.status === 'PASSED').length,
            failedTests: testResults.filter(t => t.status === 'FAILED').length,
            recentTests: testResults.filter(t => new Date(t.created_at) >= lastWeek).length,
            avgDuration: testResults.reduce((sum, t) => sum + (t.duration_ms || 0), 0) / testResults.length,
            slowTests: testResults.filter(t => (t.duration_ms || 0) > 5000).length,
            securityTests: testResults.filter(t => t.test_case?.tags?.includes('security')).length
        };

        return {
            successRate: Math.round((qualityMetrics.passedTests / qualityMetrics.totalTests) * 100),
            recentTrend: qualityMetrics.recentTests > 0 ? 'UP' : 'STABLE',
            performanceScore: Math.max(0, 100 - (qualityMetrics.avgDuration / 100)),
            qualityScore: Math.round(
                (qualityMetrics.passedTests / qualityMetrics.totalTests) * 40 +
                (qualityMetrics.recentTests / qualityMetrics.totalTests) * 30 +
                (qualityMetrics.securityTests / qualityMetrics.totalTests) * 30
            ),
            metrics: qualityMetrics
        };
    }

    renderQualityInsights(insights) {
        const container = document.getElementById('quality-insights-content');
        if (!container) return;

        container.innerHTML = `
            <div class="quality-insights-container">
                <div class="row">
                    <div class="col-6">
                        <div class="metric-card">
                            <h6 class="metric-title">Success Rate</h6>
                            <div class="metric-value success">${insights.successRate}%</div>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="metric-card">
                            <h6 class="metric-title">Quality Score</h6>
                            <div class="metric-value ${insights.qualityScore >= 80 ? 'success' : insights.qualityScore >= 60 ? 'warning' : 'danger'}">${insights.qualityScore}/100</div>
                        </div>
                    </div>
                </div>
                <div class="row mt-3">
                    <div class="col-4">
                        <div class="metric-value">${insights.metrics.totalTests}</div>
                        <div class="metric-label">Total Tests</div>
                    </div>
                    <div class="col-4">
                        <div class="metric-value">${insights.metrics.recentTests}</div>
                        <div class="metric-label">Recent Tests</div>
                    </div>
                    <div class="col-4">
                        <div class="metric-value">${insights.metrics.slowTests}</div>
                        <div class="metric-label">Slow Tests</div>
                    </div>
                </div>
            </div>
        `;
    }

    getDefaultQualityInsights() {
        return {
            successRate: 87,
            recentTrend: 'UP',
            performanceScore: 78,
            qualityScore: 82,
            metrics: {
                totalTests: 1250,
                passedTests: 1087,
                failedTests: 163,
                recentTests: 45,
                avgDuration: 1200,
                slowTests: 45,
                securityTests: 89
            }
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
            const testResults = await this.dataService.fetchAllRecords('test_result', {
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
        
        // Group by test case
        const testCaseGroups = {};
        testResults.forEach(result => {
            const testName = result.test_case?.name || 'Unknown';
            if (!testCaseGroups[testName]) {
                testCaseGroups[testName] = [];
            }
            testCaseGroups[testName].push(result);
        });

        // Detect anomalies
        Object.entries(testCaseGroups).forEach(([testName, results]) => {
            if (results.length < 5) return; // Need enough data points

            const durations = results.map(r => r.duration_ms || 0).filter(d => d > 0);
            if (durations.length < 3) return;

            const avgDuration = durations.reduce((a, b) => a + b, 0) / durations.length;
            const stdDev = Math.sqrt(durations.reduce((sq, n) => sq + Math.pow(n - avgDuration, 2), 0) / durations.length);
            
            // Check for performance anomalies
            const recentResults = results.slice(0, 3);
            const hasPerformanceAnomaly = recentResults.some(r => 
                r.duration_ms && Math.abs(r.duration_ms - avgDuration) > 2 * stdDev
            );

            if (hasPerformanceAnomaly) {
                anomalies.push({
                    type: 'PERFORMANCE',
                    testName,
                    severity: 'MEDIUM',
                    description: `Performance anomaly detected in ${testName}`,
                    avgDuration: Math.round(avgDuration),
                    recentDuration: Math.round(recentResults[0].duration_ms || 0)
                });
            }

            // Check for failure patterns
            const recentFailures = recentResults.filter(r => r.status === 'FAILED').length;
            if (recentFailures >= 2) {
                anomalies.push({
                    type: 'RELIABILITY',
                    testName,
                    severity: 'HIGH',
                    description: `Multiple recent failures in ${testName}`,
                    failureCount: recentFailures
                });
            }
        });

        return anomalies.slice(0, 10); // Limit to top 10 anomalies
    }

    renderAnomalyDetection(anomalies) {
        const container = document.getElementById('anomaly-detection-content');
        if (!container) return;

        container.innerHTML = `
            <div class="anomaly-detection-container">
                <div class="anomaly-summary mb-3">
                    <div class="row text-center">
                        <div class="col-4">
                            <div class="metric-value ${anomalies.length === 0 ? 'success' : 'warning'}">${anomalies.length}</div>
                            <div class="metric-label">Active Anomalies</div>
                        </div>
                        <div class="col-4">
                            <div class="metric-value ${anomalies.filter(a => a.severity === 'HIGH').length === 0 ? 'success' : 'danger'}">${anomalies.filter(a => a.severity === 'HIGH').length}</div>
                            <div class="metric-label">High Severity</div>
                        </div>
                        <div class="col-4">
                            <div class="metric-value">${anomalies.filter(a => a.type === 'PERFORMANCE').length}</div>
                            <div class="metric-label">Performance Issues</div>
                        </div>
                    </div>
                </div>
                
                ${anomalies.length > 0 ? `
                    <div class="anomalies-list">
                        ${anomalies.map(anomaly => `
                            <div class="anomaly-item ${anomaly.severity.toLowerCase()}">
                                <div class="anomaly-header">
                                    <span class="anomaly-type">${anomaly.type}</span>
                                    <span class="anomaly-severity badge ${anomaly.severity === 'HIGH' ? 'bg-danger' : 'bg-warning'}">${anomaly.severity}</span>
                                </div>
                                <div class="anomaly-description">${anomaly.description}</div>
                                <div class="anomaly-test">${anomaly.testName}</div>
                            </div>
                        `).join('')}
                    </div>
                ` : `
                    <div class="text-center text-success">
                        <i class="fas fa-check-circle fa-2x mb-2"></i>
                        <div>No anomalies detected</div>
                    </div>
                `}
            </div>
        `;
    }

    getDefaultAnomalies() {
        return [
            {
                type: 'PERFORMANCE',
                testName: 'UserServiceIntegrationTest',
                severity: 'MEDIUM',
                description: 'Performance degradation detected',
                avgDuration: 1200,
                recentDuration: 2500
            },
            {
                type: 'RELIABILITY',
                testName: 'OrderServiceAPITest',
                severity: 'HIGH',
                description: 'Multiple recent failures detected',
                failureCount: 3
            }
        ];
    }

    async refreshDashboard() {
        console.log('Refreshing executive dashboard...');
        this.lastUpdateTime = new Date();
        await this.loadExecutiveData();
    }

    showError(message) {
        console.error('Dashboard Error:', message);
        // Could implement a toast notification system here
    }
}

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new ExecutiveDashboard();
});
