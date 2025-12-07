# Lead API Documentation

## Create Lead API

### Endpoint

```
POST /api/leads
```

### Description

Creates a new lead in the system with encrypted sensitive information. The API performs duplicate detection based on email and phone number.

### Authentication

Requires authenticated user (Admin/Counselor role)

### Request Headers

```
Content-Type: application/json
Authorization: Bearer <token>
```

### Request Body

```json
{
  "first_name": "Priya",
  "last_name": "Sharma",
  "email": "priya.sharma@email.com",
  "phone_number": "+91 98765 43210",
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

### Required Fields

- `first_name` (string, not blank)
- `last_name` (string, not blank)
- `email` (string, valid email format)
- `phone_number` (string, valid phone format with country code)

### Optional Fields

- `country` (string)
- `status` (enum: HOT, WARM, COLD, CONVERTED, CLOSED - defaults to WARM)
- `score` (integer: 0-100, defaults to 0)
- `lead_source` (string)
- `preferred_countries` (string: comma-separated)
- `preferred_courses` (string: comma-separated)
- `budget_range` (string)
- `intake` (string)
- `tags` (array of strings)
- `assigned_to` (long: user ID of counselor)
- `encrypted_personal_details` (string: encrypted JSON)
- `encrypted_academic_details` (string: encrypted JSON)
- `encrypted_preferences` (string: encrypted JSON)

### Success Response (201 Created)

```json
{
  "data": {
    "id": 1,
    "first_name": "Priya",
    "last_name": "Sharma",
    "email": "priya.sharma@email.com",
    "phone_number": "+91 98765 43210",
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
    "encrypted_personal_details": "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=...",
    "encrypted_academic_details": "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=...",
    "encrypted_preferences": "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=...",
    "is_duplicate": false,
    "duplicate_of": null,
    "created_at": "2025-12-06T10:30:00.000Z",
    "updated_at": "2025-12-06T10:30:00.000Z"
  },
  "message": "Lead created successfully",
  "statusCode": 201
}
```

### Error Responses

#### 400 Bad Request - Validation Error

```json
{
  "errors": {
    "first_name": "First name is required",
    "email": "Email should be valid"
  },
  "message": "Validation failed",
  "statusCode": 400
}
```

#### 400 Bad Request - Duplicate Lead

```json
{
  "errors": [],
  "message": "Duplicate lead found. Lead already exists with ID: 123",
  "statusCode": 400
}
```

#### 404 Not Found - User Not Found

```json
{
  "errors": [],
  "message": "Assigned counselor not found",
  "statusCode": 404
}
```

#### 500 Internal Server Error

```json
{
  "errors": [],
  "message": "Internal server error",
  "statusCode": 500
}
```

## Encrypted Data Format

### Personal Details Structure (before encryption)

```json
{
  "alternatePhoneNumber": "+91 98765 43211",
  "dateOfBirth": "15/05/1998",
  "gender": "MALE",
  "fullAddress": "123 Main Street, Apartment 4B",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": "400001"
}
```

### Academic Details Structure (before encryption)

```json
{
  "currentEducationLevel": "UNDERGRADUATE",
  "degreeCourse": "B.Tech in Computer Science",
  "universityCollege": "IIT Bombay",
  "percentageCGPA": "8.5 CGPA",
  "yearOfPassing": "2020",
  "workExperience": "3 years at TCS",
  "ieltsScore": "7.5",
  "toeflScore": "100",
  "greScore": "320",
  "gmatScore": "700"
}
```

### Preferences Structure (before encryption)

```json
{
  "preferredCollege": "Harvard University, MIT, Stanford",
  "additionalNotes": "Looking for scholarship opportunities"
}
```

## Frontend Integration Notes

1. **Encryption**: Frontend should encrypt the sensitive data structures using AES-256 or similar before sending
2. **Tags**: Send as array, will be stored as comma-separated string
3. **Duplicate Detection**: API checks email and phone number for duplicates
4. **Assignment**: `assigned_to` should be the user ID of a counselor
5. **Authentication**: User creating the lead is automatically captured from authentication context

## Business Logic

1. **Duplicate Detection**:

   - Checks if email OR phone number already exists
   - Throws error if duplicate found
   - Returns the ID of existing lead in error message

2. **Default Values**:

   - Status defaults to "WARM" if not provided
   - Score defaults to 0 if not provided
   - isDuplicate is set to false by default

3. **Timestamps**:
   - `created_at` and `updated_at` are automatically set
   - `updated_at` is automatically updated on entity updates

## Testing with cURL

```bash
curl -X POST http://localhost:8080/api/leads \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "first_name": "Priya",
    "last_name": "Sharma",
    "email": "priya.sharma@email.com",
    "phone_number": "+919876543210",
    "country": "India",
    "status": "HOT",
    "score": 95,
    "lead_source": "Website",
    "preferred_countries": "USA,Canada",
    "preferred_courses": "MBA",
    "budget_range": "20-30 Lakhs",
    "intake": "Fall 2026",
    "tags": ["MBA", "USA"],
    "assigned_to": 5
  }'
```
