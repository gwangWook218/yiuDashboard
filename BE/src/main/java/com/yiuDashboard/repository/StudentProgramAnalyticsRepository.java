package com.yiuDashboard.repository;

import com.yiuDashboard.entity.StudentProgramAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface StudentProgramAnalyticsRepository
        extends JpaRepository<StudentProgramAnalytics, StudentProgramAnalytics.Pk> {

    List<StudentProgramAnalytics> findByYear(Integer year);

    Optional<StudentProgramAnalytics> findByYearAndDepartmentId(Integer year, Integer departmentId);

    @Query("""
           select avg(coalesce(s.enrolledInMale,0) + coalesce(s.enrolledInFemale,0))
           from StudentProgramAnalytics s
             join s.department d
           where s.year = :year
             and d.college = :college
           """)
    Double findPeerAverageByCollege(@Param("year") Integer year, @Param("college") String college);
}
