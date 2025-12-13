package com.consultancy.education.DTOs.responseDTOs.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Counselor response DTO for assigning leads.")
public class CounselorDto {
    @Schema(description = "Counselor user ID")
    Long id;

    @Schema(description = "Counselor full name")
    String name;

    @Schema(description = "Counselor email")
    String email;

    @Schema(description = "Counselor phone number")
    String phoneNumber;
}
