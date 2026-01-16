package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.invitation.InvitationRequestDto;
import com.consultancy.education.DTOs.responseDTOs.invitation.InvitationResponseDto;
import com.consultancy.education.DTOs.responseDTOs.invitation.PagedInvitationResponseDto;
import com.consultancy.education.DTOs.responseDTOs.invitation.ValidationResponseDto;
import com.consultancy.education.exception.AlreadyExistException;
import com.consultancy.education.exception.BadRequestException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.model.InvitedUser;
import com.consultancy.education.model.InvitedUser.InvitationStatus;
import com.consultancy.education.model.Role;
import com.consultancy.education.model.User;
import com.consultancy.education.repository.InvitedUserRepository;
import com.consultancy.education.repository.RoleRepository;
import com.consultancy.education.repository.UserRepository;
import com.consultancy.education.service.EmailService;
import com.consultancy.education.service.InvitationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    private InvitedUserRepository invitedUserRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public InvitationResponseDto createInvitation(InvitationRequestDto requestDto, Long invitedByUserId) {
        log.info("Creating invitation for email: {}", requestDto.getEmail());

        // Check if email already exists in users table
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new AlreadyExistException(List.of("User with this email already exists"));
        }

        // Check if email already has a pending invitation
        invitedUserRepository.findByEmail(requestDto.getEmail()).ifPresent(existingInvitation -> {
            if (existingInvitation.getStatus() == InvitationStatus.PENDING) {
                throw new AlreadyExistException(
                        List.of("A pending invitation already exists for this email. Please revoke the old invitation first."));
            }
        });

        // Validate role
        Role role = roleRepository.findByName(requestDto.getRoleName().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Role not found: " + requestDto.getRoleName()));

        // Get inviting user
        User invitedBy = userRepository.findById(invitedByUserId)
                .orElseThrow(() -> new NotFoundException("Inviting user not found"));

        // Generate unique invitation token
        String invitationToken = UUID.randomUUID().toString();

        // Calculate expiry date (default 7 days)
        int expiryDays = requestDto.getExpiryDays() != null ? requestDto.getExpiryDays() : 7;
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(expiryDays);

        // Create invitation
        InvitedUser invitation = InvitedUser.builder()
                .email(requestDto.getEmail())
                .username(requestDto.getUsername())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .phoneNumber(requestDto.getPhoneNumber())
                .role(role)
                .invitationToken(invitationToken)
                .status(InvitationStatus.PENDING)
                .invitedBy(invitedBy)
                .invitedAt(LocalDateTime.now())
                .expiresAt(expiresAt)
                .build();

        InvitedUser savedInvitation = invitedUserRepository.save(invitation);
        log.info("Invitation created successfully with ID: {} for email: {}", savedInvitation.getId(),
                savedInvitation.getEmail());

        // Send invitation email with token
        EmailService.EmailDetails emailDetails = null;
        try {
            int expiryHours = expiryDays * 24;
            emailDetails = emailService.sendInvitationEmail(
                    savedInvitation.getEmail(),
                    savedInvitation.getFirstName(),
                    savedInvitation.getInvitationToken(),
                    expiryHours);
            log.info("Invitation email processed for: {}", savedInvitation.getEmail());
        } catch (Exception e) {
            log.error("Failed to send invitation email to: {}", savedInvitation.getEmail(), e);
            // Don't fail invitation creation if email fails
        }

        InvitationResponseDto response = toResponseDto(savedInvitation);

        // Add email details to response (for dev mode verification)
        if (emailDetails != null) {
            response.setEmailDetails(InvitationResponseDto.EmailDetailsDto.builder()
                    .recipientEmail(emailDetails.getRecipientEmail())
                    .subject(emailDetails.getSubject())
                    .signupLink(emailDetails.getSignupLink())
                    .sent(emailDetails.isSent())
                    .build());
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ValidationResponseDto validateInvitationToken(String token) {
        log.info("Validating invitation token");

        InvitedUser invitation = invitedUserRepository.findByInvitationToken(token)
                .orElse(null);

        if (invitation == null) {
            return ValidationResponseDto.builder()
                    .valid(false)
                    .message("Invalid invitation token")
                    .build();
        }

        if (invitation.getStatus() == InvitationStatus.ACTIVE) {
            return ValidationResponseDto.builder()
                    .valid(false)
                    .message("This invitation has already been used")
                    .build();
        }

        if (invitation.getStatus() == InvitationStatus.REVOKED) {
            return ValidationResponseDto.builder()
                    .valid(false)
                    .message("This invitation has been revoked")
                    .build();
        }

        if (invitation.isExpired()) {
            return ValidationResponseDto.builder()
                    .valid(false)
                    .message("This invitation has expired")
                    .build();
        }

        // Valid invitation
        return ValidationResponseDto.builder()
                .valid(true)
                .message("Invitation is valid")
                .email(invitation.getEmail())
                .username(invitation.getUsername())
                .firstName(invitation.getFirstName())
                .lastName(invitation.getLastName())
                .phoneNumber(invitation.getPhoneNumber())
                .roleName(invitation.getRole().getName())
                .invitationToken(token)
                .invitationId(invitation.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedInvitationResponseDto getAllInvitations(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "invitedAt"));

        Page<InvitedUser> invitationsPage;
        if (search != null && !search.trim().isEmpty()) {
            invitationsPage = invitedUserRepository.searchInvitations(search, pageable);
        } else {
            invitationsPage = invitedUserRepository.findAll(pageable);
        }

        return buildPagedResponse(invitationsPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedInvitationResponseDto getInvitationsByStatus(String statusStr, int page, int size) {
        InvitationStatus status;
        try {
            status = InvitationStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + statusStr);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "invitedAt"));
        Page<InvitedUser> invitationsPage = invitedUserRepository.findByStatus(status, pageable);

        return buildPagedResponse(invitationsPage);
    }

    @Override
    @Transactional
    public InvitationResponseDto resendInvitation(Long invitationId) {
        log.info("Resending invitation with ID: {}", invitationId);

        InvitedUser invitation = invitedUserRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation not found with ID: " + invitationId));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BadRequestException("Only pending invitations can be resent");
        }

        // Generate new token and extend expiry
        invitation.setInvitationToken(UUID.randomUUID().toString());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7));
        invitation.setUpdatedAt(LocalDateTime.now());

        InvitedUser updatedInvitation = invitedUserRepository.save(invitation);
        log.info("Invitation resent successfully");

        // Send new invitation email
        EmailService.EmailDetails emailDetails = null;
        try {
            emailDetails = emailService.sendInvitationEmail(
                    updatedInvitation.getEmail(),
                    updatedInvitation.getFirstName(),
                    updatedInvitation.getInvitationToken(),
                    168 // 7 days = 168 hours
            );
            log.info("Invitation email resent to: {}", updatedInvitation.getEmail());
        } catch (Exception e) {
            log.error("Failed to resend invitation email to: {}", updatedInvitation.getEmail(), e);
            // Don't fail resend if email fails
        }

        InvitationResponseDto response = toResponseDto(updatedInvitation);

        // Add email details to response (for dev mode verification)
        if (emailDetails != null) {
            response.setEmailDetails(InvitationResponseDto.EmailDetailsDto.builder()
                    .recipientEmail(emailDetails.getRecipientEmail())
                    .subject(emailDetails.getSubject())
                    .signupLink(emailDetails.getSignupLink())
                    .sent(emailDetails.isSent())
                    .build());
        }

        return response;
    }

    @Override
    @Transactional
    public void revokeInvitation(Long invitationId) {
        log.info("Revoking invitation with ID: {}", invitationId);

        InvitedUser invitation = invitedUserRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation not found with ID: " + invitationId));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BadRequestException("Only pending invitations can be revoked");
        }

        invitation.setStatus(InvitationStatus.REVOKED);
        invitation.setUpdatedAt(LocalDateTime.now());

        invitedUserRepository.save(invitation);
        log.info("Invitation revoked successfully");
    }

    @Override
    @Transactional
    public void activateInvitation(String token, String cognitoUserId, Long userId) {
        log.info("Activating invitation with token");

        InvitedUser invitation = invitedUserRepository.findByInvitationToken(token)
                .orElseThrow(() -> new NotFoundException("Invitation not found"));

        if (!invitation.canBeActivated()) {
            throw new BadRequestException("Invitation cannot be activated");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        invitation.setStatus(InvitationStatus.ACTIVE);
        invitation.setCognitoUserId(cognitoUserId);
        invitation.setUser(user);
        invitation.setActivatedAt(LocalDateTime.now());
        invitation.setUpdatedAt(LocalDateTime.now());

        invitedUserRepository.save(invitation);
        log.info("Invitation activated successfully");
    }

    @Override
    @Transactional
    public int markExpiredInvitations() {
        log.info("Marking expired invitations");
        int count = invitedUserRepository.markExpiredInvitations(LocalDateTime.now());
        log.info("Marked {} invitations as expired", count);
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public InvitationResponseDto getInvitationById(Long invitationId) {
        InvitedUser invitation = invitedUserRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException("Invitation not found with ID: " + invitationId));
        return toResponseDto(invitation);
    }

    // Helper methods
    private InvitationResponseDto toResponseDto(InvitedUser invitation) {
        return InvitationResponseDto.builder()
                .id(invitation.getId())
                .email(invitation.getEmail())
                .username(invitation.getUsername())
                .firstName(invitation.getFirstName())
                .lastName(invitation.getLastName())
                .phoneNumber(invitation.getPhoneNumber())
                .roleName(invitation.getRole().getName())
                .roleDisplayName(invitation.getRole().getDisplayName())
                .invitationToken(invitation.getInvitationToken())
                .status(invitation.getStatus().name())
                .invitedByName(invitation.getInvitedBy().getFirstName() + " " + invitation.getInvitedBy().getLastName())
                .invitedByUserId(invitation.getInvitedBy().getId())
                .invitedAt(invitation.getInvitedAt())
                .expiresAt(invitation.getExpiresAt())
                .activatedAt(invitation.getActivatedAt())
                .cognitoUserId(invitation.getCognitoUserId())
                .userId(invitation.getUser() != null ? invitation.getUser().getId() : null)
                .isExpired(invitation.isExpired())
                .canResend(invitation.getStatus() == InvitationStatus.PENDING)
                .canRevoke(invitation.getStatus() == InvitationStatus.PENDING)
                .build();
    }

    private PagedInvitationResponseDto buildPagedResponse(Page<InvitedUser> page) {
        List<InvitationResponseDto> invitations = page.getContent().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return PagedInvitationResponseDto.builder()
                .invitations(invitations)
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .pageSize(page.getSize())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
