// Dashboard JavaScript Logic

// Import Supabase client
// <script src="https://cdn.jsdelivr.net/npm/@supabase/supabase-js"></script> should be added in index.html

class JarvisDashboard {
    // AGGREGATE RUNS VIEW
    async loadAggregateRuns() {
        // Fetch test runs with project and suite info
        let { data: runs, error } = await this.supabase
            .from('test_run')
            .select('id,started_at,finished_at,status,test_suite(name,project(name))')
            .order('started_at', { ascending: false });
        if (error) return;
        this.renderAggregateRunsTable(runs);
    }

    renderAggregateRunsTable(runs) {
        const tbody = document.getElementById('aggregate-runs-table');
        if (!tbody) return;
        if (!runs || runs.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">No runs found</td></tr>';
            return;
        }
        tbody.innerHTML = runs.map(run => `
            <tr style="cursor:pointer" onclick="dashboard.openRunDetails('${run.id}')">
                <td>${run.test_suite?.project?.name || '-'}</td>
                <td>${run.test_suite?.name || '-'}</td>
                <td><span class="status-badge status-${run.status?.toLowerCase()}">${run.status}</span></td>
                <td>${run.started_at ? new Date(run.started_at).toLocaleString() : '-'}</td>
                <td>${run.finished_at ? new Date(run.finished_at).toLocaleString() : '-'}</td>
                <td><button class="btn btn-sm btn-outline-primary"><i class="fas fa-eye"></i> Details</button></td>
            </tr>
        `).join('');
    }

