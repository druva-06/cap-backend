-- ============================================
-- MENU PERMISSIONS TABLE
-- Version: 1.0
-- Date: 2026-01-16
-- Purpose: Store dynamic menu-permission mappings
-- ============================================

-- Create menu_permissions table
CREATE TABLE IF NOT EXISTS menu_permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    menu_id VARCHAR(100) NOT NULL COMMENT 'Menu identifier (e.g., leads, colleges, dashboard)',
    submenu_id VARCHAR(100) COMMENT 'Submenu identifier (NULL for main menu items)',
    permission_id BIGINT NOT NULL COMMENT 'Permission required to access this menu',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE,
    INDEX idx_menu_id (menu_id),
    INDEX idx_submenu_id (submenu_id),
    INDEX idx_permission_id (permission_id),
    UNIQUE KEY unique_menu_permission (menu_id, submenu_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Maps menus/submenus to required permissions';

-- ============================================
-- SEED DATA: Main Menu Permissions
-- Based on current MenuConfigServiceImpl.java
-- ============================================

-- Dashboard (all users can access)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'dashboard', NULL, p.id FROM permissions p 
WHERE p.name IN ('DASHBOARD_VIEW_ADMIN', 'DASHBOARD_VIEW_COLLEGE', 'DASHBOARD_VIEW_COUNSELOR', 'DASHBOARD_VIEW_SUBAGENT', 'DASHBOARD_VIEW_STUDENT')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Leads Menu
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'leads', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_LEADS', 'LEAD_VIEW_ALL', 'LEAD_VIEW_ASSIGNED', 'LEAD_VIEW_OWN')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- AI Calling Menu
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'ai-calling', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_AI_CALLING', 'AI_CALLING_MAKE_CALL')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Students Menu
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'students', NULL, p.id FROM permissions p 
WHERE p.name IN ('STUDENT_VIEW_ALL', 'STUDENT_VIEW_ASSIGNED', 'STUDENT_VIEW_REFERRED')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Applications Menu
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'applications', NULL, p.id FROM permissions p 
WHERE p.name IN ('APPLICATION_VIEW_ALL', 'APPLICATION_VIEW_ASSIGNED', 'APPLICATION_VIEW_REFERRED', 'APPLICATION_VIEW_OWN', 'COLLEGE_VIEW_OWN_APPLICATIONS')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Community Menu
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'community', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_COMMUNITY', 'COMMUNICATION_SEND_EMAIL', 'COMMUNICATION_SEND_SMS')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Colleges Menu (main)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_COLLEGES', 'COLLEGE_VIEW_ALL', 'COLLEGE_VIEW_OWN_PROFILE')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Colleges Submenus
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', 'all-colleges', p.id FROM permissions p WHERE p.name = 'COLLEGE_VIEW_ALL'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', 'partner-colleges', p.id FROM permissions p WHERE p.name IN ('COLLEGE_VIEW_ALL', 'PARTNER_VIEW_ALL')
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', 'my-profile', p.id FROM permissions p WHERE p.name = 'COLLEGE_VIEW_OWN_PROFILE'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', 'analytics', p.id FROM permissions p WHERE p.name IN ('COLLEGE_VIEW_ALL', 'COLLEGE_VIEW_ANALYTICS')
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', 'commission', p.id FROM permissions p WHERE p.name IN ('COLLEGE_VIEW_ALL', 'PARTNER_VIEW_PERFORMANCE', 'COLLEGE_VIEW_COMMISSION', 'PARTNER_MANAGE_COMMISSION')
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', 'courses', p.id FROM permissions p WHERE p.name = 'COLLEGE_MANAGE_COURSES'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'colleges', 'intakes', p.id FROM permissions p WHERE p.name IN ('COLLEGE_VIEW_INTAKES', 'COLLEGE_MANAGE_INTAKES')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Partners Menu (main)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'partners', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_PARTNERS', 'PARTNER_VIEW_ALL')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Partners Submenus
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'partners', 'partners-overview', p.id FROM permissions p WHERE p.name = 'PARTNER_VIEW_ALL'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'partners', 'college-partners', p.id FROM permissions p WHERE p.name = 'PARTNER_VIEW_ALL'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'partners', 'subagent-partners', p.id FROM permissions p WHERE p.name = 'PARTNER_VIEW_ALL'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Marketing Menu (main)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_MARKETING', 'MARKETING_VIEW')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Marketing Submenus
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', 'marketing-overview', p.id FROM permissions p WHERE p.name = 'MENU_MARKETING'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', 'offline-marketing', p.id FROM permissions p WHERE p.name = 'MENU_MARKETING'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', 'webinars', p.id FROM permissions p WHERE p.name = 'MENU_MARKETING'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', 'social-media', p.id FROM permissions p WHERE p.name = 'MENU_MARKETING'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', 'digital-campaigns', p.id FROM permissions p WHERE p.name = 'MENU_MARKETING'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', 'content-marketing', p.id FROM permissions p WHERE p.name = 'MENU_MARKETING'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'marketing', 'partner-marketing', p.id FROM permissions p WHERE p.name = 'MENU_MARKETING'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Finance Menu (main)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'finance', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_FINANCE', 'FINANCE_VIEW_ALL', 'FINANCE_VIEW_OWN_COMMISSIONS', 'FINANCE_VIEW_OWN_INVOICES')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Finance Submenus
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'finance', 'finance-overview', p.id FROM permissions p WHERE p.name IN ('MENU_FINANCE', 'FINANCE_VIEW_ALL')
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'finance', 'invoices', p.id FROM permissions p WHERE p.name IN ('FINANCE_CREATE_INVOICE', 'FINANCE_VIEW_ALL', 'FINANCE_VIEW_OWN_INVOICES')
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'finance', 'expenses', p.id FROM permissions p WHERE p.name IN ('FINANCE_MANAGE_EXPENSES', 'FINANCE_VIEW_ALL')
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'finance', 'commissions', p.id FROM permissions p WHERE p.name IN ('FINANCE_VIEW_ALL', 'FINANCE_VIEW_OWN_COMMISSIONS')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- HR Menu (main)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'hr', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_HR', 'HR_VIEW_ALL')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- HR Submenus
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'hr', 'hr-overview', p.id FROM permissions p WHERE p.name = 'MENU_HR'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'hr', 'leave-management', p.id FROM permissions p WHERE p.name = 'MENU_HR'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'hr', 'attendance', p.id FROM permissions p WHERE p.name = 'MENU_HR'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'hr', 'training', p.id FROM permissions p WHERE p.name = 'MENU_HR'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Assets Menu
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'assets', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_ASSETS', 'ASSET_VIEW')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Reports Menu
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'reports', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_REPORTS', 'REPORT_VIEW_ALL', 'REPORT_VIEW_COLLEGE', 'REPORT_VIEW_COUNSELOR', 'REPORT_VIEW_SUBAGENT', 'COLLEGE_VIEW_ANALYTICS')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Roles & Permissions Menu (main)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'roles', NULL, p.id FROM permissions p 
WHERE p.name IN ('MENU_ROLES_PERMISSIONS', 'USER_MANAGE_ROLES', 'USER_MANAGE_PERMISSIONS')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Roles & Permissions Submenus
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'roles', 'roles-management', p.id FROM permissions p WHERE p.name = 'USER_MANAGE_ROLES'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'roles', 'permissions-management', p.id FROM permissions p WHERE p.name = 'USER_MANAGE_PERMISSIONS'
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'roles', 'user-permissions', p.id FROM permissions p WHERE p.name = 'USER_MANAGE_PERMISSIONS'
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Settings Menu (accessible to all authenticated users)
INSERT INTO menu_permissions (menu_id, submenu_id, permission_id)
SELECT 'settings', NULL, p.id FROM permissions p 
WHERE p.name IN ('SETTINGS_VIEW_ALL', 'SETTINGS_VIEW_OWN')
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- ============================================
-- VERIFICATION QUERY
-- Run to verify insertions
-- ============================================
-- SELECT 
--     mp.menu_id,
--     mp.submenu_id,
--     p.name as permission_name,
--     p.display_name
-- FROM menu_permissions mp
-- JOIN permissions p ON mp.permission_id = p.id
-- ORDER BY mp.menu_id, mp.submenu_id, p.name;

-- SELECT menu_id, COUNT(*) as permission_count 
-- FROM menu_permissions 
-- GROUP BY menu_id 
-- ORDER BY menu_id;

-- ============================================
-- SUMMARY
-- ============================================
-- Table: menu_permissions
-- - Stores which permissions are required for each menu/submenu
-- - Admin can modify these mappings to control menu visibility
-- - Backend reads from this table to build dynamic menus
-- - Supports both main menus and submenus
-- ============================================
