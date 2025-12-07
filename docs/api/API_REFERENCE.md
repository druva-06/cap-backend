# API Reference Documentation

## Base URL

- **Development**: `http://localhost:8080`
- **UAT**: `https://uat-api.cap.com`
- **Production**: `https://api.cap.com`

## Authentication

All protected endpoints require JWT authentication using AWS Cognito.

### Authentication Header

```
Authorization: Bearer <jwt_token>
```

### Getting a Token

```bash
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "role": "STUDENT"  # Optional, defaults to STUDENT
}
```

## Response Format

### Success Response

```json
{
  "data": { ... },
  "message": "Success message",
  "statusCode": 200
}
```

### Error Response

```json
{
  "errors": {
    "field1": "Error message",
    "field2": "Error message"
  },
  "message": "Overall error description",
  "statusCode": 400
}
```

## API Endpoints

## Authentication APIs

Base path: `/auth`

### 1. Sign Up

**Endpoint**: `POST /auth/signup`

**Description**: Register a new user in the system

**Access**: Public

**Request Body**:

```json
{
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@example.com",
  "phone_number": "+919876543210",
  "username": "johndoe",
  "password": "StrongPass@123",
  "role": "STUDENT"
}
```

**Validation Rules**:

- `first_name`: Required, not blank
- `last_name`: Required, not blank
- `email`: Required, valid email format, unique
- `phone_number`: Required, valid format, unique
- `username`: Required, unique
- `password`: Required, min 8 characters
- `role`: Required, enum (ADMIN, COUNSELOR, STUDENT, COLLEGE)

**Response** (201 Created):

```json
{
  "data": "User Registered Successfully!",
  "message": "Registered Successfully!",
  "statusCode": 201
}
```

**Errors**:

- `400`: Validation failed / Email exists / Phone exists / Username exists
- `500`: Internal server error

---

### 2. Login

**Endpoint**: `POST /auth/login`

**Description**: Authenticate user and get JWT token

**Access**: Public

**Request Body**:

```json
{
  "email": "john.doe@example.com",
  "password": "StrongPass@123",
  "role": "STUDENT" // Optional, defaults to STUDENT
}
```

**Response** (200 OK):

```json
{
  "data": {
    "access_token": "eyJhbGciOiJIUzI1NiIs...",
    "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
    "id_token": "eyJhbGciOiJIUzI1NiIs...",
    "expires_in": 3600,
    "token_type": "Bearer",
    "user": {
      "user_id": 1,
      "email": "john.doe@example.com",
      "first_name": "John",
      "last_name": "Doe",
      "phone_number": "+919876543210",
      "username": "johndoe",
      "role": "STUDENT",
      "profile_picture": "https://..."
    }
  },
  "message": "Logged Successfully!",
  "statusCode": 200
}
```

**Errors**:

- `400`: Invalid credentials / Account locked / Role mismatch
- `404`: User not found
- `500`: Internal server error

---

### 3. Refresh Token

**Endpoint**: `POST /auth/refresh`

**Description**: Get new access token using refresh token

**Access**: Public (requires refresh token)

**Request Body**:

