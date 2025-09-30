package com.yiuDashboard.repository;

import com.yiuDashboard.entity.StudentLearningRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface StudentLearningRoadmapRepository extends JpaRepository<StudentLearningRoadmap, Long> {

    // 특정 학생의 학습 로드맵(목표 성적) 전체 조회
    Optional<StudentLearningRoadmap> findByStudentId(String studentId);

    // 학기별 성적 추이
    @Query("SELECT s.targetSemester, AVG(s.targetGpa) FROM StudentLearningRoadmap s " +
            "WHERE s.studentId = :studentId GROUP BY s.targetSemester ORDER BY s.targetSemester ASC")
    List<Object[]> findSemesterGpaTrend(@Param("studentId") String studentId);

    // 과목 유형별 성적 분포
    @Query("SELECT s.description, AVG(s.targetGpa) FROM StudentLearningRoadmap s " +
            "WHERE s.studentId = :studentId GROUP BY s.description")
    List<Object[]> findSubjectTypeGpaDistribution(@Param("studentId") String studentId);

    // 목표 달성 시뮬레이션 데이터 (전체 성적 평균 및 이수 학기 수)
    @Query("SELECT AVG(s.targetGpa), COUNT(s.targetSemester) " +
            "FROM StudentLearningRoadmap s WHERE s.studentId = :studentId")
    Object[] findCurrentGpaAndSemesterCount(@Param("studentId") String studentId);

    // 졸업생 성적 분포 (졸업 여부 true 조건)
    @Query("SELECT s.currentGpa FROM StudentLearningRoadmap s WHERE s.graduationSatisfied = true")
    List<Double> findAllGraduatesGpa();

    // 졸업생 성적 분포 조회 (전체 평균과 표준편차, 개인 GPA 포함)
    @Query("SELECT AVG(s.currentGpa), STDDEV(s.currentGpa) FROM StudentLearningRoadmap s WHERE s.graduationSatisfied = true")
    Object[] findGraduatesGpaStats();

    @Query("SELECT s.currentGpa FROM StudentLearningRoadmap s WHERE s.studentId = :studentId")
    Double findStudentCurrentGpa(@Param("studentId") String studentId);
}
