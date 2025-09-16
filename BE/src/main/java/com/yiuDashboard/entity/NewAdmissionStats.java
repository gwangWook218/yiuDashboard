package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "new_admission_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewAdmissionStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String majorCategory;

    @Column(nullable = false)
    private String departmentName;

    @Column(nullable = false)
    private int studentQuota;

    @Column(nullable = false)
    private int enrolledStudentCount;

    @Column
    private double fillRate;

    @Column
    private Double nationalAverageFillRate;

    @Column
    private Double similarMajorAverageFillRate;

    @Column(length = 1000)
    private String graduateCareerSummary;

    public double calculateFillRate() {
        if (studentQuota == 0) return 0.0;
        return ((double) enrolledStudentCount / studentQuota) * 100.0;
    }
}