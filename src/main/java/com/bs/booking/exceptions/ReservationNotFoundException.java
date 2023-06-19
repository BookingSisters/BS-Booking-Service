package com.bs.booking.exceptions;

public class ReservationNotFoundException extends IllegalArgumentException {

    public static final String NOT_FOUND_MESSAGE_FORMAT = "존재하지 않는 예약 : %s";

    public ReservationNotFoundException(Long id) {
        super(String.format(NOT_FOUND_MESSAGE_FORMAT, id));
    }
}
