package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "research_performance")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResearchPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학과명
    @Column(nullable = false)
    private String departmentName;

    // 해당 학과 연구비 수혜 총액
    @Column(nullable = false)
    private Long totalResearchGrant;

    // 교수 1인당 평균 연구비 수혜액
    @Column(nullable = false)
    private Long avgResearchGrantPerProfessor;

    // 전국 평균 값
    @Column(nullable = true)
    private Double nationalAverage;

    // 계열별 평균 값
    @Column(nullable = true)
    private Double categoryAverage;

    // 목표 대학 값 (선택적으로 비교)
    @Column(nullable = true)
    private Double targetUniversityValue;

    // 통계 기준 연도
    @Column(nullable = false)
    private int year;
}
