package com.yiuDashboard.repository;

import com.yiuDashboard.dto.grade.GraduateGradeDto;
import com.yiuDashboard.entity.grade.GraduateScoreDistribution;
import com.yiuDashboard.entity.grade.GraduateScoreDistributionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GraduateGradeRepository extends JpaRepository<GraduateScoreDistribution, GraduateScoreDistributionId> {

    @Query("""
            select new com.yiuDashboard.dto.grade.GraduateGradeDto(
                gsd.id.year, d.department, gsd.avgGpa, gsd.avgPercentScore,
                (gsd.score95To100Count + gsd.score90To95Count + gsd.score85To90Count + gsd.score80To85Count
                 + gsd.score75To80Count + gsd.score70To75Count + gsd.score65To70Count + gsd.score60To65Count + gsd.scoreBelow60)
            )
            from GraduateScoreDistribution gsd
            join gsd.department d
            where gsd.id.year = :year and d.department = :dept
            """)
    GraduateGradeDto findGraduateGradeByYearAndDept(@Param("year") int year, @Param("dept") String dept);

}
