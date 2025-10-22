package com.yiuDashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.yiuDashboard.dto.RecruitmentRateDto;
import com.yiuDashboard.repository.RecruitmentRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Year;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PublicMainService {

    private final WebClient webClient;
    private final RecruitmentRateRepository recruitmentRateRepository;

    @Value("${univapi.service-key}")
    private String serviceKey;

    @Value("${univapi.school-id}")
    private String schoolId;

    private int defaultYear() { return Year.now().getValue(); }
    private String requireSchoolId() { return (schoolId == null || schoolId.isBlank()) ? "0000156" : schoolId; }

    /* ───────── students ───────── */
    public List<Map<String, Object>> getComparisonEnrolledStudent(int year) throws JsonProcessingException {
        List<Map<String, Object>> results = new ArrayList<>();
        String xmlResponse = webClient.get()
                .uri(u -> u.path("/StudentService/getComparisonEnrolledStudent")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", requireSchoolId())
                        .queryParam("svyYr", year).build())
                .retrieve().bodyToMono(String.class).block();

        XmlMapper xml = new XmlMapper();
        JsonNode items = xml.readTree(xmlResponse).path("body").path("items").path("item");

        Map<String, Object> map = new HashMap<>();
        map.put("year", items.path("svyYr").asInt());
        map.put("schlKrnNm", items.path("schlKrnNm").asText());
        map.put("value", items.path("indctVal1").asInt());
        results.add(map);
        return results;
    }
    public List<Map<String, Object>> getComparisonEnrolledStudent() throws JsonProcessingException {
        return getComparisonEnrolledStudent(defaultYear());
    }

    /* ───────── foreign students ───────── */
    public List<Map<String, Object>> getComparisonForeignStudentCrntSt(int year) throws JsonProcessingException {
        List<Map<String, Object>> results = new ArrayList<>();
        String xmlResponse = webClient.get()
                .uri(u -> u.path("/StudentService/getComparisonForeignStudentCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", requireSchoolId())
                        .queryParam("svyYr", year).build())
                .retrieve().bodyToMono(String.class).block();

        XmlMapper xml = new XmlMapper();
        JsonNode items = xml.readTree(xmlResponse).path("body").path("items").path("item");

        Map<String, Object> map = new HashMap<>();
        map.put("year", items.path("svyYr").asInt());
        map.put("schlKrnNm", items.path("schlKrnNm").asText());
        map.put("value", items.path("indctVal1").asInt());
        results.add(map);
        return results;
    }
    public List<Map<String, Object>> getComparisonForeignStudentCrntSt() throws JsonProcessingException {
        return getComparisonForeignStudentCrntSt(defaultYear());
    }

    /* ───────── faculty ───────── */
    public List<Map<String, Object>> getNoticeFullTimeFacultyEnsureRate(int year) throws JsonProcessingException {
        List<Map<String, Object>> results = new ArrayList<>();
        String xmlResponse = webClient.get()
                .uri(u -> u.path("/EducationResearchService/getNoticeFullTimeFacultyEnsureRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", requireSchoolId())
                        .queryParam("svyYr", year).build())
                .retrieve().bodyToMono(String.class).block();

        XmlMapper xml = new XmlMapper();
        JsonNode items = xml.readTree(xmlResponse).path("body").path("items").path("item");

        Map<String, Object> map = new HashMap<>();
        map.put("year", items.path("svyYr").asInt());
        map.put("schlKrnNm", items.path("schlKrnNm").asText());
        map.put("value", items.path("indctVal3").asInt());
        results.add(map);
        return results;
    }
    public List<Map<String, Object>> getNoticeFullTimeFacultyEnsureRate() throws JsonProcessingException {
        return getNoticeFullTimeFacultyEnsureRate(defaultYear());
    }

    /* ───────── scholarship (비교) ───────── */
    public List<Map<String, Object>> getComparisonScholarshipBenefitCrntSt(int year) throws JsonProcessingException {
        List<Map<String, Object>> results = new ArrayList<>();
        String xmlResponse = webClient.get()
                .uri(u -> u.path("/FinancesService/getComparisonScholarshipBenefitCrntSt")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", requireSchoolId())
                        .queryParam("svyYr", year).build())
                .retrieve().bodyToMono(String.class).block();

        XmlMapper xml = new XmlMapper();
        JsonNode items = xml.readTree(xmlResponse).path("body").path("items").path("item");

        Map<String, Object> map = new HashMap<>();
        map.put("year", items.path("svyYr").asInt());
        map.put("schlKrnNm", items.path("schlKrnNm").asText());
        map.put("value", items.path("indctVal1").asDouble());
        results.add(map);
        return results;
    }
    public List<Map<String, Object>> getComparisonScholarshipBenefitCrntSt() throws JsonProcessingException {
        return getComparisonScholarshipBenefitCrntSt(defaultYear());
    }

    /* ───────── graduate employment (공지) ───────── */
    public List<Map<String, Object>> getNoticeGraduateEmploymentRate(int year) throws JsonProcessingException {
        List<Map<String, Object>> results = new ArrayList<>();
        String xmlResponse = webClient.get()
                .uri(u -> u.path("/StudentService/getNoticeGraduateEmploymentRate")
                        .queryParam("ServiceKey", serviceKey)
                        .queryParam("schlId", requireSchoolId())
                        .queryParam("svyYr", year).build())
                .retrieve().bodyToMono(String.class).block();

        XmlMapper xml = new XmlMapper();
        JsonNode items = xml.readTree(xmlResponse).path("body").path("items").path("item");

        Map<String, Object> map = new HashMap<>();
        map.put("year", items.path("svyYr").asInt());
        map.put("schlKrnNm", items.path("schlKrnNm").asText());
        map.put("value", items.path("indctVal4").asDouble());
        results.add(map);
        return results;
    }
    public List<Map<String, Object>> getNoticeGraduateEmploymentRate() throws JsonProcessingException {
        return getNoticeGraduateEmploymentRate(defaultYear());
    }

    /* ───────── staff (임직원 수) ───────── */
    public List<Map<String, Object>> getStaffCount(int year) {
        Map<String, Object> map = new HashMap<>();
        map.put("year", year);
        map.put("schlKrnNm", "용인대학교");
        map.put("staff_count", 126); // ✅ 임직원 수를 126으로 고정
        return List.of(map);
    }

    public List<Map<String, Object>> getStaffCount() {
        return getStaffCount(defaultYear());
    }

    /* ───────── recruitment rate (수시/정시 비율) ───────── */
    public List<RecruitmentRateDto> findRecruitmentRateByYear(int year) {
        return recruitmentRateRepository.findRecruitmentRateByYear(year);
    }
    public List<RecruitmentRateDto> findRecruitmentRateByYear() {
        return findRecruitmentRateByYear(defaultYear());
    }
}
