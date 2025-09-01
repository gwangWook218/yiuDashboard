package com.yiuDashboard.dto;

import lombok.Data;

@Data
public class DropoutRateDTO {
    private String year;
    private String deptName;
    private Double riskPercentage;
    private Double deviationFromAvg;

    public DropoutRateDTO(String year, String deptName, Double riskPercentage, Double deviationFromAvg) {
        this.year = year;
        this.deptName = deptName;
        this.riskPercentage = riskPercentage;
        this.deviationFromAvg = deviationFromAvg;
    }
}
