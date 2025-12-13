package com.consultancy.education.controller;

import com.consultancy.education.DTOs.requestDTOs.lead.LeadFilterDto;
import com.consultancy.education.DTOs.requestDTOs.lead.LeadRequestDto;
import com.consultancy.education.DTOs.requestDTOs.lead.UpdateLeadRequestDto;
import com.consultancy.education.DTOs.responseDTOs.lead.LeadPageResponseDto;
import com.consultancy.education.DTOs.responseDTOs.lead.LeadResponseDto;
import com.consultancy.education.exception.CustomException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.response.ApiFailureResponse;
import com.consultancy.education.response.ApiSuccessResponse;
import com.consultancy.education.service.LeadService;
import com.consultancy.education.utils.ToMap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("leads")
@Tag(name = "Lead Management", description = "APIs for managing leads in the system")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/add")
    @Operation(summary = "Create a new lead", description = "Creates a new lead with encrypted sensitive information")
    public ResponseEntity<?> createLead(
            @RequestBody @Valid LeadRequestDto leadRequestDto,
            BindingResult bindingResult) {

        log.info("Create lead request received for email: {}", leadRequestDto.getEmail());

        // Validate request
        if (bindingResult.hasErrors()) {
            log.error("Validation errors in lead creation request");
            Map<String, String> errors = ToMap.bindingResultToMap(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(errors, "Validation failed", 400));
        }

        try {
            // Get authenticated user email from SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = getEmailFromAuthentication(authentication);

            // Create lead
            LeadResponseDto response = leadService.createLead(leadRequestDto, userEmail);

            log.info("Lead created successfully with ID: {}", response.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiSuccessResponse<>(response, "Lead created successfully", 201));

        } catch (CustomException e) {
            log.error("Custom exception during lead creation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 400));

        } catch (NotFoundException e) {
            log.error("Not found exception during lead creation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));

        } catch (Exception e) {
            log.error("Unexpected error during lead creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Internal server error", 500));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COUNSELOR')")
    @GetMapping
    @Operation(summary = "Get leads with filters and pagination", description = "Retrieves leads based on filter criteria with pagination support")
    public ResponseEntity<?> getLeads(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String campaign,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) Integer scoreFrom,
            @RequestParam(required = false) Integer scoreTo,
            @RequestParam(required = false) List<String> status,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("Get leads request - search: {}, campaign: {}, page: {}, size: {}",
                search, campaign, page, size);

        try {
            // Build filter DTO
            LeadFilterDto filterDto = LeadFilterDto.builder()
                    .search(search)
                    .campaign(campaign)
                    .dateFrom(dateFrom != null ? java.time.LocalDate.parse(dateFrom) : null)
                    .dateTo(dateTo != null ? java.time.LocalDate.parse(dateTo) : null)
                    .scoreFrom(scoreFrom)
                    .scoreTo(scoreTo)
                    .status(status != null ? status.stream()
                            .map(s -> com.consultancy.education.enums.LeadStatus.valueOf(s))
                            .collect(java.util.stream.Collectors.toList()) : null)
                    .tags(tags)
                    .assignedTo(assignedTo)
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();

            // Get leads
            LeadPageResponseDto response = leadService.getLeads(filterDto);

            log.info("Successfully fetched {} leads", response.getTotalElements());
            return ResponseEntity.ok(new ApiSuccessResponse<>(response, "Leads fetched successfully", 200));

        } catch (Exception e) {
            log.error("Error fetching leads: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Error fetching leads: " + e.getMessage(), 500));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COUNSELOR')")
    @GetMapping("/{id}")
    @Operation(summary = "Get lead by ID", description = "Retrieves complete information of a lead by its ID")
    public ResponseEntity<?> getLeadById(@PathVariable Long id) {
        log.info("Get lead by ID request - ID: {}", id);

        try {
            LeadResponseDto response = leadService.getLeadById(id);
            log.info("Successfully fetched lead with ID: {}", id);
            return ResponseEntity.ok(new ApiSuccessResponse<>(response, "Lead fetched successfully", 200));

        } catch (NotFoundException e) {
            log.error("Lead not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));

        } catch (Exception e) {
            log.error("Error fetching lead by ID: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Error fetching lead: " + e.getMessage(), 500));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COUNSELOR')")
    @PutMapping("/{id}")
    @Operation(summary = "Update lead by ID", description = "Updates lead information. Email and phone number cannot be changed.")
    public ResponseEntity<?> updateLead(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLeadRequestDto updateLeadRequestDto,
            BindingResult bindingResult) {

        log.info("Update lead request received for ID: {}", id);

        // Validate request
        if (bindingResult.hasErrors()) {
            log.error("Validation errors in lead update request");
            Map<String, String> errors = ToMap.bindingResultToMap(bindingResult);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(errors, "Validation failed", 400));
        }

        try {
            // Get authenticated user email from SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = getEmailFromAuthentication(authentication);

            // Update lead
            LeadResponseDto response = leadService.updateLead(id, updateLeadRequestDto, userEmail);

            log.info("Lead updated successfully with ID: {}", response.getId());
            return ResponseEntity.ok()
                    .body(new ApiSuccessResponse<>(response, "Lead updated successfully", 200));

        } catch (NotFoundException e) {
            log.error("Not found exception during lead update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 404));

        } catch (CustomException e) {
            log.error("Custom exception during lead update: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), e.getMessage(), 400));

        } catch (Exception e) {
            log.error("Unexpected error during lead update: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Internal server error", 500));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COUNSELOR')")
    @GetMapping("/count")
    @Operation(summary = "Get total leads count", description = "Returns the total number of leads in the system")
    public ResponseEntity<?> getTotalLeadsCount() {
        log.info("Get total leads count request received");

        try {
            Long count = leadService.countTotalLeads();
            log.info("Successfully retrieved total leads count: {}", count);
            return ResponseEntity.ok(
                    new ApiSuccessResponse<>(count, "Total leads count retrieved successfully", 200));
        } catch (Exception e) {
            log.error("Error counting total leads: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Error counting leads: " + e.getMessage(), 500));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COUNSELOR')")
    @GetMapping("/status-counts")
    @Operation(summary = "Get lead counts by status", description = "Returns the count of leads grouped by status")
    public ResponseEntity<?> getLeadStatusCounts() {
        log.info("Get lead status counts request received");

        try {
            var statusCounts = leadService.getLeadStatusCounts();
            log.info("Successfully retrieved lead status counts");
            return ResponseEntity.ok(
                    new ApiSuccessResponse<>(statusCounts, "Lead status counts retrieved successfully", 200));
        } catch (Exception e) {
            log.error("Error fetching lead status counts: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiFailureResponse<>(new ArrayList<>(), "Error fetching status counts: " + e.getMessage(),
                            500));
        }
    }

    /**
     * Extract user email from authentication context
     */
    private String getEmailFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            log.error("Authentication is null or has no principal");
            throw new CustomException("User not authenticated");
        }

        String email = authentication.getName();
        log.debug("Extracted email from authentication: {}", email);
        return email;
    }
}
