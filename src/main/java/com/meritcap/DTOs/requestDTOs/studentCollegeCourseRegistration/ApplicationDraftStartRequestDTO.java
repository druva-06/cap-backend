package com.meritcap.DTOs.requestDTOs.studentCollegeCourseRegistration;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDraftStartRequestDTO {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "College Course ID is required")
    private Long collegeCourseId;

    // Optional intake/session field
    private String intakeSession;
}
