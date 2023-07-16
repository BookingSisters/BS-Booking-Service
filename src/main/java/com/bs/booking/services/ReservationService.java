package com.bs.booking.services;

import com.bs.booking.clients.PaymentServiceClient;
import com.bs.booking.dtos.PaymentCreateDto;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SessionSeatRepository sessionSeatRepository;
    private final ReservationMapper reservationMapper;
    private final PaymentServiceClient paymentServiceClient;

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getAllReservation() {
        log.info("Getting all reservations");

        List<Reservation> reservations = StreamSupport.stream(
            reservationRepository.findAll().spliterator(), false).toList();
        log.info("Returned {} reservations", reservations.size());

        return reservations.stream().map(reservationMapper::toReservationResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDto createReservation(long seatId,
        ReservationCreateDto valuesForCreate) {
        log.info("Creating reservation with values: {}", valuesForCreate);

        SessionSeat dbSessionSeat = sessionSeatRepository.findByIdWithLock(seatId)
            .orElseThrow(() -> {
                log.error("Error when creating reservation, seat not found with id: {}", seatId);
                return new SeatNotFoundException(seatId);
            });

        if (reservationRepository.existsBySessionSeatIdAndStatus(seatId,
            ReservationStatus.PENDING)) {
            log.error("Error when creating reservation, already in progress with seatId: {}",
                seatId);
            throw new ReservationInProgressException(seatId);
        }

        Reservation newReservation = new Reservation(dbSessionSeat, valuesForCreate.getUserId());
        reservationRepository.save(newReservation);

        PaymentCreateDto paymentCreateDto = new PaymentCreateDto(newReservation.getId(),
            valuesForCreate.getUserId());
        paymentServiceClient.createPayment(paymentCreateDto);

        log.info("Successfully created reservation");
        return reservationMapper.toReservationResponseDto(newReservation);
    }

    @Transactional
    public ReservationResponseDto updateStatus(long id, ReservationStatus statusForUpdate) {
        log.info("Updating reservation status with values: {}", statusForUpdate);

        Reservation dbReservation = reservationRepository.findById(id).orElseThrow(() -> {
            log.error("Error when updating reservation, reservation not found with id: {}", id);
            return new ReservationNotFoundException(id);
        });
        dbReservation.setStatus(statusForUpdate);
        return reservationMapper.toReservationResponseDto(dbReservation);
    }
}
