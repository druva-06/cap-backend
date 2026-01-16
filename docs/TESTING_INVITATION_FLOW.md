# Invitation-Based Signup Testing Guide

## Test Scenario 1: Admin Creates Invitation

1. **Login as Admin**

   - Navigate to admin portal: `http://localhost:3000/admin/user-permissions`
   - Login with admin credentials

2. **Create User Invitation**

   - Click "Add User" button
   - Fill in the form:
     - Email: `testuser@example.com`
     - Username: `testuser`
     - First Name: `Test`
     - Last Name: `User`
     - Phone Number: `+1234567890`
     - Role: Select `COLLEGE`, `COUNSELOR`, or `SUBAGENT`
   - Click "Create User"
   - Should see success message: "Invitation email sent successfully"

3. **Verify Invitation Created**

   - Click on "Pending Invitations" tab
   - Should see the invitation with:
     - Email: `testuser@example.com`
     - Status: PENDING (yellow badge with clock icon)
     - Role badge showing selected role
     - Relative time: "invited X seconds ago"
     - Expiry info: "expires in 7 days"
     - Resend and Revoke buttons visible

4. **Check Email** (if email configured)
   - User should receive invitation email with:
     - Subject: "You're invited to join Career Advice Point"
     - Signup link: `${FRONTEND_URL}/signup?token={invitation_token}`
     - Expiry warning: "This invitation link will expire in 168 hours"
     - Professional HTML template with blue theme

## Test Scenario 2: User Accepts Invitation

1. **Open Invitation Link**

   - Click signup link from email OR
   - Navigate to: `http://localhost:3000/signup?token={invitation_token}`
   - Replace `{invitation_token}` with actual token from database/email

2. **Verify Token Validation**

   - Page should show:
     - Title: "Complete Your Registration"
     - Green banner: "Invitation Verified ✓"
     - Message: "You've been invited to join as COLLEGE" (or selected role)
     - Email field pre-filled and disabled
     - Full name pre-filled from invitation

3. **Complete Signup Form**

   - Email: Pre-filled (read-only)
   - Full Name: Pre-filled (can modify if needed)
   - Phone Number: Pre-filled (can modify if needed)
   - Password: Enter strong password
   - Confirm Password: Re-enter password
   - Check "I agree to Terms" checkbox
   - Click "Complete Registration"

4. **Verify Account Created**

   - Should see success message
   - Redirected to dashboard (not document vault for non-students)
   - User session stored in localStorage

5. **Verify in Admin Portal**
   - Go back to admin portal → User Permissions
   - Click "Users" tab
   - Should see new user with:
     - Correct email, name, role
     - Active status
   - Click "Pending Invitations" tab
   - Invitation should show status: ACTIVE (green badge)
   - Resend/Revoke buttons should be disabled

## Test Scenario 3: Invalid/Expired Token

1. **Test Invalid Token**

   - Navigate to: `http://localhost:3000/signup?token=invalid-token-12345`
   - Should see:
     - Red banner with error icon
     - Message: "Invalid Invitation"
     - Details: "Invitation token not found or has been revoked"
     - Submit button disabled

2. **Test Expired Token**

   - In database, update invitation `expires_at` to past date:
     ```sql
     UPDATE invited_users SET expires_at = NOW() - INTERVAL 1 DAY WHERE email = 'testuser@example.com';
     ```
   - Navigate to signup link
   - Should see:
     - Red banner: "Invalid Invitation"
     - Message: "This invitation has expired"
     - Submit button disabled

3. **Resend Expired Invitation**
   - Admin goes to "Pending Invitations" tab
   - Finds expired invitation
   - Clicks "Resend" button
   - Should see toast: "Invitation resent successfully"
   - New token generated, expiry extended 7 days
   - User receives new email with fresh link

## Test Scenario 4: Student Direct Signup (No Invitation)

1. **Navigate to Signup**

   - Go to: `http://localhost:3000/signup` (no token parameter)
   - Should see:
     - Title: "Join WowCap"
     - Subtitle: "Create your free account today"
     - No invitation banner
     - All fields enabled

2. **Complete Student Signup**

   - Full Name: `John Student`
   - Email: `student@example.com`
   - Phone: `+1987654321`
   - Password: Strong password
   - Confirm Password: Match password
   - Click "Create Free Account"

3. **Verify Student Flow**
   - Should see document vault encouragement screen
   - User created with STUDENT role automatically
   - No invitation involved
   - Welcome email sent

## Test Scenario 5: Revoke Invitation

1. **Admin Revokes Invitation**

   - Go to admin portal → Pending Invitations tab
   - Find pending invitation
   - Click "Revoke" button (trash icon)
   - Confirm revocation
   - Should see toast: "Invitation revoked successfully"

