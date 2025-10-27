package com.yiuDashboard.repository;

import com.yiuDashboard.entity.personalGrades.CreditProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditProgressRepository extends JpaRepository<CreditProgress, Long> {
    List<CreditProgress> findByUserId(@Param("userId") Long userId);
}
