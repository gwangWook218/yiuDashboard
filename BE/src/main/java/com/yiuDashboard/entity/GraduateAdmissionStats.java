package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "graduate_admission_stats")
public class GraduateAdmissionStats {

    @EmbeddedId
    private GraduateAdmissionStatsId id;

    @Column(name = "graduates_male")
    private int graduatesMale;
    @Column(name = "graduates_female")
    private int graduatesFemale;

    @Column(name = "domestic_kor_jr_male")
    private int domesticKorJrCollegeMale;
    @Column(name = "domestic_kor_jr_female")
    private int domesticKorJrCollegeFemale;
    @Column(name = "domestic_college_male")
    private int domesticCollegeMale;
    @Column(name = "domestic_college_female")
    private int domesticCollegeFemale;
    @Column(name = "domestic_grad_male")
    private int domesticGradMale;
    @Column(name = "domestic_grad_female")
    private int domesticGradFemale;

    @Column(name = "overseas_kor_jr_male")
    private int overseasKorJrCollegeMale;
    @Column(name = "overseas_kor_jr_female")
    private int overseasKorJrCollegeFemale;
    @Column(name = "overseas_college_male")
    private int overseasCollegeMale;
    @Column(name = "overseas_college_female")
    private int overseasCollegeFemale;
    @Column(name = "overseas_grad_male")
    private int overseasGradMale;
    @Column(name = "overseas_grad_female")
    private int overseasGradFemale;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_id")
    private Department department;
}
