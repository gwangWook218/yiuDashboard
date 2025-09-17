package com.yiuDashboard.repository;

import com.yiuDashboard.entity.EmploymentStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmploymentStatsRepository extends JpaRepository<EmploymentStats, Long> {

    /** 데이터에 존재하는 최신 연도 */
    @Query("select max(e.year) from EmploymentStats e")
    Integer findLatestYear();

    /** 학과별 통계 (해당 연도) */
    List<EmploymentStats> findByDepartmentNameAndYear(String departmentName, Integer year);

    /** 전체 학과 – 취업률 내림차순 (해당 연도) */
    List<EmploymentStats> findByYearOrderByEmploymentRateDesc(Integer year);

    /** 특정 연도에서 기준 취업률 이상 학과 (내림차순) */
    @Query("""
           select e
             from EmploymentStats e
            where e.year = :year
              and e.employmentRate >= :rate
         order by e.employmentRate desc
           """)
    List<EmploymentStats> findByYearAndEmploymentRateGreaterThanEqual(
            @Param("year") Integer year, @Param("rate") double rate);

    /** 특정 학과의 진출 분야 비율 내림차순 */
    List<EmploymentStats> findByDepartmentNameOrderByCareerFieldRateDesc(String departmentName);
}
