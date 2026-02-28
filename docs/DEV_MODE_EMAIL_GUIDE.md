# Dev Mode Email Configuration - Quick Reference

## Overview

Instead of sending actual emails via SMTP, the system now returns email content in API responses. This allows you to:

- ✅ Test invitation flow without email server setup
- ✅ Copy signup links directly from browser inspector
- ✅ Verify email templates and content
- ✅ No need for AWS SES or SMTP configuration during development

## How It Works

### Backend Configuration

**Current Mode**: `dev` (returns emails in API responses)

```yaml
# application-uat.yaml
spring:
  profiles:
    active: dev # Dev mode - emails returned in responses
```

**Two Email Service Implementations**:

1. **DevEmailService** (`@Profile("dev")`)

   - Active when `spring.profiles.active=dev`
   - Captures email content without sending
   - Returns email details in API responses
   - Logs signup links to console

2. **EmailServiceImpl** (`@Profile("!dev")`)
   - Active in production/UAT (when NOT dev profile)
   - Sends actual emails via SMTP/SES
   - Requires email configuration

### API Response Structure

#### Create Invitation Response

```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roleName": "COLLEGE",
  "status": "PENDING",
  "invitationToken": "abc-123-xyz",
  "emailDetails": {
    "recipientEmail": "user@example.com",
    "subject": "You're invited to join MeritCap",
    "signupLink": "http://localhost:3000/signup?token=abc-123-xyz",
    "sent": false
  }
}
```

#### Signup Response

```json
{
  "userId": 10,
  "email": "user@example.com",
  "firstName": "John",
  "roleName": "COLLEGE",
  "message": "Account created successfully",
  "welcomeEmailDetails": {
    "recipientEmail": "user@example.com",
    "subject": "Welcome to MeritCap!",
    "sent": false
  }
}
```

## Testing the Flow

### Step 1: Create Invitation (Admin Portal)

1. Go to: `http://localhost:3000/admin/user-permissions`
2. Click "Add User"
3. Fill form and submit
4. **Check Browser Console** - you'll see:
   ```
   📧 [DEV MODE] Invitation email captured (not sent) to: user@example.com
   🔗 Signup Link: http://localhost:3000/signup?token=abc-123-xyz
   ```
5. **Check Frontend Toast** - shows signup link with "Click to copy" button
6. **Check Network Tab** - API response contains `emailDetails.signupLink`

### Step 2: Copy Signup Link

**Option A: From Toast Notification**

- Click "Click to copy signup link" button in the toast
- Link copied to clipboard

**Option B: From Browser Inspector**

- Open DevTools → Network tab
- Find `POST /api/invitation/create` request
- View response → Copy `emailDetails.signupLink`

**Option C: From Backend Logs**

- Check terminal running backend
- Look for: `🔗 Signup Link: http://localhost:3000/signup?token=...`

### Step 3: Test Signup

1. Paste signup link in browser
2. Page validates token automatically
3. Shows green banner: "Invitation Verified ✓"
4. Form pre-filled with invitation data
5. Complete signup
6. Check response for `welcomeEmailDetails`

## Backend Logs

When creating invitation:

```
📧 [DEV MODE] Invitation email captured (not sent) to: user@example.com
🔗 Signup Link: http://localhost:3000/signup?token=abc-123-xyz
```

When user signs up:

```
📧 [DEV MODE] Welcome email captured (not sent) to: user@example.com
```

## Switching to Production Email

When ready to send actual emails:

### Option 1: Keep Dev Mode, Add Email Config Later

```yaml
spring:
  profiles:
    active: dev # Keep returning emails in responses
```

### Option 2: Enable Production Email (SMTP)

```yaml
spring:
  profiles:
    active: uat # Or 'prod'
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD} # Use app-specific password
```

### Option 3: Enable AWS SES (Future)

```yaml
spring:
  profiles:
    active: prod
aws:
  ses:
    region: ap-south-1
    fromEmail: noreply@yourdomain.com
```

## API Endpoints with Email Details

### 1. Create Invitation

```bash
POST http://localhost:8080/api/invitation/create
Response: includes emailDetails.signupLink
```

### 2. Resend Invitation

```bash
POST http://localhost:8080/api/invitation/resend/{id}
Response: includes emailDetails.signupLink (new token)
```

### 3. Student Signup

```bash
POST http://localhost:8080/api/auth/signup/student
Response: includes welcomeEmailDetails
```

### 4. Invited User Signup

```bash
POST http://localhost:8080/api/auth/signup/invited
Response: includes welcomeEmailDetails
```

## Frontend Toast Behavior

**Dev Mode** (when `emailDetails.signupLink` present):

- Shows "Invitation Sent (Dev Mode)"
- Displays signup link in monospace font
- Shows "Click to copy" button
- Toast duration: 10 seconds

**Production Mode** (when `emailDetails` not present):

- Shows "Invitation Sent"
- Standard message about email being sent
- Toast duration: 5 seconds (default)

## Troubleshooting

### Issue: No signup link in response

**Solution**: Check `spring.profiles.active=dev` in application-uat.yaml

### Issue: Multiple EmailService beans error

**Solution**: Only one should be active:

- `@Profile("dev")` for DevEmailService
- `@Profile("!dev")` for EmailServiceImpl

### Issue: Can't copy signup link

**Solution**:

- Check browser console for logs
- Check Network tab for API response
- Check backend terminal for 🔗 emoji logs

## Benefits of Dev Mode

✅ **No Email Server Required** - Test immediately without SMTP/SES setup
✅ **Direct Link Access** - Copy signup links from browser inspector
✅ **Faster Testing** - No email delays, instant feedback
✅ **Email Template Verification** - See HTML content in responses
✅ **Token Debugging** - Easily track and test invitation tokens
✅ **Cost Savings** - No email service costs during development

## Next Steps

1. **Current**: Test with dev mode (emails in API responses)
2. **Later**: Configure AWS SES when ready for production
3. **Production**: Switch profile to `uat` or `prod` and configure SES credentials
