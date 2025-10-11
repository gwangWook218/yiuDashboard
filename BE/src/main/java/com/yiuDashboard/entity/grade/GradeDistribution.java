package com.yiuDashboard.entity.grade;

import com.yiuDashboard.entity.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade_distribution")
public class GradeDistribution {

    @EmbeddedId
    private GradeDistributionId id;

    @ManyToOne
    @MapsId("departmentId")
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @MapsId("gradeCode")
    @JoinColumn(name = "grade_code")
    private GradeDefinition gradeDefinition;

    private int studentCount;
}
