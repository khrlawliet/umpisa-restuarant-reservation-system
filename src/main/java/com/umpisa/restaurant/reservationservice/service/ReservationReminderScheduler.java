package com.umpisa.restaurant.reservationservice.service;

import com.umpisa.restaurant.reservationservice.entity.Reservation;
import com.umpisa.restaurant.reservationservice.entity.ReservationStatus;
import com.umpisa.restaurant.reservationservice.repository.ReservationRepository;
import com.umpisa.restaurant.notificationservice.service.NotificationService;
import com.umpisa.restaurant.notificationservice.service.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled service for sending reservation reminders.
 * Runs every 5 minutes to check for reservations that need reminders
 * (4 hours before the reservation time).
 */
@Slf4j
@Service
public class ReservationReminderScheduler {

    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;
    private final NotificationTemplateService notificationTemplateService;

    @Autowired
    public ReservationReminderScheduler(
            ReservationRepository reservationRepository,
            NotificationService notificationService,
            NotificationTemplateService notificationTemplateService) {
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
        this.notificationTemplateService = notificationTemplateService;
    }

    /**
     * Scheduled task that runs every 5 minutes to send reminders for reservations
     * that are 4 hours away.
     * Cron expression: "0 *\/5 * * * *" means:
     * - 0 seconds
     * - Every 5 minutes
     * - Every hour
     * - Every day
     * - Every month
     * - Every day of week
     */
    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void sendReservationReminders() {
        log.debug("Running reservation reminder scheduler");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fourHoursFromNow = now.plusHours(4);
        LocalDateTime reminderWindowEnd = fourHoursFromNow.plusMinutes(5);

        // Find reservations within the 5-minute window (4 hours to 4 hours 5 minutes from now)
        List<Reservation> reservationsNeedingReminders = reservationRepository
                .findByReservationDateTimeBetweenAndReminderSentAndStatus(
                        fourHoursFromNow,
                        reminderWindowEnd,
                        false,
                        ReservationStatus.CONFIRMED
                );

        if (reservationsNeedingReminders.isEmpty()) {
            log.debug("No reservations found needing reminders");
            return;
        }

        log.info("Found {} reservation(s) needing reminders", reservationsNeedingReminders.size());

        for (Reservation reservation : reservationsNeedingReminders) {
            try {
                sendReminder(reservation);
                reservation.setReminderSent(true);
                reservationRepository.save(reservation);

                log.info("Reminder sent successfully for reservation ID: {}", reservation.getId());
            } catch (Exception e) {
                log.error("Failed to send reminder for reservation ID: {}. Error: {}",
                        reservation.getId(), e.getMessage(), e);
            }
        }
    }

    /**
     * Sends a reminder notification for a specific reservation.
     *
     * @param reservation the reservation to send a reminder for
     */
    private void sendReminder(Reservation reservation) {
        String message = notificationTemplateService.buildReminderMessage(
                reservation.getCustomerName(),
                reservation.getReservationDateTime(),
                reservation.getNumberOfGuests()
        );

        String subject = notificationTemplateService.buildReminderSubject(
                reservation.getReservationDateTime()
        );

        log.debug("Sending reminder for reservation ID: {} to customer: {}",
                reservation.getId(), reservation.getEmail());

        // Send notification based on customer's preferred channel
        switch (reservation.getNotificationChannel()) {
            case EMAIL -> notificationService.sendEmail(reservation.getEmail(), subject, message);
            case SMS -> notificationService.sendSms(reservation.getPhoneNumber(), message);
            case BOTH -> {
                notificationService.sendEmail(reservation.getEmail(), subject, message);
                notificationService.sendSms(reservation.getPhoneNumber(), message);
            }
        }
    }
}
