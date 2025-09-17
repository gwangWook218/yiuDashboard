package com.yiuDashboard.service;

import com.yiuDashboard.entity.EmploymentStats;
import com.yiuDashboard.repository.EmploymentStatsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmploymentStatsService {

    private final EmploymentStatsRepository employmentStatsRepository;

    public Map<String, Object> getEmploymentRateSummary(int year, int departmentId) {
        List<EmploymentStats> rows = employmentStatsRepository.findByIdYearAndIdDepartmentId(year, departmentId);
        if (rows.isEmpty()) throw new EntityNotFoundException("No employment stats for year=" + year + ", deptId=" + departmentId);

        int totalGraduates = 0;
        int insured = 0;
        int furtherStudy = 0;
        int military = 0;
        int unable = 0;
        int foreignStudents = 0;
        int excluded = 0;
        int others = 0;
        int unknown = 0;

        for (EmploymentStats r : rows) {
            totalGraduates += nz(r.getGraduates());
            insured += nz(r.getInsuredEmployees());
            furtherStudy += nz(r.getFurtherStudy());
            military += nz(r.getMilitaryService());
            unable += nz(r.getUnableToWork());
            foreignStudents += nz(r.getForeignStudents());
            excluded += nz(r.getExcludedCases());
            others += nz(r.getOthers());
            unknown += nz(r.getUnknown());
        }

        if (totalGraduates == 0) throw new EntityNotFoundException("Total graduates is zero for year=" + year + ", deptId=" + departmentId);

        double employmentRate = round1((insured * 100.0) / totalGraduates);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("year", year);
        body.put("departmentId", departmentId);
        body.put("graduates", totalGraduates);
        body.put("insuredEmployees", insured);
        body.put("employmentRate", employmentRate);
        body.put("furtherStudy", furtherStudy);
        body.put("militaryService", military);
        body.put("unableToWork", unable);
        body.put("foreignStudents", foreignStudents);
        body.put("excludedCases", excluded);
        body.put("others", others);
        body.put("unknown", unknown);
        return body;
    }

    public Map<String, Object> getCareerDistribution(int year, int departmentId) {
        List<EmploymentStats> rows = employmentStatsRepository.findByIdYearAndIdDepartmentId(year, departmentId);
        if (rows.isEmpty()) throw new EntityNotFoundException("No employment stats for year=" + year + ", deptId=" + departmentId);

        int overseas = 0;
        int agriFishery = 0;
        int creators = 0;
        int selfEmployed = 0;
        int freelancers = 0;
        int insured = 0;

        for (EmploymentStats r : rows) {
            overseas += nz(r.getOverseasEmployees());
            agriFishery += nz(r.getAgricultureFisheryWorkers());
            creators += nz(r.getIndividualCreators());
            selfEmployed += nz(r.getSelfEmployed());
            freelancers += nz(r.getFreelancers());
            insured += nz(r.getInsuredEmployees());
        }

        if (insured == 0) throw new EntityNotFoundException("No insured employees for year=" + year + ", deptId=" + departmentId);

        Map<String, Object> distribution = new LinkedHashMap<>();
        distribution.put("overseas_employees", percent(overseas, insured));
        distribution.put("agriculture_fishery_workers", percent(agriFishery, insured));
        distribution.put("individual_creators", percent(creators, insured));
        distribution.put("self_employed", percent(selfEmployed, insured));
        distribution.put("freelancers", percent(freelancers, insured));

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("year", year);
        body.put("departmentId", departmentId);
        body.put("insuredEmployees", insured);
        body.put("distribution", distribution);
        return body;
    }

    private static int nz(Integer v) { return v == null ? 0 : v; }
    private static double percent(int part, int whole) { return round1((part * 100.0) / whole); }
    private static double round1(double v) { return Math.round(v * 10.0) / 10.0; }
}