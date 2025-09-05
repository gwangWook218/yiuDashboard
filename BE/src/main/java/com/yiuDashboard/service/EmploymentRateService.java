package com.yiuDashboard.service;

import com.yiuDashboard.dto.gradEmployment.*;
import com.yiuDashboard.repository.EmploymentRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmploymentRateService {

    private final EmploymentRateRepository repository;

    public EmploymentRateResponseDTO getEmploymentRateByYear(int year) {
        List<Object[]> results = repository.findEmployRateByYear(year);
        List<DepartmentEmploymentDTO> departments = results.stream().map(row -> new DepartmentEmploymentDTO(
                (String) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                row[4] != null ? ((Number) row[4]).doubleValue() : null
        )).toList();

//        전체 평균 계산
        Double overallAverage = departments.stream()
                .filter(d -> d.getEmploymentRate() != null)
                .mapToDouble(DepartmentEmploymentDTO::getEmploymentRate)
                .average().orElse(0.0);

        return new EmploymentRateResponseDTO(year, overallAverage, departments);
    }

    public EmploymentTrendDTO getTrendByYear(int departmentId) {
        List<Object[]> results = repository.findTrendByYear(departmentId);

        if (results.isEmpty()) {
            return null;
        }

        Object[] firstRow = results.get(0);
        String college = (String) firstRow[1];
        String department = (String) firstRow[2];
        String isDayTime = (String) firstRow[3];
        String isContract = (String) firstRow[4];

        List<YearEmploymentRateDTO> trend = results.stream().map(row -> new YearEmploymentRateDTO(
                ((Number) row[0]).intValue(),
                row[5] != null ? ((Number) row[5]).doubleValue() : null
        )).toList();

        return new EmploymentTrendDTO(college, department, isDayTime, isContract, trend);
    }

    public EmploymentRankingDTO getRankByYear(int year, int topN) {
        List<EmploymentRankingDTO.DepartmentRateDTO> allRates = repository.findRankingByYear(year);

        List<EmploymentRankingDTO.DepartmentRateDTO> topDepartments =
                allRates.stream().limit(topN).toList();

        List<EmploymentRankingDTO.DepartmentRateDTO> bottomDepartments = allRates.stream()
                .skip(Math.max(allRates.size() - topN, 0))
                .sorted(Comparator.comparingDouble(EmploymentRankingDTO.DepartmentRateDTO::getEmploymentRate))
                .toList();

        return new EmploymentRankingDTO(year, topDepartments, bottomDepartments);
    }
}
