package com.bs.booking.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.dtos.SessionSeatResponseDto;
import com.bs.booking.models.SessionSeat;
import com.bs.booking.repositories.SessionSeatRepository;
import com.bs.booking.utils.mapper.SessionSeatMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SessionSeatServiceTest {

    @InjectMocks
    SessionSeatService sessionSeatService;

    @Mock
    SessionSeatRepository sessionSeatRepository;

    @Mock
    SessionSeatMapper sessionSeatMapper;

    @DisplayName("좌석 리스트 가져오기 테스트")
    @Test
    void getAllSeats() {
        // given
        List<SessionSeat> sessionSeatList = new ArrayList<>();
        sessionSeatList.add(new SessionSeat());
        when(sessionSeatRepository.findAllByIsDeletedIsFalse()).thenReturn(sessionSeatList);
        // when
        List<SessionSeatResponseDto> result = sessionSeatService.getAllSeats();
        // then
        assertThat(result).isNotNull().isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @DisplayName("좌석 생성 테스트")
    @Test
    void createSeat() {
        // given
        SessionSeatCreateDto valuesForCreate = new SessionSeatCreateDto();
        SessionSeat sessionSeat = new SessionSeat();
        SessionSeatResponseDto expectedSeat = new SessionSeatResponseDto();

        when(sessionSeatMapper.toSessionSeat(any(SessionSeatCreateDto.class))).thenReturn(
            sessionSeat);
        when(sessionSeatRepository.save(any(SessionSeat.class))).thenReturn(sessionSeat);
        when(sessionSeatMapper.toSessionSeatResponseDto(any(SessionSeat.class))).thenReturn(
            expectedSeat);
        // when
        SessionSeatResponseDto created = sessionSeatService.createSeat(valuesForCreate);
        // then
        assertThat(created).isNotNull().isEqualTo(expectedSeat);
        verify(sessionSeatRepository, times(1)).save(any(SessionSeat.class));
        verify(sessionSeatMapper, times(1)).toSessionSeatResponseDto(any(SessionSeat.class));
    }

    @DisplayName("좌석 삭제 soft delete 동작 테스트")
    @Test
    void deleteSeat() {
        // given
        SessionSeat sessionSeat = new SessionSeat();
        when(sessionSeatRepository.findById(any(Long.class))).thenReturn(Optional.of(sessionSeat));
        // when
        sessionSeatService.deleteSeat(1L);
        // then
        assertThat(sessionSeat.getDeletedAt()).isNotNull();
        assertThat(sessionSeat.isDeleted()).isTrue();
    }

    @DisplayName("좌석 삭제: id없음 예외 처리 테스트")
    @Test
    void deleteSeat_whenSeatNotFound_throwsProperException() {
        // given
        long incorrectId = 1L;
        when(sessionSeatRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // when, then
        assertThrows(IllegalArgumentException.class,
            () -> sessionSeatService.deleteSeat(incorrectId));
    }
}