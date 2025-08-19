package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Trend {
    private String metric;
    private TrendDirection direction;
    private double changeRate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Default constructor
    public Trend() {}

    // Constructor with all fields
    public Trend(String metric, TrendDirection direction, double changeRate, LocalDateTime startTime, LocalDateTime endTime) {
        this.metric = metric;
        this.direction = direction;
        this.changeRate = changeRate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Builder pattern
    public static TrendBuilder builder() {
        return new TrendBuilder();
    }

    // Getter methods
    public String getMetric() { return metric; }
    public TrendDirection getDirection() { return direction; }
    public double getChangeRate() { return changeRate; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getName() { return metric; } // Alias for metric
    public double getCurrentValue() { return changeRate; } // Alias for changeRate
    public double getPreviousValue() { return 0.0; } // Placeholder

    // Setter methods
    public void setMetric(String metric) { this.metric = metric; }
    public void setDirection(TrendDirection direction) { this.direction = direction; }
    public void setChangeRate(double changeRate) { this.changeRate = changeRate; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trend trend = (Trend) o;
        return Double.compare(trend.changeRate, changeRate) == 0 &&
                Objects.equals(metric, trend.metric) &&
                direction == trend.direction &&
                Objects.equals(startTime, trend.startTime) &&
                Objects.equals(endTime, trend.endTime);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(metric, direction, changeRate, startTime, endTime);
    }

    // toString method
    @Override
    public String toString() {
        return "Trend{" +
                "metric='" + metric + '\'' +
                ", direction=" + direction +
                ", changeRate=" + changeRate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    // Builder class
    public static class TrendBuilder {
        private String metric;
        private TrendDirection direction;
        private double changeRate;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public TrendBuilder metric(String metric) { this.metric = metric; return this; }
        public TrendBuilder direction(TrendDirection direction) { this.direction = direction; return this; }
        public TrendBuilder changeRate(double changeRate) { this.changeRate = changeRate; return this; }
        public TrendBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public TrendBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public TrendBuilder name(String name) { this.metric = name; return this; } // Alias for metric
        public TrendBuilder description(String description) { return this; } // Placeholder
        public TrendBuilder currentValue(double currentValue) { this.changeRate = currentValue; return this; } // Alias for changeRate
        public TrendBuilder previousValue(double previousValue) { return this; } // Placeholder
        public TrendBuilder confidence(double confidence) { return this; } // Placeholder

        public Trend build() {
            return new Trend(metric, direction, changeRate, startTime, endTime);
        }
    }
} 