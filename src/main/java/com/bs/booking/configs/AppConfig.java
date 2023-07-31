package com.bs.booking.configs;

import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final AppProperties appProperties;

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of(appProperties.getTimezone());
    }
}
