package com.bs.booking.configs;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Validated
@Getter
@Setter
public class AwsProperties {

    @NotBlank
    private String accessKeyId;

    @NotBlank
    private String secretAccessKey;

    @NotBlank
    private String schedulerRoleArn;

    @NotBlank
    private String schedulerSqsArn;

}
