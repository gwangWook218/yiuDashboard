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
public class CreditProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카테고리 (예: 전체, 전공, 교양필수, 교양선택 등)
    private String category;

    // 이수 기준 학점 (없을 경우 0)
    private Integer required;

    // 취득 학점
    private Integer earned;

    // 이수율
    private Double rate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public CreditProgress(String category, int required, int earned, Long userId) {
        this.category = category;
        this.required = required;
        this.earned = earned;
        if (required > 0) {
            double rawRate = ((double) earned / required) * 100;
            if (rawRate > 100.0) {
                rawRate = 100.0; // 초과 시 100%로 제한
            }
            this.rate = Math.round(rawRate * 10) / 10.0; // 소수점 1자리 반올림
        } else {
            this.rate = 100.0; // 기준이 없는 경우(일반선택 등)는 자동 100%
        }

        User u = new User();
        u.setId(userId);
        this.user = u;
    }
}
