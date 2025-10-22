package com.yiuDashboard.repository;

import com.yiuDashboard.dto.gradEmployment.EmployAdmissionDto;
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
                ROUND(
                    (ge.insuredEmployeesMale + ge.insuredEmployeesFemale
                    + ge.overseasEmployeesMale + ge.overseasEmployeesFemale
                    + ge.agricultureFisheryWorkersMale + ge.agricultureFisheryWorkersFemale
                    + ge.individualCreatorsMale + ge.individualCreatorsFemale
                    + ge.selfEmployedMale + ge.selfEmployedFemale
                    + ge.freelancersMale + ge.freelancersFemale)
                    * 100.0 / CASE WHEN (g.graduatesMale + g.graduatesFemale) = 0 THEN 1 ELSE (g.graduatesMale + g.graduatesFemale) END
                ),
                ge.insuredEmployeesMale,
                ge.insuredEmployeesFemale,
                (ge.insuredEmployeesMale + ge.insuredEmployeesFemale),
                ge.overseasEmployeesMale,
                ge.overseasEmployeesFemale,
                (ge.overseasEmployeesMale + ge.overseasEmployeesFemale),
                ge.agricultureFisheryWorkersMale,
                ge.agricultureFisheryWorkersFemale,
                (ge.agricultureFisheryWorkersMale + ge.agricultureFisheryWorkersFemale),
                ge.individualCreatorsMale,
                ge.individualCreatorsFemale,
                (ge.individualCreatorsMale + ge.individualCreatorsFemale),
                ge.selfEmployedMale,
                ge.selfEmployedFemale,
                (ge.selfEmployedMale + ge.selfEmployedFemale),
                ge.freelancersMale,
                ge.freelancersFemale,
                (ge.freelancersMale + ge.freelancersFemale),
                ge.furtherStudyMale,
                ge.furtherStudyFemale,
                (ge.furtherStudyMale + ge.furtherStudyFemale),
                ge.militaryService,
                ge.unableToWorkMale,
                ge.unableToWorkFemale,
                (ge.unableToWorkMale + ge.unableToWorkFemale),
                ge.foreignStudentsMale,
                ge.foreignStudentsFemale,
                (ge.foreignStudentsMale + ge.foreignStudentsFemale),
                ge.excludedCasesMale,
                ge.excludedCasesFemale,
                (ge.excludedCasesMale + ge.excludedCasesFemale),
                ge.othersMale,
                ge.othersFemale,
                (ge.othersMale + ge.othersFemale),
                ge.unknownMale,
                ge.unknownFemale,
                (ge.unknownMale + ge.unknownFemale)
            )
            FROM GraduateEmployment ge
            JOIN ge.graduate g
            JOIN g.department d
            WHERE g.year = :year and d.deptId = :deptId
            """)
    GraduateStatsDTO findGraduateStats(@Param("year") int year, @Param("deptId") int deptId);

    @Query("""
            SELECT new com.yiuDashboard.dto.gradEmployment.EmployAdmissionDto(
                d.department,
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
                ROUND((ge.othersMale + ge.othersFemale) * 100.0 / (g.graduatesMale + g.graduatesFemale))
            )
            FROM GraduateEmployment ge
            JOIN ge.graduate g
            JOIN g.department d
            WHERE g.year = :year and d.deptId = :deptId
            """)
    EmployAdmissionDto findByYearAndDept(@Param("year") int year, @Param("deptId") int deptId);
}
