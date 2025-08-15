package com.kb.jarvis.core.model;

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
@Table(name = "plugin_registry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginMetadata {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(name = "plugin_name", nullable = false, unique = true)
    private String pluginName;
    
    @NotNull
    @Column(name = "plugin_version", nullable = false)
    private String pluginVersion;
    
    @NotNull
    @Column(name = "plugin_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PluginType pluginType;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "author")
    private String author;
    
    @Column(name = "main_class")
    private String mainClass;
    
    @Column(name = "jar_file_path")
    private String jarFilePath;
    
    @Column(name = "configuration", columnDefinition = "JSONB")
    private Map<String, Object> configuration;
    
    @Column(name = "dependencies", columnDefinition = "JSONB")
    private Map<String, String> dependencies;
    
    @Column(name = "capabilities", columnDefinition = "JSONB")
    private Map<String, Object> capabilities;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PluginStatus status;
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @Column(name = "load_time_ms")
    private Long loadTimeMs;
    
    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime;
    
    @Column(name = "execution_count")
    private Long executionCount;
    
    @Column(name = "error_count")
    private Long errorCount;
    
    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;
    
    @Column(name = "metadata", columnDefinition = "JSONB")
    private Map<String, Object> metadata;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = PluginStatus.REGISTERED;
        }
        if (enabled == null) {
            enabled = true;
        }
        if (executionCount == null) {
            executionCount = 0L;
        }
        if (errorCount == null) {
            errorCount = 0L;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
