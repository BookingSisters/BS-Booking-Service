package com.bs.booking.services;

import com.bs.booking.dtos.ReservationCreateDto;
import com.bs.booking.dtos.ReservationResponseDto;
import com.bs.booking.enums.ReservationStatus;
import com.bs.booking.exceptions.ReservationInProgressException;
import com.bs.booking.exceptions.ReservationNotFoundException;
import com.bs.booking.exceptions.SeatNotFoundException;
import com.bs.booking.models.Reservation;
import com.bs.booking.models.SessionSeat;
import com.bs.booking.repositories.ReservationRepository;
import com.bs.booking.repositories.SessionSeatRepository;
import com.bs.booking.utils.mapper.ReservationMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SessionSeatRepository sessionSeatRepository;
    private final ReservationMapper reservationMapper;

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getAllReservation() {
        List<Reservation> reservations = StreamSupport.stream(
            reservationRepository.findAll().spliterator(), false).toList();

        return reservations.stream()
            .map(reservationMapper::toReservationResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDto createReservation(ReservationCreateDto valuesForCreate) {
        long seatId = valuesForCreate.getSessionSeatId();
        Optional<SessionSeat> optionalSeat = sessionSeatRepository.findByIdWithLock(seatId);
        SessionSeat dbSessionSeat = optionalSeat.orElseThrow(
            () -> new SeatNotFoundException(seatId));

        if (reservationRepository.findBySessionSeatIdAndStatus(valuesForCreate.getSessionSeatId(),
            ReservationStatus.PENDING).isPresent()) {
            throw new ReservationInProgressException(valuesForCreate.getSessionSeatId());
        }

        Reservation newReservation = new Reservation(dbSessionSeat, valuesForCreate);
        reservationRepository.save(newReservation);
        return reservationMapper.toReservationResponseDto(newReservation);
    }

    @Transactional
    public ReservationResponseDto updateStatus(long id, ReservationStatus statusForUpdate)
        throws IllegalArgumentException {
        Reservation dbReservation = reservationRepository.findById(id)
            .orElseThrow(() -> new ReservationNotFoundException(id));
        dbReservation.setStatus(statusForUpdate);
        return reservationMapper.toReservationResponseDto(dbReservation);
    }
}
