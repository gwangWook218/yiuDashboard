package com.yiuDashboard.controller;

import com.yiuDashboard.entity.DepartmentOutcome;
import com.yiuDashboard.service.DepartmentOutcomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department-outcome")
@RequiredArgsConstructor
public class DepartmentOutcomeController {

    private final DepartmentOutcomeService departmentOutcomeService;

    //특정 학과 재학생 수 조회
    @GetMapping("/department")
    public ResponseEntity<List<DepartmentOutcome>> getStudentCountByDepartment(@RequestParam String name) {
        List<DepartmentOutcome> outcomes = departmentOutcomeService.getStudentCountByDepartment(name);
        return ResponseEntity.ok(outcomes);
    }

    //전체 학과 재학생 수 조회
    @GetMapping("/sorted")
    public ResponseEntity<List<DepartmentOutcome>> getAllDepartmentsByStudentCountDesc() {
        List<DepartmentOutcome> outcomes = departmentOutcomeService.getAllDepartmentsByStudentCountDesc();
        return ResponseEntity.ok(outcomes);
    }

    //학과 재학생 수 데이터 저장 (관리자용)
    @PostMapping
    public ResponseEntity<DepartmentOutcome> saveDepartmentOutcome(@RequestBody DepartmentOutcome outcome) {
        DepartmentOutcome saved = departmentOutcomeService.saveDepartmentOutcome(outcome);
        return ResponseEntity.ok(saved);
    }

    // 특정 학과 전공/교양 성적 평균 및 차이 조회
    @GetMapping("/grade-comparison")
    public ResponseEntity<List<DepartmentOutcome>> getGradeComparisonByDepartment(@RequestParam String name) {
        List<DepartmentOutcome> outcomes = departmentOutcomeService.getGradeComparisonByDepartment(name);
        return ResponseEntity.ok(outcomes);
    }

    // 전체 학과 전공 - 교양 성적 차이 조회
    @GetMapping("/grade-comparison/sorted")
    public ResponseEntity<List<DepartmentOutcome>> getAllDepartmentsByGradeDifferenceDesc() {
        List<DepartmentOutcome> outcomes = departmentOutcomeService.getAllDepartmentsByGradeDifferenceDesc();
        return ResponseEntity.ok(outcomes);
    }
}
