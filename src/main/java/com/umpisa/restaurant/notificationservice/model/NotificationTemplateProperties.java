package com.umpisa.restaurant.notificationservice.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for notification message templates.
 */
@Data
@Component
@ConfigurationProperties(prefix = "notification.templates")
public class NotificationTemplateProperties {

    private TemplateConfig confirmation;
    private TemplateConfig cancellation;
    private TemplateConfig update;
    private TemplateConfig reminder;

    @Data
    public static class TemplateConfig {
        private String subject;
        private String body;
    }
}
