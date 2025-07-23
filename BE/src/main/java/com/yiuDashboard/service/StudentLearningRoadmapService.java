package com.yiuDashboard.service;

import com.yiuDashboard.entity.StudentLearningRoadmap;
import com.yiuDashboard.repository.StudentLearningRoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentLearningRoadmapService {

    private final StudentLearningRoadmapRepository roadmapRepository;

    public StudentLearningRoadmap setTargetGrade(String studentId, Double targetGrade) {
        // 학생 학습 로드맵 데이터 조회
        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    StudentLearningRoadmap newRoadmap = new StudentLearningRoadmap();
                    newRoadmap.setStudentId(studentId);
                    return newRoadmap;
                });

        roadmap.setTargetGrade(targetGrade);

        return roadmapRepository.save(roadmap);
    }
}
