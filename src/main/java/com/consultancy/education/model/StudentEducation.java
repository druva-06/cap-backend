package com.consultancy.education.model;

import com.consultancy.education.enums.GraduationLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_education")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    GraduationLevel educationLevel;

    @Column(nullable = false)
    String institutionName;

    @Column(nullable = false)
    String board;

    String collegeCode;

    @Column(nullable = false)
    String institutionAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    LocalDate startYear;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    LocalDate endYear;

    @Column(nullable = false)
    Double percentage;

    @Column(nullable = false)
    Double cgpa;

    String division;

    @Column(nullable = false)
    String specialization;

    @Column(nullable = false)
    Integer backlogs;

    // NEW: link to document
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_document_id")
    Document certificateDocument;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

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
