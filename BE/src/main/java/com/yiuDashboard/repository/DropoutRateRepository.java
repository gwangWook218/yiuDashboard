package com.yiuDashboard.repository;

import com.yiuDashboard.entity.dropoutRate.DropoutByCollege;
import com.yiuDashboard.entity.dropoutRate.DropoutByCollegeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DropoutRateRepository extends JpaRepository<DropoutByCollege, DropoutByCollegeId> {

    @Query(value = """
            SELECT
                d.year AS year,
                d.dept_name AS deptName,
                d.percentage AS riskPercentage,
                (d.percentage - AVG(d.percentage) OVER(PARTITION BY d.year)) AS deviationFromAvg
            FROM dropout_by_college d
            WHERE d.student_type = '잠재위험학생'
              AND d.dept_name <> '소계'
              AND d.year = :year
            ORDER BY deviationFromAvg DESC
            """, nativeQuery = true)
    List<Object[]> findByDeptName(@Param("year") String year);
}
