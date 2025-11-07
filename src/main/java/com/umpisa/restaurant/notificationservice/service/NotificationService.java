package com.umpisa.restaurant.notificationservice.service;

import com.umpisa.restaurant.notificationservice.model.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for sending notifications to customers.
 */
@Slf4j
@Service
public class NotificationService {

    /**
     * Send notification via the specified channel.
     * This method routes the notification to the appropriate channel(s) based on the customer's preference.
     *
     * @param request the notification request containing all necessary information
     */
    public void sendNotification(NotificationRequest request) {
        switch (request.getChannel()) {
            case EMAIL -> sendEmail(request.getEmail(), request.getSubject(), request.getMessage());
            case SMS -> sendSms(request.getPhoneNumber(), request.getMessage());
            case BOTH -> {
                sendEmail(request.getEmail(), request.getSubject(), request.getMessage());
                sendSms(request.getPhoneNumber(), request.getMessage());
            }
            default -> log.warn("Unknown notification channel: {}", request.getChannel());
        }
    }

    /**
     * Send SMS notification to a phone number.
     *
     * @param phoneNumber the recipient's phone number
     * @param message the SMS message content
     */
    private void sendSms(String phoneNumber, String message) {
        log.info("=".repeat(80));
        log.info("SENT SMS");
        log.info("To: {}", phoneNumber);
        log.info("Message: {}", message);
        log.info("=".repeat(80));

        System.out.println("Sent SMS to " + phoneNumber);
    }

    /**
     * Send email notification to an email address.
     *
     * @param email the recipient's email address
     * @param subject the email subject
     * @param body the email body content
     */
    private void sendEmail(String email, String subject, String body) {
        log.info("=".repeat(80));
        log.info("SENT EMAIL");
        log.info("To: {}", email);
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
        log.info("=".repeat(80));

        System.out.println("Sent EMAIL to " + email);
    }

}
