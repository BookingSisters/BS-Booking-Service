package com.bs.booking.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bs.booking.configs.AppProperties;
import com.bs.booking.configs.AwsProperties;
import com.bs.booking.dtos.SchedulerCreateDto;
import com.bs.booking.exceptions.SchedulerCreateException;
import com.bs.booking.utils.CronExpressionGenerator;
import com.bs.booking.utils.CronExpressionParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleResponse;
import software.amazon.awssdk.services.scheduler.model.ListSchedulesRequest;
import software.amazon.awssdk.services.scheduler.model.ListSchedulesResponse;
import software.amazon.awssdk.services.scheduler.model.ScheduleSummary;
import software.amazon.awssdk.services.scheduler.model.SchedulerException;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceClientTest {

    @InjectMocks
    private SchedulerServiceClient schedulerServiceClient;

    @Mock
    private SchedulerClient schedulerClient;

    @Mock
    private CronExpressionGenerator cronGenerator;

    @Mock
    private CronExpressionParser cronExpressionParser;

    @Mock
    private AppProperties appProperties;

    @Mock
    private AwsProperties awsProperties;

    @DisplayName("시간초과 스케쥴 생성: 성공")
    @Test
    void createTimeOutSchedule_success() {
        // given
        SchedulerCreateDto createDto = new SchedulerCreateDto(1L, "testUser");
        String generatedCron = "0 15 10 * * ?";
        String readableCron = "2023-10-15 10:15";
        CreateScheduleResponse response = CreateScheduleResponse.builder().build();

        SchedulerServiceClient spySchedulerServiceClient = spy(schedulerServiceClient);
        doReturn(false).when(spySchedulerServiceClient).isScheduleExists(anyString());

        when(cronGenerator.generateOneTimeExecutionCron(anyInt())).thenReturn(generatedCron);
        when(schedulerClient.createSchedule(any(CreateScheduleRequest.class))).thenReturn(response);
        when(cronExpressionParser.cronToReadableDate(anyString())).thenReturn(readableCron);

        // when
        spySchedulerServiceClient.createTimeOutSchedule(createDto);

        // then
        verify(cronGenerator, times(1)).generateOneTimeExecutionCron(anyInt());
        verify(cronExpressionParser, times(1)).cronToReadableDate(anyString());
        verify(schedulerClient, times(1)).createSchedule(any(CreateScheduleRequest.class));
    }

    @DisplayName("시간초과 스케쥴 생성: 실패")
    @Test
    void createTimeOutSchedule_fail() {
        // given
        SchedulerCreateDto createDto = new SchedulerCreateDto(1L, "testUser");
        String generatedCron = "0 15 10 * * ?";
        String readableCron = "2023-10-15 10:15";
        CreateScheduleResponse response = CreateScheduleResponse.builder().build();

        SchedulerServiceClient spySchedulerServiceClient = spy(schedulerServiceClient);
        doReturn(false).when(spySchedulerServiceClient).isScheduleExists(anyString());

        when(cronGenerator.generateOneTimeExecutionCron(anyInt())).thenReturn(generatedCron);
        when(schedulerClient.createSchedule(any(CreateScheduleRequest.class)))
            .thenThrow(SchedulerException.class);

        // when, then
        assertThrows(SchedulerCreateException.class,
            () -> spySchedulerServiceClient.createTimeOutSchedule(createDto));
    }

    @DisplayName("스케쥴 여부 확인: 있음")
    @Test
    void isScheduleExists_true() {
        // given
        String scheduleName = "test";

        when(schedulerClient.listSchedules(any(ListSchedulesRequest.class)))
            .thenReturn(ListSchedulesResponse.builder()
                .schedules(ScheduleSummary.builder().build())
                .build());
        // when
        boolean result = schedulerServiceClient.isScheduleExists(scheduleName);
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("스케쥴 여부 확인: 없음")
    @Test
    void isScheduleExists_false() {
        // given
        String scheduleName = "test";

        when(schedulerClient.listSchedules(any(ListSchedulesRequest.class)))
            .thenReturn(ListSchedulesResponse.builder().build());
        // when
        boolean result = schedulerServiceClient.isScheduleExists(scheduleName);
        // then
        assertThat(result).isFalse();
    }
}