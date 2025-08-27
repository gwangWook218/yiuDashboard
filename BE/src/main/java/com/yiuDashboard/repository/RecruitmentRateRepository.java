package com.yiuDashboard.repository;

import com.yiuDashboard.dto.RecruitmentRateDto;
import com.yiuDashboard.entity.AdmissionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRateRepository extends JpaRepository<AdmissionSummary, Integer> {

    @Query("""
            select new com.yiuDashboard.dto.RecruitmentRateDto(
                a.year,
                case
                	when a.admissionType like '%수시%' then '수시'
                	when a.admissionType like '%정시%' then '정시'
                	else '기타'
                end,
                round(sum(a.recruitmentCount) * 1.0 / (select sum(sub.recruitmentCount) from AdmissionSummary sub
                where sub.year=:year) * 100.0, 1)
            )
            from AdmissionSummary a
            where a.year=:year
            group by a.year,
            case
                when a.admissionType like '%수시%' then '수시'
                when a.admissionType like '%정시%' then '정시'
                else '기타'
            end
            """)
    List<RecruitmentRateDto> findRecruitmentRateByYear(@Param("year") int year);
}
