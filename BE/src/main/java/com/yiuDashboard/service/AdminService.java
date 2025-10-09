package com.yiuDashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> getRegionalFullTimeFacultyEnsureCrntSt() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalFullTimeFacultyEnsureCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlDivCd", schlDivCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // XML → JSON 파싱
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode root = xmlMapper.readTree(xmlResponse);

        // item 배열 탐색
        JsonNode items = root.path("body").path("items").path("item");

        List<Map<String, Object>> results = new ArrayList<>();

        for (JsonNode item : items) {
            String region = item.path("fieldVal7").asText();

            // 전체 / 수도권 / 비수도권만 필터
            if (region.equals("전체") || region.equals("수도권") || region.equals("비수도권")) {
                Map<String, Object> map2023 = new HashMap<>();
                map2023.put("year", 2023);
                map2023.put("value", item.path("fieldVal4").asDouble());
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", item.path("fieldVal5").asDouble());
                map2024.put("region", region);
                results.add(map2024);
            }
        }

        return results;
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
