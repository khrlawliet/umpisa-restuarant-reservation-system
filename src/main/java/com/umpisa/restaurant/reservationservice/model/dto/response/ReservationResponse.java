package com.umpisa.restaurant.reservationservice.model.dto.response;

import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import com.umpisa.restaurant.reservationservice.model.entity.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for reservation data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Restaurant reservation details")
public class ReservationResponse {

    @Schema(description = "Unique reservation identifier")
    private Long id;

    @Schema(description = "Customer's full name")
    private String customerName;

    @Schema(description = "Customer's phone number")
    private String phoneNumber;

    @Schema(description = "Customer's email address")
    private String email;

    @Schema(description = "Reservation date and time")
    private LocalDateTime reservationDateTime;

    @Schema(description = "Number of guests")
    private Integer numberOfGuests;

    @Schema(description = "Current reservation status")
    private ReservationStatus status;

    @Schema(description = "Notification channel preference")
    private NotificationChannel notificationChannel;

    @Schema(description = "Timestamp when reservation was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when reservation was last updated")
    private LocalDateTime updatedAt;
}
