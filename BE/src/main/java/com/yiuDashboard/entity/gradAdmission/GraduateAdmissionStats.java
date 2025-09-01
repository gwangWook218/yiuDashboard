package com.yiuDashboard.entity.gradAdmission;

import com.yiuDashboard.entity.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "graduate_admission_stats")
public class GraduateAdmissionStats {

    @EmbeddedId
    private GraduateAdmissionStatsId id;

    private int graduatesMale;
    private int graduatesFemale;

    private int domesticKorJrCollegeMale;
    private int domesticKorJrCollegeFemale;
    private int domesticCollegeMale;
    private int domesticCollegeFemale;
    private int domesticGradMale;
    private int domesticGradFemale;

    private int overseasKorJrCollegeMale;
    private int overseasKorJrCollegeFemale;
    private int overseasCollegeMale;
    private int overseasCollegeFemale;
    private int overseasGradMale;
    private int overseasGradFemale;

    @ManyToOne
    @JoinColumn(name = "dept_id", referencedColumnName = "dept_id")
    private Department department;
}
