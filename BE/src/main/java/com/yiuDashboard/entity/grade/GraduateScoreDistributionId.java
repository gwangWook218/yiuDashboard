package com.yiuDashboard.entity.grade;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraduateScoreDistributionId implements Serializable {
    private Integer year;
    private Integer departmentId;
}
