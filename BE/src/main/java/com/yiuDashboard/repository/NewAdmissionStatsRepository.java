package com.yiuDashboard.repository;

import com.yiuDashboard.entity.NewAdmissionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NewAdmissionStatsRepository extends JpaRepository<NewAdmissionStats, Long> {

    public interface GraduateOutcomeAgg {
        Long getGraduates();
        Long getEmployment();
        Long getFurtherStudy();
        Long getOthers();
    }

    List<NewAdmissionStats> findByMajorCategory(String majorCategory);
    NewAdmissionStats findByDepartmentName(String departmentName);

    @Query("SELECT AVG(n.fillRate) FROM NewAdmissionStats n WHERE n.majorCategory = :majorCategory")
    Double findAverageFillRateByMajorCategory(@Param("majorCategory") String majorCategory);

    @Query(value = "SELECT SUM(recruitment_count) FROM admission_summary WHERE year = :year", nativeQuery = true)
    Long findQuotaSum(@Param("year") Integer year);

    @Query(value = "SELECT SUM(COALESCE(enrolled_in_male,0)+COALESCE(enrolled_in_female,0)) FROM student_enrollment_status WHERE year = :year", nativeQuery = true)
    Long findEnrolledSum(@Param("year") Integer year);

    @Query(value = """
        SELECT
          SUM(COALESCE(graduates,0)) AS graduates,
          SUM(COALESCE(insured_employees,0)+COALESCE(overseas_employees,0)+COALESCE(agriculture_fishery_workers,0)
              +COALESCE(individual_creators,0)+COALESCE(self_employed,0)+COALESCE(freelancers,0)) AS employment,
          SUM(COALESCE(further_study,0)) AS furtherStudy,
          SUM(COALESCE(military_service,0)+COALESCE(unable_to_work,0)+COALESCE(foreign_students,0)
              +COALESCE(excluded_cases,0)+COALESCE(others,0)+COALESCE(unknown,0)) AS others
        FROM graduate_employment
        WHERE year = :year AND department_id = :deptId
        """, nativeQuery = true)
    GraduateOutcomeAgg aggregateGraduateOutcome(@Param("year") Integer year, @Param("deptId") Integer deptId);
}