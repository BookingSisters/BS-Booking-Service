package com.bs.booking.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.bs.booking.models.SessionSeat;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class SessionSeatRepositoryTest {

    private SessionSeat sessionSeat1;
    private SessionSeat sessionSeat2;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private SessionSeatRepository sessionSeatRepository;

    @BeforeEach
    void setUp() {
        sessionSeat1 = new SessionSeat(1L, 1L, 1L);
        sessionSeat2 = new SessionSeat(2L, 1L, 1L);
        sessionSeat1.setDeletedAt();

        testEntityManager.persistAndFlush(sessionSeat1);
        testEntityManager.persistAndFlush(sessionSeat2);
    }

    @Test
    void findAllByDeletedAtIsNull() {
        // when
        List<SessionSeat> sessionSeatList = sessionSeatRepository.findAllByDeletedAtIsNull();
        // then
        assertThat(sessionSeatList).isNotNull().isNotEmpty();
        assertThat(sessionSeatList.size()).isEqualTo(1);
    }

    @Test
    void findByIdAndDeletedAtIsNull() {
        // given
        long expectedId = sessionSeat2.getId();
        // when
        Optional<SessionSeat> foundSeat = sessionSeatRepository.findByIdAndDeletedAtIsNull(
            expectedId);
        // then
        assertThat(foundSeat).isNotNull().isNotEmpty();
        assertThat(foundSeat.get().getId()).isEqualTo(expectedId);
    }
}