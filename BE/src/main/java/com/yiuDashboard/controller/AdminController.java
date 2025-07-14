package com.yiuDashboard.controller;

import com.yiuDashboard.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin/faculty")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/compare/{year}")
    public String getComparisonFullTimeFacultyEnsureCrntSt(@PathVariable int year, Authentication authentication) {
        return adminService.getComparisonFullTimeFacultyEnsureCrntSt(year).block();
    }

    @GetMapping("/region")
    public String getRegionalFullTimeFacultyEnsureCrntSt(@PathVariable int year, Authentication authentication) {
        return adminService.getRegionalFullTimeFacultyEnsureCrntSt(year).block();
    }
}
