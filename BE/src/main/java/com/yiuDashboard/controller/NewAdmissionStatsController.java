package com.yiuDashboard.controller;

import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.service.NewAdmissionStatsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/new-admission-stats")
public class NewAdmissionStatsController {

    private final NewAdmissionStatsService service;

    public NewAdmissionStatsController(NewAdmissionStatsService service) {
        this.service = service;
    }

    @GetMapping
    public List<NewAdmissionStats> getAllStats() {
        return service.getAllStats();
    }

    @GetMapping("/category")
    public List<NewAdmissionStats> getByCategory(@RequestParam String category) {
        return service.getStatsByCategory(category);
    }

    @PostMapping
    public NewAdmissionStats addStat(@RequestBody NewAdmissionStats stat) {
        return service.save(stat);
    }

    @GetMapping("/fill-rate")
    public double getFillRateByDepartment(@RequestParam String departmentCode) {
        return service.getFillRateByDepartment(departmentCode);
    }

    @GetMapping("/average-fill-rate")
    public double getAverageFillRateBySimilarMajor(@RequestParam String majorCategory) {
        return service.getAverageFillRateBySimilarMajor(majorCategory);
    }

    @GetMapping("/graduate-trend")
    public ResponseEntity<String> getGraduateTrend(@RequestParam String departmentName) {
        String trend = service.getGraduateTrendByDepartmentName(departmentName);
        return ResponseEntity.ok(trend);
    }


}

