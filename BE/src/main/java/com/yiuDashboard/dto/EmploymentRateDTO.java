package com.yiuDashboard.dto;

import lombok.Data;

@Data
public class EmploymentRateDTO {
    private Integer year;
    private String college;
    private String department;
    private String isDaytime;
    private String isContract;
    private Double employmentRate;

    public EmploymentRateDTO(Integer year, String college, String department, String isDaytime, String isContract, Double employmentRate) {
        this.year = year;
        this.college = college;
        this.department = department;
        this.isDaytime = isDaytime;
        this.isContract = isContract;
        this.employmentRate = employmentRate;

    }
}
