package com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StudentCollegeCourseRegistrationRequestDto {
    @NotNull(message = "Student ID is required")
    Long studentId;

    @NotNull(message = "College Course ID is required")
    Long collegeCourseId;

    @NotBlank(message = "Intake session is required")
    String intakeSession;

    String remarks;
}
