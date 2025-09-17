package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employment_stats")
public class EmploymentStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 기준 연도 */
    @Column(name = "year", nullable = false)
    private Integer year;

    /** 학과명 */
    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    /** 졸업생 수 */
    @Column(name = "graduates", nullable = false)
    private int graduates;

    /** 취업자 수 */
    @Column(name = "employed", nullable = false)
    private int employed;

    /** 취업률(%) */
    @Column(name = "employment_rate", nullable = false)
    private double employmentRate;

    /** 졸업생 진출 분야 (다른 브랜치에서 추가된 필드) */
    @Column(nullable = false, length = 100)
    private String careerField;

    /** 해당 분야 진출 비율 (다른 브랜치에서 추가된 필드) */
    @Column(nullable = false)
    private double careerFieldRate;

    /** 취업률 자동 계산 메서드 (다른 브랜치에서 추가된 메서드) */
    public void calculateEmploymentRate() {
        if (graduates > 0) {
            this.employmentRate = (employed * 100.0) / graduates;
        } else {
            this.employmentRate = 0.0;
        }
    }
}
