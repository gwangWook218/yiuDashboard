package com.yiuDashboard.service;

import com.yiuDashboard.dto.EnrollmentSummaryDto;
import com.yiuDashboard.repository.StudentEnrollmentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final StudentEnrollmentStatusRepository repository;

    public EnrollmentSummaryDto findByYear(int year, int deptId) {
        return repository.findByYear(year, deptId);
    }
}
