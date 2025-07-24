package com.yiuDashboard.repository;

import com.yiuDashboard.entity.DepartmentOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentOutcomeRepository extends JpaRepository<DepartmentOutcome, Long> {

    //특정 학과의 재학생 수 정보 조회
    List<DepartmentOutcome> findByDepartmentName(String departmentName);

    //재학생 수 기준 내림차순 정렬
    List<DepartmentOutcome> findAllByOrderByEnrolledStudentsDesc();
}
