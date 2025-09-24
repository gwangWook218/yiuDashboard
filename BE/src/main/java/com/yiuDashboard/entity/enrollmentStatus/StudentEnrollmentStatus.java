package com.yiuDashboard.entity.enrollmentStatus;

import com.yiuDashboard.entity.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_enrollment_status")
public class StudentEnrollmentStatus {

    @EmbeddedId
    private StudentEnrollmentStatusId id;

    private String departmentStatus;
    private String division;

    private Integer enrolledInMale;
    private Integer enrolledInFemale;
    private Integer enrolledOutMale;
    private Integer enrolledOutFemale;

    @ManyToOne
    @MapsId("departmentId")
    @JoinColumn(name = "department_id")
    private Department department;
}
