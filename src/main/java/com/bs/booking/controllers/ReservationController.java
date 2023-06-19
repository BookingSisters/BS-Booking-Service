package com.bs.booking.controllers;

import com.bs.booking.dtos.ReservationResponseDto;
import com.bs.booking.dtos.ReservationUpdateDto;
import com.bs.booking.dtos.ReservationCreateDto;
import com.bs.booking.dtos.common.ResponseDto;
import com.bs.booking.services.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getReservationList() {
        List<ReservationResponseDto> reservations = reservationService.getAllReservation();
        return new ResponseDto("success", "Reservations retrieved successfully", reservations);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createReservation(
        @Valid @RequestBody ReservationCreateDto valuesForCreate) {
        ReservationResponseDto created = reservationService.createReservation(valuesForCreate);
        return new ResponseDto("success", "Reservation created successfully", created);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto updateStatus(@PathVariable long id,
        @Valid @RequestBody ReservationUpdateDto updateDTO) {
        ReservationResponseDto updated = reservationService.updateStatus(id, updateDTO.getStatus());
        return new ResponseDto("success", "Reservation status updated successfully", updated);
    }
}
