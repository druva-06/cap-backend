package com.meritcap.DTOs.responseDTOs.abroadExam;

import com.meritcap.enums.ExamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AbroadExamResponseDto {
    Long studentId;
    Long examId;
    ExamType examType;
    String examTitle;
    Double speakingScore;
    Double readingScore;
    Double writingScore;
    Double listeningScore;
    Double overallScore;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate examDate;
}
