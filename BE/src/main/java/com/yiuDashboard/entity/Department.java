package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "department")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Department {
    @Id
    @Column(name = "dept_id")
    private Integer id;

    @Column(length = 50)
    private String college;     // 단과대

    @Column(length = 50)
    private String department;  // 학과명

    @Column(name = "is_daytime", length = 50)
    private String isDaytime;

    @Column(name = "is_contract", length = 50)
    private String isContract;
}
