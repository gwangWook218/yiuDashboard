package com.yiuDashboard.repository;

import com.yiuDashboard.entity.StaffStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffStatisticsRepository extends JpaRepository<StaffStatistics, Long> {

    @Query("SELECT SUM(s.male + s.female) FROM StaffStatistics s")
    Long getTotalStaffCount();
}
