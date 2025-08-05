package com.yiuDashboard.repository;

import com.yiuDashboard.entity.ResearchPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchPerformanceRepository extends JpaRepository<ResearchPerformance, Long> {

    /**
     * 학과별 연구비 수혜 실적 조회
     */
    List<ResearchPerformance> findByDepartmentName(String departmentName);

    /**
     * 특정 교수 연구비 수혜 실적 조회
     */
    List<ResearchPerformance> findByProfessorName(String professorName);

    /**
     * 연도별 연구비 수혜 실적 조회
     */
    List<ResearchPerformance> findByYear(int year);

    /**
     * 학과 + 연도별 연구비 수혜 실적 조회
     */
    List<ResearchPerformance> findByDepartmentNameAndYear(String departmentName, int year);
}

