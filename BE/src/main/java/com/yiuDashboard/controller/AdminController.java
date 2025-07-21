package com.yiuDashboard.controller;

import com.yiuDashboard.dto.AdmissionRateDTO;
import com.yiuDashboard.service.AdminService;
import com.yiuDashboard.service.GraduateAdmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/faculty")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final GraduateAdmissionService admissionService;

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
}
