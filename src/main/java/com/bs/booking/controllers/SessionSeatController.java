package com.bs.booking.controllers;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.dtos.SessionSeatResponseDto;
import com.bs.booking.dtos.common.ResponseDto;
import com.bs.booking.services.SessionSeatService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/seat")
public class SessionSeatController {

    private final SessionSeatService sessionSeatService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getSeatList() {
        List<SessionSeatResponseDto> seatList = sessionSeatService.getAllSeats();
        return new ResponseDto("success", "SessionSeats retrieved successfully", seatList);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createSeat(
        @Valid @RequestBody SessionSeatCreateDto valuesForCreate) {
        SessionSeatResponseDto created = sessionSeatService.createSeat(valuesForCreate);
        return new ResponseDto("success", "SessionSeats created successfully", created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteSeat(@PathVariable long id) {
        sessionSeatService.deleteSeat(id);
        return new ResponseDto("success", "SessionSeats deleted successfully", null);
    }
}
