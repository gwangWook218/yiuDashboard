package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GraduateAdmissionDto {
    private String department;
    private String isDaytime;
    private int totalAdmission;
    private double admissionRate;
    private int domesticKorJrCollege;
    private int domesticCollege;
    private int domesticGrad;
    private int overseasKorJrCollege;
    private int overseasCollege;
    private int overseasGrad;
}
