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
    private String majorCategory;      // 계열명

    @Column(nullable = false)
    private String departmentName;     // 학과명

    @Column(nullable = false)
    private int studentQuota;          // 모집 정원

    @Column(nullable = false)
    private int enrolledStudentCount;  // 재학생 수

    @Column
    private double fillRate;           // 충원율

    // 전국 평균 및 유사 계열 평균 비교용
    @Column
    private Double nationalAverageFillRate;  // 전국 평균 충원율

    @Column
    private Double similarMajorAverageFillRate; // 유사 계열 평균 충원율

    // 졸업생 진출 진로 요약 정보
    @Column(length = 1000)
    private String graduateCareerSummary;

    // 전공 특성, 주요 교과목, 관련 진로 등 요약 정보
    @Column(length = 1000)
    private String majorInfoSummary;

    public double calculateFillRate() {
        if (studentQuota == 0) return 0.0;
        return ((double) enrolledStudentCount / studentQuota) * 100.0;
    }
}

