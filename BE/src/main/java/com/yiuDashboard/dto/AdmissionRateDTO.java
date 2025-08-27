package com.yiuDashboard.dto;

import lombok.Data;

@Data
public class AdmissionRateDTO {
//    졸업생 진학률
    private int year;
    private Long totalGraduates;
    private Long totalAdmissions;
    private Double admissionRate;

    public AdmissionRateDTO(int year, Long totalGraduates, Long totalAdmissions, Double admissionRate) {
        this.year = year;
        this.totalGraduates = totalGraduates;
        this.totalAdmissions = totalAdmissions;
        this.admissionRate = admissionRate;
    }

}