```json
{
  "refresh_token": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response** (200 OK):

```json
{
  "data": {
    "access_token": "eyJhbGciOiJIUzI1NiIs...",
    "id_token": "eyJhbGciOiJIUzI1NiIs...",
    "expires_in": 3600,
    "token_type": "Bearer"
  },
  "message": "Token refreshed successfully!",
  "statusCode": 200
}
```

---

### 4. Change Password

**Endpoint**: `POST /auth/changePassword`

**Description**: Change user password

**Access**: Authenticated

**Request Body**:

```json
{
  "old_password": "OldPass@123",
  "new_password": "NewPass@123"
}
```

**Response** (200 OK):

```json
{
  "data": "Password changed successfully",
  "message": "Password changed successfully!",
  "statusCode": 200
}
```

---

### 5. Resend Verification Code

**Endpoint**: `GET /auth/resendVerificationCode/{email}`

**Description**: Resend email verification code

**Access**: Public

**Response** (200 OK):

```json
{
  "data": "Verification code sent",
  "message": "Resend verification code successfully!",
  "statusCode": 200
}
```

---

## Lead Management APIs

Base path: `/leads`

### 1. Create Lead

**Endpoint**: `POST /leads/add`

**Description**: Create a new lead with encrypted sensitive information

**Access**: Admin only (`@PreAuthorize("hasAnyRole('ADMIN')")`)

**Request Body**:

```json
{
  "first_name": "Priya",
  "last_name": "Sharma",
  "email": "priya.sharma@email.com",
  "phone_number": "+919876543210",
  "country": "India",
  "status": "HOT",
  "score": 95,
  "lead_source": "Website",
  "preferred_countries": "USA,Canada,UK",
  "preferred_courses": "MBA,Computer Science",
  "budget_range": "20-30 Lakhs",
  "intake": "Fall 2026",
  "tags": ["MBA", "USA"],
  "assigned_to": 5,
  "encrypted_personal_details": "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=...",
  "encrypted_academic_details": "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=...",
  "encrypted_preferences": "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=..."
}
```

**Required Fields**:

- `first_name`, `last_name`, `email`, `phone_number`

**Optional Fields**:

- `status` (defaults to WARM)
- `score` (defaults to 0)
- All other fields

**Response** (201 Created):

```json
{
  "data": {
    "id": 1,
    "first_name": "Priya",
    "last_name": "Sharma",
    "email": "priya.sharma@email.com",
    "phone_number": "+919876543210",
    "country": "India",
    "status": "HOT",
    "score": 95,
    "lead_source": "Website",
    "preferred_countries": "USA,Canada,UK",
    "preferred_courses": "MBA,Computer Science",
    "budget_range": "20-30 Lakhs",
    "intake": "Fall 2026",
    "tags": ["MBA", "USA"],
    "assigned_to_id": 5,
    "assigned_to_name": "John Counselor",
    "created_by_id": 2,
    "created_by_name": "Admin User",
    "encrypted_personal_details": "U2FsdGVkX1+...",
    "encrypted_academic_details": "U2FsdGVkX1+...",
    "encrypted_preferences": "U2FsdGVkX1+...",
    "is_duplicate": false,
    "duplicate_of": null,
    "created_at": "2025-12-07T10:30:00.000Z",
    "updated_at": "2025-12-07T10:30:00.000Z"
  },
  "message": "Lead created successfully",
  "statusCode": 201
}
```

**Errors**:

- `400`: Validation failed / Duplicate lead found
- `404`: User not found / Assigned counselor not found
- `401`: Unauthorized
- `403`: Forbidden (not admin)
- `500`: Internal server error

**Duplicate Detection**:
The API checks if a lead with the same email OR phone number already exists. If found, returns:

```json
{
  "errors": [],
  "message": "Duplicate lead found. Lead already exists with ID: 123",
  "statusCode": 400
}
```

---

## Student APIs

Base path: `/students`

### Common Student Operations

**Get Student Profile**: `GET /students/{id}`

**Update Student Profile**: `PUT /students/{id}`

**Delete Student**: `DELETE /students/{id}`

**List Students**: `GET /students` (with pagination)

---

## Address APIs

Base path: `/addresses`

### Operations

**Add Address**: `POST /addresses`

**Update Address**: `PUT /addresses/{id}`

**Delete Address**: `DELETE /addresses/{id}`

**Get Student Addresses**: `GET /addresses/student/{studentId}`

---

## College APIs

Base path: `/colleges`

### Operations

**List Colleges**: `GET /colleges`

**Get College Details**: `GET /colleges/{id}`

**Search Colleges**: `GET /colleges/search?query=...`

**Filter Colleges**: `GET /colleges/filter?country=USA&ranking_min=1&ranking_max=100`

---

## Course APIs

Base path: `/courses`

### Operations

**List Courses**: `GET /courses`

**Get Course Details**: `GET /courses/{id}`

**Get Courses by College**: `GET /courses/college/{collegeId}`

**Search Courses**: `GET /courses/search?query=...`

---

## Application APIs

Base path: `/applications`

### Operations

**Submit Application**: `POST /applications`

**Get Application Status**: `GET /applications/{id}`

**Update Application**: `PUT /applications/{id}`

**List Student Applications**: `GET /applications/student/{studentId}`

---

## Admin APIs

Base path: `/admin`

### User Management

**List Users**: `GET /admin/users`

**Update User Role**: `PUT /admin/users/{id}/role`

**Lock/Unlock User**: `PUT /admin/users/{id}/lock`

### Student Management

**Get All Students**: `GET /admin/students`

**Get Student Details**: `GET /admin/students/{id}`

**Update Student**: `PUT /admin/students/{id}`

### Lead Management

**Get All Leads**: `GET /admin/leads`

**Assign Lead**: `PUT /admin/leads/{id}/assign`

**Update Lead Status**: `PUT /admin/leads/{id}/status`

**Merge Duplicate Leads**: `POST /admin/leads/merge`

---

## Common Query Parameters

### Pagination

```
?page=0&size=20&sort=createdAt,desc
```

**Parameters**:

- `page`: Page number (0-indexed)
- `size`: Items per page
- `sort`: Sort field and direction

### Search

```
?search=keyword
```

### Filtering

```
?status=HOT&country=USA&score_min=80
```

## HTTP Status Codes

- `200 OK` - Successful GET/PUT/DELETE
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE with no response
- `400 Bad Request` - Validation error or business logic error
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate resource
- `500 Internal Server Error` - Server error

## Rate Limiting

**Limits**:

- 100 requests per minute per IP
- 1000 requests per hour per user

**Headers**:

```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1638921600
```

## CORS Configuration

**Allowed Origins**:

- Development: `http://localhost:3000`
- UAT: `https://uat.cap.com`
- Production: `https://www.cap.com`

**Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS

**Allowed Headers**: Authorization, Content-Type

## Versioning

**Current Version**: v1

**API Path**: `/api/v1/{resource}`

Future versions will use: `/api/v2/{resource}`

## Testing with cURL

### Example: Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

### Example: Create Lead (with auth)

```bash
curl -X POST http://localhost:8080/leads/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "first_name": "John",
    "last_name": "Doe",
    "email": "john@example.com",
    "phone_number": "+919876543210"
  }'
```

## Postman Collection

A Postman collection with all API endpoints is available at:
`/docs/api/CAP_Backend.postman_collection.json`

Import this collection into Postman for easy API testing.

## WebSocket Support (Future)

**Endpoint**: `ws://localhost:8080/ws`

**Topics**:

- `/topic/notifications` - Real-time notifications
- `/topic/lead-updates` - Lead status changes
- `/user/queue/messages` - Personal messages

## GraphQL Support (Future)

**Endpoint**: `POST /graphql`

Example query:

```graphql
query {
  student(id: 1) {
    id
    user {
      firstName
      lastName
      email
    }
    addresses {
      city
      country
    }
  }
}
```

## API Changelog

### v1.0.0 (2025-12-07)

- Initial release
- Authentication endpoints
- Lead management
- Student management
- Basic CRUD operations

## Support

For API support, contact:

- Email: api-support@cap.com
- Slack: #api-support
- Documentation: https://docs.cap.com

## SDK and Client Libraries (Planned)

- JavaScript/TypeScript SDK
- Python SDK
- Java SDK
- Mobile SDKs (iOS, Android)
