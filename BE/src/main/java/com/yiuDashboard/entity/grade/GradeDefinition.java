package com.yiuDashboard.entity.grade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade_definition")
public class GradeDefinition {

    @Id
    @Column(name = "grade_code", length = 5)
    private String gradeCode;

    @Column(name = "gpa_value")
    private Double gpaValue;
}
