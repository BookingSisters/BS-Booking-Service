package com.bs.booking.controllers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.dtos.SessionSeatResponseDto;
import com.bs.booking.services.SessionSeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SessionSeatController.class)
class SessionSeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionSeatService sessionSeatService;

    @DisplayName("controller: 좌석 목록 가져오기")
    @Test
    void getSeatList() throws Exception {
        // given
        List<SessionSeatResponseDto> seats = new ArrayList<>();
        SessionSeatResponseDto seat = SessionSeatResponseDto.builder().build();
        seats.add(seat);

        when(sessionSeatService.getAllSeats()).thenReturn(seats);
        // when, then
        mockMvc.perform(get("/seat"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @DisplayName("controller: 좌석 생성 테스트")
    @Test
    void createSeat() throws Exception {
        // given
        String valToCreate =
            "{\"performId\":1,\"sessionId\":1,\"seatGradeId\":1}";
        SessionSeatResponseDto seat = SessionSeatResponseDto.builder().build();

        when(sessionSeatService.createSeat(any(SessionSeatCreateDto.class))).thenReturn(seat);
        // when, then
        mockMvc.perform(post("/seat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valToCreate))
            .andExpect(status().isCreated());
    }

    @DisplayName("controller: 좌석 생성 requestBody 일부 값 누락 테스트")
    @Test
    void createSeat_whenNotEnoughRequestBody_shouldThrowsException() throws Exception {
        // given
        SessionSeatCreateDto valToCreate = new SessionSeatCreateDto(null, null, null);

        // when, then
        mockMvc.perform(post("/seat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(valToCreate)))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(
                result.getResolvedException() instanceof MethodArgumentNotValidException))
            .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }


    @DisplayName("controller: 좌석 삭제 테스트")
    @Test
    void deleteSeat() throws Exception {
        doNothing().when(sessionSeatService).deleteSeat(1L);

        mockMvc.perform(delete("/seat/1"))
            .andExpect(status().isOk());
    }
}