#!/bin/bash

# Database Migration Script for Portal Unification Permissions
# This script runs migrations 005 and 006

echo "=========================================="
echo "Portal Unification - Database Migration"
echo "=========================================="
echo ""

# Load database configuration (adjust path if needed)
# You can set these environment variables or hardcode them
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-3306}"
DB_NAME="${DB_NAME:-cap_education}"
DB_USER="${DB_USER:-root}"
DB_PASSWORD="${DB_PASSWORD:-}"

echo "Database Configuration:"
echo "  Host: $DB_HOST"
echo "  Port: $DB_PORT"
echo "  Database: $DB_NAME"
echo "  User: $DB_USER"
echo ""

# Check if mysql client is installed
if ! command -v mysql &> /dev/null; then
    echo "ERROR: mysql client not found. Please install MySQL client."
    exit 1
fi

# Test database connection
echo "Testing database connection..."
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1;" "$DB_NAME" &> /dev/null
if [ $? -ne 0 ]; then
    echo "ERROR: Cannot connect to database. Please check your credentials."
    exit 1
fi
echo "✓ Database connection successful"
echo ""

# Run Migration 005
echo "Running Migration 005: Add Portal Unification Permissions..."
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "database/migrations/005_add_portal_unification_permissions.sql"
if [ $? -eq 0 ]; then
    echo "✓ Migration 005 completed successfully"
else
    echo "✗ Migration 005 failed"
    exit 1
fi
echo ""

# Run Migration 006
echo "Running Migration 006: Assign Portal Permissions to Roles..."
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "database/migrations/006_assign_portal_permissions_to_roles.sql"
if [ $? -eq 0 ]; then
    echo "✓ Migration 006 completed successfully"
else
    echo "✗ Migration 006 failed"
    exit 1
fi
echo ""

# Verify migrations
echo "Verifying migrations..."
echo ""
echo "Checking new permissions count:"
NEW_PERMS=$(mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" -sN -e "SELECT COUNT(*) FROM permissions WHERE name IN ('DASHBOARD_VIEW_ADMIN', 'MENU_LEADS', 'COLLEGE_VIEW_OWN_APPLICATIONS', 'COUNSELOR_VIEW_TASKS');")
echo "  Found $NEW_PERMS new permissions (expected: at least 4)"

echo ""
echo "Checking role permission assignments:"
mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" -e "
SELECT 
    r.name as role_name,
    COUNT(DISTINCT p.id) as permission_count
FROM roles r
LEFT JOIN role_permissions rp ON r.id = rp.role_id
LEFT JOIN permissions p ON rp.permission_id = p.id
WHERE r.name IN ('ADMIN', 'COLLEGE', 'COUNSELOR', 'SUB_AGENT')
GROUP BY r.id, r.name
ORDER BY r.name;
"

echo ""
echo "=========================================="
echo "Migration completed successfully!"
echo "=========================================="
echo ""
echo "Next steps:"
echo "1. Restart the Spring Boot application"
echo "2. Test the new menu configuration API: GET /api/menu-config"
echo "3. Test user permissions API: GET /api/user-permissions/{userId}"
echo "4. Verify login response includes permissions"
