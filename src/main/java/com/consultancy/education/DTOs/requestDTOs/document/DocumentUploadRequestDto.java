package com.consultancy.education.DTOs.requestDTOs.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentUploadRequestDto {
    @NotBlank(message = "referenceType is required")
    String referenceType;

    @NotNull(message = "referenceId is required")
    Long referenceId;

    @NotBlank(message = "documentType is required")
    String documentType;

    @NotBlank(message = "category is required")
    String category;

    @Size(max = 1000, message = "remarks must be <= 1000 characters")
    String remarks;
}
