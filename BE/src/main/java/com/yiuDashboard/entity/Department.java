package com.yiuDashboard.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "college")
    private String college;
    @Column(name = "department")
    private String department;
    @Column(name = "is_daytime")
    private String isDaytime;
    @Column(name = "is_contract")
    private String isContract;
}
