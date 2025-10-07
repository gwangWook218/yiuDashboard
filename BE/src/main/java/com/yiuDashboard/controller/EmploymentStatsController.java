package com.yiuDashboard.controller;

import com.yiuDashboard.entity.EmploymentStats;
import com.yiuDashboard.service.EmploymentStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/employment-stats")
@RequiredArgsConstructor
public class EmploymentStatsController {

    private final EmploymentStatsService employmentStatsService;

    // 특정 학과(최신년도)
    @GetMapping("/department")
    public ResponseEntity<List<EmploymentStats>> getStatsByDepartment(@RequestParam String name) {
        return ResponseEntity.ok(employmentStatsService.getStatsByDepartment(name));
    }

    // 전체 학과 정렬(최신년도)
    @GetMapping("/sorted")
    public ResponseEntity<List<EmploymentStats>> getAllStatsSortedByEmploymentRate() {
        return ResponseEntity.ok(employmentStatsService.getAllStatsSortedByEmploymentRate());
    }

    // 기준 취업률 이상(최신년도)
    @GetMapping("/above")
    public ResponseEntity<List<EmploymentStats>> getStatsAboveRate(@RequestParam double rate) {
        return ResponseEntity.ok(employmentStatsService.getStatsAboveRate(rate));
    }

    @PostMapping
    public ResponseEntity<EmploymentStats> saveEmploymentStats(@RequestBody EmploymentStats stats) {
        return ResponseEntity.ok(employmentStatsService.saveEmploymentStats(stats));
    }

    //특정 학과의 졸업생 진출 분야별 비율 조회
    @GetMapping("/career")
    public ResponseEntity<List<EmploymentStats>> getCareerDistribution(@RequestParam String name) {
        List<EmploymentStats> stats = employmentStatsService.getCareerDistributionByDepartment(name);
        return ResponseEntity.ok(stats);
    }
}
