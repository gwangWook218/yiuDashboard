package com.yiuDashboard.entity.personalGrades;

import com.yiuDashboard.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SemesterRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학기 정보: "2023-1"
    private String semester;

    // 이수 학점
    private Integer credits;

    // 평점
    private Double gpa;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public SemesterRecord(String semester, int credits, double gpa, Long userId) {
        this.semester = semester;
        this.credits = credits;
        this.gpa = gpa;

        User user = new User();
        user.setId(userId);
        this.user = user;
    }
}
