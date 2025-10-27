package com.yiuDashboard.repository;

import com.yiuDashboard.entity.personalGrades.StudentAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface StudentAdditionalInfoRepository extends JpaRepository<StudentAdditionalInfo, Long> {
    StudentAdditionalInfo findByUserId(@Param("userId") Long userId);
}
