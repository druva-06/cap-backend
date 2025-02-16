package com.consultancy.education.DTOs.requestDTOs.college;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CollegeRequestDto {

    @NotBlank(message = "College name is required")
    @Size(max = 255, message = "Name cannot be longer than 255 characters")
    String name;

    @NotBlank(message = "Campus name is required")
    String campus;

    @NotBlank(message = "Campus code is required")
    String campusCode;

    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid website URL")
    String websiteUrl;

    String collegeLogo;

    String country;

    @Min(value = 1100, message = "Established year must be after 1100")
    Integer establishedYear;

    String ranking;

    String description;

    String campusGalleryVideoLink;
}
