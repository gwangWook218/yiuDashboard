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

    // 연구비 수혜 실적
    public List<ResearchPerformance> getResearchGrantByYear(int year) {
        return researchPerformanceRepository.findResearchGrantByYear(year);
    }
    public List<ResearchPerformance> getResearchGrantByDepartment(String departmentName, int year) {
        return researchPerformanceRepository.findResearchGrantByDepartmentNameAndYear(departmentName, year);
    }

    // 경쟁력 및 유치 능력 평가
    public List<ResearchPerformance> getCompetitivenessEvaluationByYear(int year) {
        return researchPerformanceRepository.findCompetitivenessByYear(year);
    }
    public List<ResearchPerformance> getCompetitivenessEvaluation(String departmentName, int year) {
        return researchPerformanceRepository.findCompetitivenessByDepartmentNameAndYear(departmentName, year);
    }

    // 논문 실적·특허 출원 수
    public List<ResearchPerformance> getResearchOutputByYear(int year) {
        return researchPerformanceRepository.findResearchOutputByYear(year);
    }
    public List<ResearchPerformance> getResearchOutputByDepartment(String departmentName, int year) {
        return researchPerformanceRepository.findResearchOutputByDepartmentNameAndYear(departmentName, year);
    }
}
