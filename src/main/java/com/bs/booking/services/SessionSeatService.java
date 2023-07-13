package com.bs.booking.services;

import com.bs.booking.dtos.SessionSeatCreateDto;
import com.bs.booking.dtos.SessionSeatResponseDto;
import com.bs.booking.dtos.SessionSeatsCreateDto;
import com.bs.booking.exceptions.AlreadyExistSeatException;
import com.bs.booking.exceptions.SeatNotFoundException;
import com.bs.booking.models.SessionSeat;
import com.bs.booking.repositories.SessionSeatRepository;
import com.bs.booking.utils.mapper.SessionSeatMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionSeatService {

    private final SessionSeatRepository sessionSeatRepository;
    private final SessionSeatMapper seatMapper;

    public List<SessionSeatResponseDto> getAllSeats() {
        log.info("Getting all sessionSeats");

        List<SessionSeat> sessionSeats = sessionSeatRepository.findAllByIsDeletedIsFalse();
        log.info("Returned {} seats", sessionSeats.size());

        return sessionSeats.stream().map(seatMapper::toSessionSeatResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public SessionSeatResponseDto createSeat(SessionSeatCreateDto valuesForCreate) {
        validateSessionSeatNotExists(valuesForCreate);

        log.info("Creating seat with values: {}", valuesForCreate);
        SessionSeat newSessionSeat = seatMapper.toSessionSeat(valuesForCreate);
        sessionSeatRepository.save(newSessionSeat);

        log.info("Successfully created seat with id: {}", newSessionSeat.getId());
        return seatMapper.toSessionSeatResponseDto(newSessionSeat);
    }

    @Transactional
    public void createSeats(long id, SessionSeatsCreateDto valuesForCreate) {
        Set<SessionSeat> newSessionSeats = new HashSet<>();
        log.info("Creating seats. Total number of seats: {}", valuesForCreate.getData().size());

        for (SessionSeatCreateDto createDto : valuesForCreate.getData()) {
            validateSessionSeatNotExists(createDto);
            newSessionSeats.add(seatMapper.toSessionSeat(createDto));
        }
        sessionSeatRepository.saveAll(new ArrayList<>(newSessionSeats));
        log.info("Successfully created all seats");
    }

    @Transactional
    public void deleteSeat(long id) {
        SessionSeat dbSessionSeat = sessionSeatRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Error when deleting seat, seat not found with id: {}", id);
                return new SeatNotFoundException(id);
            });

        dbSessionSeat.delete();
        log.info("Successfully deleted seat with id: {}", id);
    }

    private void validateSessionSeatNotExists(SessionSeatCreateDto createDto) {
        if (sessionSeatRepository.exists(createDto)) {
            log.error("Seat already exists. Details: {}", createDto);
            throw new AlreadyExistSeatException(createDto);
        }
    }
}
