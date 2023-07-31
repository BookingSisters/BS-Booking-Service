package com.bs.booking.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CronExpressionParserTest {

    @DisplayName("크론 포맷을 일반 날짜 포맷으로 변경")
    @Test
    void cronToReadableDate() {
        // given
        CronExpressionParser parser = new CronExpressionParser();

        String cronExpression = "15 10 20 10 ? 2023";
        String expected = "2023-10-20 10:15";
        // when
        String actual = parser.cronToReadableDate(cronExpression);
        // then
        assertThat(actual).isEqualTo(expected);
    }
}