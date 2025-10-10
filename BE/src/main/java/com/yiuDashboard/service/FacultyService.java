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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final WebClient webClient;

    @Value("${univapi.service-key}")
    private String serviceKey;

    @Value("${univapi.school-divCd}")
    private String schlDivCd;

//    전임교원 1인당 학생 수
    public List<Map<String, Object>> getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/EducationResearchService/getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent")
                                .queryParam("ServiceKey", serviceKey)
                                .queryParam("schlId", schlId)
                                .queryParam("svyYr", year)
                                .build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                XmlMapper xmlMapper = new XmlMapper();
                JsonNode root = xmlMapper.readTree(xmlResponse);
                JsonNode items = root.path("body").path("items").path("item");

                Map<String, Object> map = new HashMap<>();
                map.put("year", items.path("svyYr").asInt());
                map.put("schlKrnNm", items.path("schlKrnNm").asText());
                map.put("value", items.path("indctVal1").asDouble());
                results.add(map);
            }
        }

        return results;
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
    public List<Map<String, Object>> getComparisonLectureChargeRatio() throws JsonProcessingException {

        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (int year : years) {
            String xmlResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/EducationResearchService/getComparisonLectureChargeRatio")
                            .queryParam("ServiceKey", serviceKey)
                            .queryParam("schlId", "0000156")
                            .queryParam("svyYr", year)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            XmlMapper xmlMapper = new XmlMapper();
            JsonNode root = xmlMapper.readTree(xmlResponse);
            JsonNode items = root.path("body").path("items").path("item");

            Map<String, Object> map = new HashMap<>();
            map.put("year", items.path("svyYr").asInt());
            map.put("schlKrnNm", items.path("schlKrnNm").asText());
            map.put("value", items.path("indctVal1").asDouble());
            results.add(map);
        }

        return results;
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

//    전임교원 1인당 연구비
    public List<Map<String, Object>> getComparisonFullTimeFacultyForPersonResearchGrant (String scope) throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String path;
                if ("inside".equalsIgnoreCase(scope)) {
                    path = "/EducationResearchService/getComparisonFullTimeFacultyInsideOfSchoolForPersonResearchGrant";
                } else if ("outside".equalsIgnoreCase(scope)) {
                    path = "/EducationResearchService/getComparisonFullTimeFacultyOutsideOfSchoolForPersonResearchGrant";
                } else {
                    throw new IllegalArgumentException("Invalid scope: " + scope);
                }

                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path(path)
                                .queryParam("ServiceKey", serviceKey)
                                .queryParam("schlId", schlId)
                                .queryParam("svyYr", year)
                                .build())
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                XmlMapper xmlMapper = new XmlMapper();
                JsonNode root = xmlMapper.readTree(xmlResponse);
                JsonNode items = root.path("body").path("items").path("item");

                if (items.isArray()) {
                    for (JsonNode item : items) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("year", item.path("svyYr").asInt());
                        map.put("schlKrnNm", item.path("schlKrnNm").asText());
                        map.put("value", item.path("indctVal1").asDouble());
                        results.add(map);
                    }
                } else if (!items.isMissingNode()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("year", items.path("svyYr").asInt());
                    map.put("schlKrnNm", items.path("schlKrnNm").asText());
                    map.put("value", items.path("indctVal1").asDouble());
                    results.add(map);
                }
            }
        }

        return results;
    }

    public List<Map<String, Object>> getRegionalFullTimeFacultyForPersonResearchGrant(String scope) throws JsonProcessingException {
        String path;
        if ("inside".equalsIgnoreCase(scope)) {
            path = "/EducationResearchService/getRegionalFullTimeFacultyInsideOfSchoolForPersonResearchGrant";
        } else if ("outside".equalsIgnoreCase(scope)) {
            path = "/EducationResearchService/getRegionalFullTimeFacultyOutsideOfSchoolForPersonResearchGrant";
        } else {
            throw new IllegalArgumentException("Invalid scope: " + scope);
        }

        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
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
                double val2023 = item.path("indctFirstVal").asDouble();
                double val2024 = item.path("indctSecondVal").asDouble();

                Map<String, Object> map2023 = new HashMap<>();
                map2023.put("year", 2023);
                map2023.put("value", val2023);
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", val2024);
                map2024.put("region", region);
                map2024.put("increase", val2024 - val2023);
                results.add(map2024);
            }
        }

        return results;
    }

    public List<Map<String, Object>> getFacultyWithGap() throws JsonProcessingException {
        // 교내/교외 데이터 가져오기
        List<Map<String, Object>> insideData = getRegionalFullTimeFacultyForPersonResearchGrant("inside");
        List<Map<String, Object>> outsideData = getRegionalFullTimeFacultyForPersonResearchGrant("outside");

        // region + year 기준으로 Map 생성
        Map<String, Map<Integer, Double>> insideMap = insideData.stream()
                .collect(Collectors.groupingBy(
                        m -> (String) m.get("region"),
                        Collectors.toMap(
                                m -> (Integer) m.get("year"),
                                m -> (Double) m.get("value")
                        )
                ));

        Map<String, Map<Integer, Double>> outsideMap = outsideData.stream()
                .collect(Collectors.groupingBy(
                        m -> (String) m.get("region"),
                        Collectors.toMap(
                                m -> (Integer) m.get("year"),
                                m -> (Double) m.get("value")
                        )
                ));

        List<Map<String, Object>> results = new ArrayList<>();

        for (String region : insideMap.keySet()) {
            Map<Integer, Double> insideYears = insideMap.get(region);
            Map<Integer, Double> outsideYears = outsideMap.getOrDefault(region, Map.of());

            for (int year : insideYears.keySet()) {
                double insideVal = insideYears.get(year);
                double outsideVal = outsideYears.getOrDefault(year, 0.0);

                Map<String, Object> map = new HashMap<>();
                map.put("region", region);
                map.put("year", year);
                map.put("inside", Math.round(insideVal));
                map.put("outside", Math.round(outsideVal));
                map.put("gap", Math.round(outsideVal - insideVal));

                // 전년 대비 증가량 계산 (2024년만)
                if (year == 2024) {
                    double prevYearInside = insideYears.getOrDefault(2023, 0.0);
                    map.put("increase_inside", Math.round(insideVal - prevYearInside));

                    double prevYearOutside = outsideYears.getOrDefault(2023, 0.0);
                    map.put("increase_outside", Math.round(outsideVal - prevYearOutside));
                }

                results.add(map);
            }
        }

        return results;
    }
}
