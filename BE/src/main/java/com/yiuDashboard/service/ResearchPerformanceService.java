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

    /**
     * 전국 평균 지표 데이터 조회
     */
    public List<ResearchPerformance> getNationalAverageByYear(int year) {
        return researchPerformanceRepository.findByYearAndNationalAverageNotNull(year);
    }

    /**
     * 계열별 평균 지표 데이터 조회
     */
    public List<ResearchPerformance> getCategoryAverageByYear(String categoryName, int year) {
        return researchPerformanceRepository.findByCategoryNameAndYearAndCategoryAverageNotNull(categoryName, year);
    }

    /**
     * 목표대학 비교 데이터 조회
     */
    public List<ResearchPerformance> getTargetUniversityValueByYear(String universityName, int year) {
        return researchPerformanceRepository.findByUniversityNameAndYearAndTargetUniversityValueNotNull(universityName, year);
    }
}