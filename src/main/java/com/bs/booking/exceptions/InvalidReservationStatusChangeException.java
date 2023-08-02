package com.bs.booking.exceptions;

import com.bs.booking.enums.ReservationStatus;

public class InvalidReservationStatusChangeException extends IllegalArgumentException {

    public static final String MESSAGE_FORMAT = "예약 상태 변경이 불가능합니다. %s : 요청 상태값 - %s";

    public InvalidReservationStatusChangeException(String msg, ReservationStatus status) {
        super(String.format(MESSAGE_FORMAT, msg, status.name()));
    }
}
