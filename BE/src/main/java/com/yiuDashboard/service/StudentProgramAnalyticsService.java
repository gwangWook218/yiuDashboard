package com.yiuDashboard.service;

import com.yiuDashboard.dto.FillRateResponse;
import com.yiuDashboard.entity.StudentProgramAnalytics;
import com.yiuDashboard.repository.StudentProgramAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentProgramAnalyticsService {

    private final StudentProgramAnalyticsRepository repo;

    @Transactional(readOnly = true)
    public FillRateResponse getFillRate(int year, int departmentId) {
        var row = repo.findByYearAndDepartmentId(year, departmentId)
                .orElseThrow(() -> new IllegalArgumentException("not found: year=" + year + ", dept=" + departmentId));

        var dept = row.getDepartment();
        int current = nz(row.getEnrolledInMale()) + nz(row.getEnrolledInFemale());

        Double peerAvg = null;
        if (dept != null && dept.getCollege() != null) {
            peerAvg = repo.findPeerAverageByCollege(year, dept.getCollege());
        }

        Integer recruitment = null;
        Double fillRate = (recruitment == null || recruitment == 0) ? null : (current * 1.0) / recruitment;

        return FillRateResponse.builder()
                .year(year)
                .departmentId(departmentId)
                .college(dept != null ? dept.getCollege() : null)
                .departmentName(dept != null ? dept.getDepartment() : null)
                .currentStudents(current)
                .recruitmentCount(recruitment)
                .fillRate(fillRate)
                .peerAvgCurrent(peerAvg)
                .build();
    }

    private static int nz(Integer v){ return v==null?0:v; }
}
