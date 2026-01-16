package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.auth.SignupRequestDto;
import com.consultancy.education.DTOs.requestDTOs.auth.StudentSignupRequestDto;
import com.consultancy.education.DTOs.responseDTOs.auth.CognitoUserResponseDto;
import com.consultancy.education.DTOs.responseDTOs.auth.SignupResponseDto;
import com.consultancy.education.DTOs.responseDTOs.invitation.ValidationResponseDto;
import com.consultancy.education.exception.BadRequestException;
import com.consultancy.education.model.InvitedUser;
import com.consultancy.education.model.Role;
import com.consultancy.education.model.User;
import com.consultancy.education.repository.InvitedUserRepository;
import com.consultancy.education.repository.RoleRepository;
import com.consultancy.education.repository.UserRepository;
import com.consultancy.education.service.CognitoService;
import com.consultancy.education.service.EmailService;
import com.consultancy.education.service.InvitationService;
import com.consultancy.education.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SignupServiceImpl implements SignupService {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private CognitoService cognitoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitedUserRepository invitedUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public SignupResponseDto signupStudent(StudentSignupRequestDto studentSignupRequestDto) {
        log.info("Processing direct student signup for email: {}", studentSignupRequestDto.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(studentSignupRequestDto.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        // Get STUDENT role
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new BadRequestException("STUDENT role not found in system"));

        try {
            // Parse full name
            String[] nameParts = studentSignupRequestDto.getFullName().trim().split("\\s+", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            // Create Cognito user
            CognitoUserResponseDto cognitoUser = cognitoService.createUser(
                    studentSignupRequestDto.getEmail(),
                    studentSignupRequestDto.getPassword(),
                    firstName,
                    lastName,
                    studentSignupRequestDto.getPhoneNumber());
            log.info("Cognito user created successfully: {}", cognitoUser.getCognitoUserId());

            // Generate username from email
            String username = studentSignupRequestDto.getEmail().split("@")[0];

            // Create user in database
            User user = User.builder()
                    .email(studentSignupRequestDto.getEmail())
                    .username(username)
                    .firstName(firstName)
                    .lastName(lastName)
                    .phoneNumber(studentSignupRequestDto.getPhoneNumber())
                    .role(studentRole)
                    .profilePicture(null)
                    .failedLoginAttempts(0)
                    .accountLocked(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(user);
            log.info("Student user created in database with ID: {}", savedUser.getId());

            // Send welcome email
            EmailService.EmailDetails welcomeEmail = null;
            try {
                welcomeEmail = emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());
            } catch (Exception emailError) {
                log.error("Failed to send welcome email to: {}", savedUser.getEmail(), emailError);
                // Don't fail signup if email fails
            }

            // Build response
            SignupResponseDto.SignupResponseDtoBuilder responseBuilder = SignupResponseDto.builder()
                    .userId(savedUser.getId())
                    .cognitoUserId(cognitoUser.getCognitoUserId())
                    .username(savedUser.getUsername())
                    .email(savedUser.getEmail())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .roleName(savedUser.getRole().getName())
                    .message("Student account created successfully. You can now log in.")
                    .requiresEmailVerification(false);

            // Add email details if available (for dev mode)
            if (welcomeEmail != null) {
                responseBuilder.welcomeEmailDetails(SignupResponseDto.WelcomeEmailDetailsDto.builder()
                        .recipientEmail(welcomeEmail.getRecipientEmail())
                        .subject(welcomeEmail.getSubject())
                        .sent(welcomeEmail.isSent())
                        .build());
            }

            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error during student signup process", e);

            // Cleanup: Try to delete Cognito user if database save failed
            try {
                cognitoService.deleteUser(studentSignupRequestDto.getEmail());
                log.info("Cleaned up Cognito user after signup failure");
            } catch (Exception cleanupError) {
                log.error("Failed to cleanup Cognito user", cleanupError);
            }

            if (e instanceof BadRequestException) {
                throw e;
            }
            throw new BadRequestException("Student signup failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public SignupResponseDto signupWithInvitation(SignupRequestDto signupRequestDto) {
        log.info("Processing signup for email: {}", signupRequestDto.getEmail());

        // Step 1: Validate invitation token
        ValidationResponseDto validation = invitationService.validateInvitationToken(
                signupRequestDto.getInvitationToken());

        if (!validation.isValid()) {
            throw new BadRequestException(validation.getMessage());
        }

        // Step 2: Verify email matches invitation
        if (!validation.getEmail().equalsIgnoreCase(signupRequestDto.getEmail())) {
            throw new BadRequestException("Email does not match invitation");
        }

        // Step 3: Check if user already exists in database
        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        // Step 4: Generate username from email if not provided
        String username = signupRequestDto.getUsername();
        if (username == null || username.trim().isEmpty()) {
            // Generate random username: first part of email + random 4 digits
            String emailPrefix = signupRequestDto.getEmail().split("@")[0];
            int randomNum = (int) (Math.random() * 10000);
            username = emailPrefix + randomNum;
            log.info("Generated random username: {}", username);

            // If still exists, add more random digits
            int attempts = 0;
            while (userRepository.existsByUsername(username) && attempts < 10) {
                randomNum = (int) (Math.random() * 100000);
                username = emailPrefix + randomNum;
                attempts++;
            }
        }

        // Step 5: Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists");
        }

        try {
            // Step 6: Get invitation details
            InvitedUser invitation = invitedUserRepository.findByInvitationToken(
                    signupRequestDto.getInvitationToken())
                    .orElseThrow(() -> new BadRequestException("Invitation not found"));

            // Step 7: Create Cognito user with username
            CognitoUserResponseDto cognitoUser = cognitoService.createUser(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getPassword(),
                    signupRequestDto.getFirstName() != null ? signupRequestDto.getFirstName()
                            : validation.getFirstName(),
                    signupRequestDto.getLastName() != null ? signupRequestDto.getLastName() : validation.getLastName(),
                    signupRequestDto.getPhoneNumber() != null ? signupRequestDto.getPhoneNumber()
                            : validation.getPhoneNumber());
            log.info("Cognito user created successfully: {}", cognitoUser.getCognitoUserId());

            // Step 8: Create user in database
            User user = User.builder()
                    .email(signupRequestDto.getEmail())
                    .username(username)
                    .firstName(signupRequestDto.getFirstName() != null ? signupRequestDto.getFirstName()
                            : validation.getFirstName())
                    .lastName(signupRequestDto.getLastName() != null ? signupRequestDto.getLastName()
                            : validation.getLastName())
                    .phoneNumber(signupRequestDto.getPhoneNumber() != null ? signupRequestDto.getPhoneNumber()
                            : validation.getPhoneNumber())
                    .role(invitation.getRole())
                    .profilePicture(null)
                    .failedLoginAttempts(0)
                    .accountLocked(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(user);
            log.info("User created in database with ID: {}", savedUser.getId());

            // Step 9: Activate invitation
            invitationService.activateInvitation(
                    signupRequestDto.getInvitationToken(),
                    cognitoUser.getCognitoUserId(),
                    savedUser.getId());
            log.info("Invitation activated successfully");

            // Send welcome email
            EmailService.EmailDetails welcomeEmail = null;
            try {
                welcomeEmail = emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());
            } catch (Exception emailError) {
                log.error("Failed to send welcome email to: {}", savedUser.getEmail(), emailError);
                // Don't fail signup if email fails
            }

            // Step 10: Build response
            SignupResponseDto.SignupResponseDtoBuilder responseBuilder = SignupResponseDto.builder()
                    .userId(savedUser.getId())
                    .cognitoUserId(cognitoUser.getCognitoUserId())
                    .username(savedUser.getUsername())
                    .email(savedUser.getEmail())
                    .firstName(savedUser.getFirstName())
                    .lastName(savedUser.getLastName())
                    .roleName(savedUser.getRole().getName())
                    .message("Account created successfully. You can now log in.")
                    .requiresEmailVerification(false);

            // Add email details if available (for dev mode)
            if (welcomeEmail != null) {
                responseBuilder.welcomeEmailDetails(SignupResponseDto.WelcomeEmailDetailsDto.builder()
                        .recipientEmail(welcomeEmail.getRecipientEmail())
                        .subject(welcomeEmail.getSubject())
                        .sent(welcomeEmail.isSent())
                        .build());
            }

            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error during signup process", e);

            // Cleanup: Try to delete Cognito user if database save failed
            try {
                cognitoService.deleteUser(signupRequestDto.getEmail());
                log.info("Cleaned up Cognito user after signup failure");
            } catch (Exception cleanupError) {
                log.error("Failed to cleanup Cognito user", cleanupError);
            }

            if (e instanceof BadRequestException) {
                throw e;
            }
            throw new BadRequestException("Signup failed: " + e.getMessage());
        }
    }
}
