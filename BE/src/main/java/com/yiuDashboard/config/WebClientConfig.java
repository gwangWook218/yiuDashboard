package com.yiuDashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${openapi.base-url:http://openapi.academyinfo.go.kr/openapi/service/rest}")
    private String openApiBaseUrl;

    @Bean
    public WebClient openApiWebClient(WebClient.Builder builder) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        return builder
                .baseUrl(openApiBaseUrl)
                .exchangeStrategies(strategies)
                .build();
    }
}
