package com.yiuDashboard.repository;

import com.yiuDashboard.entity.ResearchPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * 전국 평균 지표 데이터 조회
     */
    List<ResearchPerformance> findByYearAndNationalAverageNotNull(int year);

    /**
     * 계열별 평균 지표 데이터 조회
     */
    List<ResearchPerformance> findByCategoryNameAndYearAndCategoryAverageNotNull(String categoryName, int year);

    /**
     * 목표대학 비교 데이터 조회
     */
    List<ResearchPerformance> findByUniversityNameAndYearAndTargetUniversityValueNotNull(String universityName, int year);

    /**
     * 특정 계열의 모든 학과 논문·특허 평균 조회
     */
    @Query("SELECT AVG(r.paperCount), AVG(r.patentCount) " +
            "FROM ResearchPerformance r " +
            "WHERE r.categoryName = :categoryName")
    Object[] findCategoryAverage(@Param("categoryName") String categoryName);

    /**
     * 특정 목표 대학 논문·특허 데이터 조회
     */
    List<ResearchPerformance> findByUniversityName(String universityName);
}