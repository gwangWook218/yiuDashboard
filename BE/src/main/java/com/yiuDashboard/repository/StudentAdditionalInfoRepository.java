package com.yiuDashboard.repository;

import com.yiuDashboard.entity.User;
import com.yiuDashboard.entity.personalGrades.StudentAdditionalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentAdditionalInfoRepository extends JpaRepository<StudentAdditionalInfo, Long> {
    Optional<StudentAdditionalInfo> findByUser(User user);
    StudentAdditionalInfo findByUserId(@Param("userId") Long userId);
}
