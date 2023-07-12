package com.bs.booking.repositories;

import static com.bs.booking.models.QSessionSeat.sessionSeat;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.models.SessionSeat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionSeatSupportRepositoryImpl implements
    SessionSeatSupportRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean exists(SessionSeatCreateDto createDto) {
        Integer result = queryFactory
            .selectOne()
            .from(sessionSeat)
            .where(
                sessionSeat.performId.eq(createDto.getPerformId())
                .and(sessionSeat.sessionId.eq(createDto.getSessionId())
                .and(sessionSeat.seatGradeId.eq(createDto.getSeatGradeId()))
                .and(sessionSeat.isDeleted.isFalse()))
            ).fetchFirst();
        return result != null;
    }
}
