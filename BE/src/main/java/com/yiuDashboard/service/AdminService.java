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

    @Value("${univapi.school-divCd}")
    private String schlDivCd;

    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(int year, int indctId, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonFullTimeFacultyEnsureCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("indctId", indctId)
                        .queryParam("schlId", schlId)
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

    public Mono<String> getNoticeFullTimeFacultyEnsureRate(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getNoticeFullTimeFacultyEnsureRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
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

    public Mono<String> getComparisonLectureChargeRatio(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonLectureChargeRatio")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 999)
                        .queryParam("schlId", schlId)
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

    public Mono<String> getComparisonEnrolledStudentEnsureRate(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonEnrolledStudentEnsureRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonInsideFixedNumberFreshmanCompetitionRate(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonInsideFixedNumberFreshmanCompetitionRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonEntranceModelLastRegistrationRatio(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonEntranceModelLastRegistrationRatio")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getNoticeGraduateEmploymentRate(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getNoticeGraduateEmploymentRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonDropOutStudentCrntSt(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getComparisonDropOutStudentCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalDropOutStudentCrntSt() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getRegionalDropOutStudentCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getNoticeStudentsWastageRate(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getNoticeStudentsWastageRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonScholarshipBenefitCrntSt(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/FinancesService/getComparisonScholarshipBenefitCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalScholarshipBenefitCrntSt() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/FinancesService/getRegionalScholarshipBenefitCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonEducationalExpensesReductionCrntSt(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/FinancesService/getComparisonEducationalExpensesReductionCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalEducationalExpensesReductionCrntSt() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/FinancesService/getRegionalEducationalExpensesReductionCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getComparisonDormitoryAcceptanceCrntSt(int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationConditionService/getComparisonDormitoryAcceptanceCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRegionalDormitoryAcceptanceCrntSt() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationConditionService/getRegionalDormitoryAcceptanceCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
