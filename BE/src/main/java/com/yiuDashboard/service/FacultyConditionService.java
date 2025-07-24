package com.yiuDashboard.service;

import com.yiuDashboard.entity.FacultyCondition;
import com.yiuDashboard.repository.FacultyConditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyConditionService {

    private final FacultyConditionRepository facultyConditionRepository;

    //특정 학과의 전임교원 확보율 조회
    public List<FacultyCondition> getFacultyEnsureRateByDepartment(String departmentName) {
        return facultyConditionRepository.findByDepartmentName(departmentName);
    }

    //모든 학과의 전임교원 확보율 조회 (전국 평균 혹은 권고치 비교용)
    public List<FacultyCondition> getAllFacultyEnsureRatesDesc() {
        return facultyConditionRepository.findAllByOrderByFacultySecureRateDesc();
    }

    // 특정 학과의 전임교원 1인당 학생 수 조회
    public List<FacultyCondition> getStudentPerFacultyByDepartment(String departmentName) {
        return facultyConditionRepository.findByDepartmentNameOrderByStudentPerFacultyDesc(departmentName);
    }

    // 전체 학과 전임교원 1인당 학생 수 기준 정렬 조회 (비교용)
    public List<FacultyCondition> getAllDepartmentsByStudentPerFacultyDesc() {
        return facultyConditionRepository.findAllByOrderByStudentPerFacultyDesc();
    }
}
