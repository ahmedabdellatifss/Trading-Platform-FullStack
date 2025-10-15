package com.roaa.treading.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "paymob")
@Data
public class PaymobConfig {
    private String apiKey;
    private String integrationId;
    private String iframeId;
    private String baseUrl;

    // getters and setters
}