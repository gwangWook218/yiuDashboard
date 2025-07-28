package com.yiuDashboard.service;

import com.yiuDashboard.dto.DepartmentEnrollmentDto;
import com.yiuDashboard.repository.DepEnrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final DepEnrollRepository depEnrollRepository;

    public List<DepartmentEnrollmentDto> getDepartmentEnrollmentStats() {
        List<Object[]> rows = depEnrollRepository.fetchDepartmentEnrollmentStats();

        List<DepartmentEnrollmentDto> results = new ArrayList<>();

        for (Object[] row : rows) {
            DepartmentEnrollmentDto dto = new DepartmentEnrollmentDto(
                    ((Number) row[0]).intValue(),
                    (String) row[1],
                    (String) row[2],
                    ((Number) row[3]).intValue(),
                    ((Number) row[4]).doubleValue()
            );
            results.add(dto);
        }

        return results;
    }
}
