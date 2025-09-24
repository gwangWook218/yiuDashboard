package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
