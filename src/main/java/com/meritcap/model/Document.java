package com.meritcap.model;

import com.meritcap.enums.DocumentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

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
    String category;

    @Column(length = 1000)
    String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    DocumentStatus documentStatus;

    @Column(nullable = false)
    String fileUrl;

    @Column(nullable = false)
    String uploadedBy;

    @Column(nullable = false)
    Boolean isDeleted = false;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private Instant uploadedAt;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = Instant.now();
    }
}
