package com.yiuDashboard.controller;

import com.yiuDashboard.dto.NewAdmissionStatsDto;
import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.service.NewAdmissionStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admission-stats")
@RequiredArgsConstructor
public class NewAdmissionStatsController {

    private final NewAdmissionStatsService service;

    @GetMapping
    public ResponseEntity<List<NewAdmissionStats>> getAllByYear(@RequestParam int year) {
        return ResponseEntity.ok(service.getAllByYear(year));
    }

    @GetMapping("/one")
    public ResponseEntity<NewAdmissionStatsDto> getOne(
            @RequestParam int year,
            @RequestParam int deptId) {
        return ResponseEntity.ok(service.getOneDto(year, deptId));
    }

    @GetMapping("/fill-rate")
    public ResponseEntity<Double> getFillRate(
            @RequestParam int year,
            @RequestParam int deptId) {
        return ResponseEntity.ok(service.getFillRate(year, deptId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<NewAdmissionStats>> getAllLegacy() {
        return ResponseEntity.ok(service.getAllStats());
    }

    @GetMapping("/category/{majorCategory}")
    public ResponseEntity<List<NewAdmissionStats>> byCategory(@PathVariable String majorCategory) {
        return ResponseEntity.ok(service.getStatsByMajorCategory(majorCategory));
    }

    @GetMapping("/fill-rate/{departmentName}")
    public ResponseEntity<Double> fillRateLegacy(@PathVariable String departmentName) {
        return ResponseEntity.ok(service.getFillRateByDepartment(departmentName));
    }

    @PostMapping
    public ResponseEntity<NewAdmissionStats> save(@RequestBody NewAdmissionStats body) {
        return ResponseEntity.ok(service.saveStats(body));
    }
}