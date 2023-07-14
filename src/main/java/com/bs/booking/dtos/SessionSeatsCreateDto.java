package com.bs.booking.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SessionSeatsCreateDto {

    @Valid
    @NotNull(message = "생성 data는 필수입니다")
    List<SessionSeatCreateDto> data;
}
