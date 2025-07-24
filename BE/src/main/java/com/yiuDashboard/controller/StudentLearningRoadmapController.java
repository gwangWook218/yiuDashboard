package com.yiuDashboard.controller;

import com.yiuDashboard.entity.StudentLearningRoadmap;
import com.yiuDashboard.service.StudentLearningRoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/learning-roadmap")
@RequiredArgsConstructor
public class StudentLearningRoadmapController {

    private final StudentLearningRoadmapService service;

    @PostMapping("/target-grade")
    public ResponseEntity<StudentLearningRoadmap> setTargetGrade(
            @RequestParam String studentId,
            @RequestParam Double targetGrade) {
        StudentLearningRoadmap roadmap = service.setTargetGrade(studentId, targetGrade);
        return ResponseEntity.ok(roadmap);
    }
    // 학기별 성적 추이 조회
    @GetMapping("/semester-trend")
    public ResponseEntity<List<Object[]>> getSemesterGpaTrend(@RequestParam String studentId) {
        List<Object[]> trend = service.getSemesterGpaTrend(studentId);
        return ResponseEntity.ok(trend);
    }

    // 과목 유형별 성적 분포 조회
    @GetMapping("/subject-type-distribution")
    public ResponseEntity<List<Object[]>> getSubjectTypeGpaDistribution(@RequestParam String studentId) {
        List<Object[]> distribution = service.getSubjectTypeGpaDistribution(studentId);
        return ResponseEntity.ok(distribution);
    }

    // 목표 달성 가능 여부 계산
    @GetMapping("/goal-simulation")
    public ResponseEntity<String> simulateGoalAchievement(
            @RequestParam String studentId,
            @RequestParam int remainingSemesters) {

        String result = service.simulateGoalAchievement(studentId, remainingSemesters);
        return ResponseEntity.ok(result);
    }
    // 졸업 요건 충족 여부 확인
    @GetMapping("/graduation-check")
    public ResponseEntity<Boolean> checkGraduationRequirement(@RequestParam String studentId) {
        boolean result = service.checkGraduationRequirement(studentId);
        return ResponseEntity.ok(result);
    }
}
