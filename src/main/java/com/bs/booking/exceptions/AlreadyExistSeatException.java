package com.bs.booking.exceptions;

import com.bs.booking.dtos.SessionSeatCreateDto;

public class AlreadyExistSeatException extends IllegalArgumentException {

    public static final String EXIST_SEAT_MESSAGE_FORMAT = "이미 존재하는 좌석입니다. 공연:%d 회차:%d 좌석등급:%d";

    public AlreadyExistSeatException(SessionSeatCreateDto createDto) {
        super(String.format(EXIST_SEAT_MESSAGE_FORMAT,
            createDto.getPerformId(), createDto.getSessionId(), createDto.getSeatGradeId()));
    }
}
