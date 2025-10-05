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
}
