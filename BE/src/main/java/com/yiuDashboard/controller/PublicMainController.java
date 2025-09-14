package com.yiuDashboard.controller;

import com.yiuDashboard.service.PublicMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/main")
public class PublicMainController {

    private final PublicMainService publicMainService;

//    재학생 수
    @GetMapping("/students/{year}")
    public Mono<String> getComparisonEnrolledStudent(@PathVariable int year) {
        return publicMainService.getComparisonEnrolledStudent(year);
    }

//    졸업생 취업률
    @GetMapping("/students/graduateRate/{year}")
    public Mono<String> getNoticeGraduateEmploymentRate(@PathVariable int year) {
        return publicMainService.getNoticeGraduateEmploymentRate(year);
    }

//    외국인 유학생 수
    @GetMapping("/students/foreign/{year}")
    public Mono<String> getComparisonForeignStudentCrntSt(@PathVariable int year) {
        return publicMainService.getComparisonForeignStudentCrntSt(year);
    }

//    교원 수
    @GetMapping("/faculty/compare/{year}")
    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(@PathVariable int year) {
        return publicMainService.getComparisonFullTimeFacultyEnsureCrntSt(year);
    }

//    1인당 장학금
    @GetMapping("/scholarship/{year}")
    public Mono<String> getComparisonScholarshipBenefitCrntSt(@PathVariable int year) {
        return publicMainService.getComparisonScholarshipBenefitCrntSt(year);
    }
}
