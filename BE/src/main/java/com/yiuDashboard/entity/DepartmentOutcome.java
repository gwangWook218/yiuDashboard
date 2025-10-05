package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "department_outcome")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학과명
    @Column(nullable = false, length = 100)
    private String departmentName;

    // 재학생 수
    @Column(nullable = false)
    private int enrolledStudents;

    // 전공 과목 평균 성적
    @Column(nullable = false)
    private double majorAverageGrade;

    // 교양 과목 평균 성적
    @Column(nullable = false)
    private double generalAverageGrade;

    // 전공 vs 교양 성적 비교
    @Column(nullable = false)
    private double majorVsGeneralDifference;

    // 졸업생 수
    @Column(nullable = false)
    private int graduates;

    // 취업자 수
    @Column(nullable = false)
    private int employed;

    // 진학자 수
    @Column(nullable = false)
    private int furtherStudy;

    // 취업률
    @Column(nullable = false)
    private double employmentRate;

    // 진학률
    @Column(nullable = false)
    private double furtherStudyRate;

    // 참고용 메서드: 학과명과 재학생 수를 한번에 업데이트할 때 사용
    public void updateOutcome(String departmentName, int enrolledStudents) {
        this.departmentName = departmentName;
        this.enrolledStudents = enrolledStudents;
    }

    // 전공, 교양 성적 업데이트
    public void updateGrades(double majorAverageGrade, double generalAverageGrade) {
        this.majorAverageGrade = majorAverageGrade;
        this.generalAverageGrade = generalAverageGrade;
        this.majorVsGeneralDifference = majorAverageGrade - generalAverageGrade;
    }

    //졸업생 진로 통계 업데이트 메서드
    public void updateCareerStats(int graduates, int employed, int furtherStudy) {
        this.graduates = graduates;
        this.employed = employed;
        this.furtherStudy = furtherStudy;

        if (graduates > 0) {
            this.employmentRate = ((double) employed / graduates) * 100.0;
            this.furtherStudyRate = ((double) furtherStudy / graduates) * 100.0;
        } else {
            this.employmentRate = 0.0;
            this.furtherStudyRate = 0.0;
        }
    }
}

