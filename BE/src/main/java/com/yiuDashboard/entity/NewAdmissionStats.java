package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "new_admission_stats")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class NewAdmissionStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @Column(name = "major_category", length = 100, nullable = false)
    private String majorCategory;

    @Column(name = "department_name", length = 255, nullable = false)
    private String departmentName;

    @Column(name = "student_quota", nullable = false)
    private Integer studentQuota;

    @Column(name = "enrolled_student_count", nullable = false)
    private Integer enrolledStudentCount;

    @Column(name = "fill_rate")
    private Double fillRate;

    @Column(name = "national_average_fill_rate")
    private Double nationalAverageFillRate;

    @Column(name = "similar_major_average_fill_rate")
    private Double similarMajorAverageFillRate;

    public double calculateFillRate() {
        if (studentQuota == null || studentQuota == 0 || enrolledStudentCount == null) return 0.0;
        double v = enrolledStudentCount * 100.0 / studentQuota;
        return Math.round(v * 100.0) / 100.0;
    }

    @PostLoad
    private void afterLoad() {
        if (fillRate == null) fillRate = calculateFillRate();
    }

    @PrePersist @PreUpdate
    private void beforeSave() {
        fillRate = calculateFillRate();
    }

    public Long getId() { return id; }
}