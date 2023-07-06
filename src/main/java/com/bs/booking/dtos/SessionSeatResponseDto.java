package com.bs.booking.dtos;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionSeatResponseDto {

    private Long id;
    private Long performId;
    private Long sessionId;
    private Long seatGradeId;
    private List<ReservationResponseDto> reservations;
    private LocalDateTime createdAt;

}
