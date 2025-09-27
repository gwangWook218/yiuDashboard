package com.yiuDashboard.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FillRateResponse {
    private int year;
    private int departmentId;
    private String college;          // 동일 단과(유사 학과) 비교 기준
    private String departmentName;

    private int currentStudents;     // 재학생 수(남+녀)
    private Integer recruitmentCount; // 학과 정원
    private Double fillRate;         // 충원율

    private Double peerAvgCurrent;   // 동일 단과 평균 재학생 수
}

