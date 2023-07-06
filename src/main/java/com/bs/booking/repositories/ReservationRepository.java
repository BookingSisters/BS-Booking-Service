package com.bs.booking.repositories;

import com.bs.booking.enums.ReservationStatus;
import com.bs.booking.models.Reservation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Long>,
    ReservationSupportRepository {

    List<Reservation> findAllByStatus(ReservationStatus status);

    Optional<Reservation> findBySessionSeatIdAndStatus(long sessionSeatId, ReservationStatus status);
}
