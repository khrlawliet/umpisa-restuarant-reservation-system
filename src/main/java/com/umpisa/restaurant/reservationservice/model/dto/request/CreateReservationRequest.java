package com.umpisa.restaurant.reservationservice.model.dto.request;

import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for creating a new reservation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a new restaurant reservation")
public class CreateReservationRequest {

    @Schema(description = "Customer's full name", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @Schema(description = "Customer's phone number (10-15 digits, optional + prefix)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Schema(description = "Customer's email address", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Reservation date and time (must be in the future)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Reservation date and time is required")
    @Future(message = "Reservation date and time must be in the future")
    private LocalDateTime reservationDateTime;

    @Schema(description = "Number of guests (1-50)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 50, message = "Number of guests cannot exceed 50")
    private Integer numberOfGuests;

    @Schema(description = "Preferred notification channel", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"EMAIL", "SMS", "BOTH"})
    @NotNull(message = "Notification channel is required")
    private NotificationChannel notificationChannel;
}
