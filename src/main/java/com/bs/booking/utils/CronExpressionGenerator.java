package com.bs.booking.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CronExpressionGenerator {

    private final ZoneId zoneId;

    public String generateOneTimeExecutionCron(int minutesFromNow) {
        if (minutesFromNow < 0) {
            throw new IllegalArgumentException(
                String.format("Minute should be positive value: %d", minutesFromNow));
        }

        ZonedDateTime executionTime = ZonedDateTime.now(zoneId).plusMinutes(minutesFromNow);
        return String.format("%d %d %d %d ? %d",
            executionTime.getMinute(),
            executionTime.getHour(),
            executionTime.getDayOfMonth(),
            executionTime.getMonthValue(),
            executionTime.getYear());
    }
}
