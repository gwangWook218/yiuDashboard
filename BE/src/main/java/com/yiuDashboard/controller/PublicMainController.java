package com.yiuDashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.dto.RecruitmentRateDto;
import com.yiuDashboard.service.EnrollmentService;
import com.yiuDashboard.service.PublicMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/main")
@RequiredArgsConstructor
public class PublicMainController {

    private final PublicMainService publicMainService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/students")
    public Object students(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return (year == null)
                ? publicMainService.getComparisonEnrolledStudent()
                : publicMainService.getComparisonEnrolledStudent(year);
    }

    //    재학생 수
    @GetMapping("/department/students/count")
    public List<EnrollmentSummaryDto> findByYear(@RequestParam int year) {
        return enrollmentService.findByYear(year);
    }

    @GetMapping("/foreign")
    public Object foreign(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return (year == null)
                ? publicMainService.getComparisonForeignStudentCrntSt()
                : publicMainService.getComparisonForeignStudentCrntSt(year);
    }

    @GetMapping("/faculty")
    public Object faculty(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return (year == null)
                ? publicMainService.getNoticeFullTimeFacultyEnsureRate()
                : publicMainService.getNoticeFullTimeFacultyEnsureRate(year);
    }

    /* ✅ 장학금 */
    @GetMapping("/comparison/scholarship-benefit")
    public Object scholarship(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return (year == null)
                ? publicMainService.getComparisonScholarshipBenefitCrntSt()
                : publicMainService.getComparisonScholarshipBenefitCrntSt(year);
    }

    /* ✅ 졸업생 취업률 */
    @GetMapping("/notice/graduate-employment-rate")
    public Object employment(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return (year == null)
                ? publicMainService.getNoticeGraduateEmploymentRate()
                : publicMainService.getNoticeGraduateEmploymentRate(year);
    }

    /* ✅ 수시/정시 비율 */
    @GetMapping("/recruitment-rate")
    public List<RecruitmentRateDto> recruitmentRate(@RequestParam(required = false) Integer year) {
        return (year == null)
                ? publicMainService.findRecruitmentRateByYear()
                : publicMainService.findRecruitmentRateByYear(year);
    }

    /* ✅ 임직원 수 */
    @GetMapping("/staff")
    public Object staff(@RequestParam(required = false) Integer year) {
        return (year == null)
                ? publicMainService.getStaffCount()
                : publicMainService.getStaffCount(year);
    }

    /* ────── ✅ 프론트 호환 alias (구버전 경로 대응) ────── */
    @GetMapping("/scholarship")
    public Object scholarshipAlias(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return scholarship(year);
    }

    // 🔹 외국인 유학생 (프론트: /students/foreign)
    @GetMapping("/students/foreign")
    public Object foreignAlias(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return (year == null)
                ? publicMainService.getComparisonForeignStudentCrntSt()
                : publicMainService.getComparisonForeignStudentCrntSt(year);
    }

    // 🔹 졸업생 취업률 (프론트: /students/graduate)
    @GetMapping("/students/graduate")
    public Object graduateAlias(@RequestParam(required = false) Integer year) throws JsonProcessingException {
        return (year == null)
                ? publicMainService.getNoticeGraduateEmploymentRate()
                : publicMainService.getNoticeGraduateEmploymentRate(year);
    }

    // 🔹 입시 비율 (프론트: /recruitment)
    @GetMapping("/recruitment")
    public List<RecruitmentRateDto> recruitmentAlias(@RequestParam(required = false) Integer year) {
        return recruitmentRate(year);
    }
}
