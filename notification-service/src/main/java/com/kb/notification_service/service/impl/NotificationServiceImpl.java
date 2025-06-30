package com.kb.notification_service.service.impl;

import com.kb.notification_service.dto.NotificationRequest;
import com.kb.notification_service.dto.NotificationResponse;
import com.kb.notification_service.entity.Notification;
import com.kb.notification_service.entity.NotificationStatus;
import com.kb.notification_service.entity.NotificationType;
import com.kb.notification_service.exception.NotificationNotFoundException;
import com.kb.notification_service.repository.NotificationRepository;
import com.kb.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    @Override
    public NotificationResponse createNotification(NotificationRequest notificationRequest) {
        log.info("Creating notification for user: {}", notificationRequest.getUserId());
        
        Notification notification = modelMapper.map(notificationRequest, Notification.class);
        notification.setStatus(NotificationStatus.PENDING);
        
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification created successfully with ID: {}", savedNotification.getId());
        
        return convertToResponse(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long id) {
        log.info("Fetching notification by ID: {}", id);
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID: " + id));
        return convertToResponse(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        log.info("Fetching notifications for user: {}", userId);
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByUserIdAndStatus(Long userId, NotificationStatus status) {
        log.info("Fetching notifications for user: {} with status: {}", userId, status);
        List<Notification> notifications = notificationRepository.findByUserIdAndStatus(userId, status);
        return notifications.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByStatus(NotificationStatus status) {
        log.info("Fetching notifications with status: {}", status);
        List<Notification> notifications = notificationRepository.findByStatus(status);
        return notifications.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByType(NotificationType type) {
        log.info("Fetching notifications with type: {}", type);
        List<Notification> notifications = notificationRepository.findByType(type);
        return notifications.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByEmail(String recipientEmail) {
        log.info("Fetching notifications for email: {}", recipientEmail);
        List<Notification> notifications = notificationRepository.findByRecipientEmail(recipientEmail);
        return notifications.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching notifications between {} and {}", startDate, endDate);
        List<Notification> notifications = notificationRepository.findByCreatedAtBetween(startDate, endDate);
        return notifications.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByUserIdPaginated(Long userId, Pageable pageable) {
        log.info("Fetching paginated notifications for user: {}", userId);
        Page<Notification> notifications = notificationRepository.findByUserId(userId, pageable);
        return notifications.map(this::convertToResponse);
    }

    @Override
    public NotificationResponse updateNotificationStatus(Long id, NotificationStatus status) {
        log.info("Updating notification status for ID: {} to status: {}", id, status);
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new NotificationNotFoundException("Notification not found with ID: " + id));
        
        notification.setStatus(status);
        
        if (status == NotificationStatus.SENT) {
            notification.setSentAt(LocalDateTime.now());
        } else if (status == NotificationStatus.READ) {
            notification.setReadAt(LocalDateTime.now());
        }
        
        Notification updatedNotification = notificationRepository.save(notification);
        log.info("Notification status updated successfully for ID: {}", id);
        
        return convertToResponse(updatedNotification);
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        return updateNotificationStatus(id, NotificationStatus.READ);
    }

    @Override
    public NotificationResponse markAsSent(Long id) {
        return updateNotificationStatus(id, NotificationStatus.SENT);
    }

    @Override
    public void deleteNotification(Long id) {
        log.info("Deleting notification with ID: {}", id);
        if (!notificationRepository.existsById(id)) {
            throw new NotificationNotFoundException("Notification not found with ID: " + id);
        }
        notificationRepository.deleteById(id);
        log.info("Notification deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCountByUserId(Long userId) {
        log.info("Getting unread count for user: {}", userId);
        return notificationRepository.countByUserIdAndStatus(userId, NotificationStatus.PENDING);
    }

    @Override
    public void sendNotification(NotificationRequest notificationRequest) {
        log.info("Sending notification to user: {}", notificationRequest.getUserId());
        
        // Create the notification first
        NotificationResponse notification = createNotification(notificationRequest);
        
        // Simulate sending the notification based on type
        try {
            switch (notificationRequest.getType()) {
                case EMAIL:
                    sendEmailNotification(notificationRequest);
                    break;
                case SMS:
                    sendSmsNotification(notificationRequest);
                    break;
                case PUSH:
                    sendPushNotification(notificationRequest);
                    break;
                case IN_APP:
                    sendInAppNotification(notificationRequest);
                    break;
            }
            
            // Mark as sent
            updateNotificationStatus(notification.getId(), NotificationStatus.SENT);
            log.info("Notification sent successfully for ID: {}", notification.getId());
            
        } catch (Exception e) {
            log.error("Failed to send notification for ID: {}", notification.getId(), e);
            updateNotificationStatus(notification.getId(), NotificationStatus.FAILED);
            throw new RuntimeException("Failed to send notification", e);
        }
    }

    private void sendEmailNotification(NotificationRequest request) {
        log.info("Sending email notification to: {}", request.getRecipientEmail());
        // TODO: Implement actual email sending logic
        // This would integrate with an email service like SendGrid, AWS SES, etc.
    }

    private void sendSmsNotification(NotificationRequest request) {
        log.info("Sending SMS notification to: {}", request.getRecipientPhone());
        // TODO: Implement actual SMS sending logic
        // This would integrate with an SMS service like Twilio, AWS SNS, etc.
    }

    private void sendPushNotification(NotificationRequest request) {
        log.info("Sending push notification to user: {}", request.getUserId());
        // TODO: Implement actual push notification logic
        // This would integrate with Firebase Cloud Messaging, etc.
    }

    private void sendInAppNotification(NotificationRequest request) {
        log.info("Sending in-app notification to user: {}", request.getUserId());
        // TODO: Implement actual in-app notification logic
        // This would typically involve WebSocket or Server-Sent Events
    }

    private NotificationResponse convertToResponse(Notification notification) {
        return modelMapper.map(notification, NotificationResponse.class);
    }
} 