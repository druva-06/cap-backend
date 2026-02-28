package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.auth.SignupRequestDto;
import com.meritcap.DTOs.requestDTOs.auth.StudentSignupRequestDto;
import com.meritcap.DTOs.responseDTOs.auth.SignupResponseDto;

public interface SignupService {

    /**
     * Direct student signup (no invitation required)
     * This will:
     * 1. Create Cognito account
     * 2. Create user in database with STUDENT role
     * 3. Return signup response
     * 
     * @param studentSignupRequestDto Student signup form data
     * @return SignupResponseDto with created user details
     */
    SignupResponseDto signupStudent(StudentSignupRequestDto studentSignupRequestDto);

    /**
     * Complete user signup with invitation token (for admin-invited users)
     * This will:
     * 1. Validate invitation token
     * 2. Create Cognito account
     * 3. Create user in database with pre-assigned role
     * 4. Activate invitation
     * 
     * @param signupRequestDto Signup form data with invitation token
     * @return SignupResponseDto with created user details
     */
    SignupResponseDto signupWithInvitation(SignupRequestDto signupRequestDto);
}
