package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditProgress {
    private String category;
    private Integer completed;
    private Integer total;
    private Double percentage;

    public CreditProgress(String category, int completed, int total) {
        this.percentage = total == 0 ? 0 : (completed * 100.0 / total);
    }
}
