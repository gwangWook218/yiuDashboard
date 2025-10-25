package com.yiuDashboard.service;

import com.yiuDashboard.dto.gradEmployment.GraduateStatsDTO;
import com.yiuDashboard.repository.EmploymentRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentRateService {

    private final EmploymentRateRepository repository;

    public List<GraduateStatsDTO> getGraduateStats(int year) {
        return repository.findGraduateStats(year);
    }
}
