package com.meritcap.DTOs.responseDTOs.invitation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvitationResponseDto {
    Long id;
    String email;
    String username;
    String firstName;
    String lastName;
    String phoneNumber;
    String roleName;
    String roleDisplayName;
    String invitationToken;
    String status;
    String invitedByName;
    Long invitedByUserId;
    LocalDateTime invitedAt;
    LocalDateTime expiresAt;
    LocalDateTime activatedAt;
    String cognitoUserId;
    Long userId;
    boolean isExpired;
    boolean canResend;
    boolean canRevoke;

    // Email details (for dev mode)
    EmailDetailsDto emailDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmailDetailsDto {
        String recipientEmail;
        String subject;
        String signupLink;
        boolean sent;
    }
}
