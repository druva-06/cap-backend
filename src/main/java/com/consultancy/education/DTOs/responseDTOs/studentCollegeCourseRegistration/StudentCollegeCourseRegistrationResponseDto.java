package com.consultancy.education.DTOs.responseDTOs.studentCollegeCourseRegistration;

import com.consultancy.education.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StudentCollegeCourseRegistrationResponseDto {

    Long registrationId;
    Long studentId;
    Long collegeCourseSnapshotId;
    String intakeSession;
    Integer applicationYear;
    String status; // e.g., PENDING
    String remarks;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String courseName;
    String collegeName;
}
