package com.kb.jarvis.core.model;

// TEMPORARILY COMMENTED OUT FOR QUICK AI INTEGRATION TESTING
// TODO: Convert to manual implementation after AI integration testing
/*
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "plugin_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginMetadata {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(name = "plugin_name", nullable = false)
    private String pluginName;
    
    @NotNull
    @Column(name = "version", nullable = false)
    private String version;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "author")
    private String author;
    
    @Column(name = "license")
    private String license;
    
    @Column(name = "repository_url")
    private String repositoryUrl;
    
    @Column(name = "documentation_url")
    private String documentationUrl;
    
    @Column(name = "configuration_schema", columnDefinition = "JSONB")
    private Map<String, Object> configurationSchema;
    
    @Column(name = "capabilities", columnDefinition = "JSONB")
    private Map<String, Object> capabilities;
    
    @Column(name = "dependencies", columnDefinition = "JSONB")
    private Map<String, Object> dependencies;
    
    @Column(name = "compatibility", columnDefinition = "JSONB")
    private Map<String, Object> compatibility;
    
    @Column(name = "installation_instructions", columnDefinition = "TEXT")
    private String installationInstructions;
    
    @Column(name = "usage_examples", columnDefinition = "JSONB")
    private Map<String, Object> usageExamples;
    
    @Column(name = "changelog", columnDefinition = "JSONB")
    private Map<String, Object> changelog;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PluginStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
*/

// Temporary placeholder for compilation
public class PluginMetadata {
    private java.util.UUID id;
    private String pluginName;
    private String version;
    private String description;
    private String author;
    private String license;
    private String repositoryUrl;
    private String documentationUrl;
    private java.util.Map<String, Object> configurationSchema;
    private java.util.Map<String, Object> capabilities;
    private java.util.Map<String, Object> dependencies;
    private java.util.Map<String, Object> compatibility;
    private String installationInstructions;
    private java.util.Map<String, Object> usageExamples;
    private java.util.Map<String, Object> changelog;
    private PluginStatus status;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    
    public PluginMetadata() {}
    
    public java.util.UUID getId() { return id; }
    public void setId(java.util.UUID id) { this.id = id; }
    public String getPluginName() { return pluginName; }
    public void setPluginName(String pluginName) { this.pluginName = pluginName; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }
    public String getRepositoryUrl() { return repositoryUrl; }
    public void setRepositoryUrl(String repositoryUrl) { this.repositoryUrl = repositoryUrl; }
    public String getDocumentationUrl() { return documentationUrl; }
    public void setDocumentationUrl(String documentationUrl) { this.documentationUrl = documentationUrl; }
    public java.util.Map<String, Object> getConfigurationSchema() { return configurationSchema; }
    public void setConfigurationSchema(java.util.Map<String, Object> configurationSchema) { this.configurationSchema = configurationSchema; }
    public java.util.Map<String, Object> getCapabilities() { return capabilities; }
    public void setCapabilities(java.util.Map<String, Object> capabilities) { this.capabilities = capabilities; }
    public java.util.Map<String, Object> getDependencies() { return dependencies; }
    public void setDependencies(java.util.Map<String, Object> dependencies) { this.dependencies = dependencies; }
    public java.util.Map<String, Object> getCompatibility() { return compatibility; }
    public void setCompatibility(java.util.Map<String, Object> compatibility) { this.compatibility = compatibility; }
    public String getInstallationInstructions() { return installationInstructions; }
    public void setInstallationInstructions(String installationInstructions) { this.installationInstructions = installationInstructions; }
    public java.util.Map<String, Object> getUsageExamples() { return usageExamples; }
    public void setUsageExamples(java.util.Map<String, Object> usageExamples) { this.usageExamples = usageExamples; }
    public java.util.Map<String, Object> getChangelog() { return changelog; }
    public void setChangelog(java.util.Map<String, Object> changelog) { this.changelog = changelog; }
    public PluginStatus getStatus() { return status; }
    public void setStatus(PluginStatus status) { this.status = status; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
