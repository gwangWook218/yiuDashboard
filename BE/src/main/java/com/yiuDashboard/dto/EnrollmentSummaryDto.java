package com.yiuDashboard.dto;

import lombok.Data;

@Data
public class EnrollmentSummaryDto {
    private Integer year;
    private String college;
    private String department;
    private Long totalStudents;

    // JPQL에서 호출할 생성자
    public EnrollmentSummaryDto(Integer year, String college, String department, Long totalStudents) {
        this.year = year;
        this.college = college;
        this.department = department;
        this.totalStudents = totalStudents;
    }
}
