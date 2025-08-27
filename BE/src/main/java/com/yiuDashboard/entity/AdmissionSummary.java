package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admission_summary")
public class AdmissionSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer year;

    @Column(name = "admission_type")
    private String admissionType;
    @Column(name = "recruitment_count")
    private Integer recruitmentCount;
    @Column(name = "applicant_count")
    private Integer applicantCount;
}
