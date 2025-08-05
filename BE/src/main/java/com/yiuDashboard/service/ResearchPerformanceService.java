package com.yiuDashboard.service;

import com.yiuDashboard.entity.ResearchPerformance;
import com.yiuDashboard.repository.ResearchPerformanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResearchPerformanceService {

    private final ResearchPerformanceRepository researchPerformanceRepository;

    public ResearchPerformanceService(ResearchPerformanceRepository researchPerformanceRepository) {
        this.researchPerformanceRepository = researchPerformanceRepository;
    }

    /**
     * 모든 연구비 수혜 실적 조회
     */
    public List<ResearchPerformance> getAllResearchPerformances() {
        return researchPerformanceRepository.findAll();
    }

    /**
     * 학과별 연구비 수혜 실적 조회
     */
    public List<ResearchPerformance> getByDepartmentName(String departmentName) {
        return researchPerformanceRepository.findByDepartmentName(departmentName);
    }

    /**
     * 교수별 연구비 수혜 실적 조회
     */
    public List<ResearchPerformance> getByProfessorName(String professorName) {
        return researchPerformanceRepository.findByProfessorName(professorName);
    }

    /**
     * 연도별 연구비 수혜 실적 조회
     */
    public List<ResearchPerformance> getByYear(int year) {
        return researchPerformanceRepository.findByYear(year);
    }

    /**
     * 학과 + 연도별 연구비 수혜 실적 조회
     */
    public List<ResearchPerformance> getByDepartmentNameAndYear(String departmentName, int year) {
        return researchPerformanceRepository.findByDepartmentNameAndYear(departmentName, year);
    }
}

