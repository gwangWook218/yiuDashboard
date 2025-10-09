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
public class FacultyService {

    private final WebClient webClient;

    @Value("${univapi.service-key}")
    private String serviceKey;

    @Value("${univapi.school-divCd}")
    private String schlDivCd;

//    전임교원 1인당 학생 수
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

    public List<Map<String, Object>> getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent")
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
            String region = item.path("znNm").asText();

            // 전체 / 수도권 / 비수도권만 필터
            if (region.equals("전체") || region.equals("수도권") || region.equals("비수도권")) {
                Map<String, Object> map2023 = new HashMap<>();
                map2023.put("year", 2023);
                map2023.put("value", item.path("indctFirstVal").asDouble());
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", item.path("indctSecondVal").asDouble());
                map2024.put("region", region);
                results.add(map2024);
            }
        }

        return results;
    }

//    전임교원 강의담당비율
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

    public List<Map<String, Object>> getRegionalLectureChargeRatio() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalLectureChargeRatio")
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

//    전임교원 1인당 연구비(교내)
    public Mono<String> getComparisonFullTimeFacultyInsideOfSchoolForPersonResearchGrant (int year, String schlId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getComparisonFullTimeFacultyInsideOfSchoolForPersonResearchGrant")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("pageNo", 1)
                        .queryParam("numOfRows", 999)
                        .queryParam("schlId", schlId)
                        .queryParam("svyYr", year)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public List<Map<String, Object>> getRegionalFullTimeFacultyInsideOfSchoolForPersonResearchGrant() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalFullTimeFacultyInsideOfSchoolForPersonResearchGrant")
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
            String region = item.path("znNm").asText();

            // 전체 / 수도권 / 비수도권만 필터
            if (region.equals("전체") || region.equals("수도권") || region.equals("비수도권")) {
                Map<String, Object> map2023 = new HashMap<>();
                map2023.put("year", 2023);
                map2023.put("value", item.path("indctFirstVal").asDouble());
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", item.path("indctSecondVal").asDouble());
                map2024.put("region", region);
                results.add(map2024);
            }
        }

        return results;
    }

//    전임교원 1인당 연구비(교외)
public Mono<String> getComparisonFullTimeFacultyOutsideOfSchoolForPersonResearchGrant (int year, String schlId) {
    return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/EducationResearchService/getComparisonFullTimeFacultyOutsideOfSchoolForPersonResearchGrant")
                    .queryParam("ServiceKey", serviceKey)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 999)
                    .queryParam("schlId", schlId)
                    .queryParam("svyYr", year)
                    .build())
            .retrieve()
            .bodyToMono(String.class);
}

    public List<Map<String, Object>> getRegionalFullTimeFacultyOutsideOfSchoolForPersonResearchGrant() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationResearchService/getRegionalFullTimeFacultyOutsideOfSchoolForPersonResearchGrant")
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
            String region = item.path("znNm").asText();

            // 전체 / 수도권 / 비수도권만 필터
            if (region.equals("전체") || region.equals("수도권") || region.equals("비수도권")) {
                Map<String, Object> map2023 = new HashMap<>();
                map2023.put("year", 2023);
                map2023.put("value", item.path("indctFirstVal").asDouble());
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", item.path("indctSecondVal").asDouble());
                map2024.put("region", region);
                results.add(map2024);
            }
        }

        return results;
    }
}
