package com.kb.jarvis.core.model;

import java.util.Objects;

public class Recommendation {
    private String description;
    private RecommendationType type;
    private double priority;
    private String rationale;

    // Default constructor
    public Recommendation() {}

    // Constructor with all fields
    public Recommendation(String description, RecommendationType type, double priority, String rationale) {
        this.description = description;
        this.type = type;
        this.priority = priority;
        this.rationale = rationale;
    }

    // Builder pattern
    public static RecommendationBuilder builder() {
        return new RecommendationBuilder();
    }

    // Getter methods
    public String getDescription() { return description; }
    public RecommendationType getType() { return type; }
    public double getPriority() { return priority; }
    public String getRationale() { return rationale; }

    // Setter methods
    public void setDescription(String description) { this.description = description; }
    public void setType(RecommendationType type) { this.type = type; }
    public void setPriority(double priority) { this.priority = priority; }
    public void setRationale(String rationale) { this.rationale = rationale; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Double.compare(that.priority, priority) == 0 &&
                Objects.equals(description, that.description) &&
                type == that.type &&
                Objects.equals(rationale, that.rationale);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(description, type, priority, rationale);
    }

    // toString method
    @Override
    public String toString() {
        return "Recommendation{" +
                "description='" + description + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", rationale='" + rationale + '\'' +
                '}';
    }

    // Builder class
    public static class RecommendationBuilder {
        private String description;
        private RecommendationType type;
        private double priority;
        private String rationale;

        public RecommendationBuilder description(String description) { this.description = description; return this; }
        public RecommendationBuilder type(RecommendationType type) { this.type = type; return this; }
        public RecommendationBuilder priority(double priority) { this.priority = priority; return this; }
        public RecommendationBuilder rationale(String rationale) { this.rationale = rationale; return this; }

        public Recommendation build() {
            return new Recommendation(description, type, priority, rationale);
        }
    }
} 