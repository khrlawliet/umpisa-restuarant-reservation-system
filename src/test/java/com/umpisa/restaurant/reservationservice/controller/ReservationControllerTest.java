package com.umpisa.restaurant.reservationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umpisa.restaurant.reservationservice.dto.request.CreateReservationRequest;
import com.umpisa.restaurant.reservationservice.dto.request.UpdateReservationRequest;
import com.umpisa.restaurant.reservationservice.dto.response.ReservationResponse;
import com.umpisa.restaurant.reservationservice.entity.NotificationChannel;
import com.umpisa.restaurant.reservationservice.entity.ReservationStatus;
import com.umpisa.restaurant.reservationservice.service.ReservationService;
import com.umpisa.restaurant.shared.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    private CreateReservationRequest createRequest;
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
    void createReservation_ShouldReturnCreated() throws Exception {
        // Arrange
        when(reservationService.createReservation(any(CreateReservationRequest.class)))
                .thenReturn(reservationResponse);

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(reservationService).createReservation(any(CreateReservationRequest.class));
    }

    @Test
    void createReservation_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        createRequest.setEmail("invalid-email");

        // Act & Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(reservationService, never()).createReservation(any());
    }

    @Test
    void getUpcomingReservations_ShouldReturnList() throws Exception {
        // Arrange
        List<ReservationResponse> reservations = Arrays.asList(reservationResponse);
        when(reservationService.getUpcomingReservations("john@example.com"))
                .thenReturn(reservations);

        // Act & Assert
        mockMvc.perform(get("/api/reservations")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));

        verify(reservationService).getUpcomingReservations("john@example.com");
    }

    @Test
    void getReservationById_ShouldReturnReservation() throws Exception {
        // Arrange
        when(reservationService.getReservationById(1L)).thenReturn(reservationResponse);

        // Act & Assert
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerName").value("John Doe"));

        verify(reservationService).getReservationById(1L);
    }

    @Test
    void getReservationById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(reservationService.getReservationById(1L))
                .thenThrow(new ResourceNotFoundException("Reservation", 1L));

        // Act & Assert
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReservation_ShouldReturnUpdated() throws Exception {
        // Arrange
        UpdateReservationRequest updateRequest = UpdateReservationRequest.builder()
                                                                         .reservationDateTime(LocalDateTime.now().plusDays(2))
                                                                         .numberOfGuests(6)
                                                                         .build();

        when(reservationService.updateReservation(eq(1L), any(UpdateReservationRequest.class)))
                .thenReturn(reservationResponse);

        // Act & Assert
        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(reservationService).updateReservation(eq(1L), any(UpdateReservationRequest.class));
    }

    @Test
    void cancelReservation_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(reservationService).cancelReservation(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService).cancelReservation(1L);
    }
}
