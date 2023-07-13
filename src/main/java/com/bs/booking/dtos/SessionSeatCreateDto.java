package com.bs.booking.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionSeatCreateDto {

    @NotNull(message = "공연 id는 필수입니다")
    private Long performId;

    @NotNull(message = "공연 회차 id는 필수입니다")
    private Long sessionId;

    @NotNull(message = "좌석 등급 id는 필수입니다")
    private Long seatGradeId;
}
