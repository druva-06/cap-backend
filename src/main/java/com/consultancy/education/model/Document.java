package com.consultancy.education.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Reference type (STUDENT, ADMIN, etc.)
    @Column(nullable = false)
    String referenceType;

    // Reference id (e.g., studentId, adminId, etc.)
    @Column(nullable = false)
    Long referenceId;

    // Type of document (AADHAAR, PASSPORT, etc.)
    @Column(nullable = false)
    String documentType;

    @Column(nullable = false)
    String fileUrl;

    @Column(nullable = false)
    String uploadedBy; // Username or userId

    @Column(nullable = false, updatable = false)
    LocalDateTime uploadedAt;

    @Column(nullable = false)
    Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
