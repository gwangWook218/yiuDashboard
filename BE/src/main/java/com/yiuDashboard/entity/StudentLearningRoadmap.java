package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_learning_loadmap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentLearningRoadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학생 식별자 (학번 등)
    @Column(nullable = false)
    private String studentId;

    // 목표 학기
    @Column(nullable = false)
    private String targetSemester;

    // 목표 성적
    @Column(nullable = false)
    private Double targetGpa;

    // 추가 설명 (선택)
    @Column(length = 500)
    private String description;

    // 생성일
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 수정일
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private Double targetGrade;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}