package com.bs.booking.services;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.dtos.SessionSeatResponseDto;
import com.bs.booking.exceptions.SeatNotFoundException;
import com.bs.booking.models.SessionSeat;
import com.bs.booking.repositories.SessionSeatRepository;
import com.bs.booking.utils.mapper.SessionSeatMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SessionSeatService {

    private final SessionSeatRepository sessionSeatRepository;
    private final SessionSeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SessionSeatResponseDto> getAllSeats() {
        List<SessionSeat> sessionSeats = sessionSeatRepository.findAllByDeletedAtIsNull();

        return sessionSeats.stream()
            .map(seatMapper::toSessionSeatResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public SessionSeatResponseDto createSeat(SessionSeatCreateDto valuesForCreate) {
        SessionSeat newSessionSeat = seatMapper.toSessionSeat(valuesForCreate);
        sessionSeatRepository.save(newSessionSeat);
        return seatMapper.toSessionSeatResponseDto(newSessionSeat);
    }

    @Transactional
    public void deleteSeat(long id) throws IllegalArgumentException {
        SessionSeat dbSessionSeat = sessionSeatRepository.findById(id)
            .orElseThrow(() -> new SeatNotFoundException(id));
        dbSessionSeat.setDeletedAt();
    }
}
