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
                es.id.year, d.department,
                (es.enrolledInMale + es.enrolledInFemale + es.enrolledOutMale + es.enrolledOutFemale),
                (es.enrolledInMale + es.enrolledInFemale + es.enrolledOutMale + es.enrolledOutFemale)
                - (SELECT es2.enrolledInMale + es2.enrolledInFemale + es2.enrolledOutMale + es2.enrolledOutFemale
                    FROM StudentEnrollmentStatus es2
                    WHERE es2.id.year= :year - 1 and es2.id.departmentId = :deptId),
                (SELECT AVG(es3.enrolledInMale + es3.enrolledInFemale + es3.enrolledOutMale + es3.enrolledOutFemale)
                    FROM StudentEnrollmentStatus es3
                    WHERE es3.id.departmentId = :deptId
                ),
                (SELECT MAX(es4.enrolledInMale + es4.enrolledInFemale + es4.enrolledOutMale + es4.enrolledOutFemale)
                    FROM StudentEnrollmentStatus es4
                    WHERE es4.id.departmentId = :deptId
                ),
                (SELECT MIN(es5.enrolledInMale + es5.enrolledInFemale + es5.enrolledOutMale + es5.enrolledOutFemale)
                    FROM StudentEnrollmentStatus es5
                    WHERE es5.id.departmentId = :deptId
                )
            )
            FROM StudentEnrollmentStatus es
            JOIN es.department d
            WHERE es.id.year=:year and es.id.departmentId = :deptId
            """)
    List<EnrollmentSummaryDto> findByYear(@Param("year") int year, @Param("deptId") int deptId);
}
