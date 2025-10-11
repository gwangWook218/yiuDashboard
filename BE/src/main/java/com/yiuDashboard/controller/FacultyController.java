package com.yiuDashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.dto.grade.GradeRangeDto;
import com.yiuDashboard.dto.grade.GradeSummaryDto;
import com.yiuDashboard.service.*;
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
    private final GradeDistributionService gradeDistributionService;

//    전임교원 확보 현황
    @GetMapping("/fulltime/ensure/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonFullTimeFacultyEnsureCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonFullTimeFacultyEnsureCrntSt());
    }

    @GetMapping("/fulltime/ensure/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalFullTimeFacultyEnsureCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalFullTimeFacultyEnsureCrntSt());
    }

    //    전임교원 1인당 학생 수
    @GetMapping("/student-ratio/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent() throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent());
    }

    @GetMapping("/student-ratio/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent() throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent());
    }

    //    전임교원 강의담당비율
    @GetMapping("/lecture-ratio/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonLectureChargeRatio() throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getComparisonLectureChargeRatio());
    }

    @GetMapping("/lecture-ratio/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalLectureChargeRatio() throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getRegionalLectureChargeRatio());
    }

//    전임교원 1인당 연구비
    @GetMapping("/research/funding-per-faculty/compare")
    public ResponseEntity<?> getComparisonFullTimeFacultyInsideOfSchoolForPersonResearchGrant(
            @RequestParam String scope) throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getComparisonFullTimeFacultyForPersonResearchGrant(scope));
    }

    @GetMapping("/research/funding-per-faculty/region")
    public ResponseEntity<?> getFacultyWithGap() throws JsonProcessingException {
        return ResponseEntity.ok(facultyService.getFacultyWithGap());
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

    // 학년, 학과별 총 학생수 + 평균 GPA
    @GetMapping("/summary")
    public GradeSummaryDto getGradeSummary(
            @RequestParam int year,
            @RequestParam int semester,
            @RequestParam String dept
    ) {
        return gradeDistributionService.getGradeSummary(year, semester, dept);
    }

    // 학년, 학과별 점수 구간별 학생 수
    @GetMapping("/range")
    public List<GradeRangeDto> getGradeRangeSummary(
            @RequestParam int year,
            @RequestParam int semester,
            @RequestParam String dept
    ) {
        return gradeDistributionService.getGradeRangeSummary(year, semester, dept);
    }
}
