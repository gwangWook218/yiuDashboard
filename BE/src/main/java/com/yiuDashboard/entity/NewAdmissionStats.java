package com.yiuDashboard.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NewAdmissionStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departmentName;     // 학과명
    private String category;           // 계열 (인문/자연)
    private double competitionRate;    // 입학 경쟁률
    private double enrollmentRate;     // 등록률
    private double fillRate;           // 충원율
    private double nationalAvg;        // 전국 평균
    private double similarDeptAvg;     // 유사계열 평균

    private int admissionQuota;        // 모집 정원
    private int currentStudents;       // 재학생 수
    private String graduateTrend;  // 졸업 후 진출 진로 요약
    private String majorFeatures;     // 전공 특성 요약
    private String coreSubjects;      // 주요 교과목
    private String relatedCareers;    // 관련 진로


}