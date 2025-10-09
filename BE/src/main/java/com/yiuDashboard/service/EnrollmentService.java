package com.yiuDashboard.service;

import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.repository.StudentEnrollmentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final StudentEnrollmentStatusRepository repository;

    public List<EnrollmentSummaryDto> findByYear(int year, int deptId) {
        return repository.findByYear(year, deptId);
    }
}
