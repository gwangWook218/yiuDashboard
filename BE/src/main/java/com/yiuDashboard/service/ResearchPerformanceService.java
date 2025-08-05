package com.yiuDashboard.service;

import com.yiuDashboard.entity.ResearchPerformance;
import com.yiuDashboard.repository.ResearchPerformanceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 논문 실적, 특허 출원 수 비교
     */
    public Map<String, Object> compareResearchOutputs(String categoryName, String targetUniversity) {
        Object[] averages = researchPerformanceRepository.findCategoryAverage(categoryName);
        Double avgPaper = (Double) averages[0];
        Double avgPatent = (Double) averages[1];

        List<ResearchPerformance> targetUniversityData = researchPerformanceRepository.findByUniversityName(targetUniversity);

        Map<String, Object> result = new HashMap<>();
        result.put("averagePaper", avgPaper);
        result.put("averagePatent", avgPatent);
        result.put("targetUniversityData", targetUniversityData);

        return result;
    }
}