package com.yiuDashboard.controller;

import com.yiuDashboard.entity.ResearchPerformance;
import com.yiuDashboard.service.ResearchPerformanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 연구비 수혜 실적 컨트롤러
 * 교수 및 학과의 연구활동 수준(대외 연구) 데이터 제공
 */
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
}
