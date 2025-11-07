package com.umpisa.restaurant.reservationservice.mapper;

import com.umpisa.restaurant.reservationservice.model.dto.request.CreateReservationRequest;
import com.umpisa.restaurant.reservationservice.model.entity.Reservation;
import com.umpisa.restaurant.reservationservice.model.entity.ReservationStatus;
import com.umpisa.restaurant.reservationservice.model.dto.response.ReservationResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between Reservation entities and DTOs.
 */
@Component
public class ReservationMapper {

    /**
     * Convert CreateReservationRequest to Reservation entity.
     *
     * @param request the request DTO
     * @return the Reservation entity
     */
    public Reservation toEntity(CreateReservationRequest request) {
        return Reservation.builder().customerName(request.getCustomerName())
                          .phoneNumber(request.getPhoneNumber())
                          .email(request.getEmail())
                          .reservationDateTime(request.getReservationDateTime())
                          .numberOfGuests(request.getNumberOfGuests())
                          .notificationChannel(request.getNotificationChannel())
                          .status(ReservationStatus.CONFIRMED)
                          .build();
    }

    /**
     * Convert Reservation entity to ReservationResponse DTO.
     *
     * @param reservation the Reservation entity
     * @return the response DTO
     */
    public ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                                  .id(reservation.getId())
                                  .customerName(reservation.getCustomerName())
                                  .phoneNumber(reservation.getPhoneNumber())
                                  .email(reservation.getEmail())
                                  .reservationDateTime(reservation.getReservationDateTime())
                                  .numberOfGuests(reservation.getNumberOfGuests())
                                  .status(reservation.getStatus())
                                  .notificationChannel(reservation.getNotificationChannel())
                                  .createdAt(reservation.getCreatedAt())
                                  .updatedAt(reservation.getUpdatedAt())
                                  .build();
    }
}
