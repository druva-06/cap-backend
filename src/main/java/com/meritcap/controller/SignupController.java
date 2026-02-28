package com.meritcap.controller;

import com.meritcap.DTOs.requestDTOs.auth.SignupRequestDto;
import com.meritcap.DTOs.requestDTOs.auth.StudentSignupRequestDto;
import com.meritcap.DTOs.responseDTOs.auth.SignupResponseDto;
import com.meritcap.exception.BadRequestException;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.SignupService;
import com.meritcap.utils.ToMap;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    /**
     * Direct student signup (Public endpoint - no invitation required)
     * POST /api/auth/signup/student
     */
    @PostMapping("/signup/student")
    public ResponseEntity<?> signupStudent(
            @RequestBody @Valid StudentSignupRequestDto studentSignupRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult),
                            "Validation failed", 400));
        }

        try {
            SignupResponseDto response = signupService.signupStudent(studentSignupRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiSuccessResponse<>(response,
                            "Student account created successfully", 201));

        } catch (BadRequestException e) {
            log.error("Student signup validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 400));
        } catch (Exception e) {
            log.error("Student signup error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Internal server error during signup", 500));
        }
    }

    /**
     * User signup with invitation token (Public endpoint - for admin-invited users)
     * POST /api/auth/signup/invited
     */
    @PostMapping("/signup/invited")
    public ResponseEntity<?> signupWithInvitation(
            @RequestBody @Valid SignupRequestDto signupRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(ToMap.bindingResultToMap(bindingResult),
                            "Validation failed", 400));
        }

        try {
            SignupResponseDto response = signupService.signupWithInvitation(signupRequestDto);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiSuccessResponse<>(response,
                            "Account created successfully", 201));

        } catch (BadRequestException e) {
            log.error("Invitation signup validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 400));
        } catch (Exception e) {
            log.error("Invitation signup error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(),
                            "Internal server error during signup", 500));
        }
    }
}
