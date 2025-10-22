package com.yiuDashboard.entity.factorAnalysis;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "factor_analysis")
public class FactorAnalysis {

    @EmbeddedId
    private FactorAnalysisId id;

    private BigDecimal value;
}
