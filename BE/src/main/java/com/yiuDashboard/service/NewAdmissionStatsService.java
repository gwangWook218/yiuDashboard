package com.yiuDashboard.service;

import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.repository.NewAdmissionStatsRepository;
import com.yiuDashboard.repository.NewAdmissionStatsRepository.GraduateOutcomeAgg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NewAdmissionStatsService {

    private final NewAdmissionStatsRepository repository;

    public List<NewAdmissionStats> getAllStats() {
        return repository.findAll();
    }

    public List<NewAdmissionStats> getStatsByMajorCategory(String majorCategory) {
        return repository.findByMajorCategory(majorCategory);
    }

    public Map<String, Object> getUniversityFillRate(Integer year) {
        Long quota = repository.findQuotaSum(year);
        Long enrolled = repository.findEnrolledSum(year);
        if (quota == null || enrolled == null || quota == 0L) throw new EntityNotFoundException("No fill-rate data");
        double rate = (enrolled * 100.0) / quota;
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("year", year);
        m.put("quotaSum", quota);
        m.put("enrolledSum", enrolled);
        m.put("fillRate", rate);
        return m;
    }

    public Map<String, Object> getDepartmentAdmissionComparison(String departmentName) {
        NewAdmissionStats stats = repository.findByDepartmentName(departmentName);
        if (stats == null) throw new EntityNotFoundException("No department data");
        Double similar = repository.findAverageFillRateByMajorCategory(stats.getMajorCategory());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("studentQuota", stats.getStudentQuota());
        result.put("enrolledStudentCount", stats.getEnrolledStudentCount());
        result.put("fillRate", stats.calculateFillRate());
        result.put("similarMajorAverageFillRate", similar == null ? 0.0 : similar);
        return result;
    }

    public String getGraduateCareerSummary(String departmentName) {
        String summary = Optional.ofNullable(repository.findByDepartmentName(departmentName))
                .map(NewAdmissionStats::getGraduateCareerSummary).orElse(null);
        if (summary == null) throw new EntityNotFoundException("No career summary");
        return summary;
    }

    public Map<String, Object> getGraduationOutcomeSummary(Integer gradYear, Integer deptId) {
        GraduateOutcomeAgg agg = repository.aggregateGraduateOutcome(gradYear, deptId);
        if (agg == null || agg.getGraduates() == null || agg.getGraduates() == 0) {
            throw new EntityNotFoundException("No graduation outcome data");
        }
        long g = Optional.ofNullable(agg.getGraduates()).orElse(0L);
        long e = Optional.ofNullable(agg.getEmployment()).orElse(0L);
        long f = Optional.ofNullable(agg.getFurtherStudy()).orElse(0L);
        long o = Optional.ofNullable(agg.getOthers()).orElse(0L);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("year", gradYear);
        m.put("departmentId", deptId);
        m.put("graduates", g);
        m.put("employmentCount", e);
        m.put("employmentRate", g == 0 ? 0.0 : e * 100.0 / g);
        m.put("furtherStudyCount", f);
        m.put("furtherStudyRate", g == 0 ? 0.0 : f * 100.0 / g);
        m.put("othersCount", o);
        m.put("othersRate", g == 0 ? 0.0 : o * 100.0 / g);
        return m;
    }
}