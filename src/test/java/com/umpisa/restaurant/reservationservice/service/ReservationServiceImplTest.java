package com.umpisa.restaurant.reservationservice.service;

import com.umpisa.restaurant.reservationservice.mapper.ReservationMapper;
import com.umpisa.restaurant.reservationservice.model.dto.request.CreateReservationRequest;
import com.umpisa.restaurant.reservationservice.model.dto.request.UpdateReservationRequest;
import com.umpisa.restaurant.reservationservice.model.dto.response.ReservationResponse;
import com.umpisa.restaurant.reservationservice.model.entity.NotificationChannel;
import com.umpisa.restaurant.reservationservice.model.entity.Reservation;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationCancelledEvent;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationCreatedEvent;
import com.umpisa.restaurant.reservationservice.model.entity.ReservationStatus;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationUpdatedEvent;
import com.umpisa.restaurant.reservationservice.repository.ReservationRepository;
import com.umpisa.restaurant.shared.exceptions.InvalidReservationException;
import com.umpisa.restaurant.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private CreateReservationRequest createRequest;
    private Reservation reservation;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void setUp() {
        LocalDateTime futureDateTime = LocalDateTime.now().plusDays(1);

        createRequest = CreateReservationRequest.builder()
                                                .customerName("John Doe")
                                                .phoneNumber("+1234567890")
                                                .email("john@example.com")
                                                .reservationDateTime(futureDateTime)
                                                .numberOfGuests(4)
                                                .notificationChannel(NotificationChannel.EMAIL)
                                                .build();

        reservation = Reservation.builder()
                                 .id(1L)
                                 .customerName("John Doe")
                                 .phoneNumber("+1234567890")
                                 .email("john@example.com")
                                 .reservationDateTime(futureDateTime)
                                 .numberOfGuests(4)
                                 .status(ReservationStatus.CONFIRMED)
                                 .notificationChannel(NotificationChannel.EMAIL)
                                 .createdAt(LocalDateTime.now())
                                 .updatedAt(LocalDateTime.now())
                                 .build();

        reservationResponse = ReservationResponse.builder()
                                                 .id(1L)
                                                 .customerName("John Doe")
                                                 .phoneNumber("+1234567890")
                                                 .email("john@example.com")
                                                 .reservationDateTime(futureDateTime)
                                                 .numberOfGuests(4)
                                                 .status(ReservationStatus.CONFIRMED)
                                                 .notificationChannel(NotificationChannel.EMAIL)
                                                 .build();
    }

    @Test
    void createReservation_ShouldCreateAndPublishEvent() {
        when(reservationMapper.toEntity(createRequest)).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.createReservation(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo("John Doe");

        verify(reservationRepository).save(any(Reservation.class));

        ArgumentCaptor<ReservationCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(ReservationCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        ReservationCreatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getReservationId()).isEqualTo(1L);
        assertThat(capturedEvent.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void createReservation_WithPastDateTime_ShouldThrowException() {
        createRequest.setReservationDateTime(LocalDateTime.now().minusDays(1));

        assertThatThrownBy(() -> reservationService.createReservation(createRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("must be in the future");

        verify(reservationRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void cancelReservation_ShouldUpdateStatusAndPublishEvent() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        reservationService.cancelReservation(1L);

        verify(reservationRepository).save(any(Reservation.class));

        ArgumentCaptor<ReservationCancelledEvent> eventCaptor =
                ArgumentCaptor.forClass(ReservationCancelledEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        ReservationCancelledEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getReservationId()).isEqualTo(1L);
    }

    @Test
    void cancelReservation_WhenNotFound_ShouldThrowException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancelReservation(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Reservation with ID 1 not found");

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void cancelReservation_WhenAlreadyCancelled_ShouldThrowException() {
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.cancelReservation(1L))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("already cancelled");

        verify(reservationRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void updateReservation_ShouldUpdateAndPublishEvent() {
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(2);
        UpdateReservationRequest updateRequest = UpdateReservationRequest.builder()
                                                                         .reservationDateTime(newDateTime)
                                                                         .numberOfGuests(6)
                                                                         .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.updateReservation(1L, updateRequest);

        assertThat(result).isNotNull();
        verify(reservationRepository).save(any(Reservation.class));

        ArgumentCaptor<ReservationUpdatedEvent> eventCaptor =
                ArgumentCaptor.forClass(ReservationUpdatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        ReservationUpdatedEvent capturedEvent = eventCaptor.getValue();
        assertThat(capturedEvent.getReservationId()).isEqualTo(1L);
    }

    @Test
    void updateReservation_WhenCancelled_ShouldThrowException() {
        reservation.setStatus(ReservationStatus.CANCELLED);
        UpdateReservationRequest updateRequest = UpdateReservationRequest.builder()
                                                                         .reservationDateTime(LocalDateTime.now().plusDays(2))
                                                                         .numberOfGuests(6)
                                                                         .build();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.updateReservation(1L, updateRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessageContaining("Cannot update a cancelled reservation");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void getUpcomingReservations_ShouldReturnFilteredList() {
        String email = "john@example.com";
        List<Reservation> reservations = Collections.singletonList(reservation);

        when(reservationRepository.findByEmailAndStatusAndReservationDateTimeAfter(
                eq(email), eq(ReservationStatus.CONFIRMED), any(LocalDateTime.class)))
                .thenReturn(reservations);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);

        List<ReservationResponse> result = reservationService.getUpcomingReservations(email);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo(email);
    }

    @Test
    void getReservationById_ShouldReturnReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toResponse(reservation)).thenReturn(reservationResponse);

        ReservationResponse result = reservationService.getReservationById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getReservationById_WhenNotFound_ShouldThrowException() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getReservationById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
