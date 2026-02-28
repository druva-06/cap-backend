package com.meritcap.DTOs.responseDTOs.project;

import com.meritcap.enums.ProjectStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProjectResponseDto {
    Long studentId;
    Long projectId;
    String projectTitle;
    String description;
    String projectDuration;
    String projectType;
    Integer projectYear;
    String technology;
    String role;
    String projectFiles;
    String productionUrl;
    String repositoryUrl;
    Integer teamSize;
    ProjectStatus status;
}
