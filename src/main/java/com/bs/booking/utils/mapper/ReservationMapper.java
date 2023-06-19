package com.bs.booking.utils.mapper;

import com.bs.booking.dtos.ReservationResponseDto;
import com.bs.booking.models.Reservation;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservationMapper {

    public ReservationResponseDto toReservationResponseDto(Reservation reservation) {
        ReservationResponseDto dto = ReservationResponseDto.builder()
            .id(reservation.getId())
            .sessionSeatId(reservation.getSessionSeat().getId())
            .userId(reservation.getUserId())
            .status(reservation.getStatus())
            .createdAt(reservation.getCreatedAt())
            .build();
        return dto;
    }

    public List<ReservationResponseDto> toReservationResponseDtoList(
        List<Reservation> reservations) {
        if (reservations == null) {
            return Collections.emptyList();
        }

        return reservations.stream()
            .map(this::toReservationResponseDto)
            .collect(Collectors.toList());
    }
}
