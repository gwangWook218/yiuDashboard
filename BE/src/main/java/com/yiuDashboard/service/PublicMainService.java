package com.yiuDashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PublicMainService {

    private final WebClient webClient;

    @Value("${univapi.service-key}")
    private String serviceKey;

    @Value("${univapi.school-id}")
    private String schoolId;

    public Mono<String> getComparisonEnrolledStudent(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonEnrolledStudent")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 999)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getNoticeGraduateEmploymentRate(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getNoticeGraduateEmploymentRate")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonForeignStudentCrntSt(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonForeignStudentCrntSt")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonFullTimeFacultyEnsureCrntSt")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonScholarshipBenefitCrntSt(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/FinancesService/getComparisonScholarshipBenefitCrntSt")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
