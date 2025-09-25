package com.yiuDashboard.service;

import com.yiuDashboard.repository.StaffStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffStatisticsService {

    private final StaffStatisticsRepository repository;

    public Long getTotalStaffCount() {
        return repository.getTotalStaffCount();
    }
}
