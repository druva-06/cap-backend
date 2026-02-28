package com.meritcap.controller;

import com.meritcap.DTOs.requestDTOs.project.ProjectRequestDto;
import com.meritcap.DTOs.responseDTOs.project.ProjectResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.model.Project;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.ProjectService;
import com.meritcap.utils.ToMap;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody @Valid ProjectRequestDto projectRequestDto, BindingResult bindingResult, @RequestParam Long studentId) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult), "Validation failed", 400));
        }
        try{
            ProjectResponseDto projectResponseDto = projectService.addProject(projectRequestDto, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(projectResponseDto, "Project added successfully", 201));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        try {
            ProjectResponseDto projectResponseDto = projectService.getProject(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(projectResponseDto, "Project fetched successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@RequestBody @Valid ProjectRequestDto projectRequestDto, @PathVariable Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiFailureResponse<>(bindingResult, "Validation failed", 400));
        }
        try {
            ProjectResponseDto projectResponseDto = projectService.updateProject(projectRequestDto, id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(projectResponseDto, "Project updated successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            ProjectResponseDto projectResponseDto = projectService.deleteProject(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(projectResponseDto, "Project deleted successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProjects(@RequestParam Long studentId) {
        try{
            List<ProjectResponseDto> projectResponseDtos = projectService.getProjects(studentId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse<>(projectResponseDtos, "Projects fetched successfully", 200));
        }
        catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }
}