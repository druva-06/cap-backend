package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.project.ProjectRequestDto;
import com.meritcap.DTOs.responseDTOs.project.ProjectResponseDto;
import com.meritcap.model.Project;
import com.meritcap.model.Student;

import java.util.ArrayList;
import java.util.List;

public class ProjectTransformer {

    public static Project toEntity(ProjectRequestDto projectRequestDto) {
        return Project.builder()
                .projectTitle(projectRequestDto.getProjectTitle())
                .description(projectRequestDto.getDescription())
                .projectDuration(projectRequestDto.getProjectDuration())
                .projectType(projectRequestDto.getProjectType())
                .projectYear(projectRequestDto.getProjectYear())
                .technology(projectRequestDto.getTechnology())
                .role(projectRequestDto.getRole())
                .projectFiles(projectRequestDto.getProjectFiles())
                .productionUrl(projectRequestDto.getProductionUrl())
                .repositoryUrl(projectRequestDto.getRepositoryUrl())
                .teamSize(projectRequestDto.getTeamSize())
                .status(projectRequestDto.getStatus())
                .build();
    }

    public static ProjectResponseDto toDTO(Project project, Student student) {
        return ProjectResponseDto.builder()
                .studentId(student.getId())
                .projectId(project.getId())
                .projectTitle(project.getProjectTitle())
                .description(project.getDescription())
                .projectDuration(project.getProjectDuration())
                .projectType(project.getProjectType())
                .projectYear(project.getProjectYear())
                .technology(project.getTechnology())
                .role(project.getRole())
                .projectFiles(project.getProjectFiles())
                .productionUrl(project.getProductionUrl())
                .repositoryUrl(project.getRepositoryUrl())
                .teamSize(project.getTeamSize())
                .status(project.getStatus())
                .build();
    }

    public static List<ProjectResponseDto> toDTO(List<Project> projects, Student student) {
        List<ProjectResponseDto> projectResponseDtos = new ArrayList<>();
        for (Project project : projects) {
            projectResponseDtos.add(toDTO(project, student));
        }
        return projectResponseDtos;
    }

    public static void updateProject(Project project, ProjectRequestDto projectRequestDto) {
        project.setProjectTitle(projectRequestDto.getProjectTitle());
        project.setDescription(projectRequestDto.getDescription());
        project.setProjectDuration(projectRequestDto.getProjectDuration());
        project.setProjectType(projectRequestDto.getProjectType());
        project.setProjectYear(projectRequestDto.getProjectYear());
        project.setTechnology(projectRequestDto.getTechnology());
        project.setRole(projectRequestDto.getRole());
        project.setProjectFiles(projectRequestDto.getProjectFiles());
        project.setProductionUrl(projectRequestDto.getProductionUrl());
        project.setRepositoryUrl(projectRequestDto.getRepositoryUrl());
        project.setTeamSize(projectRequestDto.getTeamSize());
        project.setStatus(projectRequestDto.getStatus());
    }
}
