// Dashboard JavaScript Logic
class JarvisDashboard {
    constructor() {
        this.baseUrl = 'http://localhost:8085/api/dashboard';
        this.currentPage = 0;
        this.pageSize = 20;
        this.filters = {
            service: '',
            testType: '',
            status: '',
            days: 7
        };
        this.charts = {};
        this.init();
    }

    async init() {
        this.updateCurrentTime();
        setInterval(() => this.updateCurrentTime(), 1000);
        
        await this.loadDashboardData();
        this.setupEventListeners();
        
        // Auto-refresh every 30 seconds
        setInterval(() => this.refreshDashboard(), 30000);
    }

    updateCurrentTime() {
        const now = new Date();
        document.getElementById('current-time').textContent = now.toLocaleString();
    }

    setupEventListeners() {
        document.getElementById('page-size').addEventListener('change', (e) => {
            this.pageSize = parseInt(e.target.value);
            this.loadTestResults();
        });

        document.getElementById('date-range-filter').addEventListener('change', (e) => {
            this.filters.days = parseInt(e.target.value);
        });
    }

    async loadDashboardData() {
        try {
            await Promise.all([
                this.loadOverview(),
                this.loadTestResults(),
                this.loadTrends(),
                this.loadPerformanceMetrics(),
                this.loadSecurityDashboard()
            ]);
        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('Failed to load dashboard data');
        }
    }

    async loadOverview() {
        try {
            const response = await fetch(`${this.baseUrl}/overview`);
            const data = await response.json();
            
            document.getElementById('total-tests').textContent = data.totalTests;
            document.getElementById('passed-tests').textContent = data.passedTests;
            document.getElementById('failed-tests').textContent = data.failedTests;
            document.getElementById('success-rate').textContent = data.successRate.toFixed(1) + '%';
            
            // Populate service filter
            const serviceFilter = document.getElementById('service-filter');
            serviceFilter.innerHTML = '<option value="">All Services</option>';
            Object.keys(data.serviceStats).forEach(service => {
                const option = document.createElement('option');
                option.value = service;
                option.textContent = service;
                serviceFilter.appendChild(option);
            });
            
            // Populate test type filter
            const testTypeFilter = document.getElementById('test-type-filter');
            testTypeFilter.innerHTML = '<option value="">All Test Types</option>';
            Object.keys(data.testTypeStats).forEach(testType => {
                const option = document.createElement('option');
                option.value = testType;
                option.textContent = testType.replace('_', ' ');
                testTypeFilter.appendChild(option);
            });
            
        } catch (error) {
            console.error('Error loading overview:', error);
        }
    }

    async loadTestResults() {
        try {
            const params = new URLSearchParams({
                page: this.currentPage,
                size: this.pageSize,
                ...(this.filters.service && { service: this.filters.service }),
                ...(this.filters.testType && { testType: this.filters.testType }),
                ...(this.filters.status && { status: this.filters.status })
            });
            
            const response = await fetch(`${this.baseUrl}/test-results?${params}`);
            const data = await response.json();
            
            this.renderTestResultsTable(data.tests);
            this.renderPagination(data);
            
        } catch (error) {
            console.error('Error loading test results:', error);
        }
    }

    renderTestResultsTable(tests) {
        const tbody = document.getElementById('test-results-table');
        
        if (tests.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">No test results found</td></tr>';
            return;
        }
        
        tbody.innerHTML = tests.map(test => `
            <tr>
                <td>${test.testName}</td>
                <td><span class="badge bg-secondary">${test.serviceName}</span></td>
                <td><span class="badge bg-info">${test.testType.replace('_', ' ')}</span></td>
                <td><span class="status-badge status-${test.status.toLowerCase()}">${test.status}</span></td>
                <td>${test.executionTimeMs ? test.executionTimeMs + 'ms' : '-'}</td>
                <td>${new Date(test.startTime).toLocaleString()}</td>
                <td>${test.riskLevel ? `<span class="badge bg-warning">${test.riskLevel}</span>` : '-'}</td>
                <td>
                    <button class="btn btn-sm btn-outline-primary" onclick="dashboard.showTestDetails('${test.id}')">
                        <i class="fas fa-eye"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    }

    renderPagination(data) {
        const pagination = document.getElementById('pagination');
        const totalPages = data.totalPages;
        const currentPage = data.currentPage;
        
        if (totalPages <= 1) {
            pagination.innerHTML = '';
            return;
        }
        
        let paginationHTML = '';
        
        // Previous button
        paginationHTML += `
            <li class="page-item ${!data.hasPrevious ? 'disabled' : ''}">
                <a class="page-link" href="#" onclick="dashboard.goToPage(${currentPage - 1})">Previous</a>
            </li>
        `;
        
        // Page numbers
        const startPage = Math.max(0, currentPage - 2);
        const endPage = Math.min(totalPages - 1, currentPage + 2);
        
        for (let i = startPage; i <= endPage; i++) {
            paginationHTML += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" onclick="dashboard.goToPage(${i})">${i + 1}</a>
                </li>
            `;
        }
        
        // Next button
        paginationHTML += `
            <li class="page-item ${!data.hasNext ? 'disabled' : ''}">
                <a class="page-link" href="#" onclick="dashboard.goToPage(${currentPage + 1})">Next</a>
            </li>
        `;
        
        pagination.innerHTML = paginationHTML;
    }

    goToPage(page) {
        this.currentPage = page;
        this.loadTestResults();
    }

