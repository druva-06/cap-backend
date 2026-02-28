package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.auth.SignupRequestDto;
import com.meritcap.DTOs.responseDTOs.auth.CognitoUserResponseDto;

public interface CognitoService {

    /**
     * Create a new user in AWS Cognito User Pool
     * 
     * @param email       User email (also used as username)
     * @param password    User password
     * @param firstName   First name
     * @param lastName    Last name
     * @param phoneNumber Phone number (optional)
     * @return CognitoUserResponseDto with Cognito user ID and details
     */
    CognitoUserResponseDto createUser(String email, String password, String firstName, String lastName,
            String phoneNumber);

    /**
     * Sign up a new user using invitation token
     * 
     * @param signupRequestDto Signup form data including invitation token
     * @return CognitoUserResponseDto with created user details
     */
    CognitoUserResponseDto signUpWithInvitation(SignupRequestDto signupRequestDto);

    /**
     * Verify user email in Cognito
     * 
     * @param username Username/email
     */
    void verifyUserEmail(String username);

    /**
     * Delete user from Cognito (for cleanup/testing)
     * 
     * @param username Username/email to delete
     */
    void deleteUser(String username);

    /**
     * Enable user account in Cognito
     * 
     * @param username Username/email
     */
    void enableUser(String username);

    /**
     * Disable user account in Cognito
     * 
     * @param username Username/email
     */
    void disableUser(String username);

    /**
     * Reset user password
     * 
     * @param username    Username/email
     * @param newPassword New password
     */
    void resetPassword(String username, String newPassword);

    /**
     * Get user details from Cognito
     * 
     * @param username Username/email
     * @return CognitoUserResponseDto with user details
     */
    CognitoUserResponseDto getUserDetails(String username);
}
