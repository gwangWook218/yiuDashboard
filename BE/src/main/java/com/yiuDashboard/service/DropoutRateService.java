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

    public List<DropoutRateDTO> findByDeptName(int year) {
        return repository.findByDeptName(year);
    }
}
