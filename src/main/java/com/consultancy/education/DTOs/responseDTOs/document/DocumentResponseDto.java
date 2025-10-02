package com.consultancy.education.DTOs.responseDTOs.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentResponseDto {
    Long id;
    String referenceType;
    Long referenceId;
    String documentType;
    String documentStatus;
    String category;
    String remarks;
    String fileUrl;
    String uploadedBy;
    Instant uploadedAt;
}
