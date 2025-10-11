package com.yiuDashboard.entity.grade;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "liberal_gpa_distribution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LiberalGpaDistributionId.class)
public class LiberalGpaDistribution {

    @Id
    private int year;

    @Id
    private String semester;

    @Id
    @ManyToOne
    @JoinColumn(name = "grade_code", referencedColumnName = "grade_code")
    private GradeDefinition gradeDefinition;

    private int studentCount;
}
