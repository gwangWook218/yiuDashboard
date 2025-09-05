package com.yiuDashboard.repository;

import com.yiuDashboard.dto.gradEmployment.EmploymentRankingDTO;
import com.yiuDashboard.entity.gradEmployment.GraduateEmployment;
import com.yiuDashboard.entity.gradEmployment.GraduateEmploymentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploymentRateRepository extends JpaRepository<GraduateEmployment, GraduateEmploymentId> {
    @Query(value = """
             SELECT
                 d.college AS college,
                 d.department AS department,
                 d.is_daytime AS isDaytime,
                 d.is_contract AS isContract,
                 ROUND(SUM(ge.insured_employees + ge.overseas_employees + ge.agriculture_fishery_workers
                    + ge.individual_creators + ge.self_employed + ge.freelancers)
                    / (
                        SUM(ge.graduates)
                        - SUM(ge.further_study)
                        - SUM(CASE WHEN ge.gender IS NULL THEN ge.military_service ELSE 0 END)
                        - SUM(ge.unable_to_work)
                        - SUM(ge.foreign_students)
                        - SUM(ge.excluded_cases)
                    )*100, 1) AS employmentRate
             FROM graduate_employment ge
             JOIN department d ON ge.department_id = d.dept_id
             WHERE ge.year = :year
             GROUP BY ge.year, d.college, d.department, d.is_daytime, d.is_contract
            """, nativeQuery = true)
    List<Object[]> findEmployRateByYear(@Param("year") int year);

    @Query(value = """
             SELECT
                 year,
                 d.college,
                 d.department,
                 d.is_daytime,
                 d.is_contract,
                 ROUND(SUM(ge.insured_employees + ge.overseas_employees + ge.agriculture_fishery_workers
                    + ge.individual_creators + ge.self_employed + ge.freelancers)
                    / (
                        SUM(ge.graduates)
                        - SUM(ge.further_study)
                        - SUM(CASE WHEN ge.gender IS NULL THEN ge.military_service ELSE 0 END)
                        - SUM(ge.unable_to_work)
                        - SUM(ge.foreign_students)
                        - SUM(ge.excluded_cases)
                    )*100, 1) AS employmentRate
             FROM graduate_employment ge
             JOIN department d ON ge.department_id = d.dept_id
             WHERE ge.department_id = :departmentId
             GROUP BY ge.year
            """, nativeQuery = true)
    List<Object[]> findTrendByYear(@Param("departmentId") int departmentId);

    @Query("""
            SELECT new com.yiuDashboard.dto.gradEmployment.EmploymentRankingDTO$DepartmentRateDTO(
                d.college,
                d.department,
                ROUND(SUM(ge.insuredEmployees + ge.overseasEmployees + ge.agricultureFisheryWorkers
                + ge.individualCreators + ge.selfEmployed + ge.freelancers)
                / (
                    SUM(ge.graduates)
                    - SUM(ge.furtherStudy)
                    - SUM(CASE WHEN ge.id.gender IS NULL THEN ge.militaryService ELSE 0 END)
                    - SUM(ge.unableToWork)
                    - SUM(ge.foreignStudents)
                    - SUM(ge.excludedCases)
                ) * 100.0)
            )
            FROM GraduateEmployment ge
            JOIN ge.department d
            WHERE ge.id.year = :year
            GROUP BY d.department, d.college
            ORDER BY (SUM(ge.insuredEmployees + ge.overseasEmployees + ge.agricultureFisheryWorkers
                + ge.individualCreators + ge.selfEmployed + ge.freelancers)
                / (
                    SUM(ge.graduates)
                    - SUM(ge.furtherStudy)
                    - SUM(CASE WHEN ge.id.gender IS NULL THEN ge.militaryService ELSE 0 END)
                    - SUM(ge.unableToWork)
                    - SUM(ge.foreignStudents)
                    - SUM(ge.excludedCases)
                ) * 100.0) DESC
            """)
    List<EmploymentRankingDTO.DepartmentRateDTO> findRankingByYear(@Param("year") int year);
}
