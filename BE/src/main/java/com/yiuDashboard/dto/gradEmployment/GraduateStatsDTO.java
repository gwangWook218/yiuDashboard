package com.yiuDashboard.dto.gradEmployment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateStatsDTO {
    private String department;
    private Integer total;
    private Integer employed;
    private Double employedRate;
    private Integer admission;
    private Double admissionRate;
    private Integer etc;
    private Double etcRate;
}
