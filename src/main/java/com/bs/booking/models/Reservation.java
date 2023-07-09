package com.bs.booking.models;

import com.bs.booking.dtos.ReservationCreateDto;
import com.bs.booking.enums.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "reservation")
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_seat_id", nullable = false)
    private SessionSeat sessionSeat;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(SessionSeat sessionSeat, ReservationCreateDto valuesForCreate) {
        this.sessionSeat = Objects.requireNonNull(sessionSeat, "seat must not be null");
        this.userId = Objects.requireNonNull(valuesForCreate.getUserId(),
            "userId must not be null");
        this.status = ReservationStatus.PENDING;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}