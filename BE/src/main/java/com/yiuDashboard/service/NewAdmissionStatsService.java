package com.yiuDashboard.service;

import com.yiuDashboard.dto.NewAdmissionStatsDto;
import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.repository.NewAdmissionStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewAdmissionStatsService {

    private final NewAdmissionStatsRepository repository;

    public List<NewAdmissionStats> getAllByYear(int year) {
        return repository.findAllByYear(year);
    }

    public NewAdmissionStats getOne(int year, int deptId) {
        return repository.findByYearAndDepartmentId(year, deptId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "데이터 없음: year=" + year + ", deptId=" + deptId));
    }

    public NewAdmissionStatsDto getOneDto(int year, int deptId) {
        NewAdmissionStats s = repository.findByYearAndDepartmentId(year, deptId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "데이터 없음: year=%d, deptId=%d".formatted(year, deptId)));
        return NewAdmissionStatsDto.from(s);
    }

    public double getFillRate(int year, int deptId) {
        NewAdmissionStats s = repository.findByYearAndDepartmentId(year, deptId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "데이터 없음: year=%d, deptId=%d".formatted(year, deptId)));
        Double saved = s.getFillRate();
        return (saved != null) ? saved : s.calculateFillRate();
    }

    public double getFillRateByDepartment(String departmentName) {
        NewAdmissionStats stats = repository.findByDepartmentName(departmentName).orElse(null);
        if (stats == null) return 0.0;
        Double saved = stats.getFillRate();
        return (saved != null) ? saved : stats.calculateFillRate();
    }

    public NewAdmissionStats saveStats(NewAdmissionStats stats) {
        if (stats.getFillRate() == null) {
            double v = stats.calculateFillRate();
            stats.setFillRate(round2(v));
        }
        return repository.save(stats);
    }

    public List<NewAdmissionStats> getAllStats() {
        return repository.findAll();
    }

    public List<NewAdmissionStats> getStatsByMajorCategory(String majorCategory) {
        return repository.findByMajorCategory(majorCategory);
    }

    private static Double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}