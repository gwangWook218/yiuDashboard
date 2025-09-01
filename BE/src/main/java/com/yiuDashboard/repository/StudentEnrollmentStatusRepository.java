package com.yiuDashboard.repository;

import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.entity.enrollmentStatus.StudentEnrollmentStatus;
import com.yiuDashboard.entity.enrollmentStatus.StudentEnrollmentStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentEnrollmentStatusRepository extends JpaRepository<StudentEnrollmentStatus, StudentEnrollmentStatusId> {

    @Query("""
            SELECT new com.yiuDashboard.dto.EnrollmentSummaryDto(
                es.id.year, d.college, d.department,
                SUM(es.enrolledInMale + es.enrolledInFemale + es.enrolledOutMale + es.enrolledOutFemale)
            )
            FROM StudentEnrollmentStatus es
            JOIN es.department d
            WHERE es.departmentStatus <> '폐과' and es.id.year=:year
            GROUP BY es.id.year, d.college, d.department
            """)
    List<EnrollmentSummaryDto> findByYear(@Param("year") int year);
}
