package com.yiuDashboard.entity.grade;

import com.yiuDashboard.entity.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "graduate_score_distribution")
public class GraduateScoreDistribution {

    @EmbeddedId
    private GraduateScoreDistributionId id;

    @Column(name = "avg_gpa", precision = 3, scale = 2)
    private BigDecimal avgGpa;

    @Column(name = "avg_percent_score", precision = 5, scale = 2)
    private BigDecimal avgPercentScore;

    @Column(name = "score_95_100_count")
    private Integer score95To100Count;

    @Column(name = "score_90_95_count")
    private Integer score90To95Count;

    @Column(name = "score_85_90_count")
    private Integer score85To90Count;

    @Column(name = "score_80_85_count")
    private Integer score80To85Count;

    @Column(name = "score_75_80_count")
    private Integer score75To80Count;

    @Column(name = "score_70_75_count")
    private Integer score70To75Count;

    @Column(name = "score_65_70_count")
    private Integer score65To70Count;

    @Column(name = "score_60_65_count")
    private Integer score60To65Count;

    @Column(name = "score_below_60")
    private Integer scoreBelow60;

    @ManyToOne
    @MapsId("departmentId")
    @JoinColumn(name = "department_id")
    private Department department;
}
