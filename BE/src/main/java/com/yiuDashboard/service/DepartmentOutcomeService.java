package com.yiuDashboard.service;

import com.yiuDashboard.entity.DepartmentOutcome;
import com.yiuDashboard.repository.DepartmentOutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentOutcomeService {

    private final DepartmentOutcomeRepository departmentOutcomeRepository;

    //특정 학과의 재학생 수 정보 조회
    public List<DepartmentOutcome> getStudentCountByDepartment(String departmentName) {
        return departmentOutcomeRepository.findByDepartmentName(departmentName);
    }

    //모든 학과 재학생 수 정보 조회
    public List<DepartmentOutcome> getAllDepartmentsByStudentCountDesc() {
        return departmentOutcomeRepository.findAllByOrderByEnrolledStudentsDesc();
    }

    //새로운 학과 재학생 수 데이터 저장 (관리자용)
    public DepartmentOutcome saveDepartmentOutcome(DepartmentOutcome outcome) {
        return departmentOutcomeRepository.save(outcome);
    }

    //특정 학과의 전공/교양 성적 평균 및 차이 조회
    public List<DepartmentOutcome> getGradeComparisonByDepartment(String departmentName) {
        return departmentOutcomeRepository.findByDepartmentNameOrderByMajorVsGeneralDifferenceDesc(departmentName);
    }

    //전체 학과 전공/교양 성적 차이 조회
    public List<DepartmentOutcome> getAllDepartmentsByGradeDifferenceDesc() {
        return departmentOutcomeRepository.findAllByOrderByMajorVsGeneralDifferenceDesc();
    }

    // 특정 학과 졸업생 진로 통계(취업률, 진학률) 조회
    public List<DepartmentOutcome> getCareerStatisticsByDepartment(String departmentName) {
        return departmentOutcomeRepository.findByDepartmentNameOrderByEmploymentRateDesc(departmentName);
    }

    // 전체 학과 졸업생 진로 통계(취업률 기준 내림차순 정렬)
    public List<DepartmentOutcome> getAllDepartmentsByCareerDifferenceDesc() {
        return departmentOutcomeRepository.findAllByOrderByEmploymentRateDesc();
    }
}
