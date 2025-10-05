package com.yiuDashboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "student_number",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_stunum_year_dept", columnNames = {"year", "department_id"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StudentNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "department_id", nullable = false)
    private Integer departmentId;

    @Column(name = "major_category", length = 100, nullable = false)
    private String majorCategory;

    @Column(name = "department_name", length = 255, nullable = false)
    private String departmentName;

    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    @Column(name = "male_count", nullable = false)
    private Integer maleCount;

    @Column(name = "female_count", nullable = false)
    private Integer femaleCount;
}
