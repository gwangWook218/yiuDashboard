package com.yiuDashboard.repository;

import com.yiuDashboard.entity.EmploymentStats;
import com.yiuDashboard.entity.EmploymentStatsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmploymentStatsRepository extends JpaRepository<EmploymentStats, EmploymentStatsId> {
    List<EmploymentStats> findByIdYearAndIdDepartmentId(Integer year, Integer departmentId);
}