-- Create initial schema for Jarvis Core
-- This migration creates all the necessary tables for the AI-powered test orchestration system

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create test_results table
CREATE TABLE test_results (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    test_name VARCHAR(255) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    test_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    execution_time_ms BIGINT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    error_message TEXT,
    stack_trace TEXT,
    test_parameters JSONB,
    test_output JSONB,
    performance_metrics JSONB,
    environment_info JSONB,
    confidence_score DOUBLE PRECISION,
    risk_level VARCHAR(50),
    tags TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create learning_data table
CREATE TABLE learning_data (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    data_type VARCHAR(50) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    test_pattern JSONB,
    failure_pattern JSONB,
    performance_pattern JSONB,
    optimization_pattern JSONB,
    feature_vector JSONB,
    label VARCHAR(255),
    confidence_score DOUBLE PRECISION,
    model_version VARCHAR(100),
    training_used BOOLEAN,
    validation_used BOOLEAN,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create plugin_registry table
CREATE TABLE plugin_registry (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    plugin_name VARCHAR(255) NOT NULL UNIQUE,
    plugin_version VARCHAR(100) NOT NULL,
    plugin_type VARCHAR(50) NOT NULL,
    description TEXT,
    author VARCHAR(255),
    main_class VARCHAR(500),
    jar_file_path VARCHAR(500),
    configuration JSONB,
    dependencies JSONB,
    capabilities JSONB,
    status VARCHAR(50) NOT NULL DEFAULT 'REGISTERED',
    enabled BOOLEAN DEFAULT true,
    load_time_ms BIGINT,
    last_execution_time TIMESTAMP,
    execution_count BIGINT DEFAULT 0,
    error_count BIGINT DEFAULT 0,
    last_error TEXT,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create system_logs table
CREATE TABLE system_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    log_level VARCHAR(20) NOT NULL,
    component VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    service_name VARCHAR(255),
    test_name VARCHAR(255),
    user_id VARCHAR(255),
    session_id VARCHAR(255),
    request_id VARCHAR(255),
    exception TEXT,
    stack_trace TEXT,
    context_data JSONB,
    performance_metrics JSONB,
    tags TEXT,
    correlation_id VARCHAR(255),
    parent_log_id UUID REFERENCES system_logs(id),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create ai_analysis_history table
CREATE TABLE ai_analysis_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    analysis_id UUID NOT NULL,
    intent_id UUID,
    analysis_data JSONB NOT NULL,
    confidence_score DOUBLE PRECISION,
    processing_time_ms BIGINT,
    model_version VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_test_results_service_name ON test_results(service_name);
CREATE INDEX idx_test_results_test_type ON test_results(test_type);
CREATE INDEX idx_test_results_status ON test_results(status);
CREATE INDEX idx_test_results_created_at ON test_results(created_at);
CREATE INDEX idx_test_results_start_time ON test_results(start_time);

CREATE INDEX idx_learning_data_service_name ON learning_data(service_name);
CREATE INDEX idx_learning_data_data_type ON learning_data(data_type);
CREATE INDEX idx_learning_data_created_at ON learning_data(created_at);

CREATE INDEX idx_plugin_registry_plugin_name ON plugin_registry(plugin_name);
CREATE INDEX idx_plugin_registry_plugin_type ON plugin_registry(plugin_type);
CREATE INDEX idx_plugin_registry_status ON plugin_registry(status);
CREATE INDEX idx_plugin_registry_enabled ON plugin_registry(enabled);

CREATE INDEX idx_system_logs_log_level ON system_logs(log_level);
CREATE INDEX idx_system_logs_component ON system_logs(component);
CREATE INDEX idx_system_logs_timestamp ON system_logs(timestamp);
CREATE INDEX idx_system_logs_service_name ON system_logs(service_name);
CREATE INDEX idx_system_logs_correlation_id ON system_logs(correlation_id);

CREATE INDEX idx_ai_analysis_history_analysis_id ON ai_analysis_history(analysis_id);
CREATE INDEX idx_ai_analysis_history_created_at ON ai_analysis_history(created_at);

-- Create composite indexes for common query patterns
CREATE INDEX idx_test_results_service_status ON test_results(service_name, status);
CREATE INDEX idx_test_results_service_type ON test_results(service_name, test_type);
CREATE INDEX idx_system_logs_component_level ON system_logs(component, log_level);

-- Add comments for documentation
COMMENT ON TABLE test_results IS 'Stores detailed test execution results for AI analysis and learning';
COMMENT ON TABLE learning_data IS 'Stores data for machine learning training and pattern recognition';
COMMENT ON TABLE plugin_registry IS 'Manages plugin system and tracks plugin information';
COMMENT ON TABLE system_logs IS 'Comprehensive system logging for historical analysis and debugging';
COMMENT ON TABLE ai_analysis_history IS 'Stores AI analysis history for learning and pattern recognition';
