package com.yiuDashboard.repository;

import com.yiuDashboard.dto.grade.GradeRangeDto;
import com.yiuDashboard.dto.grade.GradeSummaryDto;
import com.yiuDashboard.entity.grade.GradeDistributionId;
import com.yiuDashboard.entity.grade.GradeDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeDistributionRepository extends JpaRepository<GradeDistribution, GradeDistributionId> {
    @Query("""
            select new com.yiuDashboard.dto.grade.GradeSummaryDto(
                gd.id.year,
                d.department,
                SUM(gd.studentCount),
                ROUND(SUM(gd.studentCount * gd.gradeDefinition.gpaValue) * 1.0 / SUM(gd.studentCount), 2)
            )
            FROM GradeDistribution gd
            JOIN gd.department d
            WHERE gd.id.year = :year
                AND gd.id.semester = :semester
                AND d.department = :dept
            GROUP BY gd.id.year, d.department
            """)
    GradeSummaryDto findGradeSummary(@Param("year") int year,
                                     @Param("semester") int semester,
                                     @Param("dept") String dept);

    @Query("""
        SELECT new com.yiuDashboard.dto.grade.GradeRangeDto(
            gd.id.year,
            d.department,
            CASE gd.gradeDefinition.gradeCode
                WHEN 'A+' THEN '100~95'
                WHEN 'A0' THEN '95~90'
                WHEN 'B+' THEN '90~85'
                WHEN 'B0' THEN '85~80'
                WHEN 'C+' THEN '80~75'
                WHEN 'C0' THEN '75~70'
                WHEN 'D+' THEN '70~65'
                WHEN 'D0' THEN '65~60'
                WHEN 'F' THEN '60 미만'
            END,
            SUM(gd.studentCount)
        )
        FROM GradeDistribution gd
        JOIN gd.department d
        WHERE gd.id.year = :year
          AND gd.id.semester = :semester
          AND d.department = :dept
        GROUP BY gd.id.year, d.department, gd.gradeDefinition.gradeCode
        ORDER BY CASE gd.gradeDefinition.gradeCode
            WHEN 'A+' THEN 1
            WHEN 'A0' THEN 2
            WHEN 'B+' THEN 3
            WHEN 'B0' THEN 4
            WHEN 'C+' THEN 5
            WHEN 'C0' THEN 6
            WHEN 'D+' THEN 7
            WHEN 'D0' THEN 8
            WHEN 'F'  THEN 9
        END
    """)
    List<GradeRangeDto> findGradeRangeSummary(
            @Param("year") int year,
            @Param("semester") int semester,
            @Param("dept") String dept
    );
}
