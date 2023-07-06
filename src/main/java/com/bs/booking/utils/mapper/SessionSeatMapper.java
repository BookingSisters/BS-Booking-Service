package com.bs.booking.utils.mapper;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.dtos.SessionSeatResponseDto;
import com.bs.booking.models.SessionSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SessionSeatMapper {

    private final ReservationMapper reservationMapper;

    public SessionSeatResponseDto toSessionSeatResponseDto(SessionSeat sessionSeat) {
        return SessionSeatResponseDto.builder()
            .id(sessionSeat.getId())
            .performId(sessionSeat.getPerformId())
            .sessionId(sessionSeat.getSessionId())
            .seatGradeId(sessionSeat.getSeatGradeId())
            .createdAt(sessionSeat.getCreatedAt())
            .reservations(
                reservationMapper.toReservationResponseDtoList(sessionSeat.getReservations()))
            .build();
    }

    public SessionSeat toSessionSeat(SessionSeatCreateDto valuesForCreate) {
        return new SessionSeat(valuesForCreate.getPerformId(), valuesForCreate.getSessionId(),
            valuesForCreate.getSeatGradeId());
    }
}