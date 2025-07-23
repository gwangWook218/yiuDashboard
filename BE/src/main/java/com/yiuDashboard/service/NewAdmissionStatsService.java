package com.yiuDashboard.service;

import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.repository.NewAdmissionStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> getDepartmentAdmissionComparison(String departmentName) {
        Map<String, Object> result = new HashMap<>();

        // 1. 학과 데이터 조회
        NewAdmissionStats stats = repository.findByDepartmentName(departmentName);
        if (stats == null) {
            result.put("studentQuota", 0);
            result.put("enrolledStudentCount", 0);
            result.put("fillRate", 0.0);
            result.put("similarMajorAverageFillRate", 0.0);
            return result;
        }

        // 2. 충원율 계산
        double fillRate = stats.calculateFillRate();

        // 3. 유사 계열 평균 충원율 조회
        Double similarMajorAverageFillRate = repository.findAverageFillRateByMajorCategory(stats.getMajorCategory());
        if (similarMajorAverageFillRate == null) {
            similarMajorAverageFillRate = 0.0;
        }

        // 4. 결과 데이터
        result.put("studentQuota", stats.getStudentQuota());
        result.put("enrolledStudentCount", stats.getEnrolledStudentCount());
        result.put("fillRate", fillRate);
        result.put("similarMajorAverageFillRate", similarMajorAverageFillRate);

        return result;
    }
}
