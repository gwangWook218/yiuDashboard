package com.yiuDashboard.dto;

import com.yiuDashboard.entity.StudentNumber;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StudentNumberDto {

    private Integer year;
    private Integer departmentId;
    private String  majorCategory;
    private String  departmentName;
    private Integer totalCount;
    private Integer maleCount;
    private Integer femaleCount;

    public static StudentNumberDto from(StudentNumber s) {
        return StudentNumberDto.builder()
                .year(s.getYear())
                .departmentId(s.getDepartmentId())
                .majorCategory(s.getMajorCategory())
                .departmentName(s.getDepartmentName())
                .totalCount(s.getTotalCount())
                .maleCount(s.getMaleCount())
                .femaleCount(s.getFemaleCount())
                .build();
    }
}