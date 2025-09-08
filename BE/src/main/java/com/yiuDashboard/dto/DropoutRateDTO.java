package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DropoutRateDTO {
    private Integer year;
    private String college;
    private String department;
    private Double riskPercentage;
    private Double deviationFromAvg;
}
