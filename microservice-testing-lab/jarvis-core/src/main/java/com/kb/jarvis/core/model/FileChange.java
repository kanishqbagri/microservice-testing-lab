package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a file change in a Pull Request
 * Contains information about file modifications, additions, and deletions
 */
public class FileChange {
    
    private String filePath;
    private String fileName;
    private String fileExtension;
    private ChangeType changeType;
    private int linesAdded;
    private int linesDeleted;
    private int linesModified;
    private List<String> addedLines;
    private List<String> deletedLines;
    private List<String> modifiedLines;
    private String diff;
    private String serviceName;
    private String componentName;
    private List<String> affectedMethods;
    private List<String> affectedClasses;
    private List<String> dependencies;
    private ChangeComplexity complexity;
    private List<String> keywords;
    private Map<String, Object> metadata;
    
    // Default constructor
    public FileChange() {}
    
    // Constructor with essential fields
    public FileChange(String filePath, ChangeType changeType, int linesAdded, int linesDeleted) {
        this.filePath = filePath;
        this.changeType = changeType;
        this.linesAdded = linesAdded;
        this.linesDeleted = linesDeleted;
        this.fileName = extractFileName(filePath);
        this.fileExtension = extractFileExtension(filePath);
    }
    
    // Builder pattern
    public static FileChangeBuilder builder() {
        return new FileChangeBuilder();
    }
    
    // Getters
    public String getFilePath() { return filePath; }
    public String getFileName() { return fileName; }
    public String getFileExtension() { return fileExtension; }
    public ChangeType getChangeType() { return changeType; }
    public int getLinesAdded() { return linesAdded; }
    public int getLinesDeleted() { return linesDeleted; }
    public int getLinesModified() { return linesModified; }
    public List<String> getAddedLines() { return addedLines; }
    public List<String> getDeletedLines() { return deletedLines; }
    public List<String> getModifiedLines() { return modifiedLines; }
    public String getDiff() { return diff; }
    public String getServiceName() { return serviceName; }
    public String getComponentName() { return componentName; }
    public List<String> getAffectedMethods() { return affectedMethods; }
    public List<String> getAffectedClasses() { return affectedClasses; }
    public List<String> getDependencies() { return dependencies; }
    public ChangeComplexity getComplexity() { return complexity; }
    public List<String> getKeywords() { return keywords; }
    public Map<String, Object> getMetadata() { return metadata; }
    
