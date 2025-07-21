package com.yiuDashboard.repository;

import com.yiuDashboard.dto.AdmissionRateDTO;
import com.yiuDashboard.entity.GraduateAdmissionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraduateAdmissionStatsRepository extends JpaRepository<GraduateAdmissionStats, Long> {

    @Query("""
            SELECT new com.yiuDashboard.dto.AdmissionRateDTO(
                g.year,
                SUM(g.graduatesMale + g.graduatesFemale),
                SUM(
                    g.domesticKorJrCollegeMale + g.domesticKorJrCollegeFemale +
                    g.domesticCollegeMale + g.domesticCollegeFemale +
                    g.domesticGradMale + g.domesticGradFemale +
                    g.overseasKorJrCollegeMale + g.overseasKorJrCollegeFemale +
                    g.overseasCollegeMale + g.overseasCollegeFemale +
                    g.overseasGradMale + g.overseasGradFemale
                )
            )
            FROM GraduateAdmissionStats g
            WHERE g.year=:year
            GROUP BY g.year
    """)
    List<AdmissionRateDTO> findAdmissionRateByYear(@Param("year") int year);
}
