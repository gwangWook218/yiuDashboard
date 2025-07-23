package com.yiuDashboard.service;

import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.repository.NewAdmissionStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewAdmissionStatsService {

    private final NewAdmissionStatsRepository repository;

    public List<NewAdmissionStats> getAllStats() {
        return repository.findAll();
    }

    public List<NewAdmissionStats> getStatsByMajorCategory(String majorCategory) {
        return repository.findByMajorCategory(majorCategory);
    }

    public double getFillRateByDepartment(String departmentName) {
        NewAdmissionStats stats = repository.findByDepartmentName(departmentName);
        if (stats == null) {
            return 0.0;
        }
        return stats.calculateFillRate();
    }

    public NewAdmissionStats saveStats(NewAdmissionStats stats) {
        stats.setFillRate(stats.calculateFillRate());
        return repository.save(stats);
    }
}
