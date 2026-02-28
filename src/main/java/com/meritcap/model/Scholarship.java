package com.meritcap.model;

import com.meritcap.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "scholarships")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Scholarship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "scholarship_support_required", nullable = false)
    Boolean scholarshipSupportRequired;

    @Column(name = "merit_scholarship_amount")
    Double meritScholarshipAmount;

    @Column(name = "academic_scholarship_amount")
    Double academicScholarshipAmount;

    @Column(name = "accommodation_scholarship_amount")
    Double accommodationScholarshipAmount;

    @Column(name = "total_scholarship_amount")
    Double totalScholarshipAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "scholarship_status")
    ApprovalStatus scholarshipStatus;

    @Column(name = "scholarship_remark")
    String scholarshipRemark;

    @Column(name = "scholarship_approved_date")
    LocalDateTime scholarshipApprovedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @JoinColumn
    @OneToOne
    StudentCollegeCourseRegistration studentCollegeCourseRegistration;

    @JoinColumn
    @OneToOne
    User user;

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
