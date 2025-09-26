package com.yiuDashboard.service;

import com.yiuDashboard.dto.DropoutRateDTO;
import com.yiuDashboard.repository.DropoutRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DropoutRateService {

    private final DropoutRateRepository repository;

    public List<DropoutRateDTO> getDropoutStats(int year) {
        return repository.findDropoutStatsByYear(year);
    }

    public DropoutRateDTO getDropoutDetail(int year, String dept) {
        return repository.getDropoutDetail(year, dept);
    }
}
