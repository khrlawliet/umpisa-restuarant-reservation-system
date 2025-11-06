package com.umpisa.restaurant.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for sending notifications to customers.
 * This is a mock implementation that logs notification actions.
 * In a production environment, this would integrate with actual SMS and email services.
 */
@Slf4j
@Service
public class NotificationService {

    /**
     * Send SMS notification to a phone number.
     *
     * @param phoneNumber the recipient's phone number
     * @param message the SMS message content
     */
    public void sendSms(String phoneNumber, String message) {
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
    public void sendEmail(String email, String subject, String body) {
        log.info("=".repeat(80));
        log.info("SENT EMAIL");
        log.info("To: {}", email);
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
        log.info("=".repeat(80));

        System.out.println("Sent EMAIL to " + email);
    }
}
