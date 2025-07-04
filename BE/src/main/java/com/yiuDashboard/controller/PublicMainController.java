package com.yiuDashboard.controller;

import com.yiuDashboard.service.PublicStudentService;
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

    private final PublicStudentService publicStudentService;

    @GetMapping("/students/{year}")
    public Mono<String> getComparisonEnrolledStudent(@PathVariable int year) {
        return publicStudentService.getComparisonEnrolledStudent(year);
    }

    @GetMapping("/students/graduateRate/{year}")
    public Mono<String> getNoticeGraduateEmploymentRate(@PathVariable int year) {
        return publicStudentService.getNoticeGraduateEmploymentRate(year);
    }

    @GetMapping("/students/foreign/{year}")
    public Mono<String> getComparisonForeignStudentCrntSt(@PathVariable int year) {
        return publicStudentService.getComparisonForeignStudentCrntSt(year);
    }
}
