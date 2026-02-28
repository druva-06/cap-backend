package com.meritcap.service.impl;

import com.meritcap.DTOs.requestDTOs.auth.SignupRequestDto;
import com.meritcap.DTOs.requestDTOs.auth.StudentSignupRequestDto;
import com.meritcap.DTOs.responseDTOs.auth.CognitoUserResponseDto;
import com.meritcap.DTOs.responseDTOs.auth.SignupResponseDto;
import com.meritcap.DTOs.responseDTOs.invitation.ValidationResponseDto;
import com.meritcap.exception.BadRequestException;
import com.meritcap.model.InvitedUser;
import com.meritcap.model.Role;
import com.meritcap.model.User;
import com.meritcap.repository.InvitedUserRepository;
import com.meritcap.repository.RoleRepository;
import com.meritcap.repository.UserRepository;
import com.meritcap.service.CognitoService;
import com.meritcap.service.EmailService;
import com.meritcap.service.InvitationService;
import com.meritcap.service.SignupService;
import jakarta.persistence.EntityManager;
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

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public SignupResponseDto signupStudent(StudentSignupRequestDto studentSignupRequestDto) {
        log.info("Processing direct student signup for email: {}", studentSignupRequestDto.getEmail());

        // Check if user already exists
        if (userRepository.existsByEmail(studentSignupRequestDto.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        // Get STUDENT role — validate before calling Cognito
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new BadRequestException("STUDENT role not found in system"));

        // Track whether Cognito user was created for cleanup
        boolean cognitoUserCreated = false;
        String email = studentSignupRequestDto.getEmail();

        try {
            // Parse full name
            String[] nameParts = studentSignupRequestDto.getFullName().trim().split("\\s+", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            // Step 1: Create Cognito user
            CognitoUserResponseDto cognitoUser = cognitoService.createUser(
                    email,
                    studentSignupRequestDto.getPassword(),
                    firstName,
                    lastName,
                    studentSignupRequestDto.getPhoneNumber());
            cognitoUserCreated = true;
            log.info("Cognito user created successfully: {}", cognitoUser.getCognitoUserId());

            // Generate username from email
            String username = email.split("@")[0];

            // Step 2: Create user in database
            User user = User.builder()
                    .email(email)
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
            // Flush immediately so DB constraint violations surface here, not at commit time
            entityManager.flush();
            log.info("Student user created in database with ID: {}", savedUser.getId());

            // Step 3: Send welcome email (best-effort, don't fail signup)
            EmailService.EmailDetails welcomeEmail = null;
            try {
                welcomeEmail = emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());
            } catch (Exception emailError) {
                log.error("Failed to send welcome email to: {}", savedUser.getEmail(), emailError);
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

            if (welcomeEmail != null) {
                responseBuilder.welcomeEmailDetails(SignupResponseDto.WelcomeEmailDetailsDto.builder()
                        .recipientEmail(welcomeEmail.getRecipientEmail())
                        .subject(welcomeEmail.getSubject())
                        .sent(welcomeEmail.isSent())
                        .build());
            }

            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error during student signup process for {}: {}", email, e.getMessage(), e);

            // Compensating action: delete Cognito user if DB save failed
            if (cognitoUserCreated) {
                deleteCognitoUserQuietly(email);
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
            String emailPrefix = signupRequestDto.getEmail().split("@")[0];
            int randomNum = (int) (Math.random() * 10000);
            username = emailPrefix + randomNum;
            log.info("Generated random username: {}", username);

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

        // Track whether Cognito user was created for cleanup
        boolean cognitoUserCreated = false;
        String email = signupRequestDto.getEmail();

        try {
            // Step 6: Get invitation details
            InvitedUser invitation = invitedUserRepository.findByInvitationToken(
                    signupRequestDto.getInvitationToken())
                    .orElseThrow(() -> new BadRequestException("Invitation not found"));

            // Step 7: Create Cognito user
            CognitoUserResponseDto cognitoUser = cognitoService.createUser(
                    email,
                    signupRequestDto.getPassword(),
                    signupRequestDto.getFirstName() != null ? signupRequestDto.getFirstName()
                            : validation.getFirstName(),
                    signupRequestDto.getLastName() != null ? signupRequestDto.getLastName() : validation.getLastName(),
                    signupRequestDto.getPhoneNumber() != null ? signupRequestDto.getPhoneNumber()
                            : validation.getPhoneNumber());
            cognitoUserCreated = true;
            log.info("Cognito user created successfully: {}", cognitoUser.getCognitoUserId());

            // Step 8: Create user in database
            User user = User.builder()
                    .email(email)
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
            // Flush immediately so DB constraint violations surface here, not at commit time
            entityManager.flush();
            log.info("User created in database with ID: {}", savedUser.getId());

            // Step 9: Activate invitation
            invitationService.activateInvitation(
                    signupRequestDto.getInvitationToken(),
                    cognitoUser.getCognitoUserId(),
                    savedUser.getId());
            log.info("Invitation activated successfully");

            // Send welcome email (best-effort)
            EmailService.EmailDetails welcomeEmail = null;
            try {
                welcomeEmail = emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());
            } catch (Exception emailError) {
                log.error("Failed to send welcome email to: {}", savedUser.getEmail(), emailError);
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

            if (welcomeEmail != null) {
                responseBuilder.welcomeEmailDetails(SignupResponseDto.WelcomeEmailDetailsDto.builder()
                        .recipientEmail(welcomeEmail.getRecipientEmail())
                        .subject(welcomeEmail.getSubject())
                        .sent(welcomeEmail.isSent())
                        .build());
            }

            return responseBuilder.build();

        } catch (Exception e) {
            log.error("Error during invitation signup for {}: {}", email, e.getMessage(), e);

            // Compensating action: delete Cognito user if DB save failed
            if (cognitoUserCreated) {
                deleteCognitoUserQuietly(email);
            }

            if (e instanceof BadRequestException) {
                throw e;
            }
            throw new BadRequestException("Signup failed: " + e.getMessage());
        }
    }

    /**
     * Attempts to delete a Cognito user silently for cleanup purposes.
     * Logs errors but does not throw — used during compensating rollback.
     */
    private void deleteCognitoUserQuietly(String email) {
        try {
            cognitoService.deleteUser(email);
            log.info("Cleaned up Cognito user after signup failure: {}", email);
        } catch (Exception cleanupEx) {
            log.error("Failed to clean up Cognito user {} after signup failure: {}", email, cleanupEx.getMessage());
        }
    }
}
