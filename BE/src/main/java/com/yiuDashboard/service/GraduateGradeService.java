package com.yiuDashboard.service;

import com.yiuDashboard.dto.grade.GraduateGradeDto;
import com.yiuDashboard.repository.GraduateGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GraduateGradeService {

    private final GraduateGradeRepository repository;

    public GraduateGradeDto findGraduateGradeByYearAndDept(int year, String dept) {
        return repository.findGraduateGradeByYearAndDept(year, dept);
    }
}
