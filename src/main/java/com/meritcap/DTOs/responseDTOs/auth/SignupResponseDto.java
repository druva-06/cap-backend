package com.meritcap.DTOs.responseDTOs.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SignupResponseDto {
    Long userId;
    String cognitoUserId;
    String username;
    String email;
    String firstName;
    String lastName;
    String roleName;
    String message;
    boolean requiresEmailVerification;

    // Email details (for dev mode)
    WelcomeEmailDetailsDto welcomeEmailDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WelcomeEmailDetailsDto {
        String recipientEmail;
        String subject;
        boolean sent;
    }
}
