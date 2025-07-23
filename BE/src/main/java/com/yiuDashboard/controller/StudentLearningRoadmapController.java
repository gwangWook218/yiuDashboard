package com.yiuDashboard.controller;

import com.yiuDashboard.entity.StudentLearningRoadmap;
import com.yiuDashboard.service.StudentLearningRoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
