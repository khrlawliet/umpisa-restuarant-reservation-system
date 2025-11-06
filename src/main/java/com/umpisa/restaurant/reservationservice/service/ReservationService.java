package com.umpisa.restaurant.reservationservice.service;

import com.umpisa.restaurant.reservationservice.dto.request.CreateReservationRequest;
import com.umpisa.restaurant.reservationservice.dto.response.ReservationResponse;
import com.umpisa.restaurant.reservationservice.dto.request.UpdateReservationRequest;

import java.util.List;

/**
 * Service interface for reservation operations.
 * This defines the business operations for managing reservations.
 */
public interface ReservationService {

    /**
     * Create a new reservation.
     *
     * @param request the reservation details
     * @return the created reservation
     */
    ReservationResponse createReservation(CreateReservationRequest request);

    /**
     * Cancel a reservation by ID.
     *
     * @param id the reservation ID
     */
    void cancelReservation(Long id);

    /**
     * Update an existing reservation.
     *
     * @param id the reservation ID
     * @param request the updated reservation details
     * @return the updated reservation
     */
    ReservationResponse updateReservation(Long id, UpdateReservationRequest request);

    /**
     * Get all upcoming reservations for a customer.
     *
     * @param email the customer's email
     * @return list of upcoming reservations
     */
    List<ReservationResponse> getUpcomingReservations(String email);

    /**
     * Get a reservation by ID.
     *
     * @param id the reservation ID
     * @return the reservation
     */
    ReservationResponse getReservationById(Long id);
}
