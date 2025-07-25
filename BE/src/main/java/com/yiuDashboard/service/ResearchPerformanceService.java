package com.yiuDashboard.service;

import com.yiuDashboard.entity.ResearchPerformance;
import com.yiuDashboard.repository.ResearchPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResearchPerformanceService {

    private final ResearchPerformanceRepository researchPerformanceRepository;

    //특정 연도의 전체 연구비 수혜 실적 조회
    public List<ResearchPerformance> getResearchPerformanceByYear(int year) {
        return researchPerformanceRepository.findByYear(year);
    }

    //특정 학과의 연구비 수혜 실적 조회
    public List<ResearchPerformance> getResearchPerformanceByDepartment(String departmentName) {
        return researchPerformanceRepository.findByDepartmentName(departmentName);
    }

    //모든 연구비 수혜 실적 조회
    public List<ResearchPerformance> getAllResearchPerformances() {
        return researchPerformanceRepository.findAll();
    }

    // 특정 연도, 전체 학과 경쟁력 평가 데이터 조회
    public List<ResearchPerformance> getCompetitivenessEvaluationByYear(int year) {
        return researchPerformanceRepository.findAllByYear(year);
    }

    // 특정 연도, 특정 학과 경쟁력 평가 데이터 조회
    public List<ResearchPerformance> getCompetitivenessEvaluation(String departmentName, int year) {
        return researchPerformanceRepository.findByDepartmentNameAndYear(departmentName, year);
    }
}
