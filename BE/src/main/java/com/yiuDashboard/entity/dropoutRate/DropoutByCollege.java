package com.yiuDashboard.entity.dropoutRate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dropout_by_college")
public class DropoutByCollege {

    @EmbeddedId
    private DropoutByCollegeId id;

    private Double percentage;
}
