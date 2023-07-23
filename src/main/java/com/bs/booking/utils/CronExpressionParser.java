package com.bs.booking.utils;

import org.springframework.stereotype.Component;

@Component
public class CronExpressionParser {

    /* input format : "%d %d %d %d ? %d" */
    public String cronToReadableDate(String cronExpression) {

        String readableFormat = "%s-%s-%s %s:%s";
        String[] cronParts = cronExpression.split(" ");

        String minute = cronParts[0];
        String hour = cronParts[1];
        String day = cronParts[2];
        String month = cronParts[3];
        String year = cronParts[5];

        return String.format(readableFormat, year, month, day, hour, minute);
    }
}
