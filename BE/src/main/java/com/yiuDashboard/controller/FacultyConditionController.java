package com.yiuDashboard.controller;

import com.yiuDashboard.entity.FacultyCondition;
import com.yiuDashboard.service.FacultyConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty-condition")
@RequiredArgsConstructor
public class FacultyConditionController {

    private final FacultyConditionService facultyConditionService;

    //특정 학과의 전임교원 확보율 조회
    @GetMapping("/ensure-rate")
    public ResponseEntity<List<FacultyCondition>> getFacultyEnsureRateByDepartment(@RequestParam String name) {
        List<FacultyCondition> results = facultyConditionService.getFacultyEnsureRateByDepartment(name);
        return ResponseEntity.ok(results);
    }

    //모든 학과의 전임교원 확보율 조회 (전국 평균 혹은 권고치 비교용)
    @GetMapping("/ensure-rate/sorted")
    public ResponseEntity<List<FacultyCondition>> getAllFacultyEnsureRatesDesc() {
        List<FacultyCondition> results = facultyConditionService.getAllFacultyEnsureRatesDesc();
        return ResponseEntity.ok(results);
    }

    // 특정 학과 전임교원 1인당 학생 수 조회
    @GetMapping("/student-per-faculty")
    public ResponseEntity<List<FacultyCondition>> getStudentPerFacultyByDepartment(@RequestParam String name) {
        List<FacultyCondition> results = facultyConditionService.getStudentPerFacultyByDepartment(name);
        return ResponseEntity.ok(results);
    }

    // 전체 학과 전임교원 1인당 학생 수 조회
    @GetMapping("/student-per-faculty/sorted")
    public ResponseEntity<List<FacultyCondition>> getAllDepartmentsByStudentPerFacultyDesc() {
        List<FacultyCondition> results = facultyConditionService.getAllDepartmentsByStudentPerFacultyDesc();
        return ResponseEntity.ok(results);
    }
}
