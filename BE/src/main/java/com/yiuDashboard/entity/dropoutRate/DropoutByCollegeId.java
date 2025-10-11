package com.yiuDashboard.entity.dropoutRate;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class DropoutByCollegeId {
    private Integer dropoutDeptId;
    private Integer year;
}
