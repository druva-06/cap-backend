package com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCollegeCourseRegistrationEditRequestDto {
    @NotNull(message = "Registration ID is required")
    private Long registrationId;

    @NotBlank(message = "Intake session is required")
    private String intakeSession;

    private String remarks;
    // You could allow collegeCourseId for course switch, but typically that's not editable after creation.
}

