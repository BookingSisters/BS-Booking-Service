package com.bs.booking.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "session_seat")
public class SessionSeat extends BaseEntity {

    @Column(nullable = false)
    private Long performId;

    @Column(nullable = false)
    private Long sessionId;

    @Column(nullable = false)
    private Long seatGradeId;

    @OneToMany(mappedBy = "sessionSeat")
    private List<Reservation> reservations = new ArrayList<>();

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted;

    public SessionSeat(long performId, long sessionId, long seatGradeId) {
        this.performId = performId;
        this.sessionId = sessionId;
        this.seatGradeId = seatGradeId;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
        this.isDeleted = true;
    }
}
