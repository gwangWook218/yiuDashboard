package com.yiuDashboard.dto;

import lombok.Getter;

@Getter
public class AdmissionRateDTO {
    private int year;
    private long totalGraduates;
    private long totalAdmissions;
    private double admissionRate;

    public AdmissionRateDTO(int year, long totalGraduates, long totalAdmissions) {
        this.year = year;
        this.totalGraduates = totalGraduates;
        this.totalAdmissions = totalAdmissions;
        this.admissionRate = totalGraduates == 0 ? 0 : Math.round(((double) totalAdmissions / totalGraduates) * 1000) / 10.0;
    }

}
