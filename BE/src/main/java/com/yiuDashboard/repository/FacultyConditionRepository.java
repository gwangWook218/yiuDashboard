package com.yiuDashboard.repository;

import com.yiuDashboard.entity.FacultyCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyConditionRepository extends JpaRepository<FacultyCondition, Long> {

    // 특정 학과의 전임교원 확보율 정보 조회
    List<FacultyCondition> findByDepartmentName(String departmentName);

    // 전임교원 확보율 기준 내림차순 정렬 (전국 평균과 비교용)
    List<FacultyCondition> findAllByOrderByFacultySecureRateDesc();
}