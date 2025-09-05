package com.yiuDashboard.controller;

import com.yiuDashboard.dto.AdmissionRateDTO;
import com.yiuDashboard.dto.DropoutRateDTO;
import com.yiuDashboard.dto.gradEmployment.DepartmentEmploymentDTO;
import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.dto.gradEmployment.EmploymentRankingDTO;
import com.yiuDashboard.dto.gradEmployment.EmploymentRateResponseDTO;
import com.yiuDashboard.dto.gradEmployment.EmploymentTrendDTO;
import com.yiuDashboard.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final GraduateAdmissionService admissionService;
    private final EnrollmentService enrollmentService;
    private final EmploymentRateService employmentRateService;
    private final DropoutRateService dropoutRateService;

//    전임교원 확보 현황
//    대학비교통계
    @GetMapping("/ensure/compare")
    public String getComparisonFullTimeFacultyEnsureCrntSt(@Param("year") int year, @Param("indctId") int indctId, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonFullTimeFacultyEnsureCrntSt(year, indctId, schlId).block();
    }

//    지역별통계
    @GetMapping("/ensure/region")
    public String getRegionalFullTimeFacultyEnsureCrntSt(Authentication authentication) {
        return adminService.getRegionalFullTimeFacultyEnsureCrntSt().block();
    }

//    우리대학경쟁력
    @GetMapping("/ensure/notice")
    public String getNoticeFullTimeFacultyEnsureRate(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getNoticeFullTimeFacultyEnsureRate(year, schlId).block();
    }

//    전임교원 1인당 학생 수
    @GetMapping("/stdntNum/compare")
    public String getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(year, schlId).block();
    }

    @GetMapping("/stdntNum/region")
    public String getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent(Authentication authentication) {
        return adminService.getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent().block();
    }

//    전임교원 강의담당비율
    @GetMapping("/lecRatio/compare")
    public String getComparisonLectureChargeRatio(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonLectureChargeRatio(year, schlId).block();
    }

    @GetMapping("/lecRatio/region")
    public String getRegionalLectureChargeRatio(Authentication authentication) {
        return adminService.getRegionalLectureChargeRatio().block();
    }

//    충원률(대학비교통계)
    @GetMapping("/enrolledStdnt/compare")
    public String getComparisonEnrolledStudentEnsureRate(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonEnrolledStudentEnsureRate(year, schlId).block();
    }
//    입시 경쟁률(대학비교통계)
    @GetMapping("/freshCompetite/compare")
    public String getComparisonInsideFixedNumberFreshmanCompetitionRate(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonInsideFixedNumberFreshmanCompetitionRate(year, schlId).block();
    }

//    등록률(대학비교통계)
    @GetMapping("/register/compare")
    public String getComparisonEntranceModelLastRegistrationRatio(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonEntranceModelLastRegistrationRatio(year, schlId).block();
    }

//    진학률
    @GetMapping("/graduater/admission")
    public List<AdmissionRateDTO> getAdmission(@Param("year") int year, Authentication authentication) {
        return admissionService.getAdmissionRate(year);
    }

//    취업률
    @GetMapping("graduater/employ")
    public String getNoticeGraduateEmploymentRate(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getNoticeGraduateEmploymentRate(year, schlId).block();
    }

//    중도탈락 학생비율
    @GetMapping("dropout/compare")
    public String getComparisonDropOutStudentCrntSt(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonDropOutStudentCrntSt(year, schlId).block();
    }

    @GetMapping("dropout/region")
    public String getRegionalDropOutStudentCrntSt(Authentication authentication) {
        return adminService.getRegionalDropOutStudentCrntSt().block();
    }

    @GetMapping("dropout/notice")
    public String getNoticeStudentsWastageRate(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getNoticeStudentsWastageRate(year, schlId).block();
    }

    @GetMapping("dropout/dept")
    public List<DropoutRateDTO> findByDeptName(@Param("year") String year, Authentication authentication) {
        return dropoutRateService.findByDeptName(year);
    }

//    학과별 재학생 수
    @GetMapping("/enrollment/summary")
    public List<EnrollmentSummaryDto> findByYear(@Param("year") int year, Authentication authentication) {
        return enrollmentService.findByYear(year);
    }

//    1인당 장학금 지급액
    @GetMapping("scholarship/per/compare")
    public String getComparisonScholarshipBenefitCrntSt(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonScholarshipBenefitCrntSt(year, schlId).block();
    }

    @GetMapping("scholarship/per/region")
    public String getRegionalScholarshipBenefitCrntSt(Authentication authentication) {
        return adminService.getRegionalScholarshipBenefitCrntSt().block();
    }

//    학생 1인당 교육비(대학비교통계)
    @GetMapping("education/cost/compare")
    public String getComparisonEducationalExpensesReductionCrntSt(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonEducationalExpensesReductionCrntSt(year, schlId).block();
    }

    @GetMapping("education/cost/region")
    public String getRegionalEducationalExpensesReductionCrntSt(Authentication authentication) {
        return adminService.getRegionalEducationalExpensesReductionCrntSt().block();
    }

//    기숙사 수용률
    @GetMapping("dormitory/compare")
    public String getComparisonDormitoryAcceptanceCrntSt(@Param("year") int year, @Param("schlId") String schlId, Authentication authentication) {
        return adminService.getComparisonDormitoryAcceptanceCrntSt(year, schlId).block();
    }

    @GetMapping("dormitory/region")
    public String getRegionalDormitoryAcceptanceCrntSt(Authentication authentication) {
        return adminService.getRegionalDormitoryAcceptanceCrntSt().block();
    }

//    졸업생 취업률
    @GetMapping("/employment-rates")
    public EmploymentRateResponseDTO findEmployRateByYear(@RequestParam int year) {
        return employmentRateService.getEmploymentRateByYear(year);
    }

    @GetMapping("/employment-rates/trend")
    public EmploymentTrendDTO findTrendByYear(@RequestParam int departmentId) {
        return employmentRateService.getTrendByYear(departmentId);
    }

    @GetMapping("/employment-rates/ranking")
    public EmploymentRankingDTO getRanking(@RequestParam int year, @RequestParam(defaultValue = "5") int top) {
        return employmentRateService.getRankByYear(year, top);
    }
}
