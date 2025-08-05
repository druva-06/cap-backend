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
public class AddRemarksRequestDto {
    @NotNull(message = "Registration ID is required")
    private Long registrationId;

    @NotBlank(message = "Remarks are required")
    private String remarks;
}
