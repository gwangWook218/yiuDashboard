package com.yiuDashboard.repository;

import com.yiuDashboard.entity.EmploymentStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmploymentStatsRepository extends JpaRepository<EmploymentStats, Long> {

    @Query("select max(e.year) from EmploymentStats e")
    Integer findLatestYear();

    List<EmploymentStats> findByDepartmentNameAndYear(String departmentName, Integer year);

    // 특정 년도 전체 학과 정렬
    List<EmploymentStats> findByYearOrderByEmploymentRateDesc(Integer year);

    // 특정 년도 취업률이 기준 이상인 학과
    @Query("""
           select e
             from EmploymentStats e
            where e.year = :year
              and e.employmentRate >= :rate
            order by e.employmentRate desc
           """)
    List<EmploymentStats> findByYearAndEmploymentRateGreaterThanEqual(@Param("year") Integer year,
                                                                      @Param("rate") double rate);
}