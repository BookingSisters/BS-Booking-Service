package com.bs.booking.exceptions;

import com.bs.booking.dtos.common.ResponseDto;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseDto handleIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = e.getMessage();
        log.error(errorMessage, e);
        return ResponseDto.builder().status("failed").message(errorMessage).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String errorMessage = bindingResult.getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        log.error(errorMessage, e);
        return ResponseDto.builder().status("failed").message(errorMessage).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseDto handleHttpMessageNotReadableException(HttpMessageConversionException e) {
        String errorMessage = "잘못된 요청 값 입니다";
        log.error(errorMessage, e);
        return ResponseDto.builder().status("failed").message(errorMessage).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExistSeatException.class)
    public ResponseDto handleAlreadyExistSeatException(AlreadyExistSeatException e) {
        String errorMessage = e.getMessage();
        log.error(errorMessage, e);
        return ResponseDto.builder().status("failed").message(errorMessage).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseDto handleSeatNotFoundException(SeatNotFoundException e) {
        String errorMessage = e.getMessage();
        log.error(errorMessage, e);
        return ResponseDto.builder().status("failed").message(errorMessage).build();
    }
}
