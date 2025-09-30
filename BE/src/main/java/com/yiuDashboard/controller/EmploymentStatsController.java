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

    //특정 학과 취업 통계 조회
    @GetMapping("/department")
    public ResponseEntity<List<EmploymentStats>> getStatsByDepartment(@RequestParam String name) {
        List<EmploymentStats> stats = employmentStatsService.getStatsByDepartment(name);
        return ResponseEntity.ok(stats);
    }

    //모든 학과 취업 통계 조회
    @GetMapping("/sorted")
    public ResponseEntity<List<EmploymentStats>> getAllStatsSortedByEmploymentRate() {
        List<EmploymentStats> stats = employmentStatsService.getAllStatsSortedByEmploymentRate();
        return ResponseEntity.ok(stats);
    }

    //특정 취업률 이상 학과 조회
    @GetMapping("/above")
    public ResponseEntity<List<EmploymentStats>> getStatsAboveRate(@RequestParam double rate) {
        List<EmploymentStats> stats = employmentStatsService.getStatsAboveRate(rate);
        return ResponseEntity.ok(stats);
    }

    //학과 취업 통계 데이터 저장 (관리자용)
    @PostMapping
    public ResponseEntity<EmploymentStats> saveEmploymentStats(@RequestBody EmploymentStats stats) {
        EmploymentStats saved = employmentStatsService.saveEmploymentStats(stats);
        return ResponseEntity.ok(saved);
    }

    //특정 학과의 졸업생 진출 분야별 비율 조회
    @GetMapping("/career")
    public ResponseEntity<List<EmploymentStats>> getCareerDistribution(@RequestParam String name) {
        List<EmploymentStats> stats = employmentStatsService.getCareerDistributionByDepartment(name);
        return ResponseEntity.ok(stats);
    }
}
