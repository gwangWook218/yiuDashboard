package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employment_stats")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmploymentStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학과명
    @Column(nullable = false, length = 100)
    private String departmentName;

    // 졸업생 수
    @Column(nullable = false)
    private int graduates;

    // 취업자 수
    @Column(nullable = false)
    private int employed;

    // 취업률 (계산해서 저장하거나 DB에서 바로 관리할 수 있음)
    @Column(nullable = false)
    private double employmentRate;

    /**
     * 취업률 자동 계산용 편의 메서드
     */
    public void calculateEmploymentRate() {
        if (graduates > 0) {
            this.employmentRate = ((double) employed / graduates) * 100.0;
        } else {
            this.employmentRate = 0.0;
        }
    }
}
