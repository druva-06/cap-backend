package com.meritcap.DTOs.requestDTOs.invitation;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvitationRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Role name is required")
    String roleName;

    // Optional pre-fill data
    String username;

    String firstName;

    String lastName;

    @Pattern(regexp = "^$|^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    String phoneNumber;

    // Invitation expiry in days (default: 7 days)
    @Min(value = 1, message = "Expiry days must be at least 1")
    @Max(value = 30, message = "Expiry days cannot exceed 30")
    Integer expiryDays;
}
