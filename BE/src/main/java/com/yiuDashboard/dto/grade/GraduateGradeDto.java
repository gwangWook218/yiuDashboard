package com.yiuDashboard.dto.grade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class GraduateGradeDto {
    private Integer year;
    private String department;
    private BigDecimal avgGpa;
    private BigDecimal avgPercentScore;
    private Integer count;
}
