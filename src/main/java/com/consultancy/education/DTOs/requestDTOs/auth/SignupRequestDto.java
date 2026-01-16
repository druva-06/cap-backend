package com.consultancy.education.DTOs.requestDTOs.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SignupRequestDto {

    @NotBlank(message = "Invitation token is required")
    @JsonProperty("invitation_token")
    String invitationToken;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
    String password;

    // Username is optional - will be auto-generated from email if not provided
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("phone_number")
    @Pattern(regexp = "^$|^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    String phoneNumber;
}
