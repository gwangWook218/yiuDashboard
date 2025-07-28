package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dep_enroll")
public class DepEnroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int 기준연도;
    private String 단과대학;
    private String 학과;
    private String 구분;
    private String 학과특성;
    private String 학과상태;
    private String 계열;
    private int 재학생_정원내_남;
    private int 재학생_정원내_여;
    private int 재학생_정원외_남;
    private int 재학생_정원외_여;
    private int 휴학생_정원내_남;
    private int 휴학생_정원내_여;
    private int 휴학생_정원외_남;
    private int 휴학생_정원외_여;
    private int 학사유예_정원내_남;
    private int 학사유예_정원내_여;
    private int 학사유예_정원외_남;
    private int 학사유예_정원외_여;
}
