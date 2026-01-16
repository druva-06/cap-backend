package com.consultancy.education.DTOs.responseDTOs.invitation;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ValidationResponseDto {
    boolean valid;
    String message;
    String email;
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    String roleName;
    String invitationToken;
    Long invitationId;
}
