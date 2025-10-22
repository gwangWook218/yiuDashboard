package com.yiuDashboard.dto.gradEmployment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployAdmissionDto {
    private String department;
    private int totalGraduates;
    private int totalEmployeed;
    private Double employmentRate;
    private int totalAdmission;
    private Double admissionRate;
    private int totalEtc;
    private Double etcRate;
}
