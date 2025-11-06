package com.umpisa.restaurant.reservationservice.dto.response;

import com.umpisa.restaurant.reservationservice.entity.NotificationChannel;
import com.umpisa.restaurant.reservationservice.entity.ReservationStatus;
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

    @Schema(description = "Unique reservation identifier", example = "1")
    private Long id;

    @Schema(description = "Customer's full name", example = "John Doe")
    private String customerName;

    @Schema(description = "Customer's phone number", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Customer's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Reservation date and time", example = "2025-11-15T19:00:00")
    private LocalDateTime reservationDateTime;

    @Schema(description = "Number of guests", example = "4")
    private Integer numberOfGuests;

    @Schema(description = "Current reservation status", example = "CONFIRMED")
    private ReservationStatus status;

    @Schema(description = "Notification channel preference", example = "EMAIL")
    private NotificationChannel notificationChannel;

    @Schema(description = "Timestamp when reservation was created", example = "2025-11-06T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when reservation was last updated", example = "2025-11-06T10:30:00")
    private LocalDateTime updatedAt;
}
