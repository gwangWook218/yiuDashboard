package com.yiuDashboard.repository;

import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.entity.gradEmployment.GraduateEmployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmploymentRateRepository extends JpaRepository<GraduateEmployment, Integer> {
    @Query(value = """
            SELECT new com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO(
                d.department,
                d.isDaytime,
                (g.graduatesMale + g.graduatesFemale),
                (ge.insuredEmployeesMale + ge.insuredEmployeesFemale
                + ge.overseasEmployeesMale + ge.overseasEmployeesFemale
                + ge.agricultureFisheryWorkersMale + ge.agricultureFisheryWorkersFemale
                + ge.individualCreatorsMale + ge.individualCreatorsFemale
                + ge.selfEmployedMale + ge.selfEmployedFemale
                + ge.freelancersMale + ge.freelancersFemale),
                ROUND(
                    (ge.insuredEmployeesMale + ge.insuredEmployeesFemale
                    + ge.overseasEmployeesMale + ge.overseasEmployeesFemale
                    + ge.agricultureFisheryWorkersMale + ge.agricultureFisheryWorkersFemale
                    + ge.individualCreatorsMale + ge.individualCreatorsFemale
                    + ge.selfEmployedMale + ge.selfEmployedFemale
                    + ge.freelancersMale + ge.freelancersFemale)
                    * 100.0 / (g.graduatesMale + g.graduatesFemale)
                ),
                (ge.furtherStudyMale + ge.furtherStudyFemale),
                ROUND((ge.furtherStudyMale + ge.furtherStudyFemale) * 100.0 / (g.graduatesMale + g.graduatesFemale)),
                (ge.othersMale + ge.othersFemale),
                ROUND((ge.othersMale + ge.othersFemale) * 100.0 / (g.graduatesMale + g.graduatesFemale)),
                (ge.insuredEmployeesMale + ge.insuredEmployeesFemale),
                (ge.overseasEmployeesMale + ge.overseasEmployeesFemale),
                (ge.agricultureFisheryWorkersMale + ge.agricultureFisheryWorkersFemale),
                (ge.individualCreatorsMale + ge.individualCreatorsFemale),
                (ge.selfEmployedMale + ge.selfEmployedFemale),
                (ge.freelancersMale + ge.freelancersFemale)
            )
            FROM GraduateEmployment ge
            JOIN ge.graduate g
            JOIN g.department d
            WHERE g.year = :year
            """)
    List<GraduateStatsDTO> findGraduateStats(@Param("year") int year);
}
