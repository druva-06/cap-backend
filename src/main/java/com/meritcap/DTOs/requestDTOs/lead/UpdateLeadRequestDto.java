package com.meritcap.DTOs.requestDTOs.lead;

import com.meritcap.enums.LeadStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Lead update request DTO - Email and Phone are not editable")
public class UpdateLeadRequestDto {

    // Personal Information (Editable)
    @NotBlank(message = "First name is required")
    @Schema(description = "First name of the lead")
    String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Last name of the lead")
    String lastName;

    @Schema(description = "Country of residence")
    String country;

    // Lead Management
    @Schema(description = "Lead status: HOT, WARM, COLD, CONVERTED, CLOSED")
    LeadStatus status;

    @Schema(description = "Lead score (0-100)")
    Integer score;

    @Schema(description = "Source of the lead (e.g., Website, Referral, Social Media)")
    String leadSource;

    @Schema(description = "Campaign name")
    String campaign;

    // Preferences (Plain text - for filtering)
    @Schema(description = "Comma-separated list of preferred countries")
    String preferredCountries;

    @Schema(description = "Comma-separated list of preferred courses")
    String preferredCourses;

    @Schema(description = "Budget range")
    String budgetRange;

    @Schema(description = "Intake period")
    String intake;

    @Schema(description = "Tags for categorization")
    List<String> tags;

    // Assignment
    @Schema(description = "User ID of counselor to assign this lead to")
    Long assignedTo;

    // Encrypted Data (Frontend encrypts these before sending)
    @Schema(description = "Encrypted JSON string containing: alternate phone, DOB, gender, address, city, state, pincode")
    String encryptedPersonalDetails;

    @Schema(description = "Encrypted JSON string containing: education level, degree, university, CGPA, year, work exp, test scores")
    String encryptedAcademicDetails;

    @Schema(description = "Encrypted JSON string containing: preferred college, additional notes")
    String encryptedPreferences;
}
