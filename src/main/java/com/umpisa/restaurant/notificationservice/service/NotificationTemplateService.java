package com.umpisa.restaurant.notificationservice.service;

import com.umpisa.restaurant.notificationservice.model.NotificationTemplateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for building notification message templates.
 * Templates are externalized in application.yml for easy maintenance.
 * Uses placeholder replacement for dynamic content.
 */
@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy 'at' hh:mm a");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    private final NotificationTemplateProperties templateProperties;

    /**
     * Build reservation confirmation message.
     *
     * @param customerName   the customer's name
     * @param reservationId  the reservation ID
     * @param dateTime       the reservation date and time
     * @param numberOfGuests the number of guests
     * @return the formatted confirmation message
     */
    public String buildReservationConfirmationMessage(String customerName,
                                                      Long reservationId,
                                                      LocalDateTime dateTime,
                                                      Integer numberOfGuests) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("customerName", customerName);
        placeholders.put("reservationId", String.valueOf(reservationId));
        placeholders.put("dateTime", dateTime.format(DATE_TIME_FORMATTER));
        placeholders.put("numberOfGuests", String.valueOf(numberOfGuests));

        return replacePlaceholders(templateProperties.getConfirmation().getBody(), placeholders);
    }

    /**
     * Build reservation confirmation email subject.
     *
     * @param reservationId the reservation ID
     * @return the email subject
     */
    public String buildConfirmationSubject(Long reservationId) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("reservationId", String.valueOf(reservationId));

        return replacePlaceholders(templateProperties.getConfirmation().getSubject(), placeholders);
    }

    /**
     * Build reservation cancellation message.
     *
     * @param customerName  the customer's name
     * @param reservationId the reservation ID
     * @return the formatted cancellation message
     */
    public String buildCancellationMessage(String customerName,
                                           Long reservationId) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("customerName", customerName);
        placeholders.put("reservationId", String.valueOf(reservationId));

        return replacePlaceholders(templateProperties.getCancellation().getBody(), placeholders);
    }

    /**
     * Build reservation cancellation email subject.
     *
     * @param reservationId the reservation ID
     * @return the email subject
     */
    public String buildCancellationSubject(Long reservationId) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("reservationId", String.valueOf(reservationId));

        return replacePlaceholders(templateProperties.getCancellation().getSubject(), placeholders);
    }

    /**
     * Build reservation update message.
     *
     * @param customerName      the customer's name
     * @param reservationId     the reservation ID
     * @param newDateTime       the new reservation date and time
     * @param newNumberOfGuests the new number of guests
     * @return the formatted update message
     */
    public String buildUpdateMessage(String customerName,
                                     Long reservationId,
                                     LocalDateTime newDateTime,
                                     Integer newNumberOfGuests) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("customerName", customerName);
        placeholders.put("reservationId", String.valueOf(reservationId));
        placeholders.put("dateTime", newDateTime.format(DATE_TIME_FORMATTER));
        placeholders.put("numberOfGuests", String.valueOf(newNumberOfGuests));

        return replacePlaceholders(templateProperties.getUpdate().getBody(), placeholders);
    }

    /**
     * Build reservation update email subject.
     *
     * @param reservationId the reservation ID
     * @return the email subject
     */
    public String buildUpdateSubject(Long reservationId) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("reservationId", String.valueOf(reservationId));

        return replacePlaceholders(templateProperties.getUpdate().getSubject(), placeholders);
    }

    /**
     * Build reservation reminder message (4 hours before reservation).
     *
     * @param customerName   the customer's name
     * @param dateTime       the reservation date and time
     * @param numberOfGuests the number of guests
     * @return the formatted reminder message
     */
    public String buildReminderMessage(String customerName,
                                       LocalDateTime dateTime,
                                       Integer numberOfGuests) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("customerName", customerName);
        placeholders.put("dateTime", dateTime.format(DATE_TIME_FORMATTER));
        placeholders.put("numberOfGuests", String.valueOf(numberOfGuests));

        return replacePlaceholders(templateProperties.getReminder().getBody(), placeholders);
    }

    /**
     * Build reservation reminder email subject.
     *
     * @param dateTime the reservation date and time
     * @return the email subject
     */
    public String buildReminderSubject(LocalDateTime dateTime) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("time", dateTime.format(TIME_FORMATTER));

        return replacePlaceholders(templateProperties.getReminder().getSubject(), placeholders);
    }

    /**
     * Replace placeholders in template with actual values.
     * Placeholders are in the format {placeholderName}.
     *
     * @param template     the template string with placeholders
     * @param placeholders map of placeholder names to values
     * @return the processed template with placeholders replaced
     */
    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        String result = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
