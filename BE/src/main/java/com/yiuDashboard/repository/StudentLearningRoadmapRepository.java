package com.yiuDashboard.repository;

import com.yiuDashboard.entity.StudentLearningRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface StudentLearningRoadmapRepository extends JpaRepository<StudentLearningRoadmap, Long> {

    // 특정 학생의 학습 로드맵(목표 성적) 전체 조회
    Optional<StudentLearningRoadmap> findByStudentId(String studentId);
}
