package com.bs.booking.exceptions;

public class PaymentServiceResponseException extends IllegalArgumentException {

    public static final String MESSAGE_FORMAT = "결제 서비스로부터 예기치 않은 응답 발생 : %s";

    public PaymentServiceResponseException(String msg) {
        super(String.format(MESSAGE_FORMAT, msg));
    }
}
