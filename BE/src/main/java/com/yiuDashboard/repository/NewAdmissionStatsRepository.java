package com.yiuDashboard.repository;

import com.yiuDashboard.entity.NewAdmissionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewAdmissionStatsRepository extends JpaRepository<NewAdmissionStats, Long> {

    // 계열명으로 검색
    List<NewAdmissionStats> findByMajorCategory(String majorCategory);

    // 학과명으로 검색
    NewAdmissionStats findByDepartmentName(String departmentName);
}
