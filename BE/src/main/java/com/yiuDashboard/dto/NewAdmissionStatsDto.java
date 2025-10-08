package com.yiuDashboard.dto;

import lombok.Builder;

@Builder
public record NewAdmissionStatsDto(
        Long id,
        String majorCategory,
        String departmentName,
        Integer studentQuota,
        Integer enrolledStudentCount,
        double fillRate,                    // 계산값
        Double nationalAverageFillRate,
        Double similarMajorAverageFillRate
) {
    // 필요할 때 사용할 수 있는 헬퍼 (엔티티와 무관)
    public static double calcFill(Integer enrolled, Integer quota) {
        if (quota == null || quota == 0 || enrolled == null) return 0d;
        return Math.round((enrolled * 100.0 / quota) * 10.0) / 10.0;
    }
}
