package com.bs.booking.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchedulerCreateDto {

    @NotNull(message = "예매 id는 필수입니다")
    private Long reservationId;

    @NotNull(message = "사용자 id는 필수입니다")
    private String userId;
}
