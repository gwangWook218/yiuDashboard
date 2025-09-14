package com.yiuDashboard.repository;

import com.yiuDashboard.entity.NewAdmissionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewAdmissionStatsRepository extends JpaRepository<NewAdmissionStats, Long> {

    Optional<NewAdmissionStats> findByYearAndDepartmentId(Integer year, Integer departmentId);
    List<NewAdmissionStats> findAllByYear(Integer year);

    Optional<NewAdmissionStats> findByYearAndDepartmentId(int year, int departmentId);

    List<NewAdmissionStats> findByMajorCategory(String majorCategory);
    NewAdmissionStats findByDepartmentName(String departmentName);
}