package com.meritcap.DTOs.responseDTOs.studentCollegeCourseRegistration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDraftStartResponseDTO {
    private Long applicationId;
    private Long studentId;
    private Long collegeCourseId;
    private String intakeSession;
    private String status; // DRAFT
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

