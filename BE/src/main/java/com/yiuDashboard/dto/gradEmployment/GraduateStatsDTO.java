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
    private String isDaytime;
    private int totalGraduates;
    private int totalEmployed;
    private Double employmentRate;
    private int totalAdmission;
    private Double admissionRate;
    private int totalEtc;
    private Double etcRate;
    private int insuredEmployeesTotal;
    private int overseasEmployeesTotal;
    private int agricultureFisheryTotal;
    private int individualCreatorsTotal;
    private int selfEmployedTotal;
    private int freelancersTotal;
}
