package com.yiuDashboard.controller;

import com.yiuDashboard.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/faculty")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/ensure/compare/{year}")
    public String getComparisonFullTimeFacultyEnsureCrntSt(@PathVariable int year, Authentication authentication) {
        return adminService.getComparisonFullTimeFacultyEnsureCrntSt(year).block();
    }

    @GetMapping("/ensure/region/{schlDivCd}")
    public String getRegionalFullTimeFacultyEnsureCrntSt(@PathVariable int schlDivCd, Authentication authentication) {
        return adminService.getRegionalFullTimeFacultyEnsureCrntSt(schlDivCd).block();
    }

    @GetMapping("/ensure/notice/{year}")
    public String getNoticeFullTimeFacultyEnsureRate(@PathVariable int year, Authentication authentication) {
        return adminService.getNoticeFullTimeFacultyEnsureRate(year).block();
    }

    @GetMapping("/stdntNum/compare/{year}")
    public String getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(@PathVariable int year, Authentication authentication) {
        return adminService.getComparisonFullTimeFacultyForPersonStudentNumberEnrolledStudent(year).block();
    }

    @GetMapping("/stdntNum/region/{schlDivCd}")
    public String getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent(@PathVariable int schlDivCd, Authentication authentication) {
        return adminService.getRegionalFullTimeFacultyForPersonStudentNumberEnrolledStudent(schlDivCd).block();
    }

    @GetMapping("/lecRatio/compare")
    public String getComparisonLectureChargeRatio(@PathVariable int year, Authentication authentication) {
        return adminService.getComparisonLectureChargeRatio(year).block();
    }

    @GetMapping("/lecRatio/region/{schlDivCd}")
    public String getRegionalLectureChargeRatio(@PathVariable int schlDivCd, Authentication authentication) {
        return adminService.getRegionalLectureChargeRatio(schlDivCd).block();
    }

    @GetMapping("/enrolledStdnt/compare/{year}")
    public String getComparisonEnrolledStudentEnsureRate(@PathVariable int year, Authentication authentication) {
        return adminService.getComparisonEnrolledStudentEnsureRate(year).block();
    }

    @GetMapping("/freshCompetite/compare/{year}")
    public String getComparisonInsideFixedNumberFreshmanCompetitionRate(@PathVariable int year, Authentication authentication) {
        return adminService.getComparisonInsideFixedNumberFreshmanCompetitionRate(year).block();
    }

    @GetMapping("/register/compare/{year}")
    public String getComparisonEntranceModelLastRegistrationRatio(@PathVariable int year, Authentication authentication) {
        return adminService.getComparisonEntranceModelLastRegistrationRatio(year).block();
    }
}
