package com.yiuDashboard.dto;

import com.yiuDashboard.entity.NewAdmissionStats;

public record NewAdmissionStatsDto(
        Long id,
        int year,
        int departmentId,
        String majorCategory,
        String departmentName,
        int studentQuota,
        int enrolledStudentCount,
        double fillRate,
        Double nationalAverageFillRate,
        Double similarMajorAverageFillRate
) {
    public static NewAdmissionStatsDto from(NewAdmissionStats s) {
        double fill = (s.getFillRate() != null) ? s.getFillRate() : s.calculateFillRate();
        return new NewAdmissionStatsDto(
                s.getId(),
                s.getYear(),
                s.getDepartmentId(),
                s.getMajorCategory(),
                s.getDepartmentName(),
                s.getStudentQuota(),
                s.getEnrolledStudentCount(),
                fill,
                s.getNationalAverageFillRate(),
                s.getSimilarMajorAverageFillRate()
        );
    }
}

