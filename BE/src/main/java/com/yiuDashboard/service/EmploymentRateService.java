package com.yiuDashboard.service;

import com.yiuDashboard.dto.gradEmployment.EmployAdmissionDto;
import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.repository.EmploymentRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmploymentRateService {

    private final EmploymentRateRepository repository;

    public GraduateStatsDTO getGraduateStats(int year, int deptId) {
        return repository.findGraduateStats(year, deptId);
    }

    public EmployAdmissionDto getEmployAdmission(int year, int deptId) {
        return repository.findByYearAndDept(year, deptId);
    }
}
