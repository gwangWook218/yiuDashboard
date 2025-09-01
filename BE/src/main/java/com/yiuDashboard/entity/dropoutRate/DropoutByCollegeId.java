package com.yiuDashboard.entity.dropoutRate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DropoutByCollegeId {
    @Column(length = 10)
    private String year;
    @Column(length = 50)
    private String collegeName;
    @Column(length = 50)
    private String deptName;
    @Column(length = 20)
    private String studentType;
}
