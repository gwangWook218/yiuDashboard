package com.yiuDashboard.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode
@Embeddable
public class GradeDistKey implements Serializable {

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "semester", nullable = false, length = 10)
    private String semester;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @Column(name = "grade_code", nullable = false, length = 3)
    private String gradeCode;
}
