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

    public List<Map<String, Object>> getComparisonFullTimeFacultyEnsureCrntSt() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> indctIds = List.of(66, 67);
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();
        for (String schlId : schlIds) {
            for (int year : years) {
                for (int indctId : indctIds) {
                    String xmlResponse = webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/EducationResearchService/getComparisonFullTimeFacultyEnsureCrntSt")
                                    .queryParam("ServiceKey", serviceKey)
                                    .queryParam("indctId", indctId)
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
                    map.put("indctId", items.path("indctId").asInt());
                    map.put("year", items.path("svyYr").asInt());
                    map.put("schlKrnNm", items.path("schlKrnNm").asText());
                    map.put("value", items.path("indctVal1").asDouble());
                    results.add(map);
                }
            }
        }
        return results;
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

    public List<Map<String, Object>> getComparisonLectureChargeRatio() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/EducationResearchService/getComparisonLectureChargeRatio")
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

    public List<Map<String, Object>> getComparisonEnrolledStudentEnsureRate() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/StudentService/getComparisonEnrolledStudentEnsureRate")
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

    public List<Map<String, Object>> getComparisonInsideFixedNumberFreshmanCompetitionRate() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/StudentService/getComparisonInsideFixedNumberFreshmanCompetitionRate")
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

    public List<Map<String, Object>> getComparisonEntranceModelLastRegistrationRatio() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/StudentService/getComparisonEntranceModelLastRegistrationRatio")
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

    public List<Map<String, Object>> getNoticeGraduateEmploymentRate() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/StudentService/getNoticeGraduateEmploymentRate")
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
                map.put("value1", items.path("indctVal1").asInt());
                map.put("value2", items.path("indctVal2").asInt());
                map.put("value3", items.path("indctVal3").asInt());
                map.put("value4", items.path("indctVal4").asInt());
                results.add(map);
            }
        }
        return results;
    }

    public List<Map<String, Object>> getComparisonDropOutStudentCrntSt() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/StudentService/getComparisonDropOutStudentCrntSt")
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

    public List<Map<String, Object>> getRegionalDropOutStudentCrntSt() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/StudentService/getRegionalDropOutStudentCrntSt")
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

    public List<Map<String, Object>> getComparisonScholarshipBenefitCrntSt() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/FinancesService/getComparisonScholarshipBenefitCrntSt")
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

    public List<Map<String, Object>> getRegionalScholarshipBenefitCrntSt() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/FinancesService/getRegionalScholarshipBenefitCrntSt")
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
                map2023.put("value", Math.round(item.path("fieldVal4").asDouble()));
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", Math.round(item.path("fieldVal5").asDouble()));
                map2024.put("region", region);
                results.add(map2024);
            }
        }

        return results;
    }

    public List<Map<String, Object>> getComparisonEducationalExpensesReductionCrntSt() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/FinancesService/getComparisonEducationalExpensesReductionCrntSt")
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
                map.put("value", items.path("indctVal1").asInt());
                results.add(map);
            }
        }
        return results;
    }

    public List<Map<String, Object>> getRegionalEducationalExpensesReductionCrntSt() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/FinancesService/getRegionalEducationalExpensesReductionCrntSt")
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
                map2023.put("value", Math.round(item.path("fieldVal4").asDouble()));
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", Math.round(item.path("fieldVal5").asDouble()));
                map2024.put("region", region);
                results.add(map2024);
            }
        }

        return results;
    }

    public List<Map<String, Object>> getComparisonDormitoryAcceptanceCrntSt() throws JsonProcessingException {

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xmlResponse = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/EducationConditionService/getComparisonDormitoryAcceptanceCrntSt")
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

    public List<Map<String, Object>> getRegionalDormitoryAcceptanceCrntSt() throws JsonProcessingException {
        String xmlResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/EducationConditionService/getRegionalDormitoryAcceptanceCrntSt")
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
                Map<String, Object> map2022 = new HashMap<>();
                map2022.put("year", 2022);
                map2022.put("value", item.path("fieldVal4").asDouble());
                map2022.put("region", region);
                results.add(map2022);

                Map<String, Object> map2023 = new HashMap<>();
                map2023.put("year", 2023);
                map2023.put("value", item.path("fieldVal5").asDouble());
                map2023.put("region", region);
                results.add(map2023);

                Map<String, Object> map2024 = new HashMap<>();
                map2024.put("year", 2024);
                map2024.put("value", item.path("fieldVal6").asDouble());
                map2024.put("region", region);
                results.add(map2024);
            }
        }

        return results;
    }
}
