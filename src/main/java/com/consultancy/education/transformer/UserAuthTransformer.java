package com.consultancy.education.transformer;

import com.consultancy.education.DTOs.responseDTOs.userAuth.UserAuthLoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

@Slf4j
public class UserAuthTransformer {
    public static UserAuthLoginResponseDto toLoginResDto(InitiateAuthResponse authResponse) {
        log.info("UserAuthTransformer toLoginResDto");
        return UserAuthLoginResponseDto.builder()
                .accessToken(authResponse.authenticationResult().accessToken())
                .idToken(authResponse.authenticationResult().idToken())
                .refreshToken(authResponse.authenticationResult().refreshToken())
                .expiresIn(authResponse.authenticationResult().expiresIn())
                .tokenType(authResponse.authenticationResult().tokenType())
                .build();
    }
}
