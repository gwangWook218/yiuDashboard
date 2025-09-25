package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "staff_statistics")
public class StaffStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;

    private String category;

    private String positionType;

    private Integer male;

    private Integer female;
}
