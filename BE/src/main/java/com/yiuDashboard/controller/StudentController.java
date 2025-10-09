package com.yiuDashboard.controller;

import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.service.AdminService;
import com.yiuDashboard.service.EmploymentRateService;
import com.yiuDashboard.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final AdminService adminService;
    private final EmploymentRateService employmentRateService;

    //    충원률(대학비교통계)
    @GetMapping("/enrolledStdnt/compare")
    public String getComparisonEnrolledStudentEnsureRate(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonEnrolledStudentEnsureRate(year, schlId).block();
    }
    //    입시 경쟁률(대학비교통계)
    @GetMapping("/freshCompetite/compare")
    public String getComparisonInsideFixedNumberFreshmanCompetitionRate(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonInsideFixedNumberFreshmanCompetitionRate(year, schlId).block();
    }

    //    등록률(대학비교통계)
    @GetMapping("/register/compare")
    public String getComparisonEntranceModelLastRegistrationRatio(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonEntranceModelLastRegistrationRatio(year, schlId).block();
    }

//    졸업생 취업률
    @GetMapping("/graduates/employment-rates")
    public GraduateStatsDTO findEmployRateByYear(@RequestParam int year, @RequestParam int deptId) {
        return employmentRateService.getGraduateStats(year, deptId);
    }
}
