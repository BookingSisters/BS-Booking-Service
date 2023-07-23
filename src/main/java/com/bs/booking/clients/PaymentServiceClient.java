package com.bs.booking.clients;

import com.bs.booking.configs.AppProperties;
import com.bs.booking.dtos.PaymentCreateDto;
import com.bs.booking.dtos.common.ResponseDto;
import com.bs.booking.exceptions.PaymentServiceResponseException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    private String baseUrl;

    @PostConstruct
    public void init() {
        baseUrl = appProperties.getExternalServiceBaseUrl() + "/payments";
    }

    public ResponseDto createPayment(PaymentCreateDto createDto) {
        log.info("Sending request to create payment with values: {}", createDto);

        ResponseEntity<ResponseDto> response = restTemplate.postForEntity(baseUrl, createDto,
            ResponseDto.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to create payment with values: {}", createDto);
            throw new PaymentServiceResponseException("결제 생성에 실패하였습니다");
        }
        log.info("Successfully created payment");
        return response.getBody();
    }
}
