package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faculty_condition")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultyCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학과명
    @Column(nullable = false, length = 100)
    private String departmentName;

    // 전임교원 수
    @Column(nullable = false)
    private int fullTimeFacultyCount;

    // 권장 전임교원 수 (교육부 권고치)
    @Column(nullable = false)
    private int recommendedFacultyCount;

    // 전임교원 확보율
    @Column(nullable = false)
    private double facultySecureRate;

    // 학생 수
    @Column(nullable = false)
    private int studentCount;

    // 전임교원 1인당 학생 수
    @Column(nullable = false)
    private double studentPerFaculty;

    // 데이터 업데이트 메서드 (관리자용)
    public void updateFacultyStudentData(String departmentName, int fullTimeFacultyCount, int studentCount) {
        this.departmentName = departmentName;
        this.fullTimeFacultyCount = fullTimeFacultyCount;
        this.studentCount = studentCount;
        this.studentPerFaculty = (fullTimeFacultyCount == 0) ? 0 : (double) studentCount / fullTimeFacultyCount;
    }

    // 데이터 업데이트 메서드 (관리자 수정용)
    public void updateFacultyData(String departmentName, int fullTimeFacultyCount, int recommendedFacultyCount) {
        this.departmentName = departmentName;
        this.fullTimeFacultyCount = fullTimeFacultyCount;
        this.recommendedFacultyCount = recommendedFacultyCount;
        this.facultySecureRate = (recommendedFacultyCount == 0) ? 0 :
                ((double) fullTimeFacultyCount / recommendedFacultyCount) * 100;
    }
}
