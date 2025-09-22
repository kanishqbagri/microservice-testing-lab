// Intelligent Impact Analysis Component
class IntelligentImpactAnalysis {
    constructor() {
        this.supabaseUrl = 'https://smuaribfocdanafiixzi.supabase.co';
        this.supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs';
        this.supabase = window.supabase.createClient(this.supabaseUrl, this.supabaseKey);
        
        this.dependencyGraph = this.buildDependencyGraph();
        this.testCoverageMap = this.buildTestCoverageMap();
        this.riskWeights = {
            'code_complexity': 0.2,
            'test_coverage': 0.25,
            'dependency_depth': 0.2,
            'historical_failures': 0.15,
            'critical_path_changes': 0.2
        };
    }

    // Build service dependency graph
    buildDependencyGraph() {
        return {
            'user-service': {
                dependencies: [],
                dependents: ['order-service', 'notification-service'],
                criticality: 'HIGH',
                apiEndpoints: ['/api/users', '/api/auth', '/api/profile']
            },
            'product-service': {
                dependencies: [],
                dependents: ['order-service'],
                criticality: 'MEDIUM',
                apiEndpoints: ['/api/products', '/api/catalog', '/api/inventory']
            },
            'order-service': {
                dependencies: ['user-service', 'product-service'],
                dependents: ['notification-service', 'payment-service'],
                criticality: 'HIGH',
                apiEndpoints: ['/api/orders', '/api/cart', '/api/checkout']
            },
            'notification-service': {
                dependencies: ['user-service', 'order-service'],
                dependents: [],
                criticality: 'MEDIUM',
                apiEndpoints: ['/api/notifications', '/api/alerts']
            },
            'gateway-service': {
                dependencies: [],
                dependents: ['user-service', 'product-service', 'order-service', 'notification-service'],
                criticality: 'CRITICAL',
                apiEndpoints: ['/api/gateway', '/api/proxy']
            }
        };
    }

    // Build test coverage mapping
    buildTestCoverageMap() {
        return {
            'user-service': {
                'unit': { coverage: 92, tests: 145, avgDuration: 85 },
                'api': { coverage: 88, tests: 65, avgDuration: 420 },
                'integration': { coverage: 85, tests: 45, avgDuration: 1850 },
                'ui': { coverage: 75, tests: 15, avgDuration: 4200 }
            },
            'product-service': {
                'unit': { coverage: 89, tests: 120, avgDuration: 95 },
                'api': { coverage: 85, tests: 55, avgDuration: 450 },
                'integration': { coverage: 82, tests: 35, avgDuration: 2100 }
            },
            'order-service': {
                'unit': { coverage: 91, tests: 180, avgDuration: 90 },
                'api': { coverage: 87, tests: 75, avgDuration: 520 },
                'integration': { coverage: 84, tests: 55, avgDuration: 2200 },
                'contract': { coverage: 80, tests: 25, avgDuration: 1500 }
            },
            'notification-service': {
                'unit': { coverage: 94, tests: 95, avgDuration: 75 },
                'api': { coverage: 90, tests: 40, avgDuration: 380 },
                'integration': { coverage: 87, tests: 30, avgDuration: 1650 }
            },
            'gateway-service': {
                'unit': { coverage: 88, tests: 110, avgDuration: 100 },
                'api': { coverage: 85, tests: 50, avgDuration: 350 },
                'integration': { coverage: 82, tests: 40, avgDuration: 1800 }
            }
        };
    }

    // Analyze PR impact
    async analyzePRImpact(prData) {
        try {
            console.log('analyzePRImpact received prData:', prData);
            // Extract features from PR data
            const features = this.extractPRFeatures(prData);
            
            // Calculate impact score
            const impactScore = this.calculateImpactScore(features);
            
            // Determine blast radius
            console.log('PR Data services_modified:', prData.services_modified);
            const blastRadius = this.calculateBlastRadius(prData.services_modified, features);
            
            // Assess risk
            const riskAssessment = this.assessRisk(features, impactScore);
            
            // Generate recommendations
            const recommendations = this.generateRecommendations(riskAssessment, blastRadius);
            
            return {
                impact_score: impactScore,
                risk_level: riskAssessment.risk_level,
                blast_radius: blastRadius,
                confidence: this.calculateConfidence(features),
                impact_description: this.generateImpactDescription(blastRadius, impactScore),
                key_changes: this.identifyKeyChanges(prData),
                risk_assessment: riskAssessment,
                recommendations: recommendations
            };
        } catch (error) {
            console.error('Error analyzing PR impact:', error);
            return this.getDefaultImpactAnalysis();
        }
    }

