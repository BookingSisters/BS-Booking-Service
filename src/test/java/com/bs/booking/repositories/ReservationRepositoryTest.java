package com.bs.booking.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.bs.booking.configs.QuerydslConfig;
import com.bs.booking.dtos.ReservationCreateDto;
import com.bs.booking.enums.ReservationStatus;
import com.bs.booking.models.Reservation;
import com.bs.booking.models.SessionSeat;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import({QuerydslConfig.class})
class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    private SessionSeat sessionSeat;

    @BeforeEach
    void setUp() {
        sessionSeat = new SessionSeat(1L, 1L, 1L);
        testEntityManager.persistAndFlush(sessionSeat);
        ReservationCreateDto createDto = new ReservationCreateDto("testUser");
        Reservation reservation1 = new Reservation(sessionSeat, createDto.getUserId());
        Reservation reservation2 = new Reservation(sessionSeat, createDto.getUserId());
        Reservation reservation3 = new Reservation(sessionSeat, createDto.getUserId());
        reservation2.setStatus(ReservationStatus.CANCEL);
        reservation3.setStatus(ReservationStatus.TIME_OUT);
        testEntityManager.persistAndFlush(reservation1);
        testEntityManager.persistAndFlush(reservation2);
        testEntityManager.persistAndFlush(reservation3);
    }

    @DisplayName("특정 상태값을 가진 모든 예매 불러오기")
    @Test
    void findAllByStatus() {
        // when
        List<Reservation> results = reservationRepository.findAllByStatus(
            ReservationStatus.PENDING);
        // then
        assertThat(results).isNotNull().isNotEmpty();
        assertThat(results.size()).isEqualTo(1);
    }

    @DisplayName("특정 상태값을 가진 특정 세션좌석의 예매 존재여부 확인")
    @Test
    void existsBySessionSeatIdAndStatus() {
        // when
        boolean result = reservationRepository.existsBySessionSeatIdAndStatus(
            sessionSeat.getId(), ReservationStatus.PENDING);
        // then
        assertThat(result).isNotNull();
        assertThat(result).isTrue();
    }
}