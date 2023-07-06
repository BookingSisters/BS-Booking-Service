package com.bs.booking.dtos;

import com.bs.booking.enums.ReservationStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {

    private Long id;
    private Long sessionSeatId;
    private Long userId;
    private ReservationStatus status;
    private LocalDateTime createdAt;
}
