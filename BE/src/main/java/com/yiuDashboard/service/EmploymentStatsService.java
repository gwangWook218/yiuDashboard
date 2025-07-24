package com.yiuDashboard.service;

import com.yiuDashboard.entity.EmploymentStats;
import com.yiuDashboard.repository.EmploymentStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentStatsService {

    private final EmploymentStatsRepository employmentStatsRepository;

    //특정 학과의 취업 통계 조회
    public List<EmploymentStats> getStatsByDepartment(String departmentName) {
        return employmentStatsRepository.findByDepartmentName(departmentName);
    }

    //취업률 내림차순 정렬로 모든 학과 통계 조회
    public List<EmploymentStats> getAllStatsSortedByEmploymentRate() {
        return employmentStatsRepository.findAllByOrderByEmploymentRateDesc();
    }

    //기준 취업률 이상인 학과 조회
    public List<EmploymentStats> getStatsAboveRate(double rate) {
        return employmentStatsRepository.findByEmploymentRateGreaterThanEqual(rate);
    }

    //학과별 취업 통계 신규 저장 (관리자용)
    public EmploymentStats saveEmploymentStats(EmploymentStats stats) {
        stats.calculateEmploymentRate(); // 취업률 자동 계산
        return employmentStatsRepository.save(stats);
    }
}
