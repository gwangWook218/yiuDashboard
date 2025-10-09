package com.yiuDashboard.service;

import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.repository.EmploymentRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmploymentRateService {

    private final EmploymentRateRepository repository;

    public GraduateStatsDTO getGraduateStats(int year, int deptId) {
        GraduateStatsDTO dto = repository.findGraduateStats(year, deptId)
                .orElse(null);

        if (dto == null) return null;

        int total = dto.getTotal();
        int employed = dto.getEmployed();
        int admission = dto.getAdmission();
        int etc = dto.getEtc();
        int excluded = total - (employed + admission + etc); // 계산 필요

        dto.setEmployedRate(Math.round((double) employed / (total - excluded) * 1000) / 10.0);
        dto.setAdmissionRate(Math.round((double) admission / (total - excluded) * 1000) / 10.0);
        dto.setEtcRate(Math.round((double) etc / (total - excluded) * 1000) / 10.0);

        return dto;
    }
}
