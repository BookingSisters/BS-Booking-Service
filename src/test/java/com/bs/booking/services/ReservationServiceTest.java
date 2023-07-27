package com.bs.booking.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bs.booking.clients.PaymentServiceClient;
import com.bs.booking.clients.SchedulerServiceClient;
import com.bs.booking.dtos.PaymentCreateDto;
import com.bs.booking.dtos.ReservationCreateDto;
import com.bs.booking.dtos.ReservationResponseDto;
import com.bs.booking.dtos.SchedulerCreateDto;
import com.bs.booking.dtos.common.ResponseDto;
import com.bs.booking.enums.ReservationStatus;
import com.bs.booking.exceptions.InvalidReservationStatusChangeException;
import com.bs.booking.models.Reservation;
import com.bs.booking.models.SessionSeat;
import com.bs.booking.repositories.ReservationRepository;
import com.bs.booking.repositories.SessionSeatRepository;
import com.bs.booking.utils.mapper.ReservationMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    SessionSeatRepository sessionSeatRepository;

    @Mock
    ReservationMapper reservationMapper;

    @Mock
    PaymentServiceClient paymentServiceClient;

    @Mock
    SchedulerServiceClient schedulerServiceClient;

    @DisplayName("모든 예약정보 가져오기")
    @Test
    void getAllReservation() {
        // given
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        List<ReservationResponseDto> expectedReservations = Arrays.asList(
            new ReservationResponseDto(), new ReservationResponseDto());

        when(reservationRepository.findAll()).thenReturn(reservations);
        when(reservationMapper.toReservationResponseDto(any(Reservation.class)))
            .thenReturn(expectedReservations.get(0), expectedReservations.get(1));
        // when
        List<ReservationResponseDto> allReservations = reservationService.getAllReservation();
        // then
        assertThat(allReservations).isNotNull().isNotEmpty();
        assertThat(allReservations.size()).isEqualTo(expectedReservations.size());
        verify(reservationRepository, times(1)).findAll();
        verify(reservationMapper, times(2)).toReservationResponseDto(any(Reservation.class));
    }

    @DisplayName("예약 생성 테스트")
    @Test
    void createReservation() {
        // given
        ReservationCreateDto valuesForCreate = new ReservationCreateDto("testUser");
        SessionSeat sessionSeat = new SessionSeat();
        Reservation reservation = new Reservation(sessionSeat, valuesForCreate.getUserId());
        ReservationResponseDto expectedReservation = new ReservationResponseDto();

        when(sessionSeatRepository.findByIdWithLock(any(Long.class))).thenReturn(
            Optional.of(sessionSeat));
        when(reservationRepository.existsBySessionSeatIdAndStatus(any(Long.class),
            any(ReservationStatus.class)))
            .thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toReservationResponseDto(any(Reservation.class))).thenReturn(
            expectedReservation);
        when(paymentServiceClient.createPayment(any(PaymentCreateDto.class))).thenReturn(
            new ResponseDto());
        doNothing().when(schedulerServiceClient).createTimeOutSchedule(any(SchedulerCreateDto.class));

        // when
        ReservationResponseDto created = reservationService.createReservation(1L, valuesForCreate);
        // then
        assertThat(created).isNotNull().isEqualTo(expectedReservation);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(reservationMapper, times(1)).toReservationResponseDto(any(Reservation.class));
    }

    @DisplayName("예약 상태 수정 테스트")
    @Test
    void updateStatus() {
        // given
        Long id = 1L;
        Reservation reservation = new Reservation(new SessionSeat(),"testUser");
        ReservationStatus statusForUpdate = ReservationStatus.TIME_OUT;
        ReservationResponseDto expectedReservation = ReservationResponseDto.builder()
            .status(statusForUpdate).build();

        when(reservationRepository.findById(any(Long.class))).thenReturn(Optional.of(reservation));
        when(reservationMapper.toReservationResponseDto(any(Reservation.class))).thenReturn(
            expectedReservation);
        // when
        ReservationResponseDto updated = reservationService.updateStatus(id, statusForUpdate);
        // then
        assertThat(updated).isNotNull().isEqualTo(expectedReservation);
        assertThat(updated.getStatus()).isEqualTo(statusForUpdate);
        verify(reservationRepository, times(1)).findById(any(Long.class));
        verify(reservationMapper, times(1)).toReservationResponseDto(any(Reservation.class));
    }

    @DisplayName("예약 상태 수정: id없음 예외 처리 테스트")
    @Test
    void updateStatus_whenReservationNotFound_throwsProperException() {
        // given
        long incorrectId = 1L;
        ReservationStatus statusForUpdate = ReservationStatus.TIME_OUT;
        when(reservationRepository.findById(incorrectId)).thenReturn(Optional.empty());
        // when, then
        assertThrows(IllegalArgumentException.class,
            () -> reservationService.updateStatus(incorrectId, statusForUpdate));
    }

    @DisplayName("예약 상태 수정: 예약 상태가 PENDING이 아닌 경우 예외 처리 테스트")
    @Test
    void updateStatus_whenReservationStatusIsNotPending_throwsProperException() {
        // given
        long id = 1L;
        ReservationStatus statusForUpdate = ReservationStatus.COMPLETE;
        Reservation dbReservation = new Reservation(new SessionSeat(),"testUser");
        dbReservation.updateStatus(ReservationStatus.CANCEL);

        when(reservationRepository.findById(id)).thenReturn(Optional.of(dbReservation));
        // when, then
        assertThrows(InvalidReservationStatusChangeException.class,
            () -> reservationService.updateStatus(id, statusForUpdate));
    }
}