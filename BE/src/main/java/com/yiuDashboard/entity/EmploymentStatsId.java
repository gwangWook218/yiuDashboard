package com.yiuDashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EmploymentStatsId implements Serializable {
    @Column(name = "year", nullable = false)
    private Integer year;
    @Column(name = "department_id", nullable = false)
    private Integer departmentId;
    @Column(name = "gender", length = 10, nullable = false)
    private String gender;
}