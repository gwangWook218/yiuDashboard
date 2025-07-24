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
    public List<Object[]> getSemesterGpaTrend(String studentId) {
        return roadmapRepository.findSemesterGpaTrend(studentId);
    }
    public List<Object[]> getSubjectTypeGpaDistribution(String studentId) {
        return roadmapRepository.findSubjectTypeGpaDistribution(studentId);
    }

    public String simulateGoalAchievement(String studentId, int remainingSemesters) {

        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생 데이터를 찾을 수 없습니다."));

        Double currentGpa = roadmap.getTargetGpa();
        Double targetGrade = roadmap.getTargetGrade();
        if (targetGrade == null) {
            return "목표 성적이 설정되어 있지 않습니다.";
        }

        double requiredAverage = (targetGrade - currentGpa) / remainingSemesters;

        if (requiredAverage <= 0) {
            return "이미 목표 성적을 달성했습니다!";
        } else if (requiredAverage <= 4.5) {
            return "목표 성적 달성을 위해 학기당 평균 " + String.format("%.2f", requiredAverage) + " 이상이 필요합니다.";
        } else {
            return "목표 성적 달성이 어려울 수 있습니다. 추가 학습 계획이 필요합니다.";
        }

    }
    public boolean checkGraduationRequirement(String studentId) {
        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생 데이터를 찾을 수 없습니다."));

        int earnedCredits = roadmap.getEarnedCredits();
        int requiredCredits = roadmap.getGraduationRequirementCredits();

        return earnedCredits >= requiredCredits;  // boolean만 반환
    }
}
