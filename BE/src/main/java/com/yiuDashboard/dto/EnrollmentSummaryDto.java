package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentSummaryDto {
    private Integer year;
    private String department;
    private Number totalStudents;
    private Number oneYearLater;
    private Number avgStudents;
    private Number maxStudents;
    private Number minStudents;
}
