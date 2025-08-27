package com.yiuDashboard.repository;

import com.yiuDashboard.dto.EmploymentRateDTO;
import com.yiuDashboard.entity.GraduateEmployment;
import com.yiuDashboard.entity.GraduateEmploymentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploymentRateRepository extends JpaRepository<GraduateEmployment, GraduateEmploymentId> {
    @Query("""
            SELECT new com.yiuDashboard.dto.EmploymentRateDTO(
                ge.id.year,
                d.college,
                d.department,
                d.isDaytime,
                d.isContract,
                ROUND(SUM(ge.insuredEmployees + ge.overseasEmployees + ge.agricultureFisheryWorkers + ge.individualCreators + ge.selfEmployed + ge.freelancers)
                / (
                    SUM(ge.graduates)
                    - SUM(ge.furtherStudy)
                    - SUM(CASE WHEN ge.id.gender IS NULL THEN ge.militaryService ELSE 0 END)
                    - SUM(ge.unableToWork)
                    - SUM(ge.foreignStudents)
                    - SUM(ge.excludedCases)
                )*100.0, 1)
            )
            FROM GraduateEmployment ge
            JOIN ge.department d
            WHERE ge.id.year = :year
            GROUP BY ge.id.year, ge.id.departmentId
            """)
    List<EmploymentRateDTO> findEmployRateByYear(@Param("year") int year);
}
