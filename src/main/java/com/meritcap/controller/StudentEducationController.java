package com.meritcap.controller;

import com.meritcap.DTOs.requestDTOs.studentEducation.StudentEducationRequestDto;
import com.meritcap.DTOs.responseDTOs.studentEducation.StudentEducationResponseDto;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.StudentEducationService;
import com.meritcap.utils.ToMap;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/student-education")
public class StudentEducationController {

    private final StudentEducationService studentEducationService;

    public StudentEducationController(StudentEducationService studentEducationService) {
        this.studentEducationService = studentEducationService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addStudentEducation(@RequestBody @Valid StudentEducationRequestDto dto, BindingResult bindingResult, @RequestParam Long userId) {
        log.info("Add student education request received: userId={}, payload={}", userId, dto);
        if (bindingResult.hasErrors()) {
            log.error("Validation errors in addStudentEducation: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult), "Validation failed", 400));
        }
        try {
            StudentEducationResponseDto response = studentEducationService.addStudentEducation(dto, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiSuccessResponse<>(response, "Student education added successfully", 201));
        } catch (Exception e) {
            log.error("Error in addStudentEducation", e);
            return ResponseEntity.internalServerError().body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateStudentEducation(@RequestBody @Valid StudentEducationRequestDto dto, @RequestParam Long educationId, BindingResult bindingResult) {
        log.info("Update student education request: educationId={}, payload={}", educationId, dto);
        if (bindingResult.hasErrors()) {
            log.error("Validation errors in updateStudentEducation: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult), "Validation failed", 400));
        }
        try {
            StudentEducationResponseDto response = studentEducationService.updateStudentEducation(dto, educationId);
            return ResponseEntity.ok(new ApiSuccessResponse<>(response, "Student education updated successfully", 200));
        } catch (Exception e) {
            log.error("Error in updateStudentEducation", e);
            return ResponseEntity.internalServerError().body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStudentEducation(@RequestParam Long educationId) {
        log.info("Delete student education request: educationId={}", educationId);
        try {
            studentEducationService.deleteStudentEducation(educationId);
            return ResponseEntity.ok(new ApiSuccessResponse<>(null, "Student education deleted successfully", 200));
        } catch (Exception e) {
            log.error("Error in deleteStudentEducation", e);
            return ResponseEntity.internalServerError().body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getStudentEducation(@RequestParam Long userId) {
        log.info("Get student education request: userId={}", userId);
        try {
            List<StudentEducationResponseDto> result = studentEducationService.getStudentEducation(userId);
            return ResponseEntity.ok(new ApiSuccessResponse<>(result, "Student education fetched successfully", 200));
        } catch (Exception e) {
            log.error("Error in getStudentEducation", e);
            return ResponseEntity.internalServerError().body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 500));
        }
    }
}
