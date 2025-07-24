package com.yiuDashboard.repository;

import com.yiuDashboard.entity.EmploymentStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EmploymentStatsRepository extends JpaRepository<EmploymentStats, Long> {

    //특정 학과의 취업 통계 조회
    List<EmploymentStats> findByDepartmentName(String departmentName);

    //취업률 기준 내림차순 정렬 조회
    List<EmploymentStats> findAllByOrderByEmploymentRateDesc();

    //취업률이 특정 값 이상인 학과 조회
    @Query("SELECT e FROM EmploymentStats e WHERE e.employmentRate >= :rate")
    List<EmploymentStats> findByEmploymentRateGreaterThanEqual(@Param("rate") double rate);
}
