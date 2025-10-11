package com.yiuDashboard.dto.grade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GradeRangeDto {
    private Integer year;
    private String department;
    private String scoreRange;
    private Long studentCount;
}
