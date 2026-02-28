package com.meritcap.model;

import com.meritcap.enums.CertificationType;
import com.meritcap.enums.Level;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "certifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "certification_title", nullable = false)
    String certificationTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    CertificationType type;

    @Column(name = "certified_by", nullable = false)
    String certifiedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    Level level;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "expiration_date")
    LocalDate expirationDate;

    @Column(name = "description")
    String description;

    @Column(name = "certificate_file", nullable = false)
    String certificateFile;

    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @JoinColumn
    @ManyToOne
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
