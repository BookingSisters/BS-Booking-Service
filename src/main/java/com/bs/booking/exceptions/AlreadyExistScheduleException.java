package com.bs.booking.exceptions;

import com.bs.booking.dtos.SchedulerCreateDto;

public class AlreadyExistScheduleException extends IllegalArgumentException {

    public static final String EXIST_SCHEDULE_MESSAGE_FORMAT = "이미 존재하는 스케쥴입니다. 예매ID:%d 사용자ID:%s";

    public AlreadyExistScheduleException(SchedulerCreateDto createDto) {
        super(String.format(EXIST_SCHEDULE_MESSAGE_FORMAT,
            createDto.getReservationId(), createDto.getUserId()));
    }
}
