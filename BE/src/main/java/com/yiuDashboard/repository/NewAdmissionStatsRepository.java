package com.yiuDashboard.repository;

import com.yiuDashboard.entity.NewAdmissionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface NewAdmissionStatsRepository extends JpaRepository<NewAdmissionStats, Long> {

    // 계열명으로 검색
    List<NewAdmissionStats> findByMajorCategory(String majorCategory);

    // 학과명으로 검색
    NewAdmissionStats findByDepartmentName(String departmentName);

    // 유사 계열 평균 충원율 계산
    @Query("SELECT AVG(n.fillRate) FROM NewAdmissionStats n WHERE n.majorCategory = :majorCategory")
    Double findAverageFillRateByMajorCategory(@Param("majorCategory") String majorCategory);
}
