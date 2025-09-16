package com.yiuDashboard.repository;

import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.entity.key.YearDeptKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NewAdmissionStatsRepository
        extends JpaRepository<NewAdmissionStats, YearDeptKey> {

    Optional<NewAdmissionStats> findByYearAndDepartmentId(Integer year, Integer departmentId);

    List<NewAdmissionStats> findAllByYear(Integer year);

    List<NewAdmissionStats> findByMajorCategory(String majorCategory);

    Optional<NewAdmissionStats> findByDepartmentName(String departmentName);
}