package com.yiuDashboard.entity.gradAdmission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GraduateAdmissionStatsId implements Serializable {
    @Column(name = "year")
    private Integer year;
    @Column(name = "department_id")
    private Integer departmentId;
}
