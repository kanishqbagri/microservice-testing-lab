package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents information about a Pull Request for analysis
 * Contains PR metadata, changes, and context information
 */
public class PRInfo {
    
    private String prId;
    private String title;
    private String description;
    private String author;
    private String sourceBranch;
    private String targetBranch;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private List<String> reviewers;
    private List<String> labels;
    private List<FileChange> fileChanges;
    private Map<String, Object> metadata;
    private String repository;
    private String organization;
    private int linesAdded;
    private int linesDeleted;
    private int filesChanged;
    private List<String> affectedServices;
    private List<String> affectedComponents;
    private String prUrl;
    private String diffUrl;
    private Map<String, Object> commitInfo;
    
    // Default constructor
    public PRInfo() {}
    
    // Constructor with essential fields
    public PRInfo(String prId, String title, String description, String author, 
                  String sourceBranch, String targetBranch, String repository) {
        this.prId = prId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.sourceBranch = sourceBranch;
        this.targetBranch = targetBranch;
        this.repository = repository;
        this.createdAt = LocalDateTime.now();
    }
    
    // Builder pattern
    public static PRInfoBuilder builder() {
        return new PRInfoBuilder();
    }
    
    // Getters
    public String getPrId() { return prId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getSourceBranch() { return sourceBranch; }
    public String getTargetBranch() { return targetBranch; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getStatus() { return status; }
    public List<String> getReviewers() { return reviewers; }
    public List<String> getLabels() { return labels; }
    public List<FileChange> getFileChanges() { return fileChanges; }
    public Map<String, Object> getMetadata() { return metadata; }
    public String getRepository() { return repository; }
    public String getOrganization() { return organization; }
    public int getLinesAdded() { return linesAdded; }
    public int getLinesDeleted() { return linesDeleted; }
    public int getFilesChanged() { return filesChanged; }
    public List<String> getAffectedServices() { return affectedServices; }
    public List<String> getAffectedComponents() { return affectedComponents; }
    public String getPrUrl() { return prUrl; }
    public String getDiffUrl() { return diffUrl; }
    public Map<String, Object> getCommitInfo() { return commitInfo; }
    
    // Setters
    public void setPrId(String prId) { this.prId = prId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setAuthor(String author) { this.author = author; }
    public void setSourceBranch(String sourceBranch) { this.sourceBranch = sourceBranch; }
    public void setTargetBranch(String targetBranch) { this.targetBranch = targetBranch; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setStatus(String status) { this.status = status; }
    public void setReviewers(List<String> reviewers) { this.reviewers = reviewers; }
    public void setLabels(List<String> labels) { this.labels = labels; }
    public void setFileChanges(List<FileChange> fileChanges) { this.fileChanges = fileChanges; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void setRepository(String repository) { this.repository = repository; }
    public void setOrganization(String organization) { this.organization = organization; }
    public void setLinesAdded(int linesAdded) { this.linesAdded = linesAdded; }
    public void setLinesDeleted(int linesDeleted) { this.linesDeleted = linesDeleted; }
    public void setFilesChanged(int filesChanged) { this.filesChanged = filesChanged; }
    public void setAffectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; }
    public void setAffectedComponents(List<String> affectedComponents) { this.affectedComponents = affectedComponents; }
    public void setPrUrl(String prUrl) { this.prUrl = prUrl; }
    public void setDiffUrl(String diffUrl) { this.diffUrl = diffUrl; }
    public void setCommitInfo(Map<String, Object> commitInfo) { this.commitInfo = commitInfo; }
    
    // Helper methods
    public int getTotalLinesChanged() {
        return linesAdded + linesDeleted;
    }
    
    public boolean isLargePR() {
        return getTotalLinesChanged() > 500 || filesChanged > 20;
    }
    
    public boolean affectsService(String serviceName) {
        return affectedServices != null && affectedServices.contains(serviceName);
    }
    
    public boolean hasLabel(String label) {
        return labels != null && labels.contains(label);
    }
    
    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PRInfo prInfo = (PRInfo) o;
        return Objects.equals(prId, prInfo.prId);
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(prId);
    }
    
    // toString method
    @Override
    public String toString() {
        return "PRInfo{" +
                "prId='" + prId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", repository='" + repository + '\'' +
                ", filesChanged=" + filesChanged +
                ", linesAdded=" + linesAdded +
                ", linesDeleted=" + linesDeleted +
                '}';
    }
    
    // Builder class
    public static class PRInfoBuilder {
        private String prId;
        private String title;
        private String description;
        private String author;
        private String sourceBranch;
        private String targetBranch;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String status;
        private List<String> reviewers;
        private List<String> labels;
        private List<FileChange> fileChanges;
        private Map<String, Object> metadata;
        private String repository;
        private String organization;
        private int linesAdded;
        private int linesDeleted;
        private int filesChanged;
        private List<String> affectedServices;
        private List<String> affectedComponents;
        private String prUrl;
        private String diffUrl;
        private Map<String, Object> commitInfo;
        
        public PRInfoBuilder prId(String prId) { this.prId = prId; return this; }
        public PRInfoBuilder title(String title) { this.title = title; return this; }
        public PRInfoBuilder description(String description) { this.description = description; return this; }
        public PRInfoBuilder author(String author) { this.author = author; return this; }
        public PRInfoBuilder sourceBranch(String sourceBranch) { this.sourceBranch = sourceBranch; return this; }
        public PRInfoBuilder targetBranch(String targetBranch) { this.targetBranch = targetBranch; return this; }
        public PRInfoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public PRInfoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public PRInfoBuilder status(String status) { this.status = status; return this; }
        public PRInfoBuilder reviewers(List<String> reviewers) { this.reviewers = reviewers; return this; }
        public PRInfoBuilder labels(List<String> labels) { this.labels = labels; return this; }
        public PRInfoBuilder fileChanges(List<FileChange> fileChanges) { this.fileChanges = fileChanges; return this; }
        public PRInfoBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        public PRInfoBuilder repository(String repository) { this.repository = repository; return this; }
        public PRInfoBuilder organization(String organization) { this.organization = organization; return this; }
        public PRInfoBuilder linesAdded(int linesAdded) { this.linesAdded = linesAdded; return this; }
        public PRInfoBuilder linesDeleted(int linesDeleted) { this.linesDeleted = linesDeleted; return this; }
        public PRInfoBuilder filesChanged(int filesChanged) { this.filesChanged = filesChanged; return this; }
        public PRInfoBuilder affectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; return this; }
        public PRInfoBuilder affectedComponents(List<String> affectedComponents) { this.affectedComponents = affectedComponents; return this; }
        public PRInfoBuilder prUrl(String prUrl) { this.prUrl = prUrl; return this; }
        public PRInfoBuilder diffUrl(String diffUrl) { this.diffUrl = diffUrl; return this; }
        public PRInfoBuilder commitInfo(Map<String, Object> commitInfo) { this.commitInfo = commitInfo; return this; }
        
        public PRInfo build() {
            PRInfo prInfo = new PRInfo();
            prInfo.setPrId(prId);
            prInfo.setTitle(title);
            prInfo.setDescription(description);
            prInfo.setAuthor(author);
            prInfo.setSourceBranch(sourceBranch);
            prInfo.setTargetBranch(targetBranch);
            prInfo.setCreatedAt(createdAt);
            prInfo.setUpdatedAt(updatedAt);
            prInfo.setStatus(status);
            prInfo.setReviewers(reviewers);
            prInfo.setLabels(labels);
            prInfo.setFileChanges(fileChanges);
            prInfo.setMetadata(metadata);
            prInfo.setRepository(repository);
            prInfo.setOrganization(organization);
            prInfo.setLinesAdded(linesAdded);
            prInfo.setLinesDeleted(linesDeleted);
            prInfo.setFilesChanged(filesChanged);
            prInfo.setAffectedServices(affectedServices);
            prInfo.setAffectedComponents(affectedComponents);
            prInfo.setPrUrl(prUrl);
            prInfo.setDiffUrl(diffUrl);
            prInfo.setCommitInfo(commitInfo);
            return prInfo;
        }
    }
}
