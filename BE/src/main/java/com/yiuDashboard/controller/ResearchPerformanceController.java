package com.yiuDashboard.controller;

import com.yiuDashboard.entity.ResearchPerformance;
import com.yiuDashboard.service.ResearchPerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/research-performance")
@RequiredArgsConstructor
public class ResearchPerformanceController {

    private final ResearchPerformanceService researchPerformanceService;

    //특정 연도의 연구비 수혜 실적 조회
    @GetMapping("/year/{year}")
    public ResponseEntity<List<ResearchPerformance>> getResearchPerformanceByYear(@PathVariable int year) {
        List<ResearchPerformance> performances = researchPerformanceService.getResearchPerformanceByYear(year);
        return ResponseEntity.ok(performances);
    }

    //특정 학과의 연구비 수혜 실적 조회
    @GetMapping("/department/{departmentName}")
    public ResponseEntity<List<ResearchPerformance>> getResearchPerformanceByDepartment(@PathVariable String departmentName) {
        List<ResearchPerformance> performances = researchPerformanceService.getResearchPerformanceByDepartment(departmentName);
        return ResponseEntity.ok(performances);
    }

    //전체 연구비 수혜 실적 조회
    @GetMapping("/all")
    public ResponseEntity<List<ResearchPerformance>> getAllResearchPerformances() {
        List<ResearchPerformance> performances = researchPerformanceService.getAllResearchPerformances();
        return ResponseEntity.ok(performances);
    }

    // 특정 연도, 전체 학과 경쟁력 평가
    @GetMapping("/competitiveness/{year}")
    public ResponseEntity<List<ResearchPerformance>> getCompetitivenessEvaluationByYear(
            @PathVariable int year) {
        List<ResearchPerformance> data =
                researchPerformanceService.getCompetitivenessEvaluationByYear(year);
        return ResponseEntity.ok(data);
    }

    // 특정 연도, 특정 학과 경쟁력 평가
    @GetMapping("/competitiveness/{departmentName}/{year}")
    public ResponseEntity<List<ResearchPerformance>> getCompetitivenessEvaluation(
            @PathVariable String departmentName,
            @PathVariable int year) {
        List<ResearchPerformance> data =
                researchPerformanceService.getCompetitivenessEvaluation(departmentName, year);
        return ResponseEntity.ok(data);
    }
}