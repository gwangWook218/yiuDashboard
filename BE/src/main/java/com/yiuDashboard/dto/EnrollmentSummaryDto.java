package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentSummaryDto {
    private Integer year;
    private String college;
    private String department;
    private Long totalStudents;
}
