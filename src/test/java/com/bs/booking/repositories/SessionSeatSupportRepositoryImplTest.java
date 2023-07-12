package com.bs.booking.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.models.SessionSeat;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class SessionSeatSupportRepositoryImplTest {

    @Autowired
    private SessionSeatSupportRepositoryImpl sessionSeatSupportRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("createDto로 sessionSeat 존재 여부 확인")
    @Test
    @Transactional
    public void exists() {
        // given
        SessionSeatCreateDto createDto = new SessionSeatCreateDto(1L, 1L, 1L);

        SessionSeat sessionSeat = new SessionSeat(1L, 1L, 1L);
        entityManager.persist(sessionSeat);

        // when, then
        assertThat(sessionSeatSupportRepository.exists(createDto)).isTrue();

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("sessionSeat이 존재하지 않을 경우")
    @Test
    @Transactional
    public void exists_whenNotExists() {
        // given
        SessionSeatCreateDto createDto = new SessionSeatCreateDto(2L, 2L, 2L);

        // when, then
        assertThat(sessionSeatSupportRepository.exists(createDto)).isFalse();
    }
}