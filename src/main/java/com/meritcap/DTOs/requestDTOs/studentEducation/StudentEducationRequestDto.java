package com.meritcap.DTOs.requestDTOs.studentEducation;

import com.meritcap.enums.GraduationLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudentEducationRequestDto {

    @NotNull
    GraduationLevel educationLevel;

    @NotBlank
    String degree;

    @NotBlank
    String fieldOfStudy;

    @NotBlank
    String institutionName;

    @NotBlank
    String institutionAddress;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate startYear;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate endYear;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("100.0")
    Double percentage;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("10.0")
    Double cgpa;

    String board;
    String collegeCode;

    @Min(0)
    Integer backlogs;
}
