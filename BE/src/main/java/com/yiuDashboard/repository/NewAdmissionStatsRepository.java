package com.yiuDashboard.repository;

import com.yiuDashboard.entity.NewAdmissionStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewAdmissionStatsRepository extends JpaRepository<NewAdmissionStats, Long> {
    List<NewAdmissionStats> findByCategory(String category);
    Optional<NewAdmissionStats> findByDepartmentName(String departmentName);
    Optional<NewAdmissionStats> findByDepartmentCode(String departmentCode);
}
