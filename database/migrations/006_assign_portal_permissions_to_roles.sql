-- Migration 006: Assign Portal Unification Permissions to Roles
-- This migration assigns the new permissions to appropriate roles

-- =============================================================================
-- ADMIN ROLE - Full Access to Everything
-- =============================================================================
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
AND p.name IN (
    -- Dashboard
    'DASHBOARD_VIEW_ADMIN',
    -- All Menu Access
    'MENU_LEADS', 'MENU_AI_CALLING', 'MENU_COMMUNITY', 'MENU_COLLEGES',
    'MENU_PARTNERS', 'MENU_MARKETING', 'MENU_FINANCE', 'MENU_HR',
    'MENU_ASSETS', 'MENU_REPORTS', 'MENU_ROLES_PERMISSIONS',
    -- All Tasks
    'TASK_VIEW_ALL', 'TASK_CREATE', 'TASK_EDIT', 'TASK_DELETE', 'TASK_ASSIGN',
    -- All Appointments
    'APPOINTMENT_VIEW_ALL', 'APPOINTMENT_CREATE', 'APPOINTMENT_EDIT', 'APPOINTMENT_DELETE',
    -- Document Management
    'DOCUMENT_REVIEW', 'DOCUMENT_VERIFY'
)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- =============================================================================
-- COLLEGE ROLE - College Portal Features
-- =============================================================================
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'COLLEGE'
AND p.name IN (
    -- Dashboard
    'DASHBOARD_VIEW_COLLEGE',
    -- Menu Access
    'MENU_COLLEGES', 'MENU_REPORTS',
    -- College Specific
    'COLLEGE_VIEW_OWN_APPLICATIONS',
    'COLLEGE_REVIEW_APPLICATIONS',
    'COLLEGE_MANAGE_DOCUMENTS',
    'COLLEGE_VIEW_ANALYTICS',
    'COLLEGE_MANAGE_PROFILE',
    'COLLEGE_MANAGE_COURSES',
    -- Application Management
    'APPLICATION_VIEW_ASSIGNED', -- They see applications to their college
    'APPLICATION_APPROVE', -- Can accept/reject
    -- Student View
    'STUDENT_VIEW_ASSIGNED', -- Students who applied to their college
    -- Documents
    'DOCUMENT_VIEW_ALL',
    'DOCUMENT_UPLOAD',
    'DOCUMENT_DOWNLOAD',
    'DOCUMENT_MANAGE_COLLEGE_DOCS',
    -- Reports
    'REPORT_VIEW_ALL', -- Their own reports
    'REPORT_EXPORT',
    'REPORT_ANALYTICS'
)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- =============================================================================
-- COUNSELOR ROLE - Counselor Portal Features
-- =============================================================================
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'COUNSELOR'
AND p.name IN (
    -- Dashboard
    'DASHBOARD_VIEW_COUNSELOR',
    -- Menu Access
    'MENU_LEADS', 'MENU_COMMUNITY',
    -- Lead Management
    'LEAD_VIEW_ASSIGNED',
    'LEAD_CREATE',
    'LEAD_EDIT',
    'COUNSELOR_VIEW_ASSIGNED_LEADS',
    -- Student Management
    'STUDENT_VIEW_ASSIGNED',
    'STUDENT_CREATE',
    'STUDENT_EDIT',
    -- Application Management
    'APPLICATION_VIEW_ASSIGNED',
    'APPLICATION_CREATE',
    'APPLICATION_EDIT',
    -- Task Management
    'TASK_VIEW_ASSIGNED',
    'TASK_CREATE',
    'TASK_EDIT',
    'COUNSELOR_VIEW_TASKS',
    'COUNSELOR_MANAGE_TASKS',
    -- Appointment Management
    'APPOINTMENT_VIEW_OWN',
    'APPOINTMENT_CREATE',
    'APPOINTMENT_EDIT',
    'APPOINTMENT_DELETE',
    'COUNSELOR_MANAGE_APPOINTMENTS',
    'COUNSELOR_VIEW_APPOINTMENTS',
    -- Performance
    'COUNSELOR_VIEW_PERFORMANCE',
    -- Communication
    'COMMUNICATION_SEND_EMAIL',
    'COMMUNICATION_SEND_SMS',
    'COMMUNICATION_VIEW_HISTORY',
    -- Documents
    'DOCUMENT_VIEW_ALL',
    'DOCUMENT_UPLOAD',
    'DOCUMENT_DOWNLOAD',
    -- College/Course Viewing
    'COLLEGE_VIEW_ALL',
    'COURSE_VIEW_ALL'
)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- =============================================================================
-- SUB_AGENT ROLE - SubAgent Portal Features
-- =============================================================================
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name IN ('SUB_AGENT', 'SUBAGENT')
AND p.name IN (
    -- Dashboard
    'DASHBOARD_VIEW_SUBAGENT',
    -- Student Management
    'STUDENT_VIEW_ASSIGNED',
    'STUDENT_CREATE',
    'SUBAGENT_VIEW_STUDENTS',
    -- Application Management
    'APPLICATION_VIEW_ASSIGNED',
    'APPLICATION_CREATE',
    'SUBAGENT_SUBMIT_APPLICATIONS',
    -- Reports
    'SUBAGENT_VIEW_COMMISSION',
    'REPORT_VIEW_ALL', -- Their own reports
    -- Documents
    'DOCUMENT_VIEW_ALL',
    'DOCUMENT_UPLOAD',
    'DOCUMENT_DOWNLOAD',
    -- College/Course Viewing
    'COLLEGE_VIEW_ALL',
    'COURSE_VIEW_ALL'
)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- =============================================================================
-- STUDENT ROLE - Student Portal Features (for reference)
-- =============================================================================
-- Students already have their permissions from 004_assign_default_role_permissions.sql
-- Just adding any new ones
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'STUDENT'
AND p.name IN (
    'COLLEGE_VIEW_ALL',
    'COURSE_VIEW_ALL',
    'DOCUMENT_VIEW_ALL',
    'DOCUMENT_UPLOAD'
)
ON DUPLICATE KEY UPDATE role_id = role_id;

-- =============================================================================
-- Verification Query (Optional - for testing)
-- =============================================================================
-- Run this to see what permissions each role has:
-- SELECT 
--     r.name as role_name,
--     COUNT(DISTINCT p.id) as permission_count,
--     GROUP_CONCAT(DISTINCT p.category ORDER BY p.category) as categories
-- FROM roles r
-- LEFT JOIN role_permissions rp ON r.id = rp.role_id
-- LEFT JOIN permissions p ON rp.permission_id = p.id
-- WHERE r.name IN ('ADMIN', 'COLLEGE', 'COUNSELOR', 'SUB_AGENT')
-- GROUP BY r.id, r.name
-- ORDER BY r.name;
