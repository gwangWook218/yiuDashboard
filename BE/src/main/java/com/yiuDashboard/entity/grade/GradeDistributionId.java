package com.yiuDashboard.entity.grade;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDistributionId implements Serializable {
    private int year;
    private int semester;
    private Integer departmentId;
    private String gradeCode;
}
