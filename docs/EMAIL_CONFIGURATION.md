# Email Configuration Guide

## Overview

The email service is configured to send invitation emails, password reset emails, and welcome emails using JavaMail with SMTP.

## Environment Variables Required

Add these environment variables to your deployment configuration:

```bash
# Email Configuration
MAIL_HOST=smtp.gmail.com              # SMTP host (default: smtp.gmail.com)
MAIL_PORT=587                          # SMTP port (default: 587 for TLS)
MAIL_USERNAME=your-email@domain.com    # Sender email address
MAIL_PASSWORD=your-app-specific-password  # Email password or app-specific password

# Frontend URL (for signup links in emails)
FRONTEND_URL=https://your-frontend-domain.com  # Production frontend URL
```

## Gmail Configuration

If using Gmail:

1. **Enable 2-Factor Authentication** on your Google account
2. **Generate App-Specific Password**:

   - Go to Google Account Settings
   - Security → 2-Step Verification → App passwords
   - Generate a new app password for "Mail"
   - Use this password in `MAIL_PASSWORD`

3. **Alternative**: Use OAuth2 for Gmail (more secure, requires additional configuration)

## AWS SES Configuration (Alternative)

For production, consider using AWS SES instead of SMTP:

1. Add AWS SES dependency to `pom.xml`
2. Verify sender email in AWS SES
3. Configure AWS credentials
4. Create `SesEmailServiceImpl` implementation

## Email Templates

The service includes three email templates:

### 1. Invitation Email

- **Trigger**: When admin creates a user invitation
- **Contains**: Signup link with invitation token, expiry time
- **Template**: HTML with blue theme, CTA button
- **Expiry**: Displays hours until invitation expires

### 2. Password Reset Email

- **Trigger**: When user requests password reset
- **Contains**: Password reset link with token
- **Template**: HTML with red theme, security warning

### 3. Welcome Email

- **Trigger**: After successful user signup (student or invited)
- **Contains**: Welcome message and getting started info
- **Template**: HTML with green theme, celebration emoji

## Testing Email Functionality

### Local Testing with Mailtrap

For local development, use [Mailtrap](https://mailtrap.io):

```yaml
spring:
  mail:
    host: sandbox.smtp.mailtrap.io
    port: 2525
    username: your-mailtrap-username
    password: your-mailtrap-password
```

### Test Invitation Flow

1. **Create Invitation**: Admin clicks "Add User" in admin portal
2. **Check Email**: User receives invitation email with signup link
3. **Verify Link**: Signup link format: `${FRONTEND_URL}/signup?token={invitation_token}`
4. **Complete Signup**: User clicks link, fills form, creates account
5. **Welcome Email**: User receives welcome email after successful signup

## Email Sending Behavior

- **Non-Blocking**: Email failures won't block user operations
- **Error Logging**: All email errors are logged with details
- **Retry Logic**: Currently not implemented (consider adding for production)
- **Queue**: Currently sends emails synchronously (consider async for production)

## Production Recommendations

1. **Use AWS SES or dedicated email service** for reliability
2. **Implement email queue** (RabbitMQ, AWS SQS) for async sending
3. **Add retry logic** for failed email sends
4. **Monitor email delivery rates** and bounce rates
5. **Set up email tracking** (opens, clicks) if needed
6. **Configure SPF, DKIM, DMARC** records for your domain
7. **Implement rate limiting** to prevent spam
8. **Store email logs** in database for auditing

## Email Service Methods

### EmailService Interface

```java
void sendInvitationEmail(String email, String firstName, String invitationToken, int expiryHours);
void sendPasswordResetEmail(String email, String firstName, String resetToken);
void sendWelcomeEmail(String email, String firstName);
```

## Troubleshooting

### Email Not Sending

1. Check logs for error messages
2. Verify SMTP credentials are correct
3. Check firewall/network allows SMTP port (587/465)
4. Verify sender email is authorized

### Gmail "Less Secure App" Error

- Use app-specific password instead of account password
- Enable 2FA on Google account first

### Email Goes to Spam

- Configure SPF records for your domain
- Add DKIM signature
- Use verified sender email
- Avoid spammy content in templates