    openRunDetails(runId) {
        window.location.href = `run-details.html?run_id=${runId}`;
    }
    constructor() {
        // Supabase connection
        this.supabaseUrl = 'https://smuaribfocdanafiixzi.supabase.co';
        this.supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs';
        this.supabase = window.supabase.createClient(this.supabaseUrl, this.supabaseKey);

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
                this.loadSecurityDashboard(),
            ]);
            // Load aggregate runs after other widgets
            await this.loadAggregateRuns();
        } catch (error) {
            console.error('Error loading dashboard data:', error);
            this.showError('Failed to load dashboard data');
        }
    }

    async loadOverview() {
        try {
            // Fetch all test results with joins to project (service), suite, and run
            let { data: results, error } = await this.supabase
                .from('test_result')
                .select(`id,status,duration_ms,created_at,
                    test_case(name),
                    test_run(started_at,suite_id,test_suite(name,project(name)))`);
            if (error) throw error;

            // Calculate stats
            const totalTests = results.length;
            const passedTests = results.filter(t => t.status === 'PASSED').length;
            const failedTests = results.filter(t => t.status === 'FAILED').length;
            const successRate = totalTests > 0 ? (passedTests / totalTests) * 100 : 0;

            document.getElementById('total-tests').textContent = totalTests;
            document.getElementById('passed-tests').textContent = passedTests;
            document.getElementById('failed-tests').textContent = failedTests;
            document.getElementById('success-rate').textContent = successRate.toFixed(1) + '%';

            // Populate service filter (project.name)
            const serviceFilter = document.getElementById('service-filter');
            serviceFilter.innerHTML = '<option value="">All Services</option>';
            const services = [...new Set(results.map(t => t.test_run.test_suite.project?.name).filter(Boolean))];
            services.forEach(service => {
                const option = document.createElement('option');
                option.value = service;
                option.textContent = service;
                serviceFilter.appendChild(option);
            });

            // Populate test type filter (suite name)
            const testTypeFilter = document.getElementById('test-type-filter');
            testTypeFilter.innerHTML = '<option value="">All Test Types</option>';
            const testTypes = [...new Set(results.map(t => t.test_run.test_suite?.name).filter(Boolean))];
            testTypes.forEach(testType => {
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
            // Build Supabase query with joins
            let query = this.supabase
                .from('test_result')
                .select(`id,status,duration_ms,created_at,
                    test_case(name),
                    test_run(started_at,suite_id,test_suite(name,project(name)))`, { count: 'exact' })
                .order('created_at', { ascending: false });

            // Filter by service (project.name)
            if (this.filters.service) query = query.eq('test_run.test_suite.project.name', this.filters.service);
            // Filter by test type (suite name)
            if (this.filters.testType) query = query.eq('test_run.test_suite.name', this.filters.testType);
            // Filter by status
            if (this.filters.status) query = query.eq('status', this.filters.status);

            // Pagination
            const from = this.currentPage * this.pageSize;
            const to = from + this.pageSize - 1;
            query = query.range(from, to);

            let { data: tests, count, error } = await query;
            if (error) throw error;

            // Fake pagination data for compatibility
            const data = {
                tests,
                totalPages: Math.ceil(count / this.pageSize),
                currentPage: this.currentPage,
                hasPrevious: this.currentPage > 0,
                hasNext: (this.currentPage + 1) * this.pageSize < count
            };
            this.renderTestResultsTable(tests);
            this.renderPagination(data);
        } catch (error) {
            console.error('Error loading test results:', error);
        }
    }

    renderTestResultsTable(tests) {
        const tbody = document.getElementById('test-results-table');
        if (!tests || tests.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">No test results found</td></tr>';
            return;
        }
        tbody.innerHTML = tests.map(test => {
            const serviceName = test.test_run?.test_suite?.project?.name || '-';
            const testType = test.test_run?.test_suite?.name || '-';
            const testName = test.test_case?.name || '-';
            const startTime = test.test_run?.started_at ? new Date(test.test_run.started_at).toLocaleString() : '-';
            return `
                <tr>
                    <td>${testName}</td>
                    <td><span class="badge bg-secondary">${serviceName}</span></td>
                    <td><span class="badge bg-info">${testType.replace('_', ' ')}</span></td>
                    <td><span class="status-badge status-${test.status.toLowerCase()}">${test.status}</span></td>
                    <td>${test.duration_ms ? test.duration_ms + 'ms' : '-'}</td>
                    <td>${startTime}</td>
                    <td>-</td>
                    <td>
                        <button class="btn btn-sm btn-outline-primary" onclick="dashboard.showTestDetails('${test.id}')">
                            <i class="fas fa-eye"></i>
                        </button>
                    </td>
                </tr>
            `;
        }).join('');
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
            // Get date N days ago
            const since = new Date();
            since.setDate(since.getDate() - this.filters.days);
            // Fetch all test results in range, with joins
            let { data: results, error } = await this.supabase
                .from('test_result')
                .select(`id,status,created_at,
                    test_case(name),
                    test_run(started_at,suite_id,test_suite(name,project(name)))`)
                .gte('created_at', since.toISOString());
            if (error) throw error;

            // Group by date
            const trendData = {};
            results.forEach(t => {
                const date = t.created_at.split('T')[0];
                if (!trendData[date]) trendData[date] = { date, passed: 0, failed: 0, skipped: 0 };
                if (t.status === 'PASSED') trendData[date].passed++;
                else if (t.status === 'FAILED') trendData[date].failed++;
                else if (t.status === 'SKIPPED') trendData[date].skipped++;
            });
            const trendArr = Object.values(trendData).sort((a, b) => a.date.localeCompare(b.date));
            this.renderTrendsChart(trendArr);
            this.renderDistributionChart(trendArr);
            // Render per-service trends
            this.renderServiceTrendsWidget(results);
        } catch (error) {
            console.error('Error loading trends:', error);
        }
    }

    // New: Render widgets for per-service trends
    renderServiceTrendsWidget(results) {
        const container = document.getElementById('service-trends-row');
        if (!container) return;
        container.innerHTML = '';
        // Group by service (project.name)
        const byService = {};
        results.forEach(t => {
            const service = t.test_run?.test_suite?.project?.name || '-';
            if (!byService[service]) byService[service] = [];
            byService[service].push(t);
        });
        Object.entries(byService).forEach(([service, runs]) => {
            // Group by date
            const trend = {};
            runs.forEach(t => {
                const date = t.created_at.split('T')[0];
                if (!trend[date]) trend[date] = { date, passed: 0, failed: 0, skipped: 0 };
                if (t.status === 'PASSED') trend[date].passed++;
                else if (t.status === 'FAILED') trend[date].failed++;
                else if (t.status === 'SKIPPED') trend[date].skipped++;
            });
            const trendArr = Object.values(trend).sort((a, b) => a.date.localeCompare(b.date));
            // Create canvas for chart
            const chartId = `service-trend-${service.replace(/[^a-zA-Z0-9]/g, '')}`;
            const card = document.createElement('div');
            card.className = 'col-md-4 mb-4';
            card.innerHTML = `
                <div class=\"card\">
                    <div class=\"card-header\"><b>${service}</b> Trend</div>
                    <div class=\"card-body\"><canvas id=\"${chartId}\" height=\"180\"></canvas></div>
                </div>
            `;
            container.appendChild(card);
            // Draw chart
            setTimeout(() => {
                const ctx = document.getElementById(chartId).getContext('2d');
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: trendArr.map(d => d.date),
                        datasets: [
                            { label: 'Passed', data: trendArr.map(d => d.passed), borderColor: '#27ae60', backgroundColor: 'rgba(39,174,96,0.1)', tension: 0.4 },
                            { label: 'Failed', data: trendArr.map(d => d.failed), borderColor: '#e74c3c', backgroundColor: 'rgba(231,76,60,0.1)', tension: 0.4 },
                            { label: 'Skipped', data: trendArr.map(d => d.skipped), borderColor: '#f39c12', backgroundColor: 'rgba(243,156,18,0.1)', tension: 0.4 }
                        ]
                    },
                    options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'top' }, title: { display: false } }, scales: { y: { beginAtZero: true } } }
                });
            }, 100);
        });
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
