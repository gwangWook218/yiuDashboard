package com.yiuDashboard.dto.gradEmployment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateStatsDTO {
    private String department;
    private int totalGraduates;
    private int totalEmployed;
    private Double employmentRate;

    private int insuredEmployeesMale;
    private int insuredEmployeesFemale;
    private int insuredEmployeesTotal;

    private int overseasEmployeesMale;
    private int overseasEmployeesFemale;
    private int overseasEmployeesTotal;

    private int agricultureFisheryWorkersMale;
    private int agricultureFisheryWorkersFemale;
    private int agricultureFisheryTotal;

    private int individualCreatorsMale;
    private int individualCreatorsFemale;
    private int individualCreatorsTotal;

    private int selfEmployedMale;
    private int selfEmployedFemale;
    private int selfEmployedTotal;

    private int freelancersMale;
    private int freelancersFemale;
    private int freelancersTotal;

    private int furtherStudyMale;
    private int furtherStudyFemale;
    private int furtherStudyTotal;

    private int militaryService;

    private int unableToWorkMale;
    private int unableToWorkFemale;
    private int unableToWorkTotal;

    private int foreignStudentsMale;
    private int foreignStudentsFemale;
    private int foreignStudentsTotal;

    private int excludedCasesMale;
    private int excludedCasesFemale;
    private int excludedCasesTotal;

    private int othersMale;
    private int othersFemale;
    private int othersTotal;

    private int unknownMale;
    private int unknownFemale;
    private int unknownTotal;
}
