package com.umpisa.restaurant.reservationservice.model.entity.event;

import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event published when a reservation is updated.
 * This event is consumed by the notification service to send update notifications.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdatedEvent {

    private Long reservationId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private LocalDateTime newReservationDateTime;
    private Integer newNumberOfGuests;
    private NotificationChannel notificationChannel;
}
