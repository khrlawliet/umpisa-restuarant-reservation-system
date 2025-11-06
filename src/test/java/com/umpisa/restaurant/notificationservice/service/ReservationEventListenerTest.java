package com.umpisa.restaurant.notificationservice.service;

import com.umpisa.restaurant.reservationservice.entity.NotificationChannel;
import com.umpisa.restaurant.reservationservice.entity.event.ReservationCancelledEvent;
import com.umpisa.restaurant.reservationservice.entity.event.ReservationCreatedEvent;
import com.umpisa.restaurant.reservationservice.entity.event.ReservationUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationEventListenerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationTemplateService templateService;

    @InjectMocks
    private ReservationEventListener eventListener;

    private LocalDateTime futureDateTime;

    @BeforeEach
    void setUp() {
        futureDateTime = LocalDateTime.now().plusDays(1);
    }

    @Test
    void onReservationCreated_WithEmailChannel_ShouldSendEmail() {
        // Arrange
        ReservationCreatedEvent event = ReservationCreatedEvent.builder()
                .reservationId(1L)
                .customerName("John Doe")
                .email("john@example.com")
                .phoneNumber("+1234567890")
                .reservationDateTime(futureDateTime)
                .numberOfGuests(4)
                .notificationChannel(NotificationChannel.EMAIL)
                .build();

        when(templateService.buildReservationConfirmationMessage(anyString(), anyLong(), any(), anyInt()))
                .thenReturn("Confirmation message");
        when(templateService.buildConfirmationSubject(anyLong()))
                .thenReturn("Subject");

        // Act
        eventListener.onReservationCreated(event);

        // Assert
        verify(notificationService).sendEmail(eq("john@example.com"), eq("Subject"), anyString());
        verify(notificationService, never()).sendSms(anyString(), anyString());
    }

    @Test
    void onReservationCreated_WithSmsChannel_ShouldSendSms() {
        // Arrange
        ReservationCreatedEvent event = ReservationCreatedEvent.builder()
                .reservationId(1L)
                .customerName("John Doe")
                .email("john@example.com")
                .phoneNumber("+1234567890")
                .reservationDateTime(futureDateTime)
                .numberOfGuests(4)
                .notificationChannel(NotificationChannel.SMS)
                .build();

        when(templateService.buildReservationConfirmationMessage(anyString(), anyLong(), any(), anyInt()))
                .thenReturn("Confirmation message");

        // Act
        eventListener.onReservationCreated(event);

        // Assert
        verify(notificationService).sendSms(eq("+1234567890"), anyString());
        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void onReservationCreated_WithBothChannel_ShouldSendBoth() {
        // Arrange
        ReservationCreatedEvent event = ReservationCreatedEvent.builder()
                .reservationId(1L)
                .customerName("John Doe")
                .email("john@example.com")
                .phoneNumber("+1234567890")
                .reservationDateTime(futureDateTime)
                .numberOfGuests(4)
                .notificationChannel(NotificationChannel.BOTH)
                .build();

        when(templateService.buildReservationConfirmationMessage(anyString(), anyLong(), any(), anyInt()))
                .thenReturn("Confirmation message");
        when(templateService.buildConfirmationSubject(anyLong()))
                .thenReturn("Subject");

        // Act
        eventListener.onReservationCreated(event);

        // Assert
        verify(notificationService).sendEmail(eq("john@example.com"), eq("Subject"), anyString());
        verify(notificationService).sendSms(eq("+1234567890"), anyString());
    }

    @Test
    void onReservationCancelled_ShouldSendNotification() {
        // Arrange
        ReservationCancelledEvent event = ReservationCancelledEvent.builder()
                .reservationId(1L)
                .customerName("John Doe")
                .email("john@example.com")
                .phoneNumber("+1234567890")
                .notificationChannel(NotificationChannel.EMAIL)
                .build();

        when(templateService.buildCancellationMessage(anyString(), anyLong()))
                .thenReturn("Cancellation message");
        when(templateService.buildCancellationSubject(anyLong()))
                .thenReturn("Subject");

        // Act
        eventListener.onReservationCancelled(event);

        // Assert
        verify(notificationService).sendEmail(eq("john@example.com"), eq("Subject"), anyString());
    }

    @Test
    void onReservationUpdated_ShouldSendNotification() {
        // Arrange
        ReservationUpdatedEvent event = ReservationUpdatedEvent.builder()
                .reservationId(1L)
                .customerName("John Doe")
                .email("john@example.com")
                .phoneNumber("+1234567890")
                .newReservationDateTime(futureDateTime)
                .newNumberOfGuests(6)
                .notificationChannel(NotificationChannel.SMS)
                .build();

        when(templateService.buildUpdateMessage(anyString(), anyLong(), any(), anyInt()))
                .thenReturn("Update message");

        // Act
        eventListener.onReservationUpdated(event);

        // Assert
        verify(notificationService).sendSms(eq("+1234567890"), anyString());
    }
}
