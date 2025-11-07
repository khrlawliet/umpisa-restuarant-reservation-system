package com.umpisa.restaurant.reservationservice.model.entity.event;

import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event published when a new reservation is created.
 * This event is consumed by the notification service to send confirmation messages.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreatedEvent {

    private Long reservationId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private LocalDateTime reservationDateTime;
    private Integer numberOfGuests;
    private NotificationChannel notificationChannel;
}