    // Extract features from PR data
    extractPRFeatures(prData) {
        const servicesModified = prData.services_modified || [];
        const totalLinesChanged = prData.lines_added + prData.lines_deleted;
        
        return {
            lines_added: prData.lines_added || 0,
            lines_deleted: prData.lines_deleted || 0,
            files_changed: prData.files_changed || 0,
            services_modified: servicesModified,
            services_modified_count: servicesModified.length,
            total_lines_changed: totalLinesChanged,
            cyclomatic_complexity: this.estimateComplexity(prData),
            test_coverage_change: this.calculateCoverageChange(servicesModified),
            dependency_depth: this.calculateDependencyDepth(servicesModified),
            critical_path_changes: this.identifyCriticalPathChanges(servicesModified),
            historical_failure_rate: this.getHistoricalFailureRate(servicesModified),
            test_files_modified: this.countTestFiles(prData),
            api_endpoints_changed: this.identifyAPIChanges(prData)
        };
    }

    // Calculate impact score (0-10 scale)
    calculateImpactScore(features) {
        let score = 0;
        
        // Base score from lines changed
        const linesScore = Math.min(5, features.total_lines_changed / 100);
        score += linesScore;
        
        // Service modification impact
        const serviceScore = Math.min(3, features.services_modified_count * 0.5);
        score += serviceScore;
        
        // Dependency impact
        const dependencyScore = Math.min(2, features.dependency_depth * 0.4);
        score += dependencyScore;
        
        // Critical path impact
        if (features.critical_path_changes > 0) {
            score += 2;
        }
        
        // Test coverage impact (penalty for low coverage)
        if (features.test_coverage_change < 80) {
            score += (80 - features.test_coverage_change) / 40;
        }
        
        return Math.min(10, Math.max(0, score));
    }

