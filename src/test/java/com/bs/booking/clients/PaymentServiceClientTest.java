package com.bs.booking.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bs.booking.dtos.PaymentCreateDto;
import com.bs.booking.dtos.common.ResponseDto;
import com.bs.booking.exceptions.PaymentServiceResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PaymentServiceClientTest {

    @InjectMocks
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        paymentServiceClient.init();
    }

    @DisplayName("[success] 결제 데이터 생성 요청")
    @Test
    void createPayment_success() {
        // given
        PaymentCreateDto createDto = new PaymentCreateDto(1L, "user1");
        ResponseDto mockResponse = new ResponseDto("success", "", null);

        ResponseEntity<ResponseDto> response = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(PaymentCreateDto.class),
            eq(ResponseDto.class))).thenReturn(response);

        // when
        ResponseDto actualResponse = paymentServiceClient.createPayment(createDto);

        // then
        verify(restTemplate, times(1))
            .postForEntity(anyString(), any(PaymentCreateDto.class), eq(ResponseDto.class));
        assertThat(actualResponse.getStatus()).isEqualTo("success");
    }

    @DisplayName("[fail] 결제 데이터 생성 요청")
    @Test
    void createPayment_fail() {
        // given
        PaymentCreateDto createDto = new PaymentCreateDto(1L, "user1");
        ResponseDto mockResponse = new ResponseDto("fail", "", null);

        ResponseEntity<ResponseDto> response = new ResponseEntity<>(mockResponse,
            HttpStatus.BAD_REQUEST);

        when(restTemplate.postForEntity(anyString(), any(PaymentCreateDto.class),
            eq(ResponseDto.class))).thenReturn(response);

        // when, then
        assertThrows(PaymentServiceResponseException.class,
            () -> paymentServiceClient.createPayment(createDto));
    }
}