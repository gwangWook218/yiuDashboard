package com.yiuDashboard.entity.dropoutRate;

import jakarta.persistence.Embeddable;

@Embeddable
public class DropoutByCollegeId {
    private Integer dropoutDeptId;
    private Integer year;
}
