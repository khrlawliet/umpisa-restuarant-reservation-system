package com.umpisa.restaurant.notificationservice.model;

import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import lombok.Builder;
import lombok.Data;

/**
 * Request object for sending notifications.
 * Encapsulates all parameters needed to send a notification via any channel.
 */
@Data
@Builder
public class NotificationRequest {
    private NotificationChannel channel;
    private String email;
    private String phoneNumber;
    private String subject;
    private String message;
}
