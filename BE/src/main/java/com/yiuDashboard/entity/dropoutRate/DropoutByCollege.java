package com.yiuDashboard.entity.dropoutRate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dropout_by_college")
public class DropoutByCollege {

    @EmbeddedId
    private DropoutByCollegeId id;

    private Integer atRiskStudents;
    private Integer nonRiskStudents;

    @ManyToOne
    @MapsId("dropoutDeptId")
    @JoinColumn(name = "dropout_dept_id", referencedColumnName = "dropout_dept_id")
    private DropoutDepartment dropoutDepartment;
}
