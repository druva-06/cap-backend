package com.consultancy.education.DTOs.requestDTOs.student;

import com.consultancy.education.enums.ActiveStatus;
import com.consultancy.education.enums.Gender;
import com.consultancy.education.enums.GraduationLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudentProfileUpdateRequestDto {

    @Pattern(regexp = "^\\d{10}$", message = "Alternate phone number must be 10 digits.")
    String alternatePhoneNumber;

    @Past(message = "Birth date must be in the past.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dateOfBirth;

    Gender gender;

    GraduationLevel graduationLevel;

    ActiveStatus profileActiveStatus;

    @Min(value = 0, message = "Profile completion must be between 0 and 100.")
    @Max(value = 100, message = "Profile completion must be between 0 and 100.")
    Integer profileCompletion;

    // You can add profile image or other optional info here if needed
    String profileImage;
}
