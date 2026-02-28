package com.meritcap.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "invited_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvitedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "username", length = 100)
    String username;

    @Column(name = "first_name", length = 100)
    String firstName;

    @Column(name = "last_name", length = 100)
    String lastName;

    @Column(name = "phone_number", length = 20)
    String phoneNumber;

    // Role assignment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    // Invitation tracking
    @Column(name = "invitation_token", nullable = false, unique = true)
    String invitationToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    InvitationStatus status = InvitationStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_by_user_id", nullable = false)
    User invitedBy;

    @Column(name = "invited_at", nullable = false, updatable = false)
    LocalDateTime invitedAt;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;

    // Signup completion tracking
    @Column(name = "cognito_user_id")
    String cognitoUserId;

    @Column(name = "activated_at")
    LocalDateTime activatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    // Audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (invitedAt == null) {
            invitedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum InvitationStatus {
        PENDING, // Invitation sent, awaiting user signup
        ACTIVE, // User has signed up successfully
        EXPIRED, // Invitation has passed expiry date
        REVOKED // Admin cancelled the invitation
    }

    // Helper methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isPending() {
        return status == InvitationStatus.PENDING && !isExpired();
    }

    public boolean canBeActivated() {
        return status == InvitationStatus.PENDING && !isExpired();
    }
}
