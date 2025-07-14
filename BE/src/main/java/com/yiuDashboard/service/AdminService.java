package com.yiuDashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final WebClient webClient;

    @Value("${univapi.service-key}")
    private String serviceKey;

    @Value("${univapi.school-id}")
    private String schoolId;

    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonFullTimeFacultyEnsureCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("indctId", 66)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalFullTimeFacultyEnsureCrntSt(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalFullTimeFacultyEnsureCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", 02)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
