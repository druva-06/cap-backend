package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.project.ProjectRequestDto;
import com.meritcap.DTOs.responseDTOs.project.ProjectResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto addProject(ProjectRequestDto projectRequestDto, Long studentId);

    ProjectResponseDto getProject(Long id);

    List<ProjectResponseDto> getProjects(Long studentId);

    ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id);

    ProjectResponseDto deleteProject(Long id);
}
