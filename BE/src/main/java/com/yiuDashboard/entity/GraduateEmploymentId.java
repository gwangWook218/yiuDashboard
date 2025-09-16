package com.yiuDashboard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GraduateEmploymentId implements Serializable {
    private Integer year;
    private Integer departmentId;
    private String gender;
}

