package com.yiuDashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DropoutRateDTO {
    private String departments;
    private Double riskRate;
    private Double normalRate;
    private Double totalRate;
}
