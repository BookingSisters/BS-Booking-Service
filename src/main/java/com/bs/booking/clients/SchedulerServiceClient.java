package com.bs.booking.clients;

import com.bs.booking.configs.AppProperties;
import com.bs.booking.configs.AwsProperties;
import com.bs.booking.dtos.SchedulerCreateDto;
import com.bs.booking.exceptions.AlreadyExistScheduleException;
import com.bs.booking.exceptions.SchedulerCreateException;
import com.bs.booking.utils.CronExpressionGenerator;
import com.bs.booking.utils.CronExpressionParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindow;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.ListSchedulesRequest;
import software.amazon.awssdk.services.scheduler.model.ListSchedulesResponse;
import software.amazon.awssdk.services.scheduler.model.SchedulerException;
import software.amazon.awssdk.services.scheduler.model.Target;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchedulerServiceClient {

    private static final String RESERVATION_PREFIX = "Reservation-";
    private static final String SCHEDULE_SUFFIX = "-time_out_schedule";
    private static final String CRON_FORMAT = "cron(%s)";
    private static final int ALLOWED_MINUTES = 10;

    private final SchedulerClient schedulerClient;
    private final CronExpressionGenerator cronGenerator;
    private final CronExpressionParser cronExpressionParser;
    private final AppProperties appProperties;
    private final AwsProperties awsProperties;

    public void createTimeOutSchedule(SchedulerCreateDto createDto) {
        String title = buildTimeOutTitle(createDto.getReservationId(), createDto.getUserId());
        if (isScheduleExists(title)) {
            throw new AlreadyExistScheduleException(createDto);
        }

        String cronExpression = generateCronExpression();
        String sqsMessage = buildTimeOutSqsMessage(createDto);

        CreateScheduleRequest createScheduleRequest =
            createScheduleRequest(title, cronExpression, sqsMessage);

        try {
            schedulerClient.createSchedule(createScheduleRequest);
        } catch (SchedulerException e) {
            log.error("Failed to create schedule : {}", e.getMessage());
            throw new SchedulerCreateException(createDto);
        }

        log.info("Successfully created schedule: {}", title);
        log.info("Schedule will be executed at : {}",
            cronExpressionParser.cronToReadableDate(cronExpression));
    }

    public boolean isScheduleExists(String title) {
        ListSchedulesRequest request = ListSchedulesRequest.builder()
            .namePrefix(title)
            .build();
        ListSchedulesResponse response = schedulerClient.listSchedules(request);

        return !response.schedules().isEmpty();
    }

    private String buildTimeOutTitle(Long reservationId, String userId) {
        return RESERVATION_PREFIX + reservationId + "-" + userId + SCHEDULE_SUFFIX;
    }

    private String generateCronExpression() {
        return String.format(CRON_FORMAT,
            cronGenerator.generateOneTimeExecutionCron(ALLOWED_MINUTES));
    }

    private String buildTimeOutSqsMessage(SchedulerCreateDto createDto) {
        return String.format(
            "{\"type\":\"time_out\",\"data\":{\"reservationId\":%s,\"userId\":\"%s\"}}",
            createDto.getReservationId(), createDto.getUserId());
    }

    private CreateScheduleRequest createScheduleRequest(String title, String cronExpression,
        String sqsMessage) {
        Target sqsTarget = Target.builder()
            .roleArn(awsProperties.getSchedulerRoleArn())
            .arn(awsProperties.getSchedulerSqsArn())
            .input(sqsMessage)
            .build();

        return CreateScheduleRequest.builder()
            .name(title)
            .scheduleExpression(cronExpression)
            .scheduleExpressionTimezone(appProperties.getTimezone())
            .target(sqsTarget)
            .flexibleTimeWindow(FlexibleTimeWindow.builder()
                .mode(FlexibleTimeWindowMode.OFF)
                .build())
            .build();
    }
}
