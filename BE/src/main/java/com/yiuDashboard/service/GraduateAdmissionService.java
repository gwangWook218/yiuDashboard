package com.yiuDashboard.service;

import com.yiuDashboard.dto.GraduateAdmissionDto;
import com.yiuDashboard.repository.GraduateAdmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraduateAdmissionService {

    private final GraduateAdmissionRepository repository;

    public List<GraduateAdmissionDto> getAdmission(int year) {
        return repository.findByYearAndDeptId(year);
    }
}
