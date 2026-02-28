package com.meritcap.DTOs.responseDTOs.certification;

import com.meritcap.enums.CertificationType;
import com.meritcap.enums.Level;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CertificationResponseDto {
    Long studentId;
    Long certificationId;
    String certificationTitle;
    CertificationType type;
    String certifiedBy;
    Level level;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate issueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate expirationDate;

    String description;
    String certificateFile;
}
