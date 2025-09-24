package com.yiuDashboard.repository;

import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.entity.gradEmployment.GraduateEmployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmploymentRateRepository extends JpaRepository<GraduateEmployment, Integer> {
    @Query(value = """
            SELECT new com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO(
                d.department,
                (g.graduatesMale + g.graduatesFemale),
                (ge.insuredEmployeesMale + ge.insuredEmployeesFemale
                + ge.overseasEmployeesMale + ge.overseasEmployeesFemale
                + ge.agricultureFisheryWorkersMale + ge.agricultureFisheryWorkersFemale
                + ge.individualCreatorsMale + ge.individualCreatorsFemale
                + ge.selfEmployedMale + ge.selfEmployedFemale
                + ge.freelancersMale + ge.freelancersFemale),
                0.0,
                (ge.furtherStudyMale + ge.furtherStudyFemale),
                0.0,
                (ge.othersMale + ge.othersFemale
                + ge.unknownMale + ge.unknownFemale
                + ge.unableToWorkMale + ge.unableToWorkFemale),
                0.0
            )
            FROM GraduateEmployment ge
            JOIN ge.graduate g
            JOIN g.department d
            WHERE g.year = :year and d.department = :department
            """)
    Optional<GraduateStatsDTO> findGraduateStats(@Param("year") int year,
                                                 @Param("department") String department);
}
