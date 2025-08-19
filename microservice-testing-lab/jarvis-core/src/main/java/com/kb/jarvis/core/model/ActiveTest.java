package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ActiveTest {
    private String testId;
    private String testName;
    private String service;
    private TestStatus status;
    private LocalDateTime startTime;
    private long duration;
    private double progress;
    private String id;
    private TestType type;
    private String serviceName;

    // Default constructor
    public ActiveTest() {}

    // Constructor with all fields
    public ActiveTest(String testId, String testName, String service, TestStatus status, LocalDateTime startTime,
                     long duration, double progress, String id, TestType type, String serviceName) {
        this.testId = testId;
        this.testName = testName;
        this.service = service;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.progress = progress;
        this.id = id;
        this.type = type;
        this.serviceName = serviceName;
    }

    // Builder pattern
    public static ActiveTestBuilder builder() {
        return new ActiveTestBuilder();
    }

    // Getter methods
    public String getTestId() { return testId; }
    public String getTestName() { return testName; }
    public String getService() { return service; }
    public TestStatus getStatus() { return status; }
    public LocalDateTime getStartTime() { return startTime; }
    public long getDuration() { return duration; }
    public double getProgress() { return progress; }
    public String getId() { return id; }
    public TestType getType() { return type; }
    public String getServiceName() { return serviceName; }

    // Setter methods
    public void setTestId(String testId) { this.testId = testId; }
    public void setTestName(String testName) { this.testName = testName; }
    public void setService(String service) { this.service = service; }
    public void setStatus(TestStatus status) { this.status = status; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setDuration(long duration) { this.duration = duration; }
    public void setProgress(double progress) { this.progress = progress; }
    public void setId(String id) { this.id = id; }
    public void setType(TestType type) { this.type = type; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActiveTest that = (ActiveTest) o;
        return duration == that.duration &&
                Double.compare(that.progress, progress) == 0 &&
                Objects.equals(testId, that.testId) &&
                Objects.equals(testName, that.testName) &&
                Objects.equals(service, that.service) &&
                status == that.status &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(id, that.id) &&
                type == that.type &&
                Objects.equals(serviceName, that.serviceName);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(testId, testName, service, status, startTime, duration, progress, id, type, serviceName);
    }

    // toString method
    @Override
    public String toString() {
        return "ActiveTest{" +
                "testId='" + testId + '\'' +
                ", testName='" + testName + '\'' +
                ", service='" + service + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", progress=" + progress +
                ", id='" + id + '\'' +
                ", type=" + type +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }

    // Builder class
    public static class ActiveTestBuilder {
        private String testId;
        private String testName;
        private String service;
        private TestStatus status;
        private LocalDateTime startTime;
        private long duration;
        private double progress;
        private String id;
        private TestType type;
        private String serviceName;

        public ActiveTestBuilder testId(String testId) { this.testId = testId; return this; }
        public ActiveTestBuilder testName(String testName) { this.testName = testName; return this; }
        public ActiveTestBuilder service(String service) { this.service = service; return this; }
        public ActiveTestBuilder status(TestStatus status) { this.status = status; return this; }
        public ActiveTestBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public ActiveTestBuilder duration(long duration) { this.duration = duration; return this; }
        public ActiveTestBuilder progress(double progress) { this.progress = progress; return this; }
        public ActiveTestBuilder id(String id) { this.id = id; return this; }
        public ActiveTestBuilder type(TestType type) { this.type = type; return this; }
        public ActiveTestBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }

        public ActiveTest build() {
            return new ActiveTest(testId, testName, service, status, startTime, duration, progress, id, type, serviceName);
        }
    }
} 