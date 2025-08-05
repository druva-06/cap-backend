package com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDecisionRequestDto {
    @NotNull(message = "Registration ID is required")
    private Long registrationId;

    @NotNull(message = "Decision is required")
    private String decision; // "APPROVED" or "REJECTED"

    private String remarks;
}
