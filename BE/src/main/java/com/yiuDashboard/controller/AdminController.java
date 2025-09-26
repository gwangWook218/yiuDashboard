package com.yiuDashboard.controller;

import com.yiuDashboard.dto.DropoutRateDTO;
import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String getComparisonFullTimeFacultyEnsureCrntSt(@RequestParam int year, @RequestParam int indctId, @RequestParam String schlId) {
        return adminService.getComparisonFullTimeFacultyEnsureCrntSt(year, indctId, schlId).block();
    }

//    지역별통계
    @GetMapping("/ensure/region")
    public String getRegionalFullTimeFacultyEnsureCrntSt() {
        return adminService.getRegionalFullTimeFacultyEnsureCrntSt().block();
    }

//    우리대학경쟁력
    @GetMapping("/ensure/notice")
    public String getNoticeFullTimeFacultyEnsureRate(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getNoticeFullTimeFacultyEnsureRate(year, schlId).block();
    }

//    전임교원 1인당 학생 수
    @GetMapping("/stdntNum/compare")
    public String getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(year, schlId).block();
    }

    @GetMapping("/stdntNum/region")
    public String getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent() {
        return adminService.getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent().block();
    }

//    전임교원 강의담당비율
    @GetMapping("/lecRatio/compare")
    public String getComparisonLectureChargeRatio(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonLectureChargeRatio(year, schlId).block();
    }

    @GetMapping("/lecRatio/region")
    public String getRegionalLectureChargeRatio() {
        return adminService.getRegionalLectureChargeRatio().block();
    }

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

//    취업률
    @GetMapping("/graduater/employ")
    public String getNoticeGraduateEmploymentRate(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getNoticeGraduateEmploymentRate(year, schlId).block();
    }

//    중도탈락 학생비율
    @GetMapping("/dropout/compare")
    public String getComparisonDropOutStudentCrntSt(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonDropOutStudentCrntSt(year, schlId).block();
    }

    @GetMapping("/dropout/region")
    public String getRegionalDropOutStudentCrntSt() {
        return adminService.getRegionalDropOutStudentCrntSt().block();
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
    public DropoutRateDTO getDropoutDetail(@RequestParam int year, @RequestParam String dept) {
        return dropoutRateService.getDropoutDetail(year, dept);
    }

//    학과별 재학생 수
    @GetMapping("/enrollment/summary")
    public List<EnrollmentSummaryDto> findByYear(@RequestParam int year) {
        return enrollmentService.findByYear(year);
    }

//    1인당 장학금 지급액
    @GetMapping("/scholarship/per/compare")
    public String getComparisonScholarshipBenefitCrntSt(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonScholarshipBenefitCrntSt(year, schlId).block();
    }

    @GetMapping("/scholarship/per/region")
    public String getRegionalScholarshipBenefitCrntSt() {
        return adminService.getRegionalScholarshipBenefitCrntSt().block();
    }

//    학생 1인당 교육비(대학비교통계)
    @GetMapping("/education/cost/compare")
    public String getComparisonEducationalExpensesReductionCrntSt(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonEducationalExpensesReductionCrntSt(year, schlId).block();
    }

    @GetMapping("/education/cost/region")
    public String getRegionalEducationalExpensesReductionCrntSt() {
        return adminService.getRegionalEducationalExpensesReductionCrntSt().block();
    }

//    기숙사 수용률
    @GetMapping("/dormitory/compare")
    public String getComparisonDormitoryAcceptanceCrntSt(@RequestParam int year, @RequestParam String schlId) {
        return adminService.getComparisonDormitoryAcceptanceCrntSt(year, schlId).block();
    }

    @GetMapping("/dormitory/region")
    public String getRegionalDormitoryAcceptanceCrntSt() {
        return adminService.getRegionalDormitoryAcceptanceCrntSt().block();
    }

//    졸업생 취업률
    @GetMapping("/employment-rates")
    public GraduateStatsDTO findEmployRateByYear(@RequestParam int year, @RequestParam String department) {
        return employmentRateService.getGraduateStats(year, department);
    }
}
