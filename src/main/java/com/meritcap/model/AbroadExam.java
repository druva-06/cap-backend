package com.meritcap.model;

import com.meritcap.enums.ExamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "abroad_exam")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbroadExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false)
    ExamType examType;

    @Column(name = "exam_title", nullable = false)
    String examTitle;

    @Column(name = "speaking_score")
    Double speakingScore;

    @Column(name = "reading_score")
    Double readingScore;

    @Column(name = "writing_score")
    Double writingScore;

    @Column(name = "listening_score")
    Double listeningScore;

    @Column(name = "overall_score", nullable = false)
    Double overallScore;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Column(name = "exam_date", nullable = false)
    LocalDate examDate;

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
