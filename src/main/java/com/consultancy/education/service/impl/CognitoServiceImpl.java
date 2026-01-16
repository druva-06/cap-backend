package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.auth.SignupRequestDto;
import com.consultancy.education.DTOs.responseDTOs.auth.CognitoUserResponseDto;
import com.consultancy.education.exception.BadRequestException;
import com.consultancy.education.exception.InternalServerException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.service.CognitoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CognitoServiceImpl implements CognitoService {

    @Value("${aws.cognito.userPoolId:}")
    private String userPoolId;

    @Value("${aws.cognito.region:ap-south-1}")
    private String region;

    @Value("${aws.cognito.accessKeyId:}")
    private String accessKeyId;

    @Value("${aws.cognito.secretAccessKey:}")
    private String secretAccessKey;

    private CognitoIdentityProviderClient cognitoClient;

    @PostConstruct
    public void initCognitoClient() {
        try {
            if (accessKeyId != null && !accessKeyId.isEmpty() &&
                    secretAccessKey != null && !secretAccessKey.isEmpty()) {

                this.cognitoClient = CognitoIdentityProviderClient.builder()
                        .region(Region.of(region))
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                        .build();

                log.info("Cognito client initialized successfully with credentials");
            } else {
                // Use default credentials provider (for EC2, Lambda, etc.)
                this.cognitoClient = CognitoIdentityProviderClient.builder()
                        .region(Region.of(region))
                        .build();

                log.info("Cognito client initialized with default credentials provider");
            }
        } catch (Exception e) {
            log.error("Failed to initialize Cognito client", e);
            throw new InternalServerException("Failed to initialize AWS Cognito service");
        }
    }

    @Override
    public CognitoUserResponseDto createUser(String email, String password, String firstName,
            String lastName, String phoneNumber) {
        log.info("Creating Cognito user for email: {}", email);

        try {
            // Build user attributes
            List<AttributeType> attributes = new ArrayList<>();
            attributes.add(AttributeType.builder()
                    .name("email")
                    .value(email)
                    .build());
            attributes.add(AttributeType.builder()
                    .name("email_verified")
                    .value("true")
                    .build());

            if (firstName != null && !firstName.isEmpty()) {
                attributes.add(AttributeType.builder()
                        .name("given_name")
                        .value(firstName)
                        .build());
            }

            if (lastName != null && !lastName.isEmpty()) {
                attributes.add(AttributeType.builder()
                        .name("family_name")
                        .value(lastName)
                        .build());
            }

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                // Format phone number for Cognito (+countrycode)
                String formattedPhone = phoneNumber.startsWith("+") ? phoneNumber : "+91" + phoneNumber;
                attributes.add(AttributeType.builder()
                        .name("phone_number")
                        .value(formattedPhone)
                        .build());
            }

            // Create admin create user request
            AdminCreateUserRequest createUserRequest = AdminCreateUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .userAttributes(attributes)
                    .temporaryPassword(password)
                    .messageAction(MessageActionType.SUPPRESS) // Don't send welcome email
                    .build();

            AdminCreateUserResponse response = cognitoClient.adminCreateUser(createUserRequest);

            // Set permanent password
            AdminSetUserPasswordRequest setPasswordRequest = AdminSetUserPasswordRequest.builder()
                    .userPoolId(userPoolId)
                    .username(email)
                    .password(password)
                    .permanent(true)
                    .build();

            cognitoClient.adminSetUserPassword(setPasswordRequest);

            log.info("Cognito user created successfully for email: {}", email);

            // Build response
            UserType user = response.user();
            return CognitoUserResponseDto.builder()
                    .cognitoUserId(user.username())
                    .username(email)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phoneNumber(phoneNumber)
                    .userStatus(user.userStatusAsString())
                    .emailVerified(true)
                    .phoneVerified(false)
                    .createdAt(user.userCreateDate() != null
                            ? LocalDateTime.ofInstant(user.userCreateDate(), ZoneId.systemDefault())
                            : null)
                    .build();

        } catch (UsernameExistsException e) {
            log.error("User already exists in Cognito: {}", email);
            throw new BadRequestException("User with this email already exists in Cognito");
        } catch (InvalidPasswordException e) {
            log.error("Invalid password for Cognito user: {}", email, e);
            throw new BadRequestException("Password does not meet requirements: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error creating Cognito user", e);
            throw new InternalServerException("Failed to create user in Cognito: " + e.getMessage());
        }
    }

    @Override
    public CognitoUserResponseDto signUpWithInvitation(SignupRequestDto signupRequestDto) {
        log.info("Signing up user with invitation: {}", signupRequestDto.getEmail());

        return createUser(
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getFirstName(),
                signupRequestDto.getLastName(),
                signupRequestDto.getPhoneNumber());
    }

    @Override
    public void verifyUserEmail(String username) {
        log.info("Verifying email for user: {}", username);

        try {
            AdminUpdateUserAttributesRequest request = AdminUpdateUserAttributesRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .userAttributes(AttributeType.builder()
                            .name("email_verified")
                            .value("true")
                            .build())
                    .build();

            cognitoClient.adminUpdateUserAttributes(request);
            log.info("Email verified successfully for user: {}", username);

        } catch (Exception e) {
            log.error("Error verifying email for user: {}", username, e);
            throw new InternalServerException("Failed to verify email in Cognito");
        }
    }

    @Override
    public void deleteUser(String username) {
        log.info("Deleting Cognito user: {}", username);

        try {
            AdminDeleteUserRequest request = AdminDeleteUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .build();

            cognitoClient.adminDeleteUser(request);
            log.info("Cognito user deleted successfully: {}", username);

        } catch (UserNotFoundException e) {
            log.warn("User not found in Cognito: {}", username);
            throw new NotFoundException("User not found in Cognito");
        } catch (Exception e) {
            log.error("Error deleting Cognito user", e);
            throw new InternalServerException("Failed to delete user from Cognito");
        }
    }

    @Override
    public void enableUser(String username) {
        log.info("Enabling Cognito user: {}", username);

        try {
            AdminEnableUserRequest request = AdminEnableUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .build();

            cognitoClient.adminEnableUser(request);
            log.info("Cognito user enabled successfully: {}", username);

        } catch (Exception e) {
            log.error("Error enabling Cognito user", e);
            throw new InternalServerException("Failed to enable user in Cognito");
        }
    }

    @Override
    public void disableUser(String username) {
        log.info("Disabling Cognito user: {}", username);

        try {
            AdminDisableUserRequest request = AdminDisableUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .build();

            cognitoClient.adminDisableUser(request);
            log.info("Cognito user disabled successfully: {}", username);

        } catch (Exception e) {
            log.error("Error disabling Cognito user", e);
            throw new InternalServerException("Failed to disable user in Cognito");
        }
    }

    @Override
    public void resetPassword(String username, String newPassword) {
        log.info("Resetting password for user: {}", username);

        try {
            AdminSetUserPasswordRequest request = AdminSetUserPasswordRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .password(newPassword)
                    .permanent(true)
                    .build();

            cognitoClient.adminSetUserPassword(request);
            log.info("Password reset successfully for user: {}", username);

        } catch (InvalidPasswordException e) {
            log.error("Invalid password for user: {}", username, e);
            throw new BadRequestException("Password does not meet requirements");
        } catch (Exception e) {
            log.error("Error resetting password", e);
            throw new InternalServerException("Failed to reset password in Cognito");
        }
    }

    @Override
    public CognitoUserResponseDto getUserDetails(String username) {
        log.info("Getting Cognito user details: {}", username);

        try {
            AdminGetUserRequest request = AdminGetUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .build();

            AdminGetUserResponse response = cognitoClient.adminGetUser(request);

            // Extract attributes
            String email = null;
            String firstName = null;
            String lastName = null;
            String phoneNumber = null;
            boolean emailVerified = false;

            for (AttributeType attr : response.userAttributes()) {
                switch (attr.name()) {
                    case "email":
                        email = attr.value();
                        break;
                    case "given_name":
                        firstName = attr.value();
                        break;
                    case "family_name":
                        lastName = attr.value();
                        break;
                    case "phone_number":
                        phoneNumber = attr.value();
                        break;
                    case "email_verified":
                        emailVerified = "true".equalsIgnoreCase(attr.value());
                        break;
                }
            }

            return CognitoUserResponseDto.builder()
                    .cognitoUserId(response.username())
                    .username(username)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phoneNumber(phoneNumber)
                    .userStatus(response.userStatusAsString())
                    .emailVerified(emailVerified)
                    .createdAt(response.userCreateDate() != null
                            ? LocalDateTime.ofInstant(response.userCreateDate(), ZoneId.systemDefault())
                            : null)
                    .build();

        } catch (UserNotFoundException e) {
            log.warn("User not found in Cognito: {}", username);
            throw new NotFoundException("User not found in Cognito");
        } catch (Exception e) {
            log.error("Error getting Cognito user details", e);
            throw new InternalServerException("Failed to get user details from Cognito");
        }
    }
}
