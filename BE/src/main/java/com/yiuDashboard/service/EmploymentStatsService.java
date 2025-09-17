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

    /** DB에 연도가 없으면 현재 연도로 대체 */
    private Integer resolveYear() {
        Integer y = employmentStatsRepository.findLatestYear();
        return (y != null) ? y : LocalDate.now().getYear();
    }

    /** 학과별 취업 통계(최신연도) */
    public List<EmploymentStats> getStatsByDepartment(String departmentName) {
        Integer year = resolveYear();
        return employmentStatsRepository.findByDepartmentNameAndYear(departmentName, year);
    }

    /** 모든 학과 – 취업률 내림차순(최신연도) */
    public List<EmploymentStats> getAllStatsSortedByEmploymentRate() {
        Integer year = resolveYear();
        return employmentStatsRepository.findByYearOrderByEmploymentRateDesc(year);
    }

    /** 기준 취업률 이상 학과(최신연도) */
    public List<EmploymentStats> getStatsAboveRate(double rate) {
        Integer year = resolveYear();
        return employmentStatsRepository.findByYearAndEmploymentRateGreaterThanEqual(year, rate);
    }

    /** 저장 시 취업률 자동 계산 */
    public EmploymentStats saveEmploymentStats(EmploymentStats stats) {
        stats.calculateEmploymentRate();
        return employmentStatsRepository.save(stats);
    }

    /** 특정 학과의 진출 분야 분포(최신연도와 무관하게 단순 정렬 목록) */
    public List<EmploymentStats> getCareerDistributionByDepartment(String departmentName) {
        return employmentStatsRepository.findByDepartmentNameOrderByCareerFieldRateDesc(departmentName);
    }
}
