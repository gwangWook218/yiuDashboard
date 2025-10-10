package com.yiuDashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.service.AdminService;
import com.yiuDashboard.service.EmploymentRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final AdminService adminService;
    private final EmploymentRateService employmentRateService;

    //    충원률(대학비교통계)
    @GetMapping("/enroll-rate/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonEnrolledStudentEnsureRate() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonEnrolledStudentEnsureRate());
    }

    //    입시 경쟁률(대학비교통계)
    @GetMapping("/competition/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonInsideFixedNumberFreshmanCompetitionRate() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonInsideFixedNumberFreshmanCompetitionRate());
    }

    //    등록률(대학비교통계)
    @GetMapping("/register/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonEntranceModelLastRegistrationRatio() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonEntranceModelLastRegistrationRatio());
    }

//    졸업생 취업률
    @GetMapping("/graduates/employment-rates")
    public GraduateStatsDTO findEmployRateByYear(@RequestParam int year, @RequestParam int deptId) {
        return employmentRateService.getGraduateStats(year, deptId);
    }
}
