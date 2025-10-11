package com.yiuDashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yiuDashboard.dto.DropoutRateDTO;
import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final EnrollmentService enrollmentService;
    private final EmploymentRateService employmentRateService;
    private final DropoutRateService dropoutRateService;

//    전임교원 확보 현황
//    대학비교통계
    @GetMapping("/ensure/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonFullTimeFacultyEnsureCrntSt() throws JsonProcessingException, InterruptedException {
        return ResponseEntity.ok(adminService.getComparisonFullTimeFacultyEnsureCrntSt());
    }

//    지역별통계
    @GetMapping("/ensure/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalFullTimeFacultyEnsureCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalFullTimeFacultyEnsureCrntSt());
    }

//    우리대학경쟁력
    @GetMapping("/ensure/notice")
    public String getNoticeFullTimeFacultyEnsureRate(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getNoticeFullTimeFacultyEnsureRate(year, schlId).block();
    }

//    전임교원 1인당 학생 수
    @GetMapping("/student-ratio/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent());
    }

    @GetMapping("/student-ratio/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent());
    }

//    전임교원 강의담당비율
    @GetMapping("/lecture-ratio/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonLectureChargeRatio() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonLectureChargeRatio());
    }

    @GetMapping("/lecture-ratio/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalLectureChargeRatio() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalLectureChargeRatio());
    }

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

//    취업률
    @GetMapping("/graduater/employ")
    public ResponseEntity<List<Map<String, Object>>> getNoticeGraduateEmploymentRate() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getNoticeGraduateEmploymentRate());
    }

//    중도탈락 학생비율
    @GetMapping("/dropout/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonDropOutStudentCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonDropOutStudentCrntSt());
    }

    @GetMapping("/dropout/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalDropOutStudentCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalDropOutStudentCrntSt());
    }

    @GetMapping("/dropout/notice")
    public String getNoticeStudentsWastageRate(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getNoticeStudentsWastageRate(year, schlId).block();
    }

//    학과별 중도탈락현황 (AI융합대학 전체)
    @GetMapping("/dropout/ai")
    public List<DropoutRateDTO> getDropoutStats(@RequestParam int year) {
        return dropoutRateService.getDropoutStats(year);
    }

//    학과별 중도탈락현황 (학과별 상세)
    @GetMapping("/dropout/detail")
    public DropoutRateDTO getDropoutDetail(@RequestParam int year, @RequestParam int deptId) {
        return dropoutRateService.getDropoutDetail(year, deptId);
    }

//    학과별 재학생 수
    @GetMapping("/enrollment/summary")
    public List<EnrollmentSummaryDto> findByYear(@RequestParam int year, @RequestParam int deptId) {
        return enrollmentService.findByYear(year, deptId);
    }

//    1인당 장학금 지급액
    @GetMapping("/scholarship/per/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonScholarshipBenefitCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonScholarshipBenefitCrntSt());
    }

    @GetMapping("/scholarship/per/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalScholarshipBenefitCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalScholarshipBenefitCrntSt());
    }

//    학생 1인당 교육비(대학비교통계)
    @GetMapping("/education/cost/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonEducationalExpensesReductionCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonEducationalExpensesReductionCrntSt());
    }

    @GetMapping("/education/cost/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalEducationalExpensesReductionCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalEducationalExpensesReductionCrntSt());
    }

//    기숙사 수용률
    @GetMapping("/dormitory/compare")
    public ResponseEntity<List<Map<String, Object>>> getComparisonDormitoryAcceptanceCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getComparisonDormitoryAcceptanceCrntSt());
    }

    @GetMapping("/dormitory/region")
    public ResponseEntity<List<Map<String, Object>>> getRegionalDormitoryAcceptanceCrntSt() throws JsonProcessingException {
        return ResponseEntity.ok(adminService.getRegionalDormitoryAcceptanceCrntSt());
    }

//    졸업생 취업률
    @GetMapping("/employment-rates")
    public GraduateStatsDTO findEmployRateByYear(@RequestParam int year, @RequestParam int deptId) {
        return employmentRateService.getGraduateStats(year, deptId);
    }
}
