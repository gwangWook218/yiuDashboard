package com.yiuDashboard.repository;

import com.yiuDashboard.entity.DepEnroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepEnrollRepository extends JpaRepository<DepEnroll, Long> {

    @Query(value = """
            select
            d.기준연도,
            d.단과대학,
            d.학과,
            (d.재학생_정원내_남 + d.재학생_정원내_여 + d.재학생_정원외_남 + d.재학생_정원외_여) AS 재학생수,
            (SELECT
            AVG(sub.재학생_정원내_남 + sub.재학생_정원내_여 + sub.재학생_정원외_남 + sub.재학생_정원외_여)
            FROM dep_enroll AS sub
            WHERE sub.기준연도 = d.기준연도 AND sub.단과대학 = d.단과대학) AS 단과대평균재학생수
            from dep_enroll d
            WHERE d.학과상태 != '폐과'
            order by d.기준연도, d.단과대학, d.학과, 재학생수
            """, nativeQuery = true)
    List<Object[]> fetchDepartmentEnrollmentStats();
}
