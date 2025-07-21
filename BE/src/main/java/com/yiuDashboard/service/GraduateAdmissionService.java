package com.yiuDashboard.service;

import com.yiuDashboard.dto.AdmissionRateDTO;
import com.yiuDashboard.repository.GraduateAdmissionStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraduateAdmissionService {

    private final GraduateAdmissionStatsRepository repository;

    public List<AdmissionRateDTO> getAdmissionRate(int year) {
        return repository.findAdmissionRateByYear(year);
    }
}
