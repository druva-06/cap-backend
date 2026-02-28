package com.meritcap.controller;

import com.meritcap.DTOs.responseDTOs.document.DocumentResponseDto;
import com.meritcap.response.ApiFailureResponse;
import com.meritcap.response.ApiSuccessResponse;
import com.meritcap.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileImageController {

    private final DocumentService documentService;

    /**
     * Upload or replace student profile image.
     * Delegates to DocumentService.uploadProfileImage(...) which handles:
     * - uploading to S3
     * - creating a Document record (documentType = PROFILE_IMAGE, category = PROFILE)
     * - updating users.profile_picture and Cognito picture attribute atomically
     */
    @PostMapping("/image")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> uploadProfileImage(
            @RequestPart("file") MultipartFile file,
            Principal principal) {
        String principalName = principal != null ? principal.getName() : null;
        log.info("Profile image upload request received by principal={}", principalName);

        if (file == null || file.isEmpty()) {
            log.warn("Validation error: profile image file missing for principal={}", principalName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "File is required", 400));
        }

        try {
            // Delegate to DocumentService which contains uploadProfileImage method
            DocumentResponseDto responseDto = documentService.uploadProfileImage(file, principalName);
            log.info("Profile image uploaded successfully for principal={}", principalName);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiSuccessResponse<>(responseDto, "Profile image uploaded successfully", 201));
        } catch (IllegalArgumentException iae) {
            log.warn("Profile image validation failed for principal={}: {}", principalName, iae.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), iae.getMessage(), 400));
        } catch (com.meritcap.exception.ValidationException ve) {
            log.warn("Profile image validation exception for principal={}: {}", principalName, ve.getErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(ve.getErrors(), ve.getMessage(), 400));
        } catch (com.meritcap.exception.NotFoundException nfe) {
            log.warn("Profile image upload failed - user not found for principal={}", principalName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), nfe.getMessage(), 404));
        } catch (Exception ex) {
            log.error("Error while uploading profile image for principal={}", principalName, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), ex.getMessage(), 500));
        }
    }
}
