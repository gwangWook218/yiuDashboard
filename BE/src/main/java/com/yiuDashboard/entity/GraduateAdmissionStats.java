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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int year;
    private String college;
    private String department;
    private String scheduleType;
    private String programType;

    private int graduatesMale;
    private int graduatesFemale;

    private int domesticKorJrCollegeMale;
    private int domesticKorJrCollegeFemale;
    private int domesticCollegeMale;
    private int domesticCollegeFemale;
    private int domesticGradMale;
    private int domesticGradFemale;

    private int overseasKorJrCollegeMale;
    private int overseasKorJrCollegeFemale;
    private int overseasCollegeMale;
    private int overseasCollegeFemale;
    private int overseasGradMale;
    private int overseasGradFemale;
}
