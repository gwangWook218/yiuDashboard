package com.yiuDashboard.repository;

import com.yiuDashboard.dto.GraduateAdmissionDto;
import com.yiuDashboard.entity.gradAdmission.GraduateAdmissionStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraduateAdmissionRepository extends JpaRepository<GraduateAdmissionStats, Integer> {
    @Query("""
            SELECT new com.yiuDashboard.dto.GraduateAdmissionDto(
                d.department,
                d.isDaytime,
                (domesticKorJrCollegeMale + domesticKorJrCollegeFemale +
                        domesticCollegeMale + domesticCollegeFemale +
                        domesticGradMale + domesticGradFemale +
                        overseasKorJrCollegeMale + overseasKorJrCollegeFemale +
                        overseasCollegeMale + overseasCollegeFemale +
                        overseasGradMale + overseasGradFemale),
                round((domesticKorJrCollegeMale + domesticKorJrCollegeFemale +
                        domesticCollegeMale + domesticCollegeFemale +
                        domesticGradMale + domesticGradFemale +
                        overseasKorJrCollegeMale + overseasKorJrCollegeFemale +
                        overseasCollegeMale + overseasCollegeFemale +
                        overseasGradMale + overseasGradFemale) * 100.0 / (g.graduatesMale + g.graduatesFemale)),
                (domesticKorJrCollegeMale + domesticKorJrCollegeFemale),
                (domesticCollegeMale + domesticCollegeFemale),
                (domesticGradMale + domesticGradFemale),
                (overseasKorJrCollegeMale + overseasKorJrCollegeFemale),
                (overseasCollegeMale + overseasCollegeFemale),
                (overseasGradMale + overseasGradFemale)
            )
            FROM GraduateAdmissionStats gas
            join gas.graduate g
            join g.department d
            where g.year = :year
            """)
    List<GraduateAdmissionDto> findByYearAndDeptId(@Param("year") int year);
}
