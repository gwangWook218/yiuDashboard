package com.yiuDashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.service.AdminService;
import com.yiuDashboard.service.EmploymentRateService;
import com.yiuDashboard.service.EnrollmentService;
import com.yiuDashboard.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final AdminService adminService;
    private final FacultyService facultyService;
    private final EnrollmentService enrollmentService;
    private final EmploymentRateService employmentRateService;

//    전임교원 확보 현황
    @GetMapping("/fulltime/ensure/compare")
    public String getComparisonFullTimeFacultyEnsureCrntSt(@RequestParam int year, @RequestParam int indctId, @RequestParam String schlId) {
        return adminService.getComparisonFullTimeFacultyEnsureCrntSt(year, indctId, schlId).block();
    }

    @GetMapping("/fulltime/ensure/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalFullTimeFacultyEnsureCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalFullTimeFacultyEnsureCrntSt());
    }

    //    전임교원 1인당 학생 수
    @GetMapping("/student-ratio/compare")
    public String getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(@RequestParam int year, @RequestParam String schlId) {
        return facultyService.getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(year, schlId).block();
    }

    @GetMapping("/student-ratio/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent() throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent());
    }

    //    전임교원 강의담당비율
    @GetMapping("/lecture-ratio/compare")
    public String getComparisonLectureChargeRatio(@RequestParam int year, @RequestParam String schlId) {
        return facultyService.getComparisonLectureChargeRatio(year, schlId).block();
    }

    @GetMapping("/lecture-ratio/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalLectureChargeRatio() throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getRegionalLectureChargeRatio());
    }

//    전임교원 1인당 연구비
    @GetMapping("/research/funding-per-faculty/compare")
    public ResponseEntity<String> getComparisonFullTimeFacultyInsideOfSchoolForPersonResearchGrant(
            @RequestParam int year,
            @RequestParam String scope,
            @RequestParam String schlId) {
        if ("inside".equalsIgnoreCase(scope)) {
            return ResponseEntity.ok(facultyService.getComparisonFullTimeFacultyInsideOfSchoolForPersonResearchGrant(year, schlId).block());
        } else if ("outside".equalsIgnoreCase(scope)) {
            return ResponseEntity.ok(facultyService.getComparisonFullTimeFacultyOutsideOfSchoolForPersonResearchGrant(year, schlId).block());
        } else {
            return ResponseEntity.badRequest().body("Invalid scope parameter. Use 'inside' or 'outside'.");
        }
    }

    @GetMapping("/research/funding-per-faculty/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalFullTimeFacultyInsideOfSchoolForPersonResearchGrant(@RequestParam String scope) throws JsonProcessingException {
        if ("inside".equalsIgnoreCase(scope)) {
            return ResponseEntity.ok(facultyService.getRegionalFullTimeFacultyInsideOfSchoolForPersonResearchGrant());
        } else if ("outside".equalsIgnoreCase(scope)) {
            return ResponseEntity.ok(facultyService.getRegionalFullTimeFacultyOutsideOfSchoolForPersonResearchGrant());
        }
        return null;
    }

//    재학생 수
    @GetMapping("/department/students/count")
    public List<EnrollmentSummaryDto> findByYear(@RequestParam int year, @RequestParam int deptId) {
        return enrollmentService.findByYear(year, deptId);
    }

//    졸업생 진학/취업 현황
    @GetMapping("/department/graduates/employment-rates")
    public GraduateStatsDTO findEmployRateByYear(@RequestParam int year, @RequestParam int deptId) {
        return employmentRateService.getGraduateStats(year, deptId);
    }
}
