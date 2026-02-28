package com.meritcap.DTOs.requestDTOs.project;

import com.meritcap.enums.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequestDto {

    @NotBlank(message = "Project title is mandatory")
    @Size(max = 255, message = "Project title should not exceed 255 characters")
    String projectTitle;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 2000, message = "Description should not exceed 2000 characters")
    String description;

    @NotBlank(message = "Project duration is mandatory")
    @Size(max = 100, message = "Project duration should not exceed 100 characters")
    String projectDuration;

    @Size(max = 100, message = "Project type should not exceed 100 characters")
    String projectType;

    @Min(value = 1900, message = "Project year should not be earlier than 1900")
    @Max(value = 2100, message = "Project year should not be later than 2100")
    Integer projectYear;

    @NotBlank(message = "Technology is mandatory")
    @Size(max = 255, message = "Technology should not exceed 255 characters")
    String technology;

    @NotBlank(message = "Role is mandatory")
    @Size(max = 255, message = "Role should not exceed 255 characters")
    String role;

    @Size(max = 500, message = "Project files path should not exceed 500 characters")
    String projectFiles;

    @Size(max = 500, message = "Production URL should not exceed 500 characters")
    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format for production URL")
    String productionUrl;

    @Size(max = 500, message = "Repository URL should not exceed 500 characters")
    @Pattern(regexp = "^(http|https)://.*$", message = "Invalid URL format for repository URL")
    String repositoryUrl;

    @Min(value = 1, message = "Team size must be at least 1")
    @Max(value = 1000, message = "Team size must not exceed 1000")
    Integer teamSize;

    @NotNull(message = "Project status is mandatory")
    ProjectStatus status;
}
