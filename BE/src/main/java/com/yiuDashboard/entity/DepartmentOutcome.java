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

    // 참고용 메서드: 학과명과 재학생 수를 한번에 업데이트할 때 사용
    public void updateOutcome(String departmentName, int enrolledStudents) {
        this.departmentName = departmentName;
        this.enrolledStudents = enrolledStudents;
    }
}

