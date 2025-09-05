package com.yiuDashboard.dto.gradEmployment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEmploymentDTO {
    private String college;
    private String department;
    private String isDaytime;
    private String isContract;
    private Double employmentRate;
}
