package com.yiuDashboard.repository;

import com.yiuDashboard.entity.personalGrades.CreditProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditProgressRepository extends JpaRepository<CreditProgress, Long> {
    List<CreditProgress> findByUserId(Long userId);
}
