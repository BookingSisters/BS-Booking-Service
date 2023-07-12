package com.bs.booking.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationCreateDto {

    @NotNull(message = "세션좌석 id는 필수입니다")
    private Long sessionSeatId;

    @NotNull(message = "사용자 id는 필수입니다")
    private String userId;
}
