package com.yiuDashboard.entity.gradEmployment;

import com.yiuDashboard.entity.Graduates;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "graduate_employment")
public class GraduateEmployment {

    @Id
    private Integer gradId;

    @OneToOne
    @MapsId // PK와 FK를 동일하게 매핑
    @JoinColumn(name = "grad_id")
    private Graduates graduate;

    private Integer insuredEmployeesMale;
    private Integer insuredEmployeesFemale;
    private Integer overseasEmployeesMale;
    private Integer overseasEmployeesFemale;
    private Integer agricultureFisheryWorkersMale;
    private Integer agricultureFisheryWorkersFemale;
    private Integer individualCreatorsMale;
    private Integer individualCreatorsFemale;
    private Integer selfEmployedMale;
    private Integer selfEmployedFemale;
    private Integer freelancersMale;
    private Integer freelancersFemale;
    private Integer furtherStudyMale;
    private Integer furtherStudyFemale;
    private Integer militaryService;
    private Integer unableToWorkMale;
    private Integer unableToWorkFemale;
    private Integer foreignStudentsMale;
    private Integer foreignStudentsFemale;
    private Integer excludedCasesMale;
    private Integer excludedCasesFemale;
    private Integer othersMale;
    private Integer othersFemale;
    private Integer unknownMale;
    private Integer unknownFemale;
}
