package com.yiuDashboard.controller;

import com.yiuDashboard.dto.RecruitmentRateDto;
import com.yiuDashboard.service.PublicMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Mono<String> getComparisonEnrolledStudent(@RequestParam int year) {
        return publicMainService.getComparisonEnrolledStudent(year);
    }

//    졸업생 취업률
    @GetMapping("/students/graduate")
    public Mono<String> getNoticeGraduateEmploymentRate(@RequestParam int year) {
        return publicMainService.getNoticeGraduateEmploymentRate(year);
    }

//    외국인 유학생 수
    @GetMapping("/students/foreign")
    public Mono<String> getComparisonForeignStudentCrntSt(@RequestParam int year) {
        return publicMainService.getComparisonForeignStudentCrntSt(year);
    }

//    교원 수
    @GetMapping("/faculty")
    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(@RequestParam int year, @RequestParam int indctId) {
        return publicMainService.getComparisonFullTimeFacultyEnsureCrntSt(year, indctId);
    }

//    1인당 장학금
    @GetMapping("/scholarship")
    public Mono<String> getComparisonScholarshipBenefitCrntSt(@RequestParam int year) {
        return publicMainService.getComparisonScholarshipBenefitCrntSt(year);
    }

//    수시/정시 비율
    @GetMapping("/recruitment")
    public List<RecruitmentRateDto> findRecruitmentRateByYear(@RequestParam int year) {
        return publicMainService.findRecruitmentRateByYear(year);
    }
}
