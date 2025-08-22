package com.yiuDashboard.controller;

import com.yiuDashboard.dto.AdmissionRateDTO;
import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.service.AdminService;
import com.yiuDashboard.service.EnrollmentService;
import com.yiuDashboard.service.GraduateAdmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/ensure/compare")
    public String getComparisonFullTimeFacultyEnsureCrntSt(@Param("year") int year, @Param("indctId") int indctId, Authentication authentication) {
        return adminService.getComparisonFullTimeFacultyEnsureCrntSt(year, indctId).block();
    }

    @GetMapping("/ensure/region")
    public String getRegionalFullTimeFacultyEnsureCrntSt(Authentication authentication) {
        return adminService.getRegionalFullTimeFacultyEnsureCrntSt().block();
    }

    @GetMapping("/ensure/notice")
    public String getNoticeFullTimeFacultyEnsureRate(@Param("year") int year, Authentication authentication) {
        return adminService.getNoticeFullTimeFacultyEnsureRate(year).block();
    }

    @GetMapping("/stdntNum/compare")
    public String getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(year).block();
    }

    @GetMapping("/stdntNum/region")
    public String getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent(Authentication authentication) {
        return adminService.getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent().block();
    }

    @GetMapping("/lecRatio/compare")
    public String getComparisonLectureChargeRatio(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonLectureChargeRatio(year).block();
    }

    @GetMapping("/lecRatio/region")
    public String getRegionalLectureChargeRatio(Authentication authentication) {
        return adminService.getRegionalLectureChargeRatio().block();
    }

    @GetMapping("/enrolledStdnt/compare")
    public String getComparisonEnrolledStudentEnsureRate(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonEnrolledStudentEnsureRate(year).block();
    }

    @GetMapping("/freshCompetite/compare")
    public String getComparisonInsideFixedNumberFreshmanCompetitionRate(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonInsideFixedNumberFreshmanCompetitionRate(year).block();
    }

    @GetMapping("/register/compare")
    public String getComparisonEntranceModelLastRegistrationRatio(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonEntranceModelLastRegistrationRatio(year).block();
    }

    @GetMapping("/graduater/admission")
    public List<AdmissionRateDTO> getAdmission(@Param("year") int year) {
        return admissionService.getAdmissionRate(year);
    }

    @GetMapping("graduater/employ")
    public String getNoticeGraduateEmploymentRate(@Param("year") int year, Authentication authentication) {
        return adminService.getNoticeGraduateEmploymentRate(year).block();
    }

    @GetMapping("dropout/compare")
    public String getComparisonDropOutStudentCrntSt(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonDropOutStudentCrntSt(year).block();
    }

    @GetMapping("dropout/region")
    public String getRegionalDropOutStudentCrntSt(Authentication authentication) {
        return adminService.getRegionalDropOutStudentCrntSt().block();
    }

    @GetMapping("dropout/notice")
    public String getNoticeStudentsWastageRate(@Param("year") int year, Authentication authentication) {
        return adminService.getNoticeStudentsWastageRate(year).block();
    }

    @GetMapping("/enrollment/summary")
    public ResponseEntity<List<EnrollmentSummaryDto>> getSummary(Authentication authentication) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentSummary());
    }

    @GetMapping("scholarship/per/compare")
    public String getComparisonScholarshipBenefitCrntSt(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonScholarshipBenefitCrntSt(year).block();
    }

    @GetMapping("scholarship/per/region")
    public String getRegionalScholarshipBenefitCrntSt(Authentication authentication) {
        return adminService.getRegionalScholarshipBenefitCrntSt().block();
    }

    @GetMapping("eduCost/per/compare")
    public String getComparisonEducationalExpensesReductionCrntSt(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonEducationalExpensesReductionCrntSt(year).block();
    }

    @GetMapping("eduCost/per/region")
    public String getRegionalEducationalExpensesReductionCrntSt(Authentication authentication) {
        return adminService.getRegionalEducationalExpensesReductionCrntSt().block();
    }

    @GetMapping("dormitory/compare")
    public String getComparisonDormitoryAcceptanceCrntSt(@Param("year") int year, Authentication authentication) {
        return adminService.getComparisonDormitoryAcceptanceCrntSt(year).block();
    }

    @GetMapping("dormitory/region")
    public String getRegionalDormitoryAcceptanceCrntSt(Authentication authentication) {
        return adminService.getRegionalDormitoryAcceptanceCrntSt().block();
    }
}
