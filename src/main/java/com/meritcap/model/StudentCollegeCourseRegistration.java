package com.meritcap.model;

import com.meritcap.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "student_college_course_registration",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_application",
                columnNames = {"student_id", "college_course_snapshot_id", "intake_session", "application_year"}
        )
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StudentCollegeCourseRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    ApprovalStatus applicationStatus;

    @Column(name = "intake_session", nullable = false)
    String intakeSession;

    @Column(name = "application_year", nullable = false)
    Integer applicationYear;

    @Column(name = "remarks", columnDefinition = "TEXT")
    String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "offer_letter_document_id")
    Document offerLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

    // Link to the snapshotted course, not the live course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_course_snapshot_id", nullable = false)
    CollegeCourseSnapshot collegeCourseSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_counselor_id")
    private User assignedCounselor;

    // Optional: Scholarship entity if you have it
    @OneToOne(mappedBy = "studentCollegeCourseRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
    Scholarship scholarship;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
