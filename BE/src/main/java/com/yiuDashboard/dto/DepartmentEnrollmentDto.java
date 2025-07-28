package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEnrollmentDto {

    private int 기준연도;
    private String 단과대학;
    private String 학과;
    private int 재학생수;
    private double 단과대평균재학생수;
}
