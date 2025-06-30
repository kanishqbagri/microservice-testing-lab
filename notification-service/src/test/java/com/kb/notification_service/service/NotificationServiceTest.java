package com.kb.notification_service.service;

import com.kb.notification_service.dto.NotificationRequest;
import com.kb.notification_service.dto.NotificationResponse;
import com.kb.notification_service.entity.Notification;
import com.kb.notification_service.entity.NotificationStatus;
import com.kb.notification_service.entity.NotificationType;
import com.kb.notification_service.exception.NotificationNotFoundException;
import com.kb.notification_service.repository.NotificationRepository;
import com.kb.notification_service.service.impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private ModelMapper modelMapper;
    private NotificationService notificationService;

    @BeforeEach
    void setup() {
        notificationRepository = mock(NotificationRepository.class);
        modelMapper = new ModelMapper();
        notificationService = new NotificationServiceImpl(notificationRepository, modelMapper);
    }

    @Test
    void createNotification_ShouldSucceed_WhenValidRequest() {
        // Given
        NotificationRequest request = new NotificationRequest(
            1L, "Test Title", "Test Message", NotificationType.EMAIL, 
            "test@example.com", null, null
        );
        
        Notification savedNotification = new Notification();
        savedNotification.setId(1L);
        savedNotification.setStatus(NotificationStatus.PENDING);
        savedNotification.setCreatedAt(LocalDateTime.now());
        
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        // When
        NotificationResponse response = notificationService.createNotification(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(NotificationStatus.PENDING, response.getStatus());
        
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void getNotificationById_ShouldReturnNotification_WhenNotificationExists() {
        // Given
        Long notificationId = 1L;
        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setTitle("Test Title");
        notification.setStatus(NotificationStatus.PENDING);
        
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // When
        NotificationResponse response = notificationService.getNotificationById(notificationId);

        // Then
        assertNotNull(response);
        assertEquals(notificationId, response.getId());
        assertEquals("Test Title", response.getTitle());
    }

    @Test
    void getNotificationById_ShouldThrowException_WhenNotificationNotFound() {
        // Given
        Long notificationId = 999L;
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotificationById(notificationId));
    }

    @Test
    void updateNotificationStatus_ShouldSucceed_WhenNotificationExists() {
        // Given
        Long notificationId = 1L;
        NotificationStatus newStatus = NotificationStatus.SENT;
        
        Notification existingNotification = new Notification();
        existingNotification.setId(notificationId);
        existingNotification.setStatus(NotificationStatus.PENDING);
        
        Notification updatedNotification = new Notification();
        updatedNotification.setId(notificationId);
        updatedNotification.setStatus(newStatus);
        updatedNotification.setSentAt(LocalDateTime.now());
        
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(existingNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(updatedNotification);

        // When
        NotificationResponse response = notificationService.updateNotificationStatus(notificationId, newStatus);

        // Then
        assertNotNull(response);
        assertEquals(newStatus, response.getStatus());
        
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void markAsRead_ShouldUpdateStatusToRead() {
        // Given
        Long notificationId = 1L;
        Notification existingNotification = new Notification();
        existingNotification.setId(notificationId);
        existingNotification.setStatus(NotificationStatus.SENT);
        
        Notification updatedNotification = new Notification();
        updatedNotification.setId(notificationId);
        updatedNotification.setStatus(NotificationStatus.READ);
        updatedNotification.setReadAt(LocalDateTime.now());
        
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(existingNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(updatedNotification);

        // When
        NotificationResponse response = notificationService.markAsRead(notificationId);

        // Then
        assertNotNull(response);
        assertEquals(NotificationStatus.READ, response.getStatus());
    }

    @Test
    void getUnreadCountByUserId_ShouldReturnCorrectCount() {
        // Given
        Long userId = 1L;
        long expectedCount = 5L;
        when(notificationRepository.countByUserIdAndStatus(userId, NotificationStatus.PENDING)).thenReturn(expectedCount);

        // When
        long actualCount = notificationService.getUnreadCountByUserId(userId);

        // Then
        assertEquals(expectedCount, actualCount);
        verify(notificationRepository).countByUserIdAndStatus(userId, NotificationStatus.PENDING);
    }
} 