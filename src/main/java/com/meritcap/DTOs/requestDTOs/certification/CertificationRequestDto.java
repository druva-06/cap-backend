package com.meritcap.DTOs.requestDTOs.certification;


import com.meritcap.enums.CertificationType;
import com.meritcap.enums.Level;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CertificationRequestDto {

    @NotBlank(message = "Certification title is required")
    @Size(max = 255, message = "Certification title must be less than 255 characters")
    String certificationTitle;

    @NotNull(message = "Certification type is required")
    CertificationType type;

    @Size(max = 255, message = "Certified by must be less than 255 characters")
    String certifiedBy;

    @NotNull(message = "Certification level is required")
    Level level;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate issueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate expirationDate;


    @Size(max = 1000, message = "Description must be less than 1000 characters")
    String description;

    @NotBlank(message = "Certificate file is required")
    @Size(max = 255, message = "Certificate file name must be less than 255 characters")
    String certificateFile;
}
