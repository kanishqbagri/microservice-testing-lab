package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Pattern {
    private String patternType;
    private String description;
    private double confidence;
    private List<String> examples;
    private LocalDateTime discoveredAt;

    // Default constructor
    public Pattern() {}

    // Constructor with all fields
    public Pattern(String patternType, String description, double confidence, List<String> examples, LocalDateTime discoveredAt) {
        this.patternType = patternType;
        this.description = description;
        this.confidence = confidence;
        this.examples = examples;
        this.discoveredAt = discoveredAt;
    }

    // Builder pattern
    public static PatternBuilder builder() {
        return new PatternBuilder();
    }

    // Getter methods
    public String getPatternType() { return patternType; }
    public String getDescription() { return description; }
    public double getConfidence() { return confidence; }
    public List<String> getExamples() { return examples; }
    public LocalDateTime getDiscoveredAt() { return discoveredAt; }
    public String getName() { return patternType; } // Alias for patternType

    // Setter methods
    public void setPatternType(String patternType) { this.patternType = patternType; }
    public void setDescription(String description) { this.description = description; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public void setExamples(List<String> examples) { this.examples = examples; }
    public void setDiscoveredAt(LocalDateTime discoveredAt) { this.discoveredAt = discoveredAt; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern = (Pattern) o;
        return Double.compare(pattern.confidence, confidence) == 0 &&
                Objects.equals(patternType, pattern.patternType) &&
                Objects.equals(description, pattern.description) &&
                Objects.equals(examples, pattern.examples) &&
                Objects.equals(discoveredAt, pattern.discoveredAt);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(patternType, description, confidence, examples, discoveredAt);
    }

    // toString method
    @Override
    public String toString() {
        return "Pattern{" +
                "patternType='" + patternType + '\'' +
                ", description='" + description + '\'' +
                ", confidence=" + confidence +
                ", examples=" + examples +
                ", discoveredAt=" + discoveredAt +
                '}';
    }

    // Builder class
    public static class PatternBuilder {
        private String patternType;
        private String description;
        private double confidence;
        private List<String> examples;
        private LocalDateTime discoveredAt;

        public PatternBuilder patternType(String patternType) { this.patternType = patternType; return this; }
        public PatternBuilder description(String description) { this.description = description; return this; }
        public PatternBuilder confidence(double confidence) { this.confidence = confidence; return this; }
        public PatternBuilder examples(List<String> examples) { this.examples = examples; return this; }
        public PatternBuilder discoveredAt(LocalDateTime discoveredAt) { this.discoveredAt = discoveredAt; return this; }
        public PatternBuilder name(String name) { this.patternType = name; return this; } // Alias for patternType
        public PatternBuilder frequency(double frequency) { return this; } // Placeholder

        public Pattern build() {
            return new Pattern(patternType, description, confidence, examples, discoveredAt);
        }
    }
} 