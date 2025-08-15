package com.kb.jarvis.core.repository;

import com.kb.jarvis.core.model.PluginMetadata;
import com.kb.jarvis.core.model.PluginStatus;
import com.kb.jarvis.core.model.PluginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PluginMetadataRepository extends JpaRepository<PluginMetadata, UUID> {
    
    // Find by plugin name
    Optional<PluginMetadata> findByPluginName(String pluginName);
    
    // Find by plugin type
    List<PluginMetadata> findByPluginType(PluginType pluginType);
    
    // Find by status
    List<PluginMetadata> findByStatus(PluginStatus status);
    
    // Find enabled plugins
    List<PluginMetadata> findByEnabledTrue();
    
    // Find enabled plugins by type
    List<PluginMetadata> findByEnabledTrueAndPluginType(PluginType pluginType);
    
    // Find by author
    List<PluginMetadata> findByAuthor(String author);
    
    // Find by version
    List<PluginMetadata> findByPluginVersion(String pluginVersion);
    
    // Find plugins with errors
    List<PluginMetadata> findByStatusAndErrorCountGreaterThan(PluginStatus status, Long errorCount);
    
    // Find plugins by main class
    List<PluginMetadata> findByMainClass(String mainClass);
    
    // Find plugins with high error count
    @Query("SELECT p FROM PluginMetadata p WHERE p.errorCount > :threshold")
    List<PluginMetadata> findPluginsWithHighErrorCount(@Param("threshold") Long threshold);
    
    // Find plugins with high execution count
    @Query("SELECT p FROM PluginMetadata p WHERE p.executionCount > :threshold ORDER BY p.executionCount DESC")
    List<PluginMetadata> findMostUsedPlugins(@Param("threshold") Long threshold);
    
    // Find plugins by capability
    @Query("SELECT p FROM PluginMetadata p WHERE p.capabilities IS NOT NULL")
    List<PluginMetadata> findPluginsWithCapabilities();
    
    // Find plugins by dependency
    @Query("SELECT p FROM PluginMetadata p WHERE p.dependencies IS NOT NULL")
    List<PluginMetadata> findPluginsWithDependencies();
    
    // Count plugins by type
    @Query("SELECT COUNT(p) FROM PluginMetadata p WHERE p.pluginType = :pluginType")
    Long countByPluginType(@Param("pluginType") PluginType pluginType);
    
    // Count enabled plugins by type
    @Query("SELECT COUNT(p) FROM PluginMetadata p WHERE p.enabled = true AND p.pluginType = :pluginType")
    Long countEnabledByPluginType(@Param("pluginType") PluginType pluginType);
    
    // Get plugin statistics
    @Query("SELECT p.pluginType, COUNT(p), AVG(p.executionCount), AVG(p.errorCount) " +
           "FROM PluginMetadata p GROUP BY p.pluginType")
    List<Object[]> getPluginStatistics();
    
    // Find plugins that need updating (high error rate)
    @Query("SELECT p FROM PluginMetadata p WHERE p.errorCount > 0 AND p.executionCount > 0 " +
           "AND (CAST(p.errorCount AS double) / CAST(p.executionCount AS double)) > :errorRateThreshold")
    List<PluginMetadata> findPluginsNeedingUpdate(@Param("errorRateThreshold") Double errorRateThreshold);
    
    // Find plugins by configuration
    @Query("SELECT p FROM PluginMetadata p WHERE p.configuration IS NOT NULL")
    List<PluginMetadata> findPluginsWithConfiguration();
    
    // Find plugins by metadata
    @Query("SELECT p FROM PluginMetadata p WHERE p.metadata IS NOT NULL")
    List<PluginMetadata> findPluginsWithMetadata();
    
    // Find plugins by load time (slow loading plugins)
    @Query("SELECT p FROM PluginMetadata p WHERE p.loadTimeMs > :threshold ORDER BY p.loadTimeMs DESC")
    List<PluginMetadata> findSlowLoadingPlugins(@Param("threshold") Long threshold);
    
    // Find recently executed plugins
    @Query("SELECT p FROM PluginMetadata p WHERE p.lastExecutionTime IS NOT NULL " +
           "ORDER BY p.lastExecutionTime DESC")
    List<PluginMetadata> findRecentlyExecutedPlugins();
    
    // Find plugins by jar file path
    List<PluginMetadata> findByJarFilePath(String jarFilePath);
    
    // Find plugins with specific capability
    @Query("SELECT p FROM PluginMetadata p WHERE p.capabilities IS NOT NULL AND p.capabilities ? :capability")
    List<PluginMetadata> findPluginsWithCapability(@Param("capability") String capability);
    
    // Find plugins with specific dependency
    @Query("SELECT p FROM PluginMetadata p WHERE p.dependencies IS NOT NULL AND p.dependencies ? :dependency")
    List<PluginMetadata> findPluginsWithDependency(@Param("dependency") String dependency);
}
