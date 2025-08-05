package com.consultancy.education.DTOs.responseDTOs.studentEducation;

import com.consultancy.education.enums.GraduationLevel;
import com.consultancy.education.DTOs.responseDTOs.document.DocumentResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEducationResponseDto {
    Long educationId;
    GraduationLevel educationLevel;
    String institutionName;
    String board;
    String collegeCode;
    String institutionAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate startYear;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate endYear;
    Double percentage;
    Double cgpa;
    String division;
    String specialization;
    Integer backlogs;
    DocumentResponseDto certificateDocument;
}
