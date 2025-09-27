package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_enrollment_status")
@IdClass(StudentProgramAnalytics.Pk.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentProgramAnalytics {

    @Id
    @Column(name = "year")
    private Integer year;

    @Id
    @Column(name = "department_id")
    private Integer departmentId;

    // 부가 정보(JPA 조인)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @Column(name = "department_status", length = 10)
    private String departmentStatus;

    @Column(length = 50)
    private String division;

    // 재학생/이동/휴복학/자퇴 지표
    @Column(name = "enrolled_in_male")     private Integer enrolledInMale;
    @Column(name = "enrolled_in_female")   private Integer enrolledInFemale;
    @Column(name = "enrolled_out_male")    private Integer enrolledOutMale;
    @Column(name = "enrolled_out_female")  private Integer enrolledOutFemale;
    @Column(name = "leave_in_male")        private Integer leaveInMale;
    @Column(name = "leave_in_female")      private Integer leaveInFemale;
    @Column(name = "leave_out_male")       private Integer leaveOutMale;
    @Column(name = "leave_out_female")     private Integer leaveOutFemale;
    @Column(name = "dropout_in_male")      private Integer dropoutInMale;
    @Column(name = "dropout_in_female")    private Integer dropoutInFemale;
    @Column(name = "dropout_out_male")     private Integer dropoutOutMale;
    @Column(name = "dropout_out_female")   private Integer dropoutOutFemale;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class Pk implements java.io.Serializable {
        private Integer year;
        private Integer departmentId;
    }
}
