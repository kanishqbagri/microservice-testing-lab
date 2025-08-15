package com.kb.gateway_service.dto;

public class ServiceHealthResponse {
    private String service;
    private String status;
    private String message;
    private long timestamp;

    public ServiceHealthResponse() {
    }

    public ServiceHealthResponse(String service, String status, String message, long timestamp) {
        this.service = service;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
} 