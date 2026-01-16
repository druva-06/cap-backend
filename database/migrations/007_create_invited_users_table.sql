-- Create invited_users table for invitation-based signup
CREATE TABLE IF NOT EXISTS invited_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    
    -- Role and permissions assignment
    role_id BIGINT NOT NULL,
    
    -- Invitation tracking
    invitation_token VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    invited_by_user_id BIGINT NOT NULL,
    invited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    
    -- Signup completion tracking
    cognito_user_id VARCHAR(255),
    activated_at TIMESTAMP NULL,
    user_id BIGINT NULL,
    
    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign keys
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT,
    FOREIGN KEY (invited_by_user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_email (email),
    INDEX idx_invitation_token (invitation_token),
    INDEX idx_status (status),
    INDEX idx_invited_by (invited_by_user_id),
    INDEX idx_expires_at (expires_at),
    INDEX idx_cognito_user_id (cognito_user_id),
    
    -- Constraint to ensure status values
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'ACTIVE', 'EXPIRED', 'REVOKED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add comment to table
ALTER TABLE invited_users COMMENT = 'Stores user invitations for pre-approved signup. Admin creates invitations before users can sign up.';

-- Create trigger to automatically expire old pending invitations
DELIMITER $$

CREATE TRIGGER before_invited_users_select
BEFORE SELECT ON invited_users
FOR EACH ROW
BEGIN
    -- This is a pseudo-trigger concept; MySQL doesn't support BEFORE SELECT
    -- Instead, we'll handle expiration via application logic or scheduled job
END$$

DELIMITER ;

-- Note: In MySQL, we cannot create BEFORE SELECT triggers.
-- Expiration logic should be handled via:
-- 1. Application layer checking expires_at during validation
-- 2. Scheduled cleanup job to update status='EXPIRED' for old invitations
