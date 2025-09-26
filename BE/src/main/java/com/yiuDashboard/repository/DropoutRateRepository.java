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

//    학과별 중도탈락현황 (AI융합대학)
    @Query("""
            SELECT new com.yiuDashboard.dto.DropoutRateDTO(
                 dd.dropoutDept,
                 round(dbc.atRiskStudents * 100.0 / (select sum(dbc2.atRiskStudents)
                    from DropoutByCollege dbc2 where dbc2.id.year = :year), 1),
                 round(dbc.nonRiskStudents * 100.0 / (select sum(dbc3.nonRiskStudents)
                    from DropoutByCollege dbc3 where dbc3.id.year = :year), 1),
                 round((dbc.atRiskStudents + dbc.nonRiskStudents) * 100.0 / (select sum(dbc4.atRiskStudents + dbc4.nonRiskStudents)
                    from DropoutByCollege dbc4 where dbc4.id.year = :year), 1)
            )
            FROM DropoutByCollege dbc
            JOIN dbc.dropoutDepartment dd
            WHERE dbc.id.year = :year
            """)
    List<DropoutRateDTO> findDropoutStatsByYear(@Param("year") int year);

//    학과별 중도탈락현황 (학과별 상세)
    @Query("""
            SELECT new com.yiuDashboard.dto.DropoutRateDTO(
                dd.dropoutDept,
                round(dbc.atRiskStudents * 100.0 / (select sum(dbc2.atRiskStudents)
                    from DropoutByCollege dbc2 where dbc2.id.year = :year), 1),
                round(dbc.nonRiskStudents * 100.0 / (select sum(dbc3.nonRiskStudents)
                    from DropoutByCollege dbc3 where dbc3.id.year = :year), 1),
                round((dbc.atRiskStudents + dbc.nonRiskStudents) * 100.0 / (select sum(dbc4.atRiskStudents + dbc4.nonRiskStudents)
                    from DropoutByCollege dbc4 where dbc4.id.year = :year), 1)
            )
            from DropoutByCollege dbc
            join dbc.dropoutDepartment dd
            where dbc.id.year = :year and dd.dropoutDept = :dept
            """)
    DropoutRateDTO getDropoutDetail(@Param("year") int year, @Param("dept") String dept);
}