    async loadTrends() {
        try {
            const response = await fetch(`${this.baseUrl}/trends?days=${this.filters.days}`);
            const data = await response.json();
            
            this.renderTrendsChart(data.trendData);
            this.renderDistributionChart(data.trendData);
            
        } catch (error) {
            console.error('Error loading trends:', error);
        }
    }

    renderTrendsChart(trendData) {
        const ctx = document.getElementById('trendsChart').getContext('2d');
        
        if (this.charts.trends) {
            this.charts.trends.destroy();
        }
        
        this.charts.trends = new Chart(ctx, {
            type: 'line',
            data: {
                labels: trendData.map(d => d.date),
                datasets: [
                    {
                        label: 'Passed',
                        data: trendData.map(d => d.passed),
                        borderColor: '#27ae60',
                        backgroundColor: 'rgba(39, 174, 96, 0.1)',
                        tension: 0.4
                    },
                    {
                        label: 'Failed',
                        data: trendData.map(d => d.failed),
                        borderColor: '#e74c3c',
                        backgroundColor: 'rgba(231, 76, 60, 0.1)',
                        tension: 0.4
                    },
                    {
                        label: 'Skipped',
                        data: trendData.map(d => d.skipped),
                        borderColor: '#f39c12',
                        backgroundColor: 'rgba(243, 156, 18, 0.1)',
                        tension: 0.4
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Test Execution Trends'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }

    renderDistributionChart(trendData) {
        const ctx = document.getElementById('distributionChart').getContext('2d');
        
        if (this.charts.distribution) {
            this.charts.distribution.destroy();
        }
        
        const totalPassed = trendData.reduce((sum, d) => sum + d.passed, 0);
        const totalFailed = trendData.reduce((sum, d) => sum + d.failed, 0);
        const totalSkipped = trendData.reduce((sum, d) => sum + d.skipped, 0);
        
        this.charts.distribution = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Passed', 'Failed', 'Skipped'],
                datasets: [{
                    data: [totalPassed, totalFailed, totalSkipped],
                    backgroundColor: ['#27ae60', '#e74c3c', '#f39c12'],
                    borderWidth: 2,
                    borderColor: '#fff'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                    },
                    title: {
                        display: true,
                        text: 'Test Status Distribution'
                    }
                }
            }
        });
    }

    async loadPerformanceMetrics() {
        try {
            const response = await fetch(`${this.baseUrl}/performance-metrics`);
            const data = await response.json();
            
            document.getElementById('avg-execution-time').textContent = 
                data.averageExecutionTime ? Math.round(data.averageExecutionTime) : '-';
            document.getElementById('max-execution-time').textContent = 
                data.maxExecutionTime ? data.maxExecutionTime : '-';
            document.getElementById('slow-tests-count').textContent = 
                data.slowTests ? data.slowTests.length : 0;
            
        } catch (error) {
            console.error('Error loading performance metrics:', error);
        }
    }

    async loadSecurityDashboard() {
        try {
            const response = await fetch(`${this.baseUrl}/security-dashboard`);
            const data = await response.json();
            
            document.getElementById('security-score').textContent = 
                data.securityScore ? data.securityScore.toFixed(1) : '-';
            document.getElementById('security-tests-count').textContent = 
                data.totalSecurityTests || 0;
            
        } catch (error) {
            console.error('Error loading security dashboard:', error);
        }
    }

    async showTestDetails(testId) {
        try {
            // For now, we'll show a placeholder. In a real implementation,
            // you would fetch detailed test information
            const modal = new bootstrap.Modal(document.getElementById('testDetailsModal'));
            document.getElementById('test-details-content').innerHTML = `
                <div class="text-center">
                    <h6>Test Details</h6>
                    <p>Test ID: ${testId}</p>
                    <p>Detailed test information would be displayed here.</p>
                    <p>This could include:</p>
                    <ul class="text-start">
                        <li>Test parameters</li>
                        <li>Test output</li>
                        <li>Performance metrics</li>
                        <li>Environment information</li>
                        <li>Error details (if failed)</li>
                    </ul>
                </div>
            `;
            modal.show();
        } catch (error) {
            console.error('Error showing test details:', error);
        }
    }

    applyFilters() {
        this.filters.service = document.getElementById('service-filter').value;
        this.filters.testType = document.getElementById('test-type-filter').value;
        this.filters.status = document.getElementById('status-filter').value;
        this.filters.days = parseInt(document.getElementById('date-range-filter').value);
        
        this.currentPage = 0;
        this.loadTestResults();
        this.loadTrends();
    }

    async exportData() {
        try {
            const params = new URLSearchParams({
                format: 'json',
                days: this.filters.days
            });
            
            const response = await fetch(`${this.baseUrl}/export?${params}`);
            const data = await response.json();
            
            // Create and download file
            const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `jarvis-test-results-${new Date().toISOString().split('T')[0]}.json`;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
            
            this.showSuccess('Data exported successfully!');
        } catch (error) {
            console.error('Error exporting data:', error);
            this.showError('Failed to export data');
        }
    }

    async refreshDashboard() {
        await this.loadDashboardData();
        this.showSuccess('Dashboard refreshed!');
    }

    showSuccess(message) {
        this.showAlert(message, 'success');
    }

    showError(message) {
        this.showAlert(message, 'danger');
    }

    showAlert(message, type) {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show alert-custom`;
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
function applyFilters() {
    dashboard.applyFilters();
}

function exportData() {
    dashboard.exportData();
}

function refreshDashboard() {
    dashboard.refreshDashboard();
}

// Initialize dashboard when page loads
let dashboard;
document.addEventListener('DOMContentLoaded', () => {
    dashboard = new JarvisDashboard();
});
