package com.yiuDashboard.dto.gradEmployment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentRateResponseDTO {
    private int year;
    private Double overallAvg;
    private List<DepartmentEmploymentDTO> departments;

}
