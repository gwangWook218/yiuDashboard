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

    @GetMapping
    public ResponseEntity<List<NewAdmissionStats>> getAllStats() {
        return ResponseEntity.ok(service.getAllStats());
    }

    @GetMapping("/category/{majorCategory}")
    public ResponseEntity<List<NewAdmissionStats>> getStatsByMajorCategory(@PathVariable String majorCategory) {
        return ResponseEntity.ok(service.getStatsByMajorCategory(majorCategory));
    }

    @GetMapping("/fill-rate/{departmentName}")
    public ResponseEntity<Double> getFillRateByDepartment(@PathVariable String departmentName) {
        double fillRate = service.getFillRateByDepartment(departmentName);
        return ResponseEntity.ok(fillRate);
    }

    @PostMapping
    public ResponseEntity<NewAdmissionStats> saveStats(@RequestBody NewAdmissionStats stats) {
        return ResponseEntity.ok(service.saveStats(stats));
    }

    @GetMapping("/comparison/{departmentName}")
    public ResponseEntity<Map<String, Object>> getDepartmentAdmissionComparison(@PathVariable String departmentName) {
        return ResponseEntity.ok(service.getDepartmentAdmissionComparison(departmentName));
    }

    @GetMapping("/career-summary/{departmentName}")
    public ResponseEntity<String> getGraduateCareerSummary(@PathVariable String departmentName) {
        String summary = service.getGraduateCareerSummary(departmentName);
        return ResponseEntity.ok(summary);
    }
    // 전공 정보 카드 조회 API
    @GetMapping("/major-info/{departmentName}")
    public ResponseEntity<String> getMajorInfoSummary(@PathVariable String departmentName) {
        String summary = service.getMajorInfoSummary(departmentName);
        return ResponseEntity.ok(summary);
    }
}