    // Calculate blast radius
    calculateBlastRadius(servicesModified, features) {
        const blastRadius = [];
        
        // Ensure servicesModified is an array
        if (!Array.isArray(servicesModified)) {
            console.warn('servicesModified is not an array:', servicesModified);
            return blastRadius;
        }
        
        servicesModified.forEach(service => {
            if (this.dependencyGraph && this.dependencyGraph[service] && this.dependencyGraph[service].dependents) {
                // Direct dependents
                this.dependencyGraph[service].dependents.forEach(dependent => {
                    const impactProbability = this.calculateImpactProbability(service, dependent, features);
                    if (impactProbability > 0.3) {
                        const criticality = (this.dependencyGraph[dependent] && this.dependencyGraph[dependent].criticality) || 'MEDIUM';
                        
                        blastRadius.push({
                            service: dependent,
                            probability: impactProbability,
                            impact_type: 'direct',
                            relationship: 'depends_on',
                            criticality: criticality
                        });
                    }
                });
                
                // Second-level dependents
                this.dependencyGraph[service].dependents.forEach(dependent => {
                    if (this.dependencyGraph[dependent] && this.dependencyGraph[dependent].dependents) {
                        this.dependencyGraph[dependent].dependents.forEach(secondLevel => {
                            if (!blastRadius.find(b => b.service === secondLevel)) {
                                const impactProbability = this.calculateImpactProbability(dependent, secondLevel, features) * 0.5;
                                if (impactProbability > 0.2) {
                                    blastRadius.push({
                                        service: secondLevel,
                                        probability: impactProbability,
                                        impact_type: 'indirect',
                                        relationship: 'transitive_dependency',
                                        criticality: (this.dependencyGraph[secondLevel] && this.dependencyGraph[secondLevel].criticality) || 'MEDIUM'
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
        
        return blastRadius.sort((a, b) => b.probability - a.probability);
    }

    // Calculate impact probability between services
    calculateImpactProbability(sourceService, targetService, features) {
        let probability = 0.5; // Base probability
        
        // Adjust based on service criticality
        const sourceCriticality = this.dependencyGraph[sourceService]?.criticality;
        const targetCriticality = this.dependencyGraph[targetService]?.criticality;
        
        if (sourceCriticality === 'CRITICAL') probability += 0.3;
        if (targetCriticality === 'CRITICAL') probability += 0.2;
        
        // Adjust based on test coverage
        const sourceCoverage = this.getServiceTestCoverage(sourceService);
        if (sourceCoverage < 80) probability += 0.2;
        
        // Adjust based on historical failures
        if (features.historical_failure_rate > 0.1) probability += 0.1;
        
        // Adjust based on API changes
        if (features.api_endpoints_changed > 0) probability += 0.1;
        
        return Math.min(1.0, probability);
    }

    // Assess risk level
    assessRisk(features, impactScore) {
        const riskComponents = {};
        
        // Code complexity risk
        riskComponents.complexity = Math.min(1.0, features.cyclomatic_complexity / 20.0);
        
        // Test coverage risk
        riskComponents.coverage = Math.max(0, (100 - features.test_coverage_change) / 100.0);
        
        // Dependency risk
        riskComponents.dependency = Math.min(1.0, features.dependency_depth / 5.0);
        
        // Historical risk
        riskComponents.historical = Math.min(1.0, features.historical_failure_rate);
        
        // Critical path risk
        riskComponents.critical_path = features.critical_path_changes > 0 ? 1.0 : 0.0;
        
        // Calculate weighted risk score
        const totalRisk = Object.entries(riskComponents).reduce((sum, [factor, score]) => {
            return sum + (score * this.riskWeights[factor]);
        }, 0);
        
        // Determine risk level
        let riskLevel;
        if (totalRisk >= 0.7) {
            riskLevel = 'HIGH';
        } else if (totalRisk >= 0.4) {
            riskLevel = 'MEDIUM';
        } else {
            riskLevel = 'LOW';
        }
        
        return {
            risk_score: totalRisk,
            risk_level: riskLevel,
            risk_components: riskComponents
        };
    }

    // Generate recommendations
    generateRecommendations(riskAssessment, blastRadius) {
        const recommendations = [];
        
        // Test coverage recommendations
        if (riskAssessment.risk_components.coverage > 0.5) {
            recommendations.push({
                type: 'test_coverage',
                priority: 'HIGH',
                message: 'Increase test coverage for modified components',
                action: 'Add unit and integration tests for changed code paths',
                services: blastRadius.filter(s => s.impact_type === 'direct').map(s => s.service)
            });
        }
        
        // Dependency management recommendations
        if (riskAssessment.risk_components.dependency > 0.6) {
            recommendations.push({
                type: 'dependency_management',
                priority: 'MEDIUM',
                message: 'Review dependency changes carefully',
                action: 'Run comprehensive integration tests on dependent services',
                services: blastRadius.map(s => s.service)
            });
        }
        
        // Critical path recommendations
        if (riskAssessment.risk_components.critical_path > 0) {
            recommendations.push({
                type: 'critical_path',
                priority: 'HIGH',
                message: 'Critical path components modified',
                action: 'Execute full regression test suite and staging deployment',
                services: blastRadius.filter(s => s.criticality === 'CRITICAL').map(s => s.service)
            });
        }
        
        // High impact recommendations
        if (blastRadius.length > 3) {
            recommendations.push({
                type: 'impact_scope',
                priority: 'MEDIUM',
                message: 'Wide impact scope detected',
                action: 'Coordinate testing across multiple teams and services',
                services: blastRadius.slice(0, 3).map(s => s.service)
            });
        }
        
        return recommendations;
    }

    // Helper methods
    estimateComplexity(prData) {
        // Simple complexity estimation based on files and lines changed
        const complexityPerFile = prData.files_changed > 0 ? prData.total_lines_changed / prData.files_changed : 0;
        return Math.min(20, complexityPerFile / 10);
    }

    calculateCoverageChange(servicesModified) {
        if (servicesModified.length === 0) return 100;
        
        const totalCoverage = servicesModified.reduce((sum, service) => {
            const coverage = this.getServiceTestCoverage(service);
            return sum + coverage;
        }, 0);
        
        return totalCoverage / servicesModified.length;
    }

    getServiceTestCoverage(serviceName) {
        const coverage = this.testCoverageMap[serviceName];
        if (!coverage) return 0;
        
        const testTypes = Object.keys(coverage);
        const totalCoverage = testTypes.reduce((sum, type) => sum + coverage[type].coverage, 0);
        return totalCoverage / testTypes.length;
    }

    calculateDependencyDepth(servicesModified) {
        let maxDepth = 0;
        servicesModified.forEach(service => {
            const depth = this.getDependencyDepth(service);
            maxDepth = Math.max(maxDepth, depth);
        });
        return maxDepth;
    }

    getDependencyDepth(serviceName) {
        const service = this.dependencyGraph[serviceName];
        if (!service) return 0;
        
        let depth = 0;
        const visited = new Set();
        const queue = [{ service: serviceName, depth: 0 }];
        
        while (queue.length > 0) {
            const { service: current, depth: currentDepth } = queue.shift();
            if (visited.has(current)) continue;
            
            visited.add(current);
            depth = Math.max(depth, currentDepth);
            
            const currentService = this.dependencyGraph[current];
            if (currentService) {
                currentService.dependents.forEach(dependent => {
                    queue.push({ service: dependent, depth: currentDepth + 1 });
                });
            }
        }
        
        return depth;
    }

    identifyCriticalPathChanges(servicesModified) {
        return servicesModified.filter(service => {
            const serviceData = this.dependencyGraph[service];
            return serviceData && serviceData.criticality === 'CRITICAL';
        }).length;
    }

    getHistoricalFailureRate(servicesModified) {
        // Mock historical failure rate - in real implementation, this would query incident data
        const mockRates = {
            'user-service': 0.05,
            'product-service': 0.08,
            'order-service': 0.12,
            'notification-service': 0.03,
            'gateway-service': 0.15
        };
        
        if (servicesModified.length === 0) return 0;
        
        const totalRate = servicesModified.reduce((sum, service) => {
            return sum + (mockRates[service] || 0.1);
        }, 0);
        
        return totalRate / servicesModified.length;
    }

    countTestFiles(prData) {
        // Mock test file count - in real implementation, this would analyze file paths
        return Math.floor(prData.files_changed * 0.3);
    }

    identifyAPIChanges(prData) {
        // Mock API endpoint changes - in real implementation, this would analyze code changes
        return Math.floor(prData.files_changed * 0.2);
    }

    calculateConfidence(features) {
        // Calculate confidence based on data quality and completeness
        let confidence = 0.8; // Base confidence
        
        // Reduce confidence for incomplete data
        if (features.services_modified_count === 0) confidence -= 0.3;
        if (features.test_coverage_change === 0) confidence -= 0.2;
        if (features.historical_failure_rate === 0) confidence -= 0.1;
        
        return Math.max(0.1, Math.min(1.0, confidence));
    }

    generateImpactDescription(blastRadius, impactScore) {
        if (blastRadius.length === 0) {
            return "No significant impact expected on other services.";
        }
        
        const directImpacts = blastRadius.filter(s => s.impact_type === 'direct');
        const indirectImpacts = blastRadius.filter(s => s.impact_type === 'indirect');
        
        let description = `This change is expected to impact ${blastRadius.length} service(s). `;
        
        if (directImpacts.length > 0) {
            description += `Direct impact on: ${directImpacts.map(s => s.service).join(', ')}. `;
        }
        
        if (indirectImpacts.length > 0) {
            description += `Indirect impact on: ${indirectImpacts.map(s => s.service).join(', ')}. `;
        }
        
        if (impactScore > 7) {
            description += "High impact change requiring comprehensive testing.";
        } else if (impactScore > 4) {
            description += "Medium impact change requiring targeted testing.";
        } else {
            description += "Low impact change with minimal testing requirements.";
        }
        
        return description;
    }

    identifyKeyChanges(prData) {
        const changes = [];
        
        if (prData.lines_added > 100) changes.push("Significant code additions");
        if (prData.lines_deleted > 50) changes.push("Code refactoring/removal");
        if (prData.files_changed > 10) changes.push("Multiple file modifications");
        if (prData.services_modified && prData.services_modified.length > 1) {
            changes.push("Multi-service changes");
        }
        
        return changes.length > 0 ? changes : ["Minor code changes"];
    }

    getDefaultImpactAnalysis() {
        return {
            impact_score: 5.0,
            risk_level: 'MEDIUM',
            blast_radius: [
                {
                    service: 'order-service',
                    probability: 0.6,
                    impact_type: 'direct',
                    relationship: 'depends_on',
                    criticality: 'HIGH'
                }
            ],
            confidence: 0.7,
            impact_description: "Moderate impact expected on dependent services.",
            key_changes: ["Code modifications detected"],
            risk_assessment: {
                risk_score: 0.5,
                risk_level: 'MEDIUM',
                risk_components: {
                    complexity: 0.3,
                    coverage: 0.4,
                    dependency: 0.6,
                    historical: 0.2,
                    critical_path: 0.0
                }
            },
            recommendations: [
                {
                    type: 'dependency_management',
                    priority: 'MEDIUM',
                    message: 'Review dependency changes carefully',
                    action: 'Run comprehensive integration tests on dependent services',
                    services: ['order-service']
                }
            ]
        };
    }
}

// Export for use in executive dashboard
window.IntelligentImpactAnalysis = IntelligentImpactAnalysis;
