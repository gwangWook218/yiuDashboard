package com.yiuDashboard.entity.dropoutRate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dropout_department")
public class DropoutDepartment {

    @Id
    @Column(name = "dropout_dept_id")
    private Integer dropoutDeptId;

    private String dropoutCollege;
    private String dropoutDept;
}
