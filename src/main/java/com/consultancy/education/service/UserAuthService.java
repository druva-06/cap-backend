package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.userAuth.ChangePasswordRequestDto;
import com.consultancy.education.DTOs.requestDTOs.userAuth.UserAuthLoginRequestDto;
import com.consultancy.education.DTOs.requestDTOs.userAuth.UserAuthRefreshRequestDto;
import com.consultancy.education.DTOs.requestDTOs.userAuth.UserAuthSignUpRequestDto;
import com.consultancy.education.DTOs.responseDTOs.userAuth.UserAuthLoginResponseDto;
import com.consultancy.education.DTOs.responseDTOs.userAuth.UserAuthRefreshResponseDto;
import jakarta.validation.Valid;

public interface UserAuthService {
    String signup(@Valid UserAuthSignUpRequestDto userAuthSignUpRequestDto);

    UserAuthLoginResponseDto login(@Valid UserAuthLoginRequestDto userAuthLoginRequestDto);

    String resendVerificationCode(String email);

    String confirmVerificationCode(String email, String verificationCode);

    String forgotPassword(String email);

    String confirmForgotPassword(String email, String confirmationCode, String newPassword);

    String changePassword(String jwtToken, @Valid ChangePasswordRequestDto changePasswordRequestDto);

    UserAuthRefreshResponseDto refreshAccessToken(UserAuthRefreshRequestDto refreshTokenRequestDto);

}
