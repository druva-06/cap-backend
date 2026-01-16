package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.invitation.InvitationRequestDto;
import com.consultancy.education.DTOs.responseDTOs.invitation.InvitationResponseDto;
import com.consultancy.education.DTOs.responseDTOs.invitation.PagedInvitationResponseDto;
import com.consultancy.education.DTOs.responseDTOs.invitation.ValidationResponseDto;

public interface InvitationService {

    /**
     * Create a new user invitation
     * 
     * @param requestDto      Invitation details including email, role, optional
     *                        user data
     * @param invitedByUserId ID of the admin creating the invitation
     * @return InvitationResponseDto with invitation details and token
     */
    InvitationResponseDto createInvitation(InvitationRequestDto requestDto, Long invitedByUserId);

    /**
     * Validate an invitation token
     * 
     * @param token Invitation token from email link
     * @return ValidationResponseDto with invitation status and pre-filled data
     */
    ValidationResponseDto validateInvitationToken(String token);

    /**
     * Get all invitations with pagination
     * 
     * @param page   Page number (0-indexed)
     * @param size   Page size
     * @param search Search query for email/name
     * @return PagedInvitationResponseDto with invitation list
     */
    PagedInvitationResponseDto getAllInvitations(int page, int size, String search);

    /**
     * Get invitations by status
     * 
     * @param status Invitation status (PENDING, ACTIVE, EXPIRED, REVOKED)
     * @param page   Page number
     * @param size   Page size
     * @return PagedInvitationResponseDto filtered by status
     */
    PagedInvitationResponseDto getInvitationsByStatus(String status, int page, int size);

    /**
     * Resend invitation email
     * 
     * @param invitationId ID of the invitation to resend
     * @return InvitationResponseDto with updated invitation details
     */
    InvitationResponseDto resendInvitation(Long invitationId);

    /**
     * Revoke an invitation
     * 
     * @param invitationId ID of the invitation to revoke
     */
    void revokeInvitation(Long invitationId);

    /**
     * Mark invitation as activated after successful signup
     * 
     * @param token         Invitation token
     * @param cognitoUserId Cognito user ID from signup
     * @param userId        User ID from users table
     */
    void activateInvitation(String token, String cognitoUserId, Long userId);

    /**
     * Clean up expired invitations (scheduled job)
     * 
     * @return Number of invitations marked as expired
     */
    int markExpiredInvitations();

    /**
     * Get invitation by ID
     * 
     * @param invitationId Invitation ID
     * @return InvitationResponseDto
     */
    InvitationResponseDto getInvitationById(Long invitationId);
}
