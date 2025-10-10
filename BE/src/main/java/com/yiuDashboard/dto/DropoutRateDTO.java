package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DropoutRateDTO {
    private String departments;
    private Integer atRiskStudents;
    private Double riskRate;
    private Integer nonRiskStudents;
    private Double normalRate;
    private Integer total;
    private Double totalRate;
}
