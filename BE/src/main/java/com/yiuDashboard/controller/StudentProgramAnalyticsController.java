package com.yiuDashboard.controller;

import com.yiuDashboard.dto.FillRateResponse;
import com.yiuDashboard.service.StudentProgramAnalyticsService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/admission")
@RequiredArgsConstructor
@Validated
public class StudentProgramAnalyticsController {

    private final StudentProgramAnalyticsService service;

    @GetMapping("/fill-rate")
    public FillRateResponse fillRate(@RequestParam @Min(2000) int year,
                                     @RequestParam int departmentId) {
        return service.getFillRate(year, departmentId);
    }
}
