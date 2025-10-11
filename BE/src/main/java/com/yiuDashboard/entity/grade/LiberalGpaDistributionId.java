package com.yiuDashboard.entity.grade;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiberalGpaDistributionId implements Serializable {
    private int year;
    private String semester;
    private String gradeDefinition; // GradeDefinition PK 타입
}
