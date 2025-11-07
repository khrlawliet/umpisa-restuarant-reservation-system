package com.umpisa.restaurant.reservationservice.controller;

import com.umpisa.restaurant.reservationservice.model.dto.request.CreateReservationRequest;
import com.umpisa.restaurant.reservationservice.model.dto.response.ReservationResponse;
import com.umpisa.restaurant.reservationservice.model.dto.request.UpdateReservationRequest;
import com.umpisa.restaurant.reservationservice.service.ReservationService;
import com.umpisa.restaurant.shared.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for reservation operations.
 * Provides endpoints for creating, updating, cancelling, and retrieving reservations.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Restaurant reservation management APIs")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Create a new reservation.
     *
     * @param request the reservation details
     * @return the created reservation with HTTP 201 status
     */
    @Operation(
            summary = "Create a new reservation",
            description = "Creates a new restaurant reservation and sends a confirmation notification via the customer's preferred channel (EMAIL, SMS, or BOTH)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Reservation created successfully",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),

            @ApiResponse(responseCode = "400",
                    description = "Invalid request data or validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody CreateReservationRequest request) {

        log.info("POST /api/reservations - Creating reservation for: {}", request.getEmail());

        ReservationResponse response = reservationService.createReservation(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get all upcoming reservations for a customer.
     *
     * @param email the customer's email address
     * @return list of upcoming reservations
     */
    @Operation(
            summary = "Get upcoming reservations",
            description = "Retrieves all upcoming confirmed reservations for a customer by email address"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of upcoming reservations",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getUpcomingReservations(
            @Parameter(description = "Customer's email address", required = true)
            @RequestParam String email) {

        log.info("GET /api/reservations?email={} - Retrieving upcoming reservations", email);

        List<ReservationResponse> reservations = reservationService.getUpcomingReservations(email);

        return ResponseEntity.ok(reservations);
    }

    /**
     * Get a reservation by ID.
     *
     * @param id the reservation ID
     * @return the reservation details
     */
    @Operation(
            summary = "Get reservation by ID",
            description = "Retrieves a specific reservation by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservation found and returned successfully",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reservation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id) {

        log.info("GET /api/reservations/{} - Retrieving reservation", id);

        ReservationResponse response = reservationService.getReservationById(id);

        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing reservation.
     *
     * @param id      the reservation ID
     * @param request the updated reservation details
     * @return the updated reservation
     */
    @Operation(
            summary = "Update a reservation",
            description = "Updates the date/time and number of guests for an existing reservation. Sends an update notification to the customer."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reservation updated successfully",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or cannot update cancelled reservation",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reservation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationRequest request) {

        log.info("PUT /api/reservations/{} - Updating reservation", id);

        ReservationResponse response = reservationService.updateReservation(id, request);

        return ResponseEntity.ok(response);
    }

    /**
     * Cancel a reservation.
     *
     * @param id the reservation ID
     * @return HTTP 204 No Content status
     */
    @Operation(
            summary = "Cancel a reservation",
            description = "Cancels an existing reservation and sends a cancellation confirmation to the customer"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Reservation cancelled successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Reservation is already cancelled",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reservation not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @Parameter(description = "Reservation ID", required = true)
            @PathVariable Long id) {

        log.info("DELETE /api/reservations/{} - Cancelling reservation", id);

        reservationService.cancelReservation(id);

        return ResponseEntity.noContent().build();
    }
}
