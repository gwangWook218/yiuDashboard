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
public class YearDeptGenderKey implements Serializable {

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @Column(name = "gender", nullable = false, length = 3)
    private String gender;
}
