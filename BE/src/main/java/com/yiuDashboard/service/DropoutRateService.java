package com.yiuDashboard.service;

import com.yiuDashboard.dto.DropoutRateDTO;
import com.yiuDashboard.repository.DropoutRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DropoutRateService {

    private final DropoutRateRepository repository;

    public List<DropoutRateDTO> findByDeptName(String year) {
        List<Object[]> results =  repository.findByDeptName(year);

        return results.stream()
                .map(row -> new DropoutRateDTO(
                        (String) row[0],
                        (String) row[1],
                        row[2] != null ? ((Number) row[2]).doubleValue() : null,
                        row[3] != null ? ((Number) row[3]).doubleValue() : null
                ))
                .collect(Collectors.toList());
    }
}
