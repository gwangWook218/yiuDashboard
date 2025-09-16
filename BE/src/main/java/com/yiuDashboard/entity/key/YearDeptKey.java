package com.yiuDashboard.entity.key;

import java.io.Serializable;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class YearDeptKey implements Serializable {
    private Integer year;
    private Integer departmentId;
}


