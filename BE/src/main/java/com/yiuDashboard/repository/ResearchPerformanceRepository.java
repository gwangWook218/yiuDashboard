package com.yiuDashboard.repository;

import com.yiuDashboard.entity.ResearchPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchPerformanceRepository extends JpaRepository<ResearchPerformance, Long> {

    // 1. 연구비 수혜 실적 (기존 연구비 관련 조회)
    List<ResearchPerformance> findResearchGrantByYear(int year);
    List<ResearchPerformance> findResearchGrantByDepartmentNameAndYear(String departmentName, int year);

    // 2. 경쟁력 및 유치 능력 평가 (전국/계열 평균/목표대학 비교)
    List<ResearchPerformance> findCompetitivenessByYear(int year);
    List<ResearchPerformance> findCompetitivenessByDepartmentNameAndYear(String departmentName, int year);

    // 3. 논문 실적 및 특허 출원 수 (학과 연구성과 정량화)
    List<ResearchPerformance> findResearchOutputByYear(int year);
    List<ResearchPerformance> findResearchOutputByDepartmentNameAndYear(String departmentName, int year);

}
