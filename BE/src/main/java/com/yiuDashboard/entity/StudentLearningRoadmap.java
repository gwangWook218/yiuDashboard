package com.yiuDashboard.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import jakarta.persistence.Column;

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

    @Column(name = "earned_credits", nullable = false)
    private Integer earnedCredits;

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
        if (this.currentGpa == null) this.currentGpa = 0.0;
        if (this.remainingSemesters == null) this.remainingSemesters = 0;
        if (this.achievable == null) this.achievable = false;
        if (this.earnedCredits == null) this.earnedCredits = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}