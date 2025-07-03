package com.yiuDashboard.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PublicStudentService {

    private final WebClient webClient;

    @Value("${univapi.service-key}")
    private String serviceKey;

    @Value("${univapi.school-id}")
    private String schoolId;

    public Mono<String> getEnrolledStudentCount(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonEnrolledStudent")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 10)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
