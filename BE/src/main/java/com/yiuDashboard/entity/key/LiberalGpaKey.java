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
public class LiberalGpaKey implements Serializable {

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "semester", nullable = false, length = 10)
    private String semester;

    @Column(name = "grade_code", nullable = false, length = 5)
    private String gradeCode;
}

