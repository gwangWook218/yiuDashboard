package com.yiuDashboard.service;

import com.yiuDashboard.dto.EmploymentRateDTO;
import com.yiuDashboard.repository.EmploymentRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentRateService {

    private final EmploymentRateRepository repository;

    public List<EmploymentRateDTO> findEmployRateByYear(int year) {
        return repository.findEmployRateByYear(year);
    }
}
