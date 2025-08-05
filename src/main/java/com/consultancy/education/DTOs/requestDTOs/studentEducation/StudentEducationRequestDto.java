package com.consultancy.education.DTOs.requestDTOs.studentEducation;

import com.consultancy.education.enums.GraduationLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEducationRequestDto {
    @NotNull
    GraduationLevel educationLevel;

    @NotBlank
    String institutionName;

    @NotBlank
    String board;

    String collegeCode;

    @NotBlank
    String institutionAddress;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate startYear;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate endYear;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("100.0")
    Double percentage;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("10.0")
    Double cgpa;

    String division;

    @NotBlank
    String specialization;

    @NotNull
    @Min(0)
    Integer backlogs;
}
