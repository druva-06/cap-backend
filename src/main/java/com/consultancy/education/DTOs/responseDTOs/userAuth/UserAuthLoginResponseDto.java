package com.consultancy.education.DTOs.responseDTOs.userAuth;

import com.consultancy.education.DTOs.responseDTOs.user.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Login response DTO containing authentication tokens and user details.")
public class UserAuthLoginResponseDto {
    @Schema(description = "JWT ID token issued by AWS Cognito", example = "eyJraWQiOiJLTl...")
    String idToken;

    @Schema(description = "JWT access token used for API authorization", example = "eyJhbGciOiJIUzI1...")
    String accessToken;

    @Schema(description = "JWT refresh token used to obtain new access tokens", example = "eyJjdHkiOiJKV1Q...")
    String refreshToken;

    @Schema(description = "Type of token, usually 'Bearer'", example = "Bearer")
    String tokenType;

    @Schema(description = "Expiration time of the token in seconds", example = "3600")
    Integer expiresIn;

    @Schema(description = "User response DTO containing user details")
    UserResponseDto user;

    @Schema(description = "User permissions including role and additional permissions")
    UserPermissionInfo permissions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UserPermissionInfo {
        @Schema(description = "User's role name", example = "ADMIN")
        String roleName;

        @Schema(description = "All permissions (role + additional)", example = "[\"LEAD_VIEW_ALL\", \"APPLICATION_APPROVE\"]")
        List<String> allPermissions;

        @Schema(description = "Permissions grouped by category")
        Map<String, List<String>> categories;
    }
}
