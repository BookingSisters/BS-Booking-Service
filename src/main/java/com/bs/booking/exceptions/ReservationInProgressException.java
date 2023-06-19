package com.bs.booking.exceptions;

public class ReservationInProgressException extends IllegalArgumentException {

    public static final String IN_PROGRESS_MESSAGE_FORMAT = "이미 예약이 진행중인 세션좌석입니다 : %s";

    public ReservationInProgressException(long sessionSeatId) {
        super(String.format(IN_PROGRESS_MESSAGE_FORMAT, sessionSeatId));
    }
}
