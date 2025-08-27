package com.yiuDashboard.dto;

import lombok.Data;

@Data
public class RecruitmentRateDto {

    //    수시 비율
    private Integer year;
    private String admissionCategory;
    private Double recruitmentRate;

    public RecruitmentRateDto(Integer year, String admissionCategory, Double recruitmentRate) {
        this.year = year;
        this.admissionCategory = admissionCategory;
        this.recruitmentRate = recruitmentRate;
    }
}
