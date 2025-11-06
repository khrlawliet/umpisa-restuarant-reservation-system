package com.umpisa.restaurant.notificationservice.service;

import com.umpisa.restaurant.reservationservice.entity.NotificationChannel;
import com.umpisa.restaurant.reservationservice.entity.event.ReservationCancelledEvent;
import com.umpisa.restaurant.reservationservice.entity.event.ReservationCreatedEvent;
import com.umpisa.restaurant.reservationservice.entity.event.ReservationUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event listener for reservation-related events.
 * Listens to events published by the Reservation service and sends appropriate notifications.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final NotificationService notificationService;
    private final NotificationTemplateService templateService;

    /**
     * Handle reservation created event.
     * Sends confirmation notification to the customer via their preferred channel.
     *
     * @param event the reservation created event
     */
    @EventListener
    public void onReservationCreated(ReservationCreatedEvent event) {
        log.info("Received ReservationCreatedEvent for reservation ID: {}", event.getReservationId());

        String message = templateService.buildReservationConfirmationMessage(
                event.getCustomerName(),
                event.getReservationId(),
                event.getReservationDateTime(),
                event.getNumberOfGuests()
        );

        String subject = templateService.buildConfirmationSubject(event.getReservationId());

        sendNotification(
                event.getNotificationChannel(),
                event.getEmail(),
                event.getPhoneNumber(),
                subject,
                message
        );

        log.info("Sent confirmation notification for reservation ID: {}", event.getReservationId());
    }

    /**
     * Handle reservation cancelled event.
     * Sends cancellation confirmation to the customer via their preferred channel.
     *
     * @param event the reservation cancelled event
     */
    @EventListener
    public void onReservationCancelled(ReservationCancelledEvent event) {
        log.info("Received ReservationCancelledEvent for reservation ID: {}", event.getReservationId());

        String message = templateService.buildCancellationMessage(
                event.getCustomerName(),
                event.getReservationId()
        );

        String subject = templateService.buildCancellationSubject(event.getReservationId());

        sendNotification(
                event.getNotificationChannel(),
                event.getEmail(),
                event.getPhoneNumber(),
                subject,
                message
        );

        log.info("Sent cancellation notification for reservation ID: {}", event.getReservationId());
    }

    /**
     * Handle reservation updated event.
     * Sends update notification to the customer via their preferred channel.
     *
     * @param event the reservation updated event
     */
    @EventListener
    public void onReservationUpdated(ReservationUpdatedEvent event) {
        log.info("Received ReservationUpdatedEvent for reservation ID: {}", event.getReservationId());

        String message = templateService.buildUpdateMessage(
                event.getCustomerName(),
                event.getReservationId(),
                event.getNewReservationDateTime(),
                event.getNewNumberOfGuests()
        );

        String subject = templateService.buildUpdateSubject(event.getReservationId());

        sendNotification(
                event.getNotificationChannel(),
                event.getEmail(),
                event.getPhoneNumber(),
                subject,
                message
        );

        log.info("Sent update notification for reservation ID: {}", event.getReservationId());
    }

    /**
     * Send notification via the specified channel.
     *
     * @param channel the notification channel
     * @param email the customer's email
     * @param phoneNumber the customer's phone number
     * @param subject the notification subject (for email)
     * @param message the notification message
     */
    private void sendNotification(
            NotificationChannel channel,
            String email,
            String phoneNumber,
            String subject,
            String message) {

        switch (channel) {
            case EMAIL:
                notificationService.sendEmail(email, subject, message);
                break;
            case SMS:
                notificationService.sendSms(phoneNumber, message);
                break;
            case BOTH:
                notificationService.sendEmail(email, subject, message);
                notificationService.sendSms(phoneNumber, message);
                break;
            default:
                log.warn("Unknown notification channel: {}", channel);
        }
    }
}
