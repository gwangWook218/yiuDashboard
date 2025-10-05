package com.yiuDashboard.service;

import com.yiuDashboard.entity.StudentLearningRoadmap;
import com.yiuDashboard.repository.StudentLearningRoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentLearningRoadmapService {

    private final StudentLearningRoadmapRepository roadmapRepository;

    /** 목표 성적 저장/갱신 */
    public StudentLearningRoadmap setTargetGrade(String studentId, Double targetGrade) {
        // 학생별 로드맵 조회, 없으면 생성
        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    StudentLearningRoadmap m = new StudentLearningRoadmap();
                    m.setStudentId(studentId);
                    return m;
                });

        roadmap.setTargetGrade(targetGrade);
        return roadmapRepository.save(roadmap);
    }

    /** 학기별 성적 추이 */
    public List<Object[]> getSemesterGpaTrend(String studentId) {
        return roadmapRepository.findSemesterGpaTrend(studentId);
    }

    /** 과목 유형별 성적 분포 */
    public List<Object[]> getSubjectTypeGpaDistribution(String studentId) {
        return roadmapRepository.findSubjectTypeGpaDistribution(studentId);
    }
}
