package com.meritcap.DTOs.requestDTOs.abroadExam;

import com.meritcap.enums.ExamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AbroadExamRequestDto {

    @NotNull(message = "Exam type is required")
    ExamType examType;

    @NotNull(message = "Exam title is required")
    String examTitle;

    Double speakingScore;

    Double readingScore;

    Double writingScore;

    Double listeningScore;

    @Column(name = "overall_score", nullable = false)
    @NotNull(message = "Overall score is required")
    Double overallScore;

    @NotNull(message = "Exam date is required")
    @PastOrPresent(message = "Exam date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate examDate;
}
