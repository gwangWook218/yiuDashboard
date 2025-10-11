package com.yiuDashboard.service;

import com.yiuDashboard.dto.grade.GradeRangeDto;
import com.yiuDashboard.dto.grade.GradeSummaryDto;
import com.yiuDashboard.repository.GradeDistributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeDistributionService {

    private final GradeDistributionRepository repository;

    // 학년, 학과별 총 학생수 + 평균 GPA
    public GradeSummaryDto getGradeSummary(int year, int semester, String dept) {
        return repository.findGradeSummary(year, semester, dept);
    }

    // 학년, 학과별 점수 구간별 학생 수
    public List<GradeRangeDto> getGradeRangeSummary(int year, int semester, String dept) {
        return repository.findGradeRangeSummary(year, semester, dept);
    }
}
