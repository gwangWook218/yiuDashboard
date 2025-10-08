package com.yiuDashboard.service;

import com.yiuDashboard.dto.NewAdmissionStatsDto;
import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.repository.NewAdmissionStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NewAdmissionStatsService {

    private final NewAdmissionStatsRepository repository;

    public String getGraduateCareerSummary(String departmentName) {
        String summary = repository.findGraduateCareerSummaryByDepartment(departmentName);
        if (summary == null) {
            return "졸업생 진출 진로 요약 정보가 없습니다.";
        }
        return summary;
    }

    // 학과별 전공 요약 정보 조회
    public String getMajorInfoSummary(String departmentName) {
        String summary = repository.findMajorInfoSummaryByDepartment(departmentName);
        if (summary == null) {
            return "전공 요약 정보가 없습니다.";
        }
        return summary;
    }

    // ---- 기존 메서드 유지 ----
    public List<NewAdmissionStats> getAllStats() { return repository.findAll(); }

    public List<NewAdmissionStats> getStatsByMajorCategory(String majorCategory) {
        return repository.findByMajorCategory(majorCategory);
    }

    public double getFillRateByDepartment(String departmentName) {
        NewAdmissionStats stats = repository.findByDepartmentName(departmentName);
        if (stats == null) return 0.0;
        return stats.calculateFillRate();
    }

    public NewAdmissionStats saveStats(NewAdmissionStats stats) {
        // 저장 전에 항상 계산해서 반영(primitive double이므로 null 체크 불필요)
        stats.setFillRate(stats.calculateFillRate());
        return repository.save(stats);
    }

    // ---- Map을 계속 쓰는 경우(기존 API 유지) ----
    public Map<String, Object> getDepartmentAdmissionComparison(String departmentName) {
        Map<String, Object> result = new HashMap<>();
        NewAdmissionStats stats = repository.findByDepartmentName(departmentName);
        if (stats == null) {
            result.put("studentQuota", 0);
            result.put("enrolledStudentCount", 0);
            result.put("fillRate", 0.0);
            result.put("similarMajorAverageFillRate", 0.0);
            return result;
        }

        double fillRate = stats.calculateFillRate(); // <- 핵심

        Double similarMajorAverageFillRate =
                repository.findAverageFillRateByMajorCategory(stats.getMajorCategory());
        if (similarMajorAverageFillRate == null) similarMajorAverageFillRate = 0.0;

        result.put("studentQuota", stats.getStudentQuota());
        result.put("enrolledStudentCount", stats.getEnrolledStudentCount());
        result.put("fillRate", fillRate);
        result.put("similarMajorAverageFillRate", similarMajorAverageFillRate);
        return result;
    }

    // ---- DTO로 반환하고 싶은 경우 (선택) ----
    public NewAdmissionStatsDto getDepartmentAdmissionComparisonDto(String departmentName) {
        NewAdmissionStats stats = repository.findByDepartmentName(departmentName);
        if (stats == null) {
            return NewAdmissionStatsDto.builder()
                    .id(null).majorCategory(null).departmentName(departmentName)
                    .studentQuota(0).enrolledStudentCount(0).fillRate(0d)
                    .nationalAverageFillRate(null).similarMajorAverageFillRate(0d)
                    .build();
        }
        Double similarAvg = repository.findAverageFillRateByMajorCategory(stats.getMajorCategory());
        return toDto(stats, similarAvg);
    }

    // ===== 매핑 전용 메서드(엔티티 → DTO) =====
    private static NewAdmissionStatsDto toDto(NewAdmissionStats s, Double similarAvg) {
        double fill = s.calculateFillRate();
        return NewAdmissionStatsDto.builder()
                .id(s.getId())
                .majorCategory(s.getMajorCategory())
                .departmentName(s.getDepartmentName())
                .studentQuota(s.getStudentQuota())
                .enrolledStudentCount(s.getEnrolledStudentCount())
                .fillRate(fill)
                .nationalAverageFillRate(s.getNationalAverageFillRate())
                .similarMajorAverageFillRate(similarAvg)
                .build();
    }
}
