package com.consultancy.education.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(name = "college_course_snapshot")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CollegeCourseSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long courseId;
    String courseName;
    Long collegeId;
    String collegeName;

    String courseUrl;
    Integer duration;
    String intakeMonths; // e.g., "JANUARY,FEBRUARY"
    Integer intakeYear;
    String eligibilityCriteria;
    String applicationFee;
    String tuitionFee;

    // Test scores
    Double ieltsMinScore;
    Double ieltsMinBandScore;
    Double toeflMinScore;
    Double toeflMinBandScore;
    Double pteMinScore;
    Double pteMinBandScore;
    Double detMinScore;
    Double greMinScore;
    Double gmatMinScore;
    Double satMinScore;
    Double catMinScore;

    // Academics
    Double min10thScore;
    Double minInterScore;
    Double minGraduationScore;

    String scholarshipEligible;
    String scholarshipDetails;
    String backlogAcceptanceRange;
    String remarks;

    @Column(name = "snapshot_taken_at", nullable = false, updatable = false)
    LocalDateTime snapshotTakenAt;
}
