package com.yiuDashboard.entity.personalGrades;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiuDashboard.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudentAdditionalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer transferCredits; // 편입 인정 학점

    @Enumerated(EnumType.STRING)
    private GraduationThesisStatus thesisStatus; // 졸업 논문 제출 여부

    private Integer toeicScore; // TOEIC 점수

    @Enumerated(EnumType.STRING)
    private CertificateStatus certificateStatus; // 자격증 취득 여부

    public enum GraduationThesisStatus {
        제출필요,
        제출함
    }

    public enum CertificateStatus {
        미취득,
        취득
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
