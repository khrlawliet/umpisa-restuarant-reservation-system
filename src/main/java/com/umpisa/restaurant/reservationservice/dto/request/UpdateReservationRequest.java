package com.umpisa.restaurant.reservationservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for updating an existing reservation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for updating an existing reservation")
public class UpdateReservationRequest {

    @Schema(description = "New reservation date and time (must be in the future)", example = "2025-11-16T20:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Reservation date and time is required")
    @Future(message = "Reservation date and time must be in the future")
    private LocalDateTime reservationDateTime;

    @Schema(description = "New number of guests (1-50)", example = "6", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 50, message = "Number of guests cannot exceed 50")
    private Integer numberOfGuests;
}