2. **Verify Revoked Status**

   - Invitation status changes to REVOKED (red badge with X)
   - Resend and Revoke buttons disabled

3. **Test Revoked Token**
   - User tries to use old signup link
   - Should see error: "This invitation has been revoked"
   - Cannot complete signup

## Test Scenario 6: Duplicate Email Prevention

1. **Create Invitation**

   - Admin creates invitation for `existing@example.com`

2. **User Signs Up**

   - User completes signup with invitation
   - Account created successfully

3. **Try Creating Another Invitation**
   - Admin tries to create new invitation for same email
   - Should see error: "User with this email already exists"
   - Invitation not created

## Test Scenario 7: Multiple Pending Invitations

1. **Try Creating Second Invitation**

   - Admin creates invitation for `test@example.com`
   - Before user signs up, admin tries to create another invitation for same email
   - Should see error: "A pending invitation already exists for this email. Please revoke the old invitation first."

2. **Revoke and Recreate**
   - Admin revokes first invitation
   - Admin creates new invitation for same email
   - Should succeed

## Backend API Endpoints to Test

### 1. Create Invitation

```bash
POST http://localhost:8080/api/invitation/create
Headers: Authorization: Bearer {admin_jwt_token}
Body:
{
  "email": "testuser@example.com",
  "username": "testuser",
  "first_name": "Test",
  "last_name": "User",
  "phone_number": "+1234567890",
  "role_name": "COLLEGE",
  "expiry_days": 7
}
```

### 2. Validate Token

```bash
GET http://localhost:8080/api/invitation/validate?token={invitation_token}
```

### 3. Student Signup

```bash
POST http://localhost:8080/api/auth/signup/student
Body:
{
  "full_name": "John Student",
  "email": "student@example.com",
  "phone_number": "+1234567890",
  "password": "SecurePass123"
}
```

### 4. Invited Signup

```bash
POST http://localhost:8080/api/auth/signup/invited
Body:
{
  "invitation_token": "{invitation_token}",
  "email": "testuser@example.com",
  "password": "SecurePass123"
}
```

### 5. Resend Invitation

```bash
POST http://localhost:8080/api/invitation/resend/{invitation_id}
Headers: Authorization: Bearer {admin_jwt_token}
```

### 6. Revoke Invitation

```bash
DELETE http://localhost:8080/api/invitation/revoke/{invitation_id}
Headers: Authorization: Bearer {admin_jwt_token}
```

## Database Verification

### Check Invitations

```sql
SELECT id, email, first_name, last_name, role_id, status,
       invited_at, expires_at, invitation_token
FROM invited_users
ORDER BY invited_at DESC;
```

### Check Users

```sql
SELECT id, email, username, first_name, last_name, role_id, created_at
FROM users
ORDER BY created_at DESC;
```

### Check Invitation Status

```sql
SELECT
  iu.email,
  iu.status,
  iu.invited_at,
  iu.expires_at,
  iu.activated_at,
  u.username as activated_user
FROM invited_users iu
LEFT JOIN users u ON iu.user_id = u.id
WHERE iu.email = 'testuser@example.com';
```

## Common Issues and Solutions

### Issue: Email not sending

**Solution**:

- Check environment variables: `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_HOST`
- Check logs for email errors
- Use Mailtrap for local testing

### Issue: Token validation fails

**Solution**:

- Check token in URL matches database
- Verify token hasn't expired (check `expires_at`)
- Check invitation status is PENDING

### Issue: Signup button disabled

**Solution**:

- Wait for token validation to complete
- Check for validation errors in console
- Verify invitation hasn't expired or been revoked

### Issue: Wrong user role assigned

**Solution**:

- Verify invitation `role_id` matches expected role
- Check SignupService is using invitation's role, not hardcoded STUDENT

### Issue: Welcome email not sent

**Solution**:

- Check logs - welcome email errors don't fail signup
- Verify email service is configured correctly
- Non-critical failure, won't block account creation

## Success Criteria

✅ Admin can create invitations with all roles
✅ Invitation email sent with correct signup link
✅ Token validation works correctly
✅ Invalid/expired tokens show appropriate errors
✅ Form pre-fills with invitation data
✅ Email field disabled for invited users
✅ Signup creates user with correct role from invitation
✅ Invitation status updates to ACTIVE after signup
✅ Welcome email sent after successful signup
✅ Student direct signup still works (no invitation)
✅ Admin can resend expired invitations
✅ Admin can revoke pending invitations
✅ Duplicate email prevention works
✅ Role-based dashboard redirect works
