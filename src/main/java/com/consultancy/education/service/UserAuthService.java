package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.userAuth.UserAuthLoginRequestDto;
import com.consultancy.education.DTOs.requestDTOs.userAuth.UserAuthSignUpRequestDto;
import com.consultancy.education.DTOs.responseDTOs.userAuth.UserAuthLoginResponseDto;
import jakarta.validation.Valid;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;

public interface UserAuthService {
    String signup(@Valid UserAuthSignUpRequestDto userAuthSignUpRequestDto);

    UserAuthLoginResponseDto login(@Valid UserAuthLoginRequestDto userAuthLoginRequestDto);

    String resendVerificationCode(String email);

    String confirmVerificationCode(String email, String verificationCode);
}
