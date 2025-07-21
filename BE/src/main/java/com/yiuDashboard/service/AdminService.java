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

    @Value("${univapi.school-divCd}")
    private String schlDivCd;

    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(int year, int indctId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonFullTimeFacultyEnsureCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("indctId", indctId)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalFullTimeFacultyEnsureCrntSt() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalFullTimeFacultyEnsureCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getNoticeFullTimeFacultyEnsureRate(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getNoticeFullTimeFacultyEnsureRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonLectureChargeRatio(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonLectureChargeRatio")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 999)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalLectureChargeRatio() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalLectureChargeRatio")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonEnrolledStudentEnsureRate(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonEnrolledStudentEnsureRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonInsideFixedNumberFreshmanCompetitionRate(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonInsideFixedNumberFreshmanCompetitionRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonEntranceModelLastRegistrationRatio(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonEntranceModelLastRegistrationRatio")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getNoticeGraduateEmploymentRate(int year) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getNoticeGraduateEmploymentRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schoolId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
