package com.bs.booking.exceptions;

public class SeatNotFoundException extends IllegalArgumentException {

    public static final String NOT_FOUND_MESSAGE_FORMAT = "존재하지 않는 좌석 : %s";

    public SeatNotFoundException(Long id) {
        super(String.format(NOT_FOUND_MESSAGE_FORMAT, id));
    }
}
