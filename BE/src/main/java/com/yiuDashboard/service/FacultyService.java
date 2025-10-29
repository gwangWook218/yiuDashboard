package com.yiuDashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyService {

    private final WebClient webClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Value("${univapi.service-key}")
    private String serviceKey;

    @Value("${univapi.school-divCd}")
    private String schlDivCd;

    /** 학교 ID (기존엔 하드코드 0000156) — 프로퍼티로 뺄 수 있으면 가장 좋음 */
    @Value("${univapi.schlId:0000156}")
    private String schlId;

    /** 외부 API 쿨다운(ms) — 과도한 호출 제한 회피용 */
    @Value("${univapi.cooldown-ms:300}")
    private long cooldownMs;

    /* ---------------------------------------------
     * 공통 유틸
     * --------------------------------------------- */

    private String fetchXml(String path, Map<String, Object> params) {
        return webClient.get()
                .uri(uriBuilder -> {
                    var b = uriBuilder.path(path)
                            .queryParam("ServiceKey", serviceKey);
                    params.forEach(b::queryParam);
                    return b.build();
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /** body → items → item 노드 반환 (배열/단일 모두 대응) */
    private List<JsonNode> items(String xml) throws JsonProcessingException {
        if (xml == null || xml.isBlank()) return List.of();
        JsonNode root = xmlMapper.readTree(xml);
        JsonNode item = root.path("body").path("items").path("item");
        if (item.isMissingNode() || item.isNull()) return List.of();
        if (item.isArray()) {
            List<JsonNode> r = new ArrayList<>();
            item.forEach(r::add);
            return r;
        }
        return List.of(item);
    }

    private int asInt(JsonNode n, String field, int def) {
        JsonNode f = n.path(field);
        return f.isMissingNode() || f.isNull() ? def : f.asInt(def);
    }

    private double asDouble(JsonNode n, String field, double def) {
        JsonNode f = n.path(field);
        if (f.isMissingNode() || f.isNull()) return def;
        try { return Double.parseDouble(f.asText().replace(",", "")); }
        catch (Exception e) { return def; }
    }

    private String asText(JsonNode n, String field) {
        JsonNode f = n.path(field);
        return (f.isMissingNode() || f.isNull()) ? "" : f.asText();
    }

    private Map<String, Object> mapOf(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return m;
    }

    private void cooldown() {
        if (cooldownMs <= 0) return;
        try { Thread.sleep(cooldownMs); } catch (InterruptedException ignored) {}
    }

    public List<Map<String, Object>> getComparisonFullTimeFacultyEnsureCrntSt() throws JsonProcessingException {
        List<Integer> years = List.of(2022, 2023, 2024);
        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xml = fetchXml(
                        "/EducationResearchService/getComparisonFullTimeFacultyEnsureCrntSt",
                        Map.of("indctId", 66, "schlId", schlId, "svyYr", year)
                );
                List<JsonNode> its = items(xml);
                if (!its.isEmpty()) {
                    JsonNode it = its.get(0);
                    results.add(mapOf(
                            "year", asInt(it, "svyYr", year),
                            "schlKrnNm", asText(it, "schlKrnNm"),
                            "value", asDouble(it, "indctVal1", 0d)
                    ));
                }
                cooldown();
            }
        }
        return results;
    }

    /* ---------------------------------------------
     * 전임교원 1인당 학생 수 (연도별)
     * API: /EducationResearchService/getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent
     * 사용 필드: svyYr, schlKrnNm, indctVal1
     * --------------------------------------------- */
    public List<Map<String, Object>> getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent()
            throws JsonProcessingException {

        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (int year : years) {
            String xml = fetchXml(
                    "/EducationResearchService/getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent",
                    Map.of("schlId", schlId, "svyYr", year)
            );
            List<JsonNode> its = items(xml);
            if (!its.isEmpty()) {
                JsonNode it = its.get(0);
                results.add(mapOf(
                        "year", asInt(it, "svyYr", year),
                        "schlKrnNm", asText(it, "schlKrnNm"),
                        "value", asDouble(it, "indctVal1", 0d)
                ));
            }
            cooldown();
        }
        return results;
    }

    /* ---------------------------------------------
     * 지역별 전임교원 1인당 학생 수 (전체/수도권/비수도권, 2023/2024)
     * API: /EducationResearchService/getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent
     * 사용 필드: znNm, indctFirstVal(2023), indctSecondVal(2024)
     * --------------------------------------------- */
    public List<Map<String, Object>> getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent()
            throws JsonProcessingException {

        String xml = fetchXml(
                "/EducationResearchService/getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent",
                Map.of("schlDivCd", schlDivCd)
        );

        List<String> allow = List.of("전체", "수도권", "비수도권");
        List<Map<String, Object>> results = new ArrayList<>();

        for (JsonNode it : items(xml)) {
            String region = asText(it, "znNm");
            if (!allow.contains(region)) continue;

            results.add(mapOf("year", 2023, "region", region, "value", asDouble(it, "indctFirstVal", 0d)));
            results.add(mapOf("year", 2024, "region", region, "value", asDouble(it, "indctSecondVal", 0d)));
            results.add(mapOf("year", 2025, "region", region, "value", asDouble(it, "indctThirdVal", 0d)));
        }
        return results;
    }

    /* ---------------------------------------------
     * 전임교원 강의담당비율 (연도별)
     * API: /EducationResearchService/getComparisonLectureChargeRatio
     * 사용 필드: svyYr, schlKrnNm, indctVal1
     * --------------------------------------------- */
    public List<Map<String, Object>> getComparisonLectureChargeRatio() throws JsonProcessingException {
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (int year : years) {
            String xml = fetchXml(
                    "/EducationResearchService/getComparisonLectureChargeRatio",
                    Map.of("schlId", schlId, "svyYr", year)
            );
            List<JsonNode> its = items(xml);
            if (!its.isEmpty()) {
                JsonNode it = its.get(0);
                results.add(mapOf(
                        "year", asInt(it, "svyYr", year),
                        "schlKrnNm", asText(it, "schlKrnNm"),
                        "value", asDouble(it, "indctVal1", 0d)
                ));
            }
            cooldown();
        }
        return results;
    }

    /* ---------------------------------------------
     * 지역별 전임교원 강의담당비율 (전체/수도권/비수도권, 2023/2024)
     * API: /EducationResearchService/getRegionalLectureChargeRatio
     * 사용 필드: fieldVal7(지역), fieldVal4(2023), fieldVal5(2024)
     * --------------------------------------------- */
    public List<Map<String, Object>> getRegionalLectureChargeRatio() throws JsonProcessingException {
        String xml = fetchXml(
                "/EducationResearchService/getRegionalLectureChargeRatio",
                Map.of("schlDivCd", schlDivCd)
        );

        List<String> allow = List.of("전체", "수도권", "비수도권");
        List<Map<String, Object>> results = new ArrayList<>();

        for (JsonNode it : items(xml)) {
            String region = asText(it, "fieldVal7");
            if (!allow.contains(region)) continue;

            results.add(mapOf("year", 2023, "region", region, "value", asDouble(it, "fieldVal4", 0d)));
            results.add(mapOf("year", 2024, "region", region, "value", asDouble(it, "fieldVal5", 0d)));
            results.add(mapOf("year", 2025, "region", region, "value", asDouble(it, "fieldVal6", 0d)));
        }
        return results;
    }

    /* ---------------------------------------------
     * 전임교원 1인당 연구비 (교내/교외, 지역별 2023/2024)
     * API: inside → getRegionalFullTimeFacultyInsideOfSchoolForPersonResearchGrant
     *      outside→ getRegionalFullTimeFacultyOutsideOfSchoolForPersonResearchGrant
     * 사용 필드: znNm, indctFirstVal(2023), indctSecondVal(2024)
     * --------------------------------------------- */
    public List<Map<String, Object>> getComparisonFullTimeFacultyForPersonResearchGrant(String scope)
            throws JsonProcessingException {

        final String path = switch (scope.toLowerCase()) {
            case "inside"  -> "/EducationResearchService/getComparisonFullTimeFacultyInsideOfSchoolForPersonResearchGrant";
            case "outside" -> "/EducationResearchService/getComparisonFullTimeFacultyOutsideOfSchoolForPersonResearchGrant";
            default -> throw new IllegalArgumentException("Invalid scope: " + scope);
        };

        List<String> schlIds = List.of("0000156", "0000109", "0000051");
        List<Integer> years = List.of(2022, 2023, 2024);
        List<Map<String, Object>> results = new ArrayList<>();

        for (String schlId : schlIds) {
            for (int year : years) {
                String xml = fetchXml(
                        path,
                        Map.of("schlId", schlId, "svyYr", year)
                );
                List<JsonNode> its = items(xml);
                if (!its.isEmpty()) {
                    JsonNode it = its.get(0);
                    results.add(mapOf(
                            "year", asInt(it, "svyYr", year),
                            "schlKrnNm", asText(it, "schlKrnNm"),
                            "value", asDouble(it, "indctVal1", 0d)
                    ));
                }
                cooldown();
            }
        }
        return results;
    }

    public List<Map<String, Object>> getRegionalFullTimeFacultyForPersonResearchGrant(String scope)
            throws JsonProcessingException {

        final String path = switch (scope.toLowerCase()) {
            case "inside"  -> "/EducationResearchService/getRegionalFullTimeFacultyInsideOfSchoolForPersonResearchGrant";
            case "outside" -> "/EducationResearchService/getRegionalFullTimeFacultyOutsideOfSchoolForPersonResearchGrant";
            default -> throw new IllegalArgumentException("Invalid scope: " + scope);
        };

        String xml = fetchXml(path, Map.of("schlDivCd", schlDivCd));

        List<String> allow = List.of("전체", "수도권", "비수도권");
        List<Map<String, Object>> results = new ArrayList<>();

        for (JsonNode it : items(xml)) {
            String region = asText(it, "znNm");
            if (!allow.contains(region)) continue;

            double v23 = asDouble(it, "indctFirstVal", 0d);
            double v24 = asDouble(it, "indctSecondVal", 0d);
            double v25 = asDouble(it, "indctThirdVal", 0d);

            results.add(mapOf("year", 2023, "region", region, "value", v23));
            results.add(mapOf("year", 2024, "region", region, "value", v24, "increase", v24 - v23));
            results.add(mapOf("year", 2025, "region", region, "value", v25, "increase", v25 - v24));
        }
        return results;
    }

    /* ---------------------------------------------
     * 교내/교외 연구비 묶어서 갭 계산
     * --------------------------------------------- */
    public List<Map<String, Object>> getFacultyWithGap() throws JsonProcessingException {
        List<Map<String, Object>> insideData = getRegionalFullTimeFacultyForPersonResearchGrant("inside");
        List<Map<String, Object>> outsideData = getRegionalFullTimeFacultyForPersonResearchGrant("outside");

        Map<String, Map<Integer, Double>> insideMap = toYearMap(insideData);
        Map<String, Map<Integer, Double>> outsideMap = toYearMap(outsideData);

        List<Map<String, Object>> results = new ArrayList<>();
        for (String region : insideMap.keySet()) {
            Map<Integer, Double> inYears = insideMap.get(region);
            Map<Integer, Double> outYears = outsideMap.getOrDefault(region, Map.of());

            for (int year : inYears.keySet()) {
                double inVal = inYears.getOrDefault(year, 0d);
                double outVal = outYears.getOrDefault(year, 0d);

                Map<String, Object> row = new LinkedHashMap<>();
                row.put("region", region);
                row.put("year", year);
                row.put("inside", inVal);
                row.put("outside", outVal);
                row.put("gap", outVal - inVal);

                if (year == 2024) {
                    row.put("increase_inside", inVal - inYears.getOrDefault(2023, 0d));
                    row.put("increase_outside", outVal - outYears.getOrDefault(2023, 0d));
                } else if (year == 2025) {
                    row.put("increase_inside", inVal - inYears.getOrDefault(2024, 0d));
                    row.put("increase_outside", outVal - outYears.getOrDefault(2024, 0d));
                }
                results.add(row);
            }
        }
        return results;
    }

    private Map<String, Map<Integer, Double>> toYearMap(List<Map<String, Object>> list) {
        return list.stream().collect(Collectors.groupingBy(
                m -> Objects.toString(m.get("region"), ""),
                Collectors.toMap(
                        m -> (Integer) m.get("year"),
                        m -> ((Number) m.get("value")).doubleValue(),
                        (a, b) -> b, LinkedHashMap::new
                )
        ));
    }

    /* ---------------------------------------------
     * 전임교원 연구 현황(복수 지표/학교/연도 루프)
     * API: /EducationResearchService/getComparisonFullTimeFacultyResearchCrntSt
     * 사용 필드: svyYr, indctId, schlKrnNm, indctVal1
     * --------------------------------------------- */
    public List<Map<String, Object>> getComparisonFullTimeFacultyResearchCrntSt()
            throws JsonProcessingException {

        List<String> schlIds = List.of(schlId, "0000109", "0000051");
        List<Integer> indctIds = List.of(66, 67);
        List<Integer> years = List.of(2022, 2023, 2024);

        List<Map<String, Object>> results = new ArrayList<>();

        for (int year : years) {
            for (String sId : schlIds) {
                for (int indctId : indctIds) {
                    String xml = fetchXml(
                            "/EducationResearchService/getComparisonFullTimeFacultyResearchCrntSt",
                            Map.of("indctId", indctId, "schlId", sId, "svyYr", year)
                    );
                    List<JsonNode> its = items(xml);
                    if (!its.isEmpty()) {
                        JsonNode it = its.get(0);
                        results.add(mapOf(
                                "year", asInt(it, "svyYr", year),
                                "indctId", asInt(it, "indctId", indctId),
                                "schlKrnNm", asText(it, "schlKrnNm"),
                                "value", asDouble(it, "indctVal1", 0d)
                        ));
                    }
                    cooldown();
                }
            }
        }
        return results;
    }
}
