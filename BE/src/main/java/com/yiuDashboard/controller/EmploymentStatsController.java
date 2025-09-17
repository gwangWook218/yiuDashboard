package com.yiuDashboard.controller;

import com.yiuDashboard.service.EmploymentStatsService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employment-stats")
@RequiredArgsConstructor
public class EmploymentStatsController {

    private final EmploymentStatsService employmentStatsService;

    @GetMapping("/employment-rate")
    public ResponseEntity<Map<String, Object>> getEmploymentRate(@RequestParam int year, @RequestParam int deptId) {
        return ResponseEntity.ok(employmentStatsService.getEmploymentRateSummary(year, deptId));
    }

    @GetMapping("/career-distribution")
    public ResponseEntity<Map<String, Object>> getCareerDistribution(@RequestParam int year, @RequestParam int deptId) {
        return ResponseEntity.ok(employmentStatsService.getCareerDistribution(year, deptId));
    }
}
