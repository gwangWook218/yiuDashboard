package com.yiuDashboard.entity.factorAnalysis;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactorAnalysisId {
    private int year;
    private String factorType;
    private String factorName;
}
