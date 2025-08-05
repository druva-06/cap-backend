package com.consultancy.education.DTOs.requestDTOs.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUploadRequestDto {
    String referenceType; // e.g., "STUDENT"
    Long referenceId;
    String documentType;  // e.g., "AADHAAR"
}
