package com.yiuDashboard.repository;

import com.yiuDashboard.entity.personalGrades.SemesterRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonalGradesRepository extends JpaRepository<SemesterRecord, Long> {
    List<SemesterRecord> findByUserId(Long userId);
}
