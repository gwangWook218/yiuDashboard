package com.yiuDashboard.service;
import com.yiuDashboard.entity.StudentLearningRoadmap;
import com.yiuDashboard.repository.StudentLearningRoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import jakarta.annotation.security.PermitAll;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class StudentLearningRoadmapService {

    private final StudentLearningRoadmapRepository roadmapRepository;
    @PermitAll
    public StudentLearningRoadmap setTargetGrade(String studentId, Double targetGrade) {
        // 학생 학습 로드맵 데이터 조회
        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    StudentLearningRoadmap newRoadmap = new StudentLearningRoadmap();
                    newRoadmap.setStudentId(studentId);

                    // 기본값(스키마 제약 회피용)
                    if (newRoadmap.getTargetSemester() == null) newRoadmap.setTargetSemester("N/A");
                    if (newRoadmap.getTargetGpa() == null)      newRoadmap.setTargetGpa(0.0);
                    if (newRoadmap.getCurrentGpa() == null)     newRoadmap.setCurrentGpa(0.0);
                    if (newRoadmap.getRemainingSemesters() == null) newRoadmap.setRemainingSemesters(0);
                    if (newRoadmap.getAchievable() == null)     newRoadmap.setAchievable(false);
                    // earnedCredits 컬럼이 NOT NULL이면 엔티티/DB 중 한쪽에 기본값 필요
                    try {
                        newRoadmap.getClass().getDeclaredField("earnedCredits");
                        if (newRoadmap.getEarnedCredits() == null) newRoadmap.setEarnedCredits(0);
                    } catch (NoSuchFieldException ignore) {}

                    return newRoadmap; // <- 람다 블록의 마지막에서 반환
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
    @PermitAll
    public String simulateGoalAchievement(String studentId, int remainingSemesters) {
        Optional<StudentLearningRoadmap> opt = roadmapRepository.findByStudentId(studentId);
        if (opt.isEmpty()) {
            return "학생 데이터가 아직 없습니다. 먼저 목표 성적을 설정해 주세요.";
        }

        StudentLearningRoadmap roadmap = opt.get();

        // 2) 목표 성적 미설정 → 200 + 안내문
        Double targetGrade = roadmap.getTargetGrade();
        if (targetGrade == null) {
            return "목표 성적이 설정되어 있지 않습니다.";
        }

        // 3) 값 보정(Null/0 방지)
        double currentGpa = (roadmap.getCurrentGpa() == null) ? 0.0 : roadmap.getCurrentGpa();
        int rs = (remainingSemesters <= 0) ? 1 : remainingSemesters; // 0 나눗셈 방지
        roadmap.setRemainingSemesters(rs); // 필요 시 최신 값 반영

        // 4) 계산 및 메시지 반환
        double requiredAverage = (targetGrade - currentGpa) / rs;

        if (requiredAverage <= 0) {
            return "이미 목표 성적을 달성했습니다!";
        } else if (requiredAverage <= 4.5) {
            return "목표 성적 달성을 위해 학기당 평균 " + String.format("%.2f", requiredAverage) + " 이상이 필요합니다.";
        } else {
            return "목표 성적 달성이 어려울 수 있습니다. 추가 학습 계획이 필요합니다.";
        }
    }
}
