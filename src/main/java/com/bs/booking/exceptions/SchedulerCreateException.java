package com.bs.booking.exceptions;

import com.bs.booking.dtos.SchedulerCreateDto;

public class SchedulerCreateException extends IllegalArgumentException {

    public static final String MESSAGE_FORMAT = "이벤트 브릿지에서 스케쥴 생성에 실패했습니다. : 예매ID:%d 사용자ID:%s";

    public SchedulerCreateException(SchedulerCreateDto createDto) {
        super(String.format(MESSAGE_FORMAT, createDto.getReservationId(), createDto.getUserId()));
    }
}
