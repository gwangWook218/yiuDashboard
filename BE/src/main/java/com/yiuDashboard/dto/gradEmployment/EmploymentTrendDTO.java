package com.yiuDashboard.dto.gradEmployment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentTrendDTO {
    private String college;
    private String department;
    private String isDaytime;
    private String isContract;
    private List<YearEmploymentRateDTO> trend;
}
