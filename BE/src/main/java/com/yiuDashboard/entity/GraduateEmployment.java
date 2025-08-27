package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "graduate_employment")
public class GraduateEmployment {

    @EmbeddedId
    private GraduateEmploymentId id;

    private Integer graduates;
    private Integer insuredEmployees;
    private Integer overseasEmployees;
    private Integer agricultureFisheryWorkers;
    private Integer individualCreators;
    private Integer selfEmployed;
    private Integer freelancers;
    private Integer furtherStudy;
    private Integer militaryService;
    private Integer unableToWork;
    private Integer foreignStudents;
    private Integer excludedCases;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_id")
    private Department department;
}
