package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "research_performance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResearchPerformance {

    /** 기본키 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 학과명 */
    @Column(nullable = false, length = 100)
    private String departmentName;

    /** 교수명 (교수별 데이터 관리 필요시) */
    @Column(nullable = false, length = 100)
    private String professorName;

    /** 연도 */
    @Column(nullable = false)
    private int year;

    /** 연구비 금액 */
    @Column(nullable = false)
    private Long researchGrantAmount;

    /** 연구비 출처 */
    @Column(length = 100)
    private String fundingSource;

    /** 등록 일시 */
    @Column(nullable = false)
    private String createdAt;

    /** 수정 일시 */
    @Column(nullable = false)
    private String updatedAt;

    /** 전국 평균 값 */
    @Column
    private Double nationalAverage;

    /** 계열별 평균 값 */
    @Column
    private Double categoryAverage;

    /** 목표 대학 값(선택) */
    @Column
    private Double targetUniversityValue;
}

