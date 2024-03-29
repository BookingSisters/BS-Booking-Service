package com.bs.booking.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.bs.booking.clients.PaymentServiceClient;
import com.bs.booking.clients.SchedulerServiceClient;
import com.bs.booking.dtos.PaymentCreateDto;
import com.bs.booking.dtos.ReservationCreateDto;
import com.bs.booking.dtos.ReservationResponseDto;
import com.bs.booking.dtos.SchedulerCreateDto;
import com.bs.booking.dtos.common.ResponseDto;
import com.bs.booking.enums.ReservationStatus;
import com.bs.booking.models.Reservation;
import com.bs.booking.models.SessionSeat;
import com.bs.booking.repositories.ReservationRepository;
import com.bs.booking.repositories.SessionSeatRepository;
import com.bs.booking.utils.mapper.ReservationMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceMultiThreadedTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SessionSeatRepository sessionSeatRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @MockBean
    private PaymentServiceClient paymentServiceClient;

    @MockBean
    private SchedulerServiceClient schedulerServiceClient;


    private SessionSeat sessionSeat;

    @BeforeEach
    void setUp() {
        sessionSeat = new SessionSeat(1L, 1L, 1L);
        sessionSeatRepository.save(sessionSeat);
    }

    @DisplayName("하나의 좌석에 대한 예약 생성 다중 동시 요청 테스트")
    @Test
    void createReservation_whenMultiThreaded_shouldCreateOnlyOneReservation() {
        int threadCount = 10;
        ReservationCreateDto createDto = new ReservationCreateDto("testUser");

        when(paymentServiceClient.createPayment(any(PaymentCreateDto.class))).thenReturn(new ResponseDto());
        doNothing().when(schedulerServiceClient).createTimeOutSchedule(any(
            SchedulerCreateDto.class));

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<ReservationResponseDto>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(
                () -> reservationService.createReservation(sessionSeat.getId(), createDto)));
        }

        List<ReservationResponseDto> results = futures.stream().map(future -> {
            try {
                return future.get();
            } catch (ExecutionException | InterruptedException e) {
                return null;
            }
        }).collect(Collectors.toList());

        List<Reservation> dbReservations = reservationRepository.findAllByStatus(
            ReservationStatus.PENDING);
        if (dbReservations.size() != 0) {
            reservationRepository.deleteAll();
        }

        executor.shutdown();
        assertThat(results.stream().filter(Objects::nonNull).count()).isEqualTo(1);
    }
}
