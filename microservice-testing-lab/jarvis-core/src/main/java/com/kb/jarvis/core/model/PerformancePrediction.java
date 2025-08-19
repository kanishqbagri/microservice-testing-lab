package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Objects;

public class PerformancePrediction {
    private double estimatedExecutionTime;
    private double successProbability;
    private List<String> potentialBottlenecks;

    // Default constructor
    public PerformancePrediction() {}

    // Constructor with all fields
    public PerformancePrediction(double estimatedExecutionTime, double successProbability, List<String> potentialBottlenecks) {
        this.estimatedExecutionTime = estimatedExecutionTime;
        this.successProbability = successProbability;
        this.potentialBottlenecks = potentialBottlenecks;
    }

    // Builder pattern
    public static PerformancePredictionBuilder builder() {
        return new PerformancePredictionBuilder();
    }

    // Getter methods
    public double getEstimatedExecutionTime() {
        return estimatedExecutionTime;
    }

    public double getEstimatedTime() {
        return estimatedExecutionTime;
    }

    public double getConfidence() {
        return successProbability; // Use successProbability as confidence
    }

    public double getSuccessProbability() {
        return successProbability;
    }

    public List<String> getPotentialBottlenecks() {
        return potentialBottlenecks;
    }

    // Setter methods
    public void setEstimatedExecutionTime(double estimatedExecutionTime) {
        this.estimatedExecutionTime = estimatedExecutionTime;
    }

    public void setSuccessProbability(double successProbability) {
        this.successProbability = successProbability;
    }

    public void setPotentialBottlenecks(List<String> potentialBottlenecks) {
        this.potentialBottlenecks = potentialBottlenecks;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformancePrediction that = (PerformancePrediction) o;
        return Double.compare(that.estimatedExecutionTime, estimatedExecutionTime) == 0 &&
                Double.compare(that.successProbability, successProbability) == 0 &&
                Objects.equals(potentialBottlenecks, that.potentialBottlenecks);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(estimatedExecutionTime, successProbability, potentialBottlenecks);
    }

    // toString method
    @Override
    public String toString() {
        return "PerformancePrediction{" +
                "estimatedExecutionTime=" + estimatedExecutionTime +
                ", successProbability=" + successProbability +
                ", potentialBottlenecks=" + potentialBottlenecks +
                '}';
    }

    // Builder class
    public static class PerformancePredictionBuilder {
        private double estimatedExecutionTime;
        private double successProbability;
        private List<String> potentialBottlenecks;

        public PerformancePredictionBuilder estimatedExecutionTime(double estimatedExecutionTime) {
            this.estimatedExecutionTime = estimatedExecutionTime;
            return this;
        }

        public PerformancePredictionBuilder successProbability(double successProbability) {
            this.successProbability = successProbability;
            return this;
        }

        public PerformancePredictionBuilder potentialBottlenecks(List<String> potentialBottlenecks) {
            this.potentialBottlenecks = potentialBottlenecks;
            return this;
        }

        public PerformancePredictionBuilder estimatedTime(double estimatedTime) {
            this.estimatedExecutionTime = estimatedTime;
            return this;
        }

        public PerformancePredictionBuilder confidence(double confidence) {
            this.successProbability = confidence;
            return this;
        }

        public PerformancePredictionBuilder factors(java.util.List<String> factors) {
            this.potentialBottlenecks = factors;
            return this;
        }

        public PerformancePredictionBuilder timestamp(java.time.LocalDateTime timestamp) {
            return this; // Placeholder
        }

        public PerformancePrediction build() {
            return new PerformancePrediction(estimatedExecutionTime, successProbability, potentialBottlenecks);
        }
    }
} 