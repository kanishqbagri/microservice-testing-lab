// Scorecard Renderer - Handles rendering of scorecard components
import { TEST_TYPE_ICONS } from './config.js';

export class ScorecardRenderer {
    constructor() {
        this.testTypeIcons = TEST_TYPE_ICONS;
    }

    // Get test type icon
    getTestTypeIcon(testType) {
        return this.testTypeIcons[testType] || 'code';
    }

    // Get risk badge class
    getRiskBadgeClass(riskLevel) {
        switch (riskLevel) {
            case 'LOW': return 'bg-success';
            case 'MEDIUM': return 'bg-warning';
            case 'HIGH': return 'bg-danger';
            default: return 'bg-secondary';
        }
    }

    // Get score badge class
    getScoreBadgeClass(score) {
        if (score >= 80) return 'bg-success';
        if (score >= 60) return 'bg-warning';
        return 'bg-danger';
    }

    // Get health color
    getHealthColor(score) {
        if (score >= 80) return '#27ae60'; // Green
        if (score >= 60) return '#f39c12'; // Orange
        return '#e74c3c'; // Red
    }

    // Render per-service scorecards
    renderPerServiceScorecards(services) {
        const container = document.getElementById('smart-scorecards');
        
        if (services.length === 0) {
            container.innerHTML = '<div class="text-center text-muted">No service data available</div>';
            return;
        }

        container.innerHTML = services.map(service => this.renderServiceScorecard(service)).join('');
    }

    // Render individual service scorecard
    renderServiceScorecard(service) {
        return `
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
                                ${data.performancePenalty > 0 ? `<span class="text-warning">(-${data.performancePenalty}%)</span>` : ''}
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
                <div class="text-center mt-3">
                    <small class="text-muted">
                        <i class="fas fa-clock me-1"></i>
                        Last run: ${service.lastRunDate}
                    </small>
                </div>
            </div>
        `;
    }

    // Update data coverage summary
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

    // Update loading status
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
}
