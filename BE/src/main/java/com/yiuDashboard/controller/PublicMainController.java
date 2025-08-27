package com.yiuDashboard.controller;

import com.yiuDashboard.dto.RecruitmentRateDto;
import com.yiuDashboard.service.PublicMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/main")
public class PublicMainController {

    private final PublicMainService publicMainService;

//    재학생 수
    @GetMapping("/students")
    public Mono<String> getComparisonEnrolledStudent(@Param("year") int year) {
        return publicMainService.getComparisonEnrolledStudent(year);
    }

//    졸업생 취업률
    @GetMapping("/students/graduateRate")
    public Mono<String> getNoticeGraduateEmploymentRate(@Param("year") int year) {
        return publicMainService.getNoticeGraduateEmploymentRate(year);
    }

//    외국인 유학생 수
    @GetMapping("/students/foreign")
    public Mono<String> getComparisonForeignStudentCrntSt(@Param("year") int year) {
        return publicMainService.getComparisonForeignStudentCrntSt(year);
    }

//    교원 수
    @GetMapping("/faculty/compare")
    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(@Param("year") int year, @Param("indctId") int indctId) {
        return publicMainService.getComparisonFullTimeFacultyEnsureCrntSt(year, indctId);
    }

//    1인당 장학금
    @GetMapping("/scholarship")
    public Mono<String> getComparisonScholarshipBenefitCrntSt(@Param("year") int year) {
        return publicMainService.getComparisonScholarshipBenefitCrntSt(year);
    }

//    수시/정시 비율
    @GetMapping("/recruitment")
    public List<RecruitmentRateDto> findRecruitmentRateByYear(@Param("year") int year) {
        return publicMainService.findRecruitmentRateByYear(year);
    }
}
