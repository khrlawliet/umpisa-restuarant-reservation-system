package com.umpisa.restaurant.shared.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response structure for all API errors.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response containing details about the error that occurred")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2025-11-06T10:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "HTTP status reason phrase", example = "Not Found")
    private String error;

    @Schema(description = "Detailed error message", example = "Reservation with ID 123 not found")
    private String message;

    @Schema(description = "Request path where the error occurred", example = "/api/reservations/123")
    private String path;

    @Schema(description = "Field-level validation errors (only present for 400 validation errors)")
    private Map<String, String> validationErrors;
}
