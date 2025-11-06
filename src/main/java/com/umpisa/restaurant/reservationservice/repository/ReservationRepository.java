package com.umpisa.restaurant.reservationservice.repository;

import com.umpisa.restaurant.reservationservice.entity.Reservation;
import com.umpisa.restaurant.reservationservice.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Reservation entity.
 * Provides data access operations for reservations.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Find all reservations by customer email.
     *
     * @param email the customer's email address
     * @return list of reservations for the customer
     */
    List<Reservation> findByEmail(String email);

    /**
     * Find all upcoming confirmed reservations after a specific date/time.
     *
     * @param status the reservation status
     * @param dateTime the cutoff date/time
     * @return list of upcoming confirmed reservations
     */
    List<Reservation> findByStatusAndReservationDateTimeAfter(
            ReservationStatus status, LocalDateTime dateTime);

    /**
     * Find all upcoming reservations for a customer (confirmed only).
     *
     * @param email the customer's email
     * @param status the reservation status
     * @param dateTime the cutoff date/time
     * @return list of upcoming reservations for the customer
     */
    List<Reservation> findByEmailAndStatusAndReservationDateTimeAfter(
            String email, ReservationStatus status, LocalDateTime dateTime);

    /**
     * Find all confirmed reservations within a time window that haven't received a reminder yet.
     * Used by the reminder scheduler to find reservations 4 hours before their scheduled time.
     *
     * @param startDateTime start of the time window
     * @param endDateTime end of the time window
     * @param reminderSent whether reminder was already sent
     * @param status the reservation status
     * @return list of reservations needing reminders
     */
    List<Reservation> findByReservationDateTimeBetweenAndReminderSentAndStatus(
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            Boolean reminderSent, ReservationStatus status);
}
