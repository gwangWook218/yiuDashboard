package com.yiuDashboard.controller;

import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.service.NewAdmissionStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admission-stats")
@RequiredArgsConstructor
public class NewAdmissionStatsController {

    private final NewAdmissionStatsService service;

    // 전체 리스트
    @GetMapping
    public ResponseEntity<List<NewAdmissionStats>> getAllStats() {
        return ResponseEntity.ok(service.getAllStats());
    }

    // 계열(대분류)로 조회
    @GetMapping("/category/{majorCategory}")
    public ResponseEntity<List<NewAdmissionStats>> getStatsByMajorCategory(
            @PathVariable String majorCategory) {
        return ResponseEntity.ok(service.getStatsByMajorCategory(majorCategory));
    }

    // 학과 충원율(계산값)
    @GetMapping("/fill-rate/{departmentName}")
    public ResponseEntity<Double> getFillRateByDepartment(@PathVariable String departmentName) {
        return ResponseEntity.ok(service.getFillRateByDepartment(departmentName));
    }

    // 저장(저장 전에 서비스에서 fillRate 계산 반영)
    @PostMapping
    public ResponseEntity<NewAdmissionStats> saveStats(@RequestBody NewAdmissionStats stats) {
        return ResponseEntity.ok(service.saveStats(stats));
    }

    // 학과별 정원/재학생/충원율 vs 유사계열 평균 비교(맵 반환 유지)
    @GetMapping("/comparison/{departmentName}")
    public ResponseEntity<Map<String, Object>> getDepartmentAdmissionComparison(
            @PathVariable String departmentName) {
        return ResponseEntity.ok(service.getDepartmentAdmissionComparison(departmentName));
    }

    // -------- 전공 정보 카드/요약 --------

    // (A) 전공 정보 요약 - 학과명 PathVariable 버전
    @GetMapping("/major-info/{departmentName}")
    public ResponseEntity<String> getMajorInfoSummary(@PathVariable String departmentName) {
        return ResponseEntity.ok(service.getMajorInfoSummary(departmentName));
    }

    // (B) 졸업생 진로 요약 - 학과명 PathVariable 버전
    @GetMapping("/career-summary/{departmentName}")
    public ResponseEntity<String> getGraduateCareerSummary(@PathVariable String departmentName) {
        return ResponseEntity.ok(service.getGraduateCareerSummary(departmentName));
    }
}