    // Setters
    public void setFilePath(String filePath) { 
        this.filePath = filePath; 
        this.fileName = extractFileName(filePath);
        this.fileExtension = extractFileExtension(filePath);
    }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileExtension(String fileExtension) { this.fileExtension = fileExtension; }
    public void setChangeType(ChangeType changeType) { this.changeType = changeType; }
    public void setLinesAdded(int linesAdded) { this.linesAdded = linesAdded; }
    public void setLinesDeleted(int linesDeleted) { this.linesDeleted = linesDeleted; }
    public void setLinesModified(int linesModified) { this.linesModified = linesModified; }
    public void setAddedLines(List<String> addedLines) { this.addedLines = addedLines; }
    public void setDeletedLines(List<String> deletedLines) { this.deletedLines = deletedLines; }
    public void setModifiedLines(List<String> modifiedLines) { this.modifiedLines = modifiedLines; }
    public void setDiff(String diff) { this.diff = diff; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }
    public void setAffectedMethods(List<String> affectedMethods) { this.affectedMethods = affectedMethods; }
    public void setAffectedClasses(List<String> affectedClasses) { this.affectedClasses = affectedClasses; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public void setComplexity(ChangeComplexity complexity) { this.complexity = complexity; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    // Helper methods
    public int getTotalLinesChanged() {
        return linesAdded + linesDeleted + linesModified;
    }
    
    public boolean isJavaFile() {
        return "java".equalsIgnoreCase(fileExtension);
    }
    
    public boolean isConfigFile() {
        return fileExtension != null && 
               (fileExtension.equalsIgnoreCase("yml") || 
                fileExtension.equalsIgnoreCase("yaml") || 
                fileExtension.equalsIgnoreCase("properties") ||
                fileExtension.equalsIgnoreCase("json") ||
                fileExtension.equalsIgnoreCase("xml"));
    }
    
    public boolean isTestFile() {
        return fileName != null && 
               (fileName.contains("Test") || 
                fileName.contains("test") || 
                filePath.contains("/test/") ||
                filePath.contains("/tests/"));
    }
    
    public boolean isLargeChange() {
        return getTotalLinesChanged() > 100;
    }
    
    public boolean affectsService(String serviceName) {
        return this.serviceName != null && this.serviceName.equals(serviceName);
    }
    
    private String extractFileName(String filePath) {
        if (filePath == null) return null;
        int lastSlash = filePath.lastIndexOf('/');
        return lastSlash >= 0 ? filePath.substring(lastSlash + 1) : filePath;
    }
    
    private String extractFileExtension(String filePath) {
        if (filePath == null) return null;
        int lastDot = filePath.lastIndexOf('.');
        return lastDot >= 0 ? filePath.substring(lastDot + 1) : null;
    }
    
    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileChange that = (FileChange) o;
        return Objects.equals(filePath, that.filePath);
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }
    
    // toString method
    @Override
    public String toString() {
        return "FileChange{" +
                "filePath='" + filePath + '\'' +
                ", changeType=" + changeType +
                ", linesAdded=" + linesAdded +
                ", linesDeleted=" + linesDeleted +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
    
    // Builder class
    public static class FileChangeBuilder {
        private String filePath;
        private String fileName;
        private String fileExtension;
        private ChangeType changeType;
        private int linesAdded;
        private int linesDeleted;
        private int linesModified;
        private List<String> addedLines;
        private List<String> deletedLines;
        private List<String> modifiedLines;
        private String diff;
        private String serviceName;
        private String componentName;
        private List<String> affectedMethods;
        private List<String> affectedClasses;
        private List<String> dependencies;
        private ChangeComplexity complexity;
        private List<String> keywords;
        private Map<String, Object> metadata;
        
        public FileChangeBuilder filePath(String filePath) { this.filePath = filePath; return this; }
        public FileChangeBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public FileChangeBuilder fileExtension(String fileExtension) { this.fileExtension = fileExtension; return this; }
        public FileChangeBuilder changeType(ChangeType changeType) { this.changeType = changeType; return this; }
        public FileChangeBuilder linesAdded(int linesAdded) { this.linesAdded = linesAdded; return this; }
        public FileChangeBuilder linesDeleted(int linesDeleted) { this.linesDeleted = linesDeleted; return this; }
        public FileChangeBuilder linesModified(int linesModified) { this.linesModified = linesModified; return this; }
        public FileChangeBuilder addedLines(List<String> addedLines) { this.addedLines = addedLines; return this; }
        public FileChangeBuilder deletedLines(List<String> deletedLines) { this.deletedLines = deletedLines; return this; }
        public FileChangeBuilder modifiedLines(List<String> modifiedLines) { this.modifiedLines = modifiedLines; return this; }
        public FileChangeBuilder diff(String diff) { this.diff = diff; return this; }
        public FileChangeBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public FileChangeBuilder componentName(String componentName) { this.componentName = componentName; return this; }
        public FileChangeBuilder affectedMethods(List<String> affectedMethods) { this.affectedMethods = affectedMethods; return this; }
        public FileChangeBuilder affectedClasses(List<String> affectedClasses) { this.affectedClasses = affectedClasses; return this; }
        public FileChangeBuilder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public FileChangeBuilder complexity(ChangeComplexity complexity) { this.complexity = complexity; return this; }
        public FileChangeBuilder keywords(List<String> keywords) { this.keywords = keywords; return this; }
        public FileChangeBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        
        public FileChange build() {
            FileChange fileChange = new FileChange();
            fileChange.setFilePath(filePath);
            fileChange.setFileName(fileName);
            fileChange.setFileExtension(fileExtension);
            fileChange.setChangeType(changeType);
            fileChange.setLinesAdded(linesAdded);
            fileChange.setLinesDeleted(linesDeleted);
            fileChange.setLinesModified(linesModified);
            fileChange.setAddedLines(addedLines);
            fileChange.setDeletedLines(deletedLines);
            fileChange.setModifiedLines(modifiedLines);
            fileChange.setDiff(diff);
            fileChange.setServiceName(serviceName);
            fileChange.setComponentName(componentName);
            fileChange.setAffectedMethods(affectedMethods);
            fileChange.setAffectedClasses(affectedClasses);
            fileChange.setDependencies(dependencies);
            fileChange.setComplexity(complexity);
            fileChange.setKeywords(keywords);
            fileChange.setMetadata(metadata);
            return fileChange;
        }
    }
}
