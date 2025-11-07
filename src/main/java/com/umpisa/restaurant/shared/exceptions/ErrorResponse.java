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

    @Schema(description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code")
    private int status;

    @Schema(description = "HTTP status reason phrase")
    private String error;

    @Schema(description = "Detailed error message")
    private String message;

    @Schema(description = "Request path where the error occurred")
    private String path;

    @Schema(description = "Field-level validation errors (only present for 400 validation errors)")
    private Map<String, String> validationErrors;
}
