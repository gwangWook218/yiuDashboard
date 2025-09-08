package com.yiuDashboard.repository;

import com.yiuDashboard.dto.DropoutRateDTO;
import com.yiuDashboard.entity.dropoutRate.DropoutByCollege;
import com.yiuDashboard.entity.dropoutRate.DropoutByCollegeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DropoutRateRepository extends JpaRepository<DropoutByCollege, DropoutByCollegeId> {

    @Query("""
            SELECT new com.yiuDashboard.dto.DropoutRateDTO(
                dbc.id.year,
                dd.dropoutCollege,
                dd.dropoutDept,
                dbc.percentage,
                dbc.percentage - (SELECT AVG(dbc2.percentage)
                                  FROM DropoutByCollege dbc2
                                  WHERE dbc2.id.year = dbc.id.year)
            )
            FROM DropoutByCollege dbc
            JOIN dbc.dropoutDepartment dd
            WHERE dbc.id.studentType = '잠재위험학생'
              AND dbc.id.year = :year
            ORDER BY dbc.percentage - (SELECT AVG(dbc2.percentage)
                                  FROM DropoutByCollege dbc2
                                  WHERE dbc2.id.year = dbc.id.year) DESC
            """)
    List<DropoutRateDTO> findByDeptName(@Param("year") int year);
}
