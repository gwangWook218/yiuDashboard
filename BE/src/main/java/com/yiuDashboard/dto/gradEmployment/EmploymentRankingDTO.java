package com.yiuDashboard.dto.gradEmployment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentRankingDTO {
    private int year;
    private List<DepartmentRateDTO> topDepartments;
    private List<DepartmentRateDTO> bottomDepartments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentRateDTO {
        private String college;
        private String department;
        private Double employmentRate;
    }
}
