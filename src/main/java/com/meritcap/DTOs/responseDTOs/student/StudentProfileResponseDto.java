package com.meritcap.DTOs.responseDTOs.student;

import com.meritcap.enums.ActiveStatus;
import com.meritcap.enums.Gender;
import com.meritcap.enums.GraduationLevel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Student Response Dto contains student personal information.")
public class StudentProfileResponseDto {

    @Schema(description = "UserId", example = "1")
    Long userId;

    @Schema(description = "User Alternate phone number", example = "9999999999")
    String alternatePhoneNumber;

    @Schema(description = "User Date of Birth", example = "01/01/2001")
    LocalDate dateOfBirth;

    @Schema(description = "User Gender", example = "MALE/FEMALE")
    Gender gender;

    @Schema(description = "User Graduation level", example = "UNDERGRADUATE")
    GraduationLevel graduationLevel;

    @Schema(description = "User Profile status", example = "ACTIVE")
    ActiveStatus profileActiveStatus;

    @Schema(description = "User Profile completion", example = "80")
    Integer profileCompletion;
}
