package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "graduate_employment")
@IdClass(GraduateEmploymentId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateEmployment {
    @Id
    @Column(name = "year")
    private Integer year;

    @Id
    @Column(name = "department_id")
    private Integer departmentId;

    @Id
    @Column(name = "gender")
    private String gender;

    @Column(name = "graduates")
    private Integer graduates;

    @Column(name = "insured_employees")
    private Integer insuredEmployees;

    @Column(name = "overseas_employees")
    private Integer overseasEmployees;

    @Column(name = "agriculture_fishery_workers")
    private Integer agricultureFisheryWorkers;

    @Column(name = "individual_creators")
    private Integer individualCreators;

    @Column(name = "self_employed")
    private Integer selfEmployed;

    @Column(name = "freelancers")
    private Integer freelancers;
}
