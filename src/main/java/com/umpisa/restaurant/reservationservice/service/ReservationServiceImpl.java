package com.umpisa.restaurant.reservationservice.service;

import com.umpisa.restaurant.reservationservice.mapper.ReservationMapper;
import com.umpisa.restaurant.reservationservice.model.dto.request.CreateReservationRequest;
import com.umpisa.restaurant.reservationservice.model.dto.request.UpdateReservationRequest;
import com.umpisa.restaurant.reservationservice.model.dto.response.ReservationResponse;
import com.umpisa.restaurant.reservationservice.model.entity.Reservation;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationCancelledEvent;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationCreatedEvent;
import com.umpisa.restaurant.reservationservice.model.entity.ReservationStatus;
import com.umpisa.restaurant.reservationservice.model.entity.event.ReservationUpdatedEvent;
import com.umpisa.restaurant.reservationservice.repository.ReservationRepository;
import com.umpisa.restaurant.shared.exceptions.InvalidReservationException;
import com.umpisa.restaurant.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ReservationService.
 * Handles all reservation business logic and publishes events for cross-module communication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        log.info("Creating reservation for customer: {}", request.getEmail());

        // Additional business validation
        validateReservationDateTime(request.getReservationDateTime());

        // Create and save reservation
        Reservation reservation = reservationMapper.toEntity(request);
        Reservation savedReservation = reservationRepository.save(reservation);

        log.info("Reservation created with ID: {}", savedReservation.getId());

        // Publish event for notification service
        ReservationCreatedEvent event = ReservationCreatedEvent.builder()
                                                               .reservationId(savedReservation.getId())
                                                               .customerName(savedReservation.getCustomerName())
                                                               .email(savedReservation.getEmail())
                                                               .phoneNumber(savedReservation.getPhoneNumber())
                                                               .reservationDateTime(savedReservation.getReservationDateTime())
                                                               .numberOfGuests(savedReservation.getNumberOfGuests())
                                                               .notificationChannel(savedReservation.getNotificationChannel())
                                                               .build();

        eventPublisher.publishEvent(event);
        log.debug("Published ReservationCreatedEvent for reservation ID: {}", savedReservation.getId());

        return reservationMapper.toResponse(savedReservation);
    }

    @Override
    @Transactional
    public void cancelReservation(Long id) {
        log.info("Cancelling reservation with ID: {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));

        // Validate that reservation is not already cancelled
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidReservationException("Reservation is already cancelled");
        }

        // Update status to cancelled
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        log.info("Reservation with ID: {} has been cancelled", id);

        // Publish event for notification service
        ReservationCancelledEvent event = ReservationCancelledEvent.builder()
                                                                   .reservationId(reservation.getId())
                                                                   .customerName(reservation.getCustomerName())
                                                                   .email(reservation.getEmail())
                                                                   .phoneNumber(reservation.getPhoneNumber())
                                                                   .notificationChannel(reservation.getNotificationChannel())
                                                                   .build();

        eventPublisher.publishEvent(event);
        log.debug("Published ReservationCancelledEvent for reservation ID: {}", id);
    }

    @Override
    @Transactional
    public ReservationResponse updateReservation(Long id, UpdateReservationRequest request) {
        log.info("Updating reservation with ID: {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));

        // Validate that reservation is not cancelled
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new InvalidReservationException("Cannot update a cancelled reservation");
        }

        // Additional business validation
        validateReservationDateTime(request.getReservationDateTime());

        // Update reservation details
        reservation.setReservationDateTime(request.getReservationDateTime());
        reservation.setNumberOfGuests(request.getNumberOfGuests());

        Reservation updatedReservation = reservationRepository.save(reservation);

        log.info("Reservation with ID: {} has been updated", id);

        // Publish event for notification service
        ReservationUpdatedEvent event = ReservationUpdatedEvent.builder()
                                                               .reservationId(updatedReservation.getId())
                                                               .customerName(updatedReservation.getCustomerName())
                                                               .email(updatedReservation.getEmail())
                                                               .phoneNumber(updatedReservation.getPhoneNumber())
                                                               .newReservationDateTime(updatedReservation.getReservationDateTime())
                                                               .newNumberOfGuests(updatedReservation.getNumberOfGuests())
                                                               .notificationChannel(updatedReservation.getNotificationChannel())
                                                               .build();

        eventPublisher.publishEvent(event);
        log.debug("Published ReservationUpdatedEvent for reservation ID: {}", id);

        return reservationMapper.toResponse(updatedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getUpcomingReservations(String email) {
        log.info("Retrieving upcoming reservations for customer: {}", email);

        List<Reservation> reservations = reservationRepository
                .findByEmailAndStatusAndReservationDateTimeAfter(
                        email,
                        ReservationStatus.CONFIRMED,
                        LocalDateTime.now()
                );

        log.info("Found {} upcoming reservations for customer: {}", reservations.size(), email);

        return reservations.stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Long id) {
        log.info("Retrieving reservation with ID: {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));

        return reservationMapper.toResponse(reservation);
    }

    /**
     * Validate that the reservation date/time is in the future.
     *
     * @param reservationDateTime the date/time to validate
     */
    private void validateReservationDateTime(LocalDateTime reservationDateTime) {
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException(
                    "Reservation date and time must be in the future"
            );
        }
    }
}
