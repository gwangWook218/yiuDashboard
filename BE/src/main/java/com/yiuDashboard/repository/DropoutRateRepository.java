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
    @Query(value = """
            (select
            dd.dropout_dept as departments,
            at_risk_students,
            ROUND(at_risk_students/(select SUM(at_risk_students) from dashboard_db.dropout_by_college dbc where dbc.`year` = :year)*100, 1) as risk_rate,
            non_risk_students,
            ROUND(non_risk_students/(select SUM(non_risk_students) from dashboard_db.dropout_by_college dbc where dbc.`year` = :year)*100, 1) as normal_rate,
            (at_risk_students + non_risk_students) as total,
            ROUND((at_risk_students + non_risk_students)/(select SUM(at_risk_students + non_risk_students) from dashboard_db.dropout_by_college dbc where dbc.`year` = :year)*100, 1) as total_rate
            from dashboard_db.dropout_by_college dbc
            join dashboard_db.dropout_department dd on dd.dropout_dept_id = dbc.dropout_dept_id
            where dbc.`year` = :year)
            UNION ALL
            (select
            '소계' as departments,
            SUM(at_risk_students),
            ROUND(SUM(at_risk_students)/(select SUM(at_risk_students + non_risk_students) from dashboard_db.dropout_by_college dbc where dbc.`year` = :year)*100, 1) as risk_rate,
            SUM(non_risk_students),
            ROUND(SUM(non_risk_students)/(select SUM(at_risk_students + non_risk_students) from dashboard_db.dropout_by_college dbc where dbc.`year` = :year)*100, 1) as normal_rate,
            SUM(at_risk_students + non_risk_students) as total,
            ROUND(SUM(at_risk_students + non_risk_students)/(select SUM(at_risk_students + non_risk_students) from dashboard_db.dropout_by_college dbc where dbc.`year` = :year)*100, 1) as total_rate
            from dashboard_db.dropout_by_college dbc
            join dashboard_db.dropout_department dd on dd.dropout_dept_id = dbc.dropout_dept_id
            where dbc.`year` = :year)
            """, nativeQuery = true)
    List<Object[]> findDropoutStatsByYear(@Param("year") int year);

//    학과별 중도탈락현황 (학과별 상세)
    @Query("""
            SELECT new com.yiuDashboard.dto.DropoutRateDTO(
                dd.dropoutDept,
                dbc.atRiskStudents,
                round(dbc.atRiskStudents * 100.0 / (select (dbc2.atRiskStudents + dbc2.nonRiskStudents)
                    from DropoutByCollege dbc2 where dbc2.id.year = :year and dbc2.id.dropoutDeptId = :deptId), 1),
                dbc.nonRiskStudents,
                round(dbc.nonRiskStudents * 100.0 / (select (dbc3.atRiskStudents + dbc3.nonRiskStudents)
                    from DropoutByCollege dbc3 where dbc3.id.year = :year and dbc3.id.dropoutDeptId = :deptId), 1),
                (dbc.atRiskStudents + dbc.nonRiskStudents),
                round((dbc.atRiskStudents + dbc.nonRiskStudents) * 100.0 / (select SUM(dbc4.atRiskStudents + dbc4.nonRiskStudents)
                    from DropoutByCollege dbc4 where dbc4.id.year = :year and dbc4.id.dropoutDeptId = :deptId), 1)
            )
            from DropoutByCollege dbc
            join dbc.dropoutDepartment dd
            where dbc.id.year = :year and dbc.id.dropoutDeptId = :deptId
            """)
    DropoutRateDTO getDropoutDetail(@Param("year") int year, @Param("deptId") int deptId);
}
