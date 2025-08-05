package com.consultancy.education.controller;

import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.AddRemarksRequestDto;
import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.AssignCounselorRequestDto;
import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.RegistrationDecisionRequestDto;
import com.consultancy.education.DTOs.requestDTOs.studentCollegeCourseRegistration.RegistrationStatisticsDto;
import com.consultancy.education.DTOs.responseDTOs.studentCollegeCourseRegistration.StudentCollegeCourseRegistrationResponseDto;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.response.ApiFailureResponse;
import com.consultancy.education.response.ApiSuccessResponse;
import com.consultancy.education.service.AdminStudentCollegeCourseRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/student-college-course-registration")
@RequiredArgsConstructor
public class AdminStudentCollegeCourseRegistrationController {

    private final AdminStudentCollegeCourseRegistrationService adminStudentCollegeCourseRegistrationService;

    @GetMapping
    public ResponseEntity<?> listRegistrations(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long collegeId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Integer intakeYear,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo
    ) {
        log.info("Admin: Listing registrations with filters - status={}, studentId={}, collegeId={}, courseId={}, intakeYear={}, dateFrom={}, dateTo={}",
                status, studentId, collegeId, courseId, intakeYear, dateFrom, dateTo);

        try {
            List<StudentCollegeCourseRegistrationResponseDto> registrations =
                    adminStudentCollegeCourseRegistrationService.listRegistrations(
                            status, studentId, collegeId, courseId, intakeYear, dateFrom, dateTo
                    );
            log.info("Admin: {} registrations fetched", registrations.size());
            return ResponseEntity.ok(new ApiSuccessResponse<>(registrations, "Registrations fetched", 200));
        } catch (Exception e) {
            log.error("Admin: Error fetching registrations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(null, "Server error", 500));
        }
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<?> getRegistrationById(@PathVariable Long registrationId) {
        log.info("Admin: GET /{} called", registrationId);
        try {
            StudentCollegeCourseRegistrationResponseDto response =
                    adminStudentCollegeCourseRegistrationService.getRegistrationById(registrationId);
            log.info("Admin: Registration details fetched for id={}", registrationId);
            return ResponseEntity.ok(
                    new ApiSuccessResponse<>(response, "Registration details fetched", 200)
            );
        } catch (NotFoundException e) {
            log.warn("Admin: Registration not found, id={}", registrationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(null, e.getMessage(), 404));
        } catch (Exception e) {
            log.error("Admin: Error fetching registration id={}", registrationId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(null, "Server error", 500));
        }
    }

    @PostMapping("/assign-counselor")
    public ResponseEntity<?> assignCounselor(@RequestBody @Valid AssignCounselorRequestDto requestDto) {
        log.info("Admin: Assign counselor - registrationId={}, counselorId={}", requestDto.getRegistrationId(), requestDto.getCounselorId());
        try {
            StudentCollegeCourseRegistrationResponseDto response =
                    adminStudentCollegeCourseRegistrationService.assignCounselor(requestDto);
            log.info("Admin: Counselor assigned successfully to registrationId={}", requestDto.getRegistrationId());
            return ResponseEntity.ok(
                    new ApiSuccessResponse<>(response, "Counselor assigned successfully", 200)
            );
        } catch (NotFoundException e) {
            log.warn("Admin: Assign counselor failed - entity not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(null, e.getMessage(), 404));
        } catch (Exception e) {
            log.error("Admin: Error assigning counselor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(null, "Server error", 500));
        }
    }

    @PostMapping("/decision")
    public ResponseEntity<?> decideOnRegistration(@RequestBody @Valid RegistrationDecisionRequestDto requestDto) {
        log.info("Admin: Decision API called for registrationId={}, decision={}", requestDto.getRegistrationId(), requestDto.getDecision());
        try {
            StudentCollegeCourseRegistrationResponseDto response =
                    adminStudentCollegeCourseRegistrationService.decideOnRegistration(requestDto);
            log.info("Admin: Registration {} successfully for id={}", requestDto.getDecision(), requestDto.getRegistrationId());
            return ResponseEntity.ok(
                    new ApiSuccessResponse<>(response, "Registration " + requestDto.getDecision().toLowerCase(), 200)
            );
        } catch (NotFoundException e) {
            log.warn("Admin: Registration not found for decision, id={}", requestDto.getRegistrationId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(null, e.getMessage(), 404));
        } catch (IllegalStateException | IllegalArgumentException e) {
            log.warn("Admin: Decision failed - {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiFailureResponse<>(null, e.getMessage(), 409));
        } catch (Exception e) {
            log.error("Admin: Error deciding on registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(null, "Server error", 500));
        }
    }

    @PostMapping("/add-remarks")
    public ResponseEntity<?> addRemarks(@RequestBody @Valid AddRemarksRequestDto requestDto) {
        log.info("Admin: Add remarks API called for registrationId={}", requestDto.getRegistrationId());
        try {
            StudentCollegeCourseRegistrationResponseDto response =
                    adminStudentCollegeCourseRegistrationService.addRemarks(requestDto);
            log.info("Admin: Remarks added for registrationId={}", requestDto.getRegistrationId());
            return ResponseEntity.ok(
                    new ApiSuccessResponse<>(response, "Remarks added successfully", 200)
            );
        } catch (NotFoundException e) {
            log.warn("Admin: Registration not found for adding remarks, id={}", requestDto.getRegistrationId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(null, e.getMessage(), 404));
        } catch (Exception e) {
            log.error("Admin: Error adding remarks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(null, "Server error", 500));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStatistics() {
        log.info("Admin: Fetching registration statistics");
        try {
            RegistrationStatisticsDto stats = adminStudentCollegeCourseRegistrationService.getStatistics();
            log.info("Admin: Statistics fetched successfully");
            return ResponseEntity.ok(new ApiSuccessResponse<>(stats, "Statistics fetched", 200));
        } catch (Exception e) {
            log.error("Admin: Error fetching statistics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(null, "Server error", 500));
        }
    }
}
