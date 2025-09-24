package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "graduates")
public class Graduates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "graduates_male")
    private Integer graduatesMale;

    @Column(name = "graduates_female")
    private Integer graduatesFemale;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "dept_id")
    private Department department;
}
