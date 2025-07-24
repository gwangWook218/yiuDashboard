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
}
