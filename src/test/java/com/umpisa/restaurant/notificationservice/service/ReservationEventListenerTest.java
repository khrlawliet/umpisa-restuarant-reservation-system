package com.umpisa.restaurant.notificationservice.service;

import com.umpisa.restaurant.notificationservice.model.NotificationRequest;
import com.umpisa.restaurant.notificationservice.service.event.ReservationEventListener;
import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationCancelledEvent;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationCreatedEvent;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationUpdatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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

        eventListener.onReservationCreated(event);

        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationService).sendNotification(requestCaptor.capture());

        NotificationRequest capturedRequest = requestCaptor.getValue();
        assertEquals(NotificationChannel.EMAIL, capturedRequest.getChannel());
        assertEquals("john@example.com", capturedRequest.getEmail());
        assertEquals("+1234567890", capturedRequest.getPhoneNumber());
        assertEquals("Subject", capturedRequest.getSubject());
        assertEquals("Confirmation message", capturedRequest.getMessage());
    }

    @Test
    void onReservationCreated_WithSmsChannel_ShouldSendSms() {
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
        when(templateService.buildConfirmationSubject(anyLong()))
                .thenReturn("Subject");

        eventListener.onReservationCreated(event);

        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationService).sendNotification(requestCaptor.capture());

        NotificationRequest capturedRequest = requestCaptor.getValue();
        assertEquals(NotificationChannel.SMS, capturedRequest.getChannel());
        assertEquals("john@example.com", capturedRequest.getEmail());
        assertEquals("+1234567890", capturedRequest.getPhoneNumber());
        assertEquals("Subject", capturedRequest.getSubject());
        assertEquals("Confirmation message", capturedRequest.getMessage());
    }

    @Test
    void onReservationCreated_WithBothChannel_ShouldSendBoth() {
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

        eventListener.onReservationCreated(event);

        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationService).sendNotification(requestCaptor.capture());

        NotificationRequest capturedRequest = requestCaptor.getValue();
        assertEquals(NotificationChannel.BOTH, capturedRequest.getChannel());
        assertEquals("john@example.com", capturedRequest.getEmail());
        assertEquals("+1234567890", capturedRequest.getPhoneNumber());
        assertEquals("Subject", capturedRequest.getSubject());
        assertEquals("Confirmation message", capturedRequest.getMessage());
    }

    @Test
    void onReservationCancelled_ShouldSendNotification() {
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

        eventListener.onReservationCancelled(event);

        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationService).sendNotification(requestCaptor.capture());

        NotificationRequest capturedRequest = requestCaptor.getValue();
        assertEquals(NotificationChannel.EMAIL, capturedRequest.getChannel());
        assertEquals("john@example.com", capturedRequest.getEmail());
        assertEquals("+1234567890", capturedRequest.getPhoneNumber());
        assertEquals("Subject", capturedRequest.getSubject());
        assertEquals("Cancellation message", capturedRequest.getMessage());
    }

    @Test
    void onReservationUpdated_ShouldSendNotification() {
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
        when(templateService.buildUpdateSubject(anyLong()))
                .thenReturn("Subject");

        eventListener.onReservationUpdated(event);

        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationService).sendNotification(requestCaptor.capture());

        NotificationRequest capturedRequest = requestCaptor.getValue();
        assertEquals(NotificationChannel.SMS, capturedRequest.getChannel());
        assertEquals("john@example.com", capturedRequest.getEmail());
        assertEquals("+1234567890", capturedRequest.getPhoneNumber());
        assertEquals("Subject", capturedRequest.getSubject());
        assertEquals("Update message", capturedRequest.getMessage());
    }
}
