package com.yiuDashboard.repository;

import com.yiuDashboard.entity.StudentNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentNumberRepository extends JpaRepository<StudentNumber, Long> {
    Optional<StudentNumber> findByYearAndDepartmentId(Integer year, Integer departmentId);
    List<StudentNumber> findAllByYear(Integer year);
    List<StudentNumber> findByMajorCategory(String majorCategory);
}

