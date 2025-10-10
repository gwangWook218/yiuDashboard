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
        List<Object[]> result = repository.findDropoutStatsByYear(year);
        return result.stream()
                .map(r -> new DropoutRateDTO(
                        (String) r[0],
                        ((Number) r[1]).intValue(),
                        ((Number) r[2]).doubleValue(),
                        ((Number) r[3]).intValue(),
                        ((Number) r[4]).doubleValue(),
                        ((Number) r[5]).intValue(),
                        ((Number) r[6]).doubleValue()
                ))
                .toList();
    }

    public DropoutRateDTO getDropoutDetail(int year, int deptId) {
        return repository.getDropoutDetail(year, deptId);
    }
}
