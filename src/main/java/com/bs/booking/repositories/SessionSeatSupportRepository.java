package com.bs.booking.repositories;

import com.bs.booking.dtos.SessionSeatCreateDto;

public interface SessionSeatSupportRepository {

    boolean exists(SessionSeatCreateDto createDto);
}
