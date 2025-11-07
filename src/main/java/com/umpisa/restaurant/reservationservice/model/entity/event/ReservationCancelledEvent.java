package com.umpisa.restaurant.reservationservice.model.entity.event;

import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event published when a reservation is cancelled.
 * This event is consumed by the notification service to send cancellation confirmation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCancelledEvent {

    private Long reservationId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private NotificationChannel notificationChannel;
}
