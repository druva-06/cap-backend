package com.meritcap.DTOs.responseDTOs.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CognitoUserResponseDto {
    String cognitoUserId;
    String username;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    String userStatus;
    boolean emailVerified;
    boolean phoneVerified;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
