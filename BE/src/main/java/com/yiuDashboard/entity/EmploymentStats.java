package com.yiuDashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "graduate_employment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentStats {

    @EmbeddedId
    private EmploymentStatsId id;

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

    @Column(name = "further_study")
    private Integer furtherStudy;

    @Column(name = "military_service")
    private Integer militaryService;

    @Column(name = "unable_to_work")
    private Integer unableToWork;

    @Column(name = "foreign_students")
    private Integer foreignStudents;

    @Column(name = "excluded_cases")
    private Integer excludedCases;

    @Column(name = "others")
    private Integer others;

    @Column(name = "unknown")
    private Integer unknown;
}