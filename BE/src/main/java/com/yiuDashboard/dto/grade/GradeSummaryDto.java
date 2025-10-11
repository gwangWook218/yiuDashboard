package com.yiuDashboard.dto.grade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GradeSummaryDto {
    private Integer year;
    private String department;
    private Long totalCount;
    private Double avgGpa;
}
