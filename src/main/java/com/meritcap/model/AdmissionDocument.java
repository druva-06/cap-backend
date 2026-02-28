package com.meritcap.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "admission_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdmissionDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "letter_of_recommendation_count")
    Integer letterOfRecommendationCount;

    @Column(name = "medium_of_instruction")
    String mediumOfInstruction;

    @Column(name = "statement_of_purpose_count")
    Integer statementOfPurposeCount;

    @Column(name = "bank_statement")
    String bankStatement;

    @Column(name = "bank_covering_letter")
    String bankCoveringLetter;

    @Column(name = "noc_required")
    Boolean nocRequired;

    @Column(name = "noc_document")
    String nocDocument;

    @Column(name = "affidavit_required")
    Boolean affidavitRequired;

    @Column(name = "affidavit_document")
    String affidavitDocument;

    @Column(name = "translation_document_required")
    Boolean translationDocumentRequired;

    @Column(name = "translation_document")
    String translationDocument;

    @Column(name = "enable_update")
    Boolean enableUpdate;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToOne
    @JoinColumn
    Student student;
}
