-- Migration 005: Add Portal Unification Permissions
-- This migration adds permissions needed for the unified portal system

-- Dashboard View Permissions
INSERT INTO permissions (name, display_name, description, category, is_active) VALUES
('DASHBOARD_VIEW_ADMIN', 'View Admin Dashboard', 'Access admin dashboard view with full statistics', 'DASHBOARD', TRUE),
('DASHBOARD_VIEW_COLLEGE', 'View College Dashboard', 'Access college dashboard view with college-specific metrics', 'DASHBOARD', TRUE),
('DASHBOARD_VIEW_COUNSELOR', 'View Counselor Dashboard', 'Access counselor dashboard view with counselor metrics', 'DASHBOARD', TRUE),
('DASHBOARD_VIEW_SUBAGENT', 'View SubAgent Dashboard', 'Access sub-agent dashboard view', 'DASHBOARD', TRUE),

-- Menu Access Permissions
('MENU_LEADS', 'Access Leads Menu', 'Can see leads menu item', 'MENU', TRUE),
('MENU_AI_CALLING', 'Access AI Calling Menu', 'Can see AI calling menu', 'MENU', TRUE),
('MENU_COMMUNITY', 'Access Community Menu', 'Can see community menu', 'MENU', TRUE),
('MENU_COLLEGES', 'Access Colleges Menu', 'Can see colleges menu', 'MENU', TRUE),
('MENU_PARTNERS', 'Access Partners Menu', 'Can see partners menu', 'MENU', TRUE),
('MENU_MARKETING', 'Access Marketing Menu', 'Can see marketing menu', 'MENU', TRUE),
('MENU_FINANCE', 'Access Finance Menu', 'Can see finance menu', 'MENU', TRUE),
('MENU_HR', 'Access HR Menu', 'Can see HR menu', 'MENU', TRUE),
('MENU_ASSETS', 'Access Assets Menu', 'Can see assets menu', 'MENU', TRUE),
('MENU_REPORTS', 'Access Reports Menu', 'Can see reports menu', 'MENU', TRUE),
('MENU_ROLES_PERMISSIONS', 'Access Roles & Permissions Menu', 'Can see roles & permissions menu', 'MENU', TRUE),

-- College Portal Specific Permissions
('COLLEGE_VIEW_OWN_APPLICATIONS', 'View Own Applications', 'Can view applications submitted to their college', 'COLLEGES', TRUE),
('COLLEGE_REVIEW_APPLICATIONS', 'Review Applications', 'Can accept/reject applications to their college', 'COLLEGES', TRUE),
('COLLEGE_MANAGE_DOCUMENTS', 'Manage College Documents', 'Can upload and manage college documents', 'COLLEGES', TRUE),
('COLLEGE_VIEW_ANALYTICS', 'View College Analytics', 'Can view college-specific analytics and reports', 'COLLEGES', TRUE),
('COLLEGE_MANAGE_PROFILE', 'Manage College Profile', 'Can edit college profile information', 'COLLEGES', TRUE),
('COLLEGE_MANAGE_COURSES', 'Manage College Courses', 'Can add/edit courses for their college', 'COLLEGES', TRUE),

-- Counselor Portal Specific Permissions
('COUNSELOR_VIEW_TASKS', 'View Counselor Tasks', 'Can view assigned tasks', 'TASKS', TRUE),
('COUNSELOR_MANAGE_TASKS', 'Manage Counselor Tasks', 'Can create and update tasks', 'TASKS', TRUE),
('COUNSELOR_MANAGE_APPOINTMENTS', 'Manage Appointments', 'Can schedule and manage appointments', 'APPOINTMENTS', TRUE),
('COUNSELOR_VIEW_APPOINTMENTS', 'View Appointments', 'Can view scheduled appointments', 'APPOINTMENTS', TRUE),
('COUNSELOR_VIEW_PERFORMANCE', 'View Performance Metrics', 'Can view own performance metrics', 'REPORTS', TRUE),
('COUNSELOR_VIEW_ASSIGNED_LEADS', 'View Assigned Leads Only', 'Can only view leads assigned to them', 'LEADS', TRUE),

-- SubAgent Portal Specific Permissions
('SUBAGENT_VIEW_COMMISSION', 'View Commission', 'Can view commission reports', 'REPORTS', TRUE),
('SUBAGENT_VIEW_STUDENTS', 'View SubAgent Students', 'Can view students referred by sub-agent', 'STUDENTS', TRUE),
('SUBAGENT_SUBMIT_APPLICATIONS', 'Submit Applications', 'Can submit applications on behalf of students', 'APPLICATIONS', TRUE),

-- Task Management Permissions
('TASK_VIEW_ALL', 'View All Tasks', 'Can view all tasks in the system', 'TASKS', TRUE),
('TASK_VIEW_ASSIGNED', 'View Assigned Tasks', 'Can view only assigned tasks', 'TASKS', TRUE),
('TASK_CREATE', 'Create Tasks', 'Can create new tasks', 'TASKS', TRUE),
('TASK_EDIT', 'Edit Tasks', 'Can edit task details', 'TASKS', TRUE),
('TASK_DELETE', 'Delete Tasks', 'Can delete tasks', 'TASKS', TRUE),
('TASK_ASSIGN', 'Assign Tasks', 'Can assign tasks to users', 'TASKS', TRUE),

-- Appointment Management Permissions
('APPOINTMENT_VIEW_ALL', 'View All Appointments', 'Can view all appointments', 'APPOINTMENTS', TRUE),
('APPOINTMENT_VIEW_OWN', 'View Own Appointments', 'Can view only own appointments', 'APPOINTMENTS', TRUE),
('APPOINTMENT_CREATE', 'Create Appointments', 'Can schedule new appointments', 'APPOINTMENTS', TRUE),
('APPOINTMENT_EDIT', 'Edit Appointments', 'Can edit appointment details', 'APPOINTMENTS', TRUE),
('APPOINTMENT_DELETE', 'Delete Appointments', 'Can cancel/delete appointments', 'APPOINTMENTS', TRUE),

-- Additional Document Permissions
('DOCUMENT_MANAGE_COLLEGE_DOCS', 'Manage College Documents', 'Can manage college-specific documents', 'DOCUMENTS', TRUE),
('DOCUMENT_REVIEW', 'Review Documents', 'Can review and approve/reject documents', 'DOCUMENTS', TRUE),
('DOCUMENT_VERIFY', 'Verify Documents', 'Can verify document authenticity', 'DOCUMENTS', TRUE);
