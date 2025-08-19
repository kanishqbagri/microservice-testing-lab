package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Objects;

public class RiskAssessment {
    private RiskLevel level;
    private List<String> riskFactors;
    private String mitigationStrategy;
    private double riskScore;

    // Default constructor
    public RiskAssessment() {}

    // Constructor with all fields
    public RiskAssessment(RiskLevel level, List<String> riskFactors, String mitigationStrategy, double riskScore) {
        this.level = level;
        this.riskFactors = riskFactors;
        this.mitigationStrategy = mitigationStrategy;
        this.riskScore = riskScore;
    }

    // Builder pattern
    public static RiskAssessmentBuilder builder() {
        return new RiskAssessmentBuilder();
    }

    // Getter methods
    public RiskLevel getLevel() {
        return level;
    }

    public RiskLevel getRiskLevel() {
        return level;
    }

    public List<String> getRiskFactors() {
        return riskFactors;
    }

    public String getMitigationStrategy() {
        return mitigationStrategy;
    }

    public double getRiskScore() {
        return riskScore;
    }

    // Setter methods
    public void setLevel(RiskLevel level) {
        this.level = level;
    }

    public void setRiskFactors(List<String> riskFactors) {
        this.riskFactors = riskFactors;
    }

    public void setMitigationStrategy(String mitigationStrategy) {
        this.mitigationStrategy = mitigationStrategy;
    }

    public void setRiskScore(double riskScore) {
        this.riskScore = riskScore;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RiskAssessment that = (RiskAssessment) o;
        return level == that.level &&
                Objects.equals(riskFactors, that.riskFactors) &&
                Objects.equals(mitigationStrategy, that.mitigationStrategy);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(level, riskFactors, mitigationStrategy);
    }

    // toString method
    @Override
    public String toString() {
        return "RiskAssessment{" +
                "level=" + level +
                ", riskFactors=" + riskFactors +
                ", mitigationStrategy='" + mitigationStrategy + '\'' +
                '}';
    }

    // Builder class
    public static class RiskAssessmentBuilder {
        private RiskLevel level;
        private List<String> riskFactors;
        private String mitigationStrategy;
        private double riskScore;

        public RiskAssessmentBuilder level(RiskLevel level) {
            this.level = level;
            return this;
        }

        public RiskAssessmentBuilder riskLevel(RiskLevel riskLevel) {
            this.level = riskLevel;
            return this;
        }

        public RiskAssessmentBuilder riskFactors(List<String> riskFactors) {
            this.riskFactors = riskFactors;
            return this;
        }

        public RiskAssessmentBuilder mitigationStrategy(String mitigationStrategy) {
            this.mitigationStrategy = mitigationStrategy;
            return this;
        }

        public RiskAssessmentBuilder riskScore(double riskScore) {
            this.riskScore = riskScore;
            return this;
        }

        public RiskAssessmentBuilder timestamp(java.time.LocalDateTime timestamp) {
            return this; // Placeholder
        }

        public RiskAssessment build() {
            return new RiskAssessment(level, riskFactors, mitigationStrategy, riskScore);
        }
    }
} 