package com.yiuDashboard.service;
import com.yiuDashboard.entity.NewAdmissionStats;
import com.yiuDashboard.repository.NewAdmissionStatsRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class NewAdmissionStatsService {

    private final NewAdmissionStatsRepository repository;

    public NewAdmissionStatsService(NewAdmissionStatsRepository repository) {
        this.repository = repository;
    }

    public List<NewAdmissionStats> getAllStats() {
        return repository.findAll();
    }

    public List<NewAdmissionStats> getStatsByCategory(String category) {
        return repository.findByCategory(category);
    }

    public NewAdmissionStats save(NewAdmissionStats stat) {
        return repository.save(stat);
    }

    // 특정 학과 코드로 충원율 계산
    public double getFillRateByDepartment(String departmentCode) {
        Optional<NewAdmissionStats> optionalStat = repository.findByDepartmentCode(departmentCode);
        if (optionalStat.isEmpty() || optionalStat.get().getAdmissionQuota() == 0) return 0.0;

        NewAdmissionStats stat = optionalStat.get();
        return (double) stat.getCurrentStudents() / stat.getAdmissionQuota();
    }

    // 유사 계열 평균 충원율 계산
    public double getAverageFillRateBySimilarMajor(String majorCategory) {
        List<NewAdmissionStats> stats = repository.findByCategory(majorCategory);
        if (stats == null || stats.isEmpty()) return 0.0;

        double totalRate = 0.0;
        int count = 0;

        for (NewAdmissionStats stat : stats) {
            if (stat.getAdmissionQuota() > 0) {
                totalRate += (double) stat.getCurrentStudents() / stat.getAdmissionQuota();
                count++;
            }
        }

        return count == 0 ? 0.0 : totalRate / count;
    }
    public String getGraduateTrendByDepartmentName(String departmentName) {
        Optional<NewAdmissionStats> optionalStat = repository.findByDepartmentName(departmentName);
        return optionalStat.map(NewAdmissionStats::getGraduateTrend).orElse("해당 학과 정보를 찾을 수 없습니다.");
    }

    public Map<String, String> getMajorInfoByDepartmentName(String departmentName) {
        Optional<NewAdmissionStats> optionalStat = repository.findByDepartmentName(departmentName);

        if (optionalStat.isEmpty()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "해당 학과 정보를 찾을 수 없습니다.");
            return errorMap;
        }

        NewAdmissionStats stat = optionalStat.get();

        Map<String, String> majorInfo = new HashMap<>();
        majorInfo.put("majorFeatures", stat.getMajorFeatures());
        majorInfo.put("coreSubjects", stat.getCoreSubjects());
        majorInfo.put("relatedCareers", stat.getRelatedCareers());

        return majorInfo;
    }
}