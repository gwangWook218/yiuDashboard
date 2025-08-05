package com.yiuDashboard.controller;

import com.yiuDashboard.entity.ResearchPerformance;
import com.yiuDashboard.service.ResearchPerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/research-performance")
public class ResearchPerformanceController {

    private final ResearchPerformanceService researchPerformanceService;

    public ResearchPerformanceController(ResearchPerformanceService researchPerformanceService) {
        this.researchPerformanceService = researchPerformanceService;
    }

    /**
     * 전체 연구비 수혜 실적 조회
     */
    @GetMapping
    public ResponseEntity<List<ResearchPerformance>> getAllResearchPerformances() {
        return ResponseEntity.ok(researchPerformanceService.getAllResearchPerformances());
    }

    /**
     * 학과별 연구비 수혜 실적 조회
     */
    @GetMapping("/department/{departmentName}")
    public ResponseEntity<List<ResearchPerformance>> getByDepartmentName(@PathVariable String departmentName) {
        return ResponseEntity.ok(researchPerformanceService.getByDepartmentName(departmentName));
    }

    /**
     * 교수별 연구비 수혜 실적 조회
     */
    @GetMapping("/professor/{professorName}")
    public ResponseEntity<List<ResearchPerformance>> getByProfessorName(@PathVariable String professorName) {
        return ResponseEntity.ok(researchPerformanceService.getByProfessorName(professorName));
    }

    /**
     * 연도별 연구비 수혜 실적 조회
     */
    @GetMapping("/year/{year}")
    public ResponseEntity<List<ResearchPerformance>> getByYear(@PathVariable int year) {
        return ResponseEntity.ok(researchPerformanceService.getByYear(year));
    }

    /**
     * 학과 + 연도별 연구비 수혜 실적 조회
     */
    @GetMapping("/department/{departmentName}/year/{year}")
    public ResponseEntity<List<ResearchPerformance>> getByDepartmentNameAndYear(
            @PathVariable String departmentName,
            @PathVariable int year) {
        return ResponseEntity.ok(researchPerformanceService.getByDepartmentNameAndYear(departmentName, year));
    }

    /**
     * 전국 평균 지표 데이터 조회
     */
    @GetMapping("/competitiveness/national/{year}")
    public ResponseEntity<List<ResearchPerformance>> getNationalAverageByYear(@PathVariable int year) {
        return ResponseEntity.ok(researchPerformanceService.getNationalAverageByYear(year));
    }

    /**
     * 계열별 평균 지표 데이터 조회
     */
    @GetMapping("/competitiveness/category/{categoryName}/{year}")
    public ResponseEntity<List<ResearchPerformance>> getCategoryAverageByYear(
            @PathVariable String categoryName,
            @PathVariable int year) {
        return ResponseEntity.ok(researchPerformanceService.getCategoryAverageByYear(categoryName, year));
    }

    /**
     * 목표대학 비교 데이터 조회
     */
    @GetMapping("/competitiveness/target/{universityName}/{year}")
    public ResponseEntity<List<ResearchPerformance>> getTargetUniversityValueByYear(
            @PathVariable String universityName,
            @PathVariable int year) {
        return ResponseEntity.ok(researchPerformanceService.getTargetUniversityValueByYear(universityName, year));
    }

    /**
     * 논문 실적, 특허 출원 수 비교
     */
    @GetMapping("/compare/{categoryName}/{targetUniversity}")
    public ResponseEntity<Map<String, Object>> compareResearchOutputs(
            @PathVariable String categoryName,
            @PathVariable String targetUniversity) {
        return ResponseEntity.ok(
                researchPerformanceService.compareResearchOutputs(categoryName, targetUniversity)
        );
    }
}