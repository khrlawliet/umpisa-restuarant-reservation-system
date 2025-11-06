package com.umpisa.restaurant.notificationservice.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service for building notification message templates.
 * Provides methods to create formatted messages for different notification types.
 */
@Service
public class NotificationTemplateService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a");

    /**
     * Build reservation confirmation message.
     *
     * @param customerName the customer's name
     * @param reservationId the reservation ID
     * @param dateTime the reservation date and time
     * @param numberOfGuests the number of guests
     * @return the formatted confirmation message
     */
    public String buildReservationConfirmationMessage(
            String customerName,
            Long reservationId,
            LocalDateTime dateTime,
            Integer numberOfGuests) {

        return String.format(
                "Dear %s,\n\n" +
                "Your reservation has been confirmed!\n\n" +
                "Reservation ID: %d\n" +
                "Date & Time: %s\n" +
                "Number of Guests: %d\n\n" +
                "We look forward to serving you!\n\n" +
                "Best regards,\n" +
                "Restaurant Reservation System",
                customerName,
                reservationId,
                dateTime.format(DATE_TIME_FORMATTER),
                numberOfGuests
        );
    }

    /**
     * Build reservation confirmation email subject.
     *
     * @param reservationId the reservation ID
     * @return the email subject
     */
    public String buildConfirmationSubject(Long reservationId) {
        return String.format("Reservation Confirmed - ID #%d", reservationId);
    }

    /**
     * Build reservation cancellation message.
     *
     * @param customerName the customer's name
     * @param reservationId the reservation ID
     * @return the formatted cancellation message
     */
    public String buildCancellationMessage(String customerName, Long reservationId) {
        return String.format(
                "Dear %s,\n\n" +
                "Your reservation (ID: %d) has been cancelled.\n\n" +
                "If you did not request this cancellation, please contact us immediately.\n\n" +
                "Thank you,\n" +
                "Restaurant Reservation System",
                customerName,
                reservationId
        );
    }

    /**
     * Build reservation cancellation email subject.
     *
     * @param reservationId the reservation ID
     * @return the email subject
     */
    public String buildCancellationSubject(Long reservationId) {
        return String.format("Reservation Cancelled - ID #%d", reservationId);
    }

    /**
     * Build reservation update message.
     *
     * @param customerName the customer's name
     * @param reservationId the reservation ID
     * @param newDateTime the new reservation date and time
     * @param newNumberOfGuests the new number of guests
     * @return the formatted update message
     */
    public String buildUpdateMessage(
            String customerName,
            Long reservationId,
            LocalDateTime newDateTime,
            Integer newNumberOfGuests) {

        return String.format(
                "Dear %s,\n\n" +
                "Your reservation (ID: %d) has been updated.\n\n" +
                "New Date & Time: %s\n" +
                "New Number of Guests: %d\n\n" +
                "We look forward to serving you!\n\n" +
                "Best regards,\n" +
                "Restaurant Reservation System",
                customerName,
                reservationId,
                newDateTime.format(DATE_TIME_FORMATTER),
                newNumberOfGuests
        );
    }

    /**
     * Build reservation update email subject.
     *
     * @param reservationId the reservation ID
     * @return the email subject
     */
    public String buildUpdateSubject(Long reservationId) {
        return String.format("Reservation Updated - ID #%d", reservationId);
    }

    /**
     * Build reservation reminder message (4 hours before reservation).
     *
     * @param customerName the customer's name
     * @param dateTime the reservation date and time
     * @param numberOfGuests the number of guests
     * @return the formatted reminder message
     */
    public String buildReminderMessage(
            String customerName,
            LocalDateTime dateTime,
            Integer numberOfGuests) {

        return String.format(
                "Dear %s,\n\n" +
                "This is a friendly reminder about your upcoming reservation.\n\n" +
                "Date & Time: %s\n" +
                "Number of Guests: %d\n\n" +
                "Your table will be ready for you in approximately 4 hours.\n\n" +
                "If you need to make any changes or cancel, please contact us as soon as possible.\n\n" +
                "We look forward to serving you!\n\n" +
                "Best regards,\n" +
                "Restaurant Reservation System",
                customerName,
                dateTime.format(DATE_TIME_FORMATTER),
                numberOfGuests
        );
    }

    /**
     * Build reservation reminder email subject.
     *
     * @param dateTime the reservation date and time
     * @return the email subject
     */
    public String buildReminderSubject(LocalDateTime dateTime) {
        return String.format("Reminder: Your Reservation Today at %s",
                dateTime.format(DateTimeFormatter.ofPattern("hh:mm a")));
    }
}
