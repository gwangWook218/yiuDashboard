package com.yiuDashboard.service;

import com.yiuDashboard.entity.StudentLearningRoadmap;
import com.yiuDashboard.repository.StudentLearningRoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentLearningRoadmapService {

    private final StudentLearningRoadmapRepository roadmapRepository;

    /** 공통: NOT NULL 컬럼들 기본값 보정 */
    private StudentLearningRoadmap applyDefaults(StudentLearningRoadmap m) {
        if (m.getDescription() == null) m.setDescription(null);

        if (m.getTargetGpa() == null) m.setTargetGpa(0.0);
        if (m.getCurrentGpa() == null) m.setCurrentGpa(0.0);
        if (m.getRemainingSemesters() == null) m.setRemainingSemesters(0);
        if (m.getAchievable() == null) m.setAchievable(false);

        // 스키마에 있다면 꼭 기본값 지정 (에러에 등장했던 필드)
        if (m.getEarnedCredits() == null) m.setEarnedCredits(0);
        if (m.getGraduationRequirementCredits() == null) m.setGraduationRequirementCredits(0);

        // (엔티티에 earnedCredits / graduationRequirementCredits 가 있다면 기본값도 여기서 보정)
        // if (m.getEarnedCredits() == null) m.setEarnedCredits(0);
        // if (m.getGraduationRequirementCredits() == null) m.setGraduationRequirementCredits(0);
        return m;
    }

    /** 목표 성적 저장 */
    @Transactional
    public StudentLearningRoadmap setTargetGrade(String studentId, Double targetGrade) {
        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .orElseGet(() -> {
                    StudentLearningRoadmap n = new StudentLearningRoadmap();
                    n.setStudentId(studentId);
                    return applyDefaults(n);   // ← 기본값 먼저 적용
                });

        applyDefaults(roadmap);            // 기존행도 보정
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

    /** 목표 달성 시뮬레이션 */
    public String simulateGoalAchievement(String studentId, int remainingSemesters) {
        // 데이터 조회
        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .map(this::applyDefaults)
                .orElse(null);

        if (roadmap == null) {
            return "학생 데이터가 아직 없습니다. 먼저 목표 성적을 설정해 주세요.";
        }
        if (remainingSemesters <= 0) {
            return "남은 학기 수(remainingSemesters)가 1 이상이어야 합니다.";
        }

        Double targetGrade = roadmap.getTargetGrade();
        if (targetGrade == null) {
            return "목표 성적이 설정되어 있지 않습니다. 먼저 /target-grade 로 저장해 주세요.";
        }

        double current = roadmap.getCurrentGpa();   // 기본값 0.0 적용됨
        double need = (targetGrade - current) / remainingSemesters;

        if (need <= 0) {
            return "이미 목표 성적을 달성했습니다!";
        } else if (need > 4.5) {
            return String.format("학기당 평균 %.2f 이상이 필요합니다. 사실상 달성이 어렵습니다.", need);
        } else {
            return String.format("목표 달성을 위해 학기당 평균 %.2f 이상이 필요합니다.", need);
        }
    }

    /** (선택) 졸업 요건 충족 여부 */
    public boolean checkGraduationRequirement(String studentId) {
        StudentLearningRoadmap roadmap = roadmapRepository.findByStudentId(studentId)
                .map(this::applyDefaults)
                .orElseThrow(() -> new IllegalArgumentException("학생 데이터를 찾을 수 없습니다."));

        // 예: earnedCredits vs requiredCredits 가 엔티티에 있다면 아래처럼
        // int earned = roadmap.getEarnedCredits();
        // int required = roadmap.getGraduationRequirementCredits();
        // return earned >= required;
        return true; // 프로젝트 정의에 맞게 구현
    }
}
