package com.yiuDashboard.repository;

import com.yiuDashboard.entity.factorAnalysis.FactorAnalysis;
import com.yiuDashboard.entity.factorAnalysis.FactorAnalysisId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactorAnalysisRepository extends JpaRepository<FactorAnalysis, FactorAnalysisId> {
    @Query("""
            select fa.id.factorName, fa.value
            from FactorAnalysis fa
            where fa.id.year = :year and fa.id.factorType = :type
            """)
    List<Object[]> findByYearAndType(@Param("year") int year, @Param("type") String type);
}
