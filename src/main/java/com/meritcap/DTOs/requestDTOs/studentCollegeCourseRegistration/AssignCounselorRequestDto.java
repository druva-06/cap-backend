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
public class AssignCounselorRequestDto {
    @NotNull(message = "Registration ID is required")
    private Long registrationId;

    @NotNull(message = "Counselor ID is required")
    private Long counselorId;
}
