package com.kb.notification_service.exception;

public class NotificationNotFoundException extends RuntimeException {
    
    public NotificationNotFoundException(String message) {
        super(message);
    }
    
    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 