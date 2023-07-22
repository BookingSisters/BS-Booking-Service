package com.bs.booking.controllers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bs.booking.dtos.ReservationCreateDto;
import com.bs.booking.dtos.ReservationResponseDto;
import com.bs.booking.enums.ReservationStatus;
import com.bs.booking.services.ReservationService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    private String BASE_URL = "/reservations";

    @DisplayName("controller : 예약 목록 가져오기")
    @Test
    void getReservationList() throws Exception {
        // given
        ReservationStatus expectStatus = ReservationStatus.TIME_OUT;
        List<ReservationResponseDto> reservations = new ArrayList<>();
        ReservationResponseDto reservation = ReservationResponseDto.builder().status(expectStatus)
            .build();
        reservations.add(reservation);

        when(reservationService.getAllReservation()).thenReturn(reservations);
        // when, then
        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk())
            .andExpect(jsonPath("$.data", hasSize(1)))
            .andExpect(jsonPath("$.data[0].status", is(expectStatus.toString())));
    }

    @DisplayName("controller: 예약 생성")
    @Test
    void createReservation() throws Exception {
        // given
        ReservationStatus expectStatus = ReservationStatus.PENDING;
        String valToCreate = "{\"userId\":1}";
        ReservationResponseDto reservation = ReservationResponseDto.builder().status(expectStatus)
            .build();

        when(reservationService.createReservation(anyLong(),
            any(ReservationCreateDto.class))).thenReturn(reservation);
        // when, then
        mockMvc.perform(post(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valToCreate))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.status", is(expectStatus.toString())));
    }

    @DisplayName("controller: 예약 생성 requestBody 일부 값 누락 테스트")
    @Test
    void createReservation_whenNotEnoughRequestBody_shouldThrowsException() throws Exception {
        // given
        String valToCreate = "{}";

        // when, then
        mockMvc.perform(post(BASE_URL+"/1").contentType(MediaType.APPLICATION_JSON).content(valToCreate))
            .andExpect(status().isBadRequest());
    }

    @DisplayName("controller: 예약 상태값 수정 테스트")
    @Test
    void updateStatus() throws Exception {
        // given
        ReservationStatus expectStatus = ReservationStatus.CANCEL;
        String valToUpdate = "{\"status\":\"" + expectStatus.name() + "\"}";
        ReservationResponseDto reservation = ReservationResponseDto.builder().status(expectStatus)
            .build();
        when(reservationService.updateStatus(anyLong(), any(ReservationStatus.class))).thenReturn(
            reservation);
        // when, then
        mockMvc.perform(
                put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON).content(valToUpdate))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status", is(expectStatus.toString())));
    }

    @DisplayName("controller: 예약 상태값 수정: status값 누락 시 예외 핸들링 테스트")
    @Test
    void updateStatus_whenStatusIsNull_throwsProperException() throws Exception {
        // given
        String valToUpdate = "{\"noStatus\":\"fd\"}";

        // when, then
        mockMvc.perform(
                put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON).content(valToUpdate))
            .andExpect(status().isBadRequest()).andExpect(result -> assertTrue(
                result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @DisplayName("controller: 예약 상태값 수정: 잘못된 status 예외 핸들링 테스트")
    @Test
    void updateStatus_whenStatusIsIncorrect_throwsProperException() throws Exception {
        // given
        String valToUpdate = "{\"status\":\"fd\"}";

        // when, then
        mockMvc.perform(
                put(BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON).content(valToUpdate))
            .andExpect(status().isBadRequest()).andExpect(result -> assertTrue(
                result.getResolvedException() instanceof HttpMessageConversionException));
    }
}