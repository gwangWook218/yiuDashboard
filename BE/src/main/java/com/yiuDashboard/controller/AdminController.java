package com.yiuDashboard.controller;

import com.yiuDashboard.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin/faculty")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/compare/{year}")
    public Mono<String> getComparisonFullTimeFacultyEnsureCrntSt(@PathVariable int year) {
        return adminService.getComparisonFullTimeFacultyEnsureCrntSt(year);
    }
}
