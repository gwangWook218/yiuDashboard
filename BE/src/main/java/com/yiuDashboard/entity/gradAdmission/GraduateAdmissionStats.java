package com.yiuDashboard.entity.gradAdmission;

import com.yiuDashboard.entity.Department;
import com.yiuDashboard.entity.Graduates;
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

    @Id
    private Integer gradId;

    @OneToOne
    @MapsId // PK와 FK를 동일하게 매핑
    @JoinColumn(name = "grad_id")
    private Graduates graduate;

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


}
