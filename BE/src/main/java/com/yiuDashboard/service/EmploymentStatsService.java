package com.yiuDashboard.service;

import com.yiuDashboard.entity.EmploymentStats;
import com.yiuDashboard.repository.EmploymentStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentStatsService {

    private final EmploymentStatsRepository employmentStatsRepository;
    private Integer resolveYear() {
        Integer y = employmentStatsRepository.findLatestYear();
        return (y != null) ? y : LocalDate.now().getYear();
    }

    /** 학과별 취업 통계 (최신년도) */
    public List<EmploymentStats> getStatsByDepartment(String departmentName) {
        Integer year = resolveYear();
        return employmentStatsRepository.findByDepartmentNameAndYear(departmentName, year);
    }

    /** 모든 학과 취업 통계 – 취업률 (최신년도) */
    public List<EmploymentStats> getAllStatsSortedByEmploymentRate() {
        Integer year = resolveYear();
        return employmentStatsRepository.findByYearOrderByEmploymentRateDesc(year);
    }

    /** 기준 취업률 이상 학과 조회 (최신년도) */
    public List<EmploymentStats> getStatsAboveRate(double rate) {
        Integer year = resolveYear();
        return employmentStatsRepository.findByYearAndEmploymentRateGreaterThanEqual(year, rate);
    }

    public EmploymentStats saveEmploymentStats(EmploymentStats stats) {
        stats.calculateEmploymentRate();
        return employmentStatsRepository.save(stats);
    }

    //특정 학과의 졸업생 진출 분야별 비율 조회
    public List<EmploymentStats> getCareerDistributionByDepartment(String departmentName) {
        return employmentStatsRepository.findByDepartmentNameOrderByCareerFieldRateDesc(departmentName);
    }
}
