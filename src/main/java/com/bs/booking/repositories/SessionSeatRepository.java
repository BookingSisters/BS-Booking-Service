package com.bs.booking.repositories;

import com.bs.booking.models.SessionSeat;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SessionSeatRepository extends CrudRepository<SessionSeat, Long>,
    SessionSeatSupportRepository {

    List<SessionSeat> findAllByDeletedAtIsNull();

    Optional<SessionSeat> findByIdAndDeletedAtIsNull(long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SessionSeat s where s.id = :id and s.deletedAt is null")
    Optional<SessionSeat> findByIdWithLock(@Param("id") long id);
}
