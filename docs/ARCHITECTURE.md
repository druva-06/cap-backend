# CAP Backend - Project Architecture

## Overview

MeritCap (CAP) Backend is a comprehensive educational consultancy management system built with Spring Boot. The system manages the complete lifecycle of student applications from lead generation to enrollment.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client Layer                             │
│  (Web Frontend, Mobile Apps, Third-party Integrations)          │
└─────────────────────────┬───────────────────────────────────────┘
                          │ HTTPS/REST
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                     API Gateway / Load Balancer                  │
└─────────────────────────┬───────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                       │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │  Controllers │  │   Security   │  │   Exception  │          │
│  │     Layer    │  │    Filter    │  │   Handler    │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
│         │                  │                  │                   │
│         ▼                  ▼                  ▼                   │
│  ┌─────────────────────────────────────────────────┐            │
│  │              Service Layer                       │            │
│  │  (Business Logic & Validations)                 │            │
│  └─────────────────┬───────────────────────────────┘            │
│                    │                                              │
│         ┌──────────┴──────────┬──────────────┐                  │
│         ▼                     ▼               ▼                  │
│  ┌──────────┐         ┌──────────┐    ┌──────────┐             │
│  │Repository│         │Transformer│    │  Utils   │             │
│  │  Layer   │         │  Layer    │    │ & Helper │             │
│  └────┬─────┘         └──────────┘    └──────────┘             │
│       │                                                           │
└───────┼───────────────────────────────────────────────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────────────┐
│                       Data Layer                                 │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │    MySQL     │  │  AWS Cognito │  │  AWS Secrets │          │
│  │   Database   │  │    (Auth)    │  │   Manager    │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└─────────────────────────────────────────────────────────────────┘
```

## Layer Architecture

### 1. Controller Layer

**Location**: `src/main/java/com/meritcap/controller/`

**Responsibility**: Handle HTTP requests and responses

**Components**:

- `UserAuthController` - Authentication endpoints
- `LeadController` - Lead management
- `StudentController` - Student operations
- `AddressController` - Address management
- `AdminController` - Administrative functions

**Best Practices**:

- Thin controllers - delegate to service layer
- Input validation using `@Valid`
- Consistent response structure (`ApiSuccessResponse`, `ApiFailureResponse`)
- Proper HTTP status codes
- Swagger/OpenAPI documentation

### 2. Service Layer

**Location**: `src/main/java/com/meritcap/service/`

**Responsibility**: Business logic and orchestration

**Components**:

- `UserAuthService` - Authentication business logic
- `LeadService` - Lead management logic
- `StudentService` - Student operations
- `CurrencyAPIService` - External API integration

**Characteristics**:

- Transactional operations
- Business rule validation
- Error handling
- Logging
- External service integration

### 3. Repository Layer

**Location**: `src/main/java/com/meritcap/repository/`

**Responsibility**: Data access and persistence

**Technology**: Spring Data JPA

**Features**:

- CRUD operations
- Custom queries
- Query methods by convention
- Native SQL support
- Pagination and sorting

### 4. Model Layer

**Location**: `src/main/java/com/meritcap/model/`

**Responsibility**: Domain entities and JPA mappings

**Key Entities**:

- `User` - System users (Admin, Counselor, Student, College)
- `Student` - Student profiles
- `Lead` - Prospective student leads
- `Address` - Address information
- `College` - Educational institutions
- `Course` - Academic programs

### 5. DTO Layer

**Location**: `src/main/java/com/meritcap/DTOs/`

**Responsibility**: Data transfer between layers

**Structure**:

```
DTOs/
├── requestDTOs/     # Incoming API requests
│   ├── userAuth/
│   ├── lead/
│   └── student/
└── responseDTOs/    # Outgoing API responses
    ├── userAuth/
    ├── lead/
    └── student/
```

### 6. Security Layer

**Location**: `src/main/java/com/meritcap/security/`

**Components**:

- `SecurityConfig` - Spring Security configuration
- `CognitoConfig` - AWS Cognito integration
- `CognitoJwtAuthFilter` - JWT validation filter

**Features**:

- Role-based access control (RBAC)
- JWT token validation
- Route protection
- CORS configuration

## Design Patterns

### 1. Repository Pattern

Used for data access abstraction

```java
public interface LeadRepository extends JpaRepository<Lead, Long> {
    Optional<Lead> findByEmail(String email);
}
```

### 2. Service Pattern

Business logic encapsulation

```java
@Service
public class LeadServiceImpl implements LeadService {
    // Business logic here
}
```

### 3. DTO Pattern

Decoupling API contracts from domain models

```java
public class LeadRequestDto {
    // API request structure
}
```

### 4. Transformer Pattern

Converting between DTOs and Entities

```java
public class LeadTransformer {
    public static Lead toEntity(LeadRequestDto dto) {
        // Conversion logic
    }
}
```

### 5. Builder Pattern

Object construction (via Lombok)

```java
@Builder
public class Lead {
    // Fields with builder support
}
```

## Configuration Management

### Application Profiles

1. **Development** (`application-dev.properties`)

   - Local database
   - Debug logging
   - Relaxed security

2. **UAT** (`application-uat.yaml`)

   - UAT database
   - Info logging
   - Standard security

3. **Production** (`application-prod.properties`)
   - Production database
   - Error logging only
   - Strict security

### Secrets Management

Sensitive configuration stored in:

- AWS Secrets Manager (production)
- Environment variables (local dev)
- Application properties (non-sensitive)

### Configuration Classes

- `AwsSecretsEnvironmentPostProcessor` - Load secrets from AWS
- `WebConfig` - Web MVC configuration
- `JpaConfig` - JPA/Hibernate settings
- `WebClientConfig` - HTTP client configuration

## Data Flow

### Request Flow Example: Create Lead

```
1. Client Request
   POST /leads/add
   Headers: Authorization: Bearer <token>
   Body: LeadRequestDto

2. Security Filter (CognitoJwtAuthFilter)
   - Validate JWT token
   - Extract user email
   - Set SecurityContext

3. Controller (LeadController)
   - Validate request (@Valid)
   - Extract authenticated user
   - Call service layer

4. Service (LeadServiceImpl)
   - Find user by email
   - Check for duplicates
   - Validate assignee
   - Create lead entity
   - Save to database

5. Repository (LeadRepository)
   - Persist to MySQL
   - Return saved entity

6. Transformer (LeadTransformer)
   - Convert entity to DTO
   - Add computed fields

7. Controller Response
   - Wrap in ApiSuccessResponse
   - Return HTTP 201 Created

8. Client receives LeadResponseDto
```

## Error Handling Strategy

### Exception Hierarchy

```
Exception
└── RuntimeException
    ├── CustomException (400 Bad Request)
    ├── NotFoundException (404 Not Found)
    └── Other custom exceptions
```

### Error Response Format

```json
{
  "errors": {
    "field1": "error message",
    "field2": "error message"
  },
  "message": "Overall error description",
  "statusCode": 400
}
```

## Logging Strategy

### Log Levels

- **ERROR**: System errors, exceptions
- **WARN**: Business validation failures, duplicate entries
- **INFO**: Important business events (login, create, update)
- **DEBUG**: Detailed flow information (development only)

### Logging Format

```java
log.info("Creating lead for email: {}", email);
log.error("User not found with ID: {}", userId);
log.warn("Duplicate lead found for email: {}", email);
```

## Database Design Principles

### 1. Normalization

- 3NF compliance
- Eliminate redundancy
- Foreign key relationships

### 2. Indexing Strategy

- Primary keys
- Foreign keys
- Frequently queried fields
- Composite indexes for common queries
- Full-text search indexes

### 3. Audit Fields

All entities include:

- `created_at` - Creation timestamp
- `updated_at` - Last modification timestamp
- `created_by` - User who created (where applicable)

### 4. Soft Deletes

Use status fields instead of hard deletes:

- `ActiveStatus` enum
- `profile_status` field
- Retain historical data

## Security Considerations

### 1. Authentication

- AWS Cognito for user management
- JWT tokens for API authentication
- Secure token validation

### 2. Authorization

- Role-based access control
- Method-level security (`@PreAuthorize`)
- Resource-level permissions

### 3. Data Protection

- Encrypted sensitive data (frontend encryption)
- HTTPS/TLS in transit
- Secrets in AWS Secrets Manager
- SQL injection prevention (JPA)

### 4. Input Validation

- Bean validation annotations
- Custom validators
- Sanitization of user input

## Performance Optimization

### 1. Database

- Proper indexing
- Query optimization
- Connection pooling
- Lazy loading for relationships

### 2. Caching

- Spring Cache abstraction
- Cache frequently accessed data
- Cache invalidation strategy

### 3. Pagination

- Page-based loading
- Configurable page sizes
- Efficient count queries

### 4. Async Processing

- `@Async` for long-running tasks
- Background job processing
- Event-driven architecture

## Testing Strategy

### 1. Unit Tests

- Service layer logic
- Transformer logic
- Utility functions
- Mock external dependencies

### 2. Integration Tests

- Repository tests
- API endpoint tests
- Database integration
- Security configuration

### 3. Test Coverage Goals

- Service layer: 80%+
- Controllers: 70%+
- Overall: 75%+

## Deployment Architecture

### Development

- Local machine
- Embedded Tomcat
- Local MySQL
- Hot reload enabled

### UAT

- AWS ECS (Docker containers)
- AWS RDS MySQL
- Application Load Balancer
- AWS Secrets Manager

### Production

- AWS ECS (High availability)
- AWS RDS MySQL (Multi-AZ)
- CloudFront CDN
- Auto-scaling enabled
- CloudWatch monitoring

## Monitoring and Observability

### Application Metrics

- Request/response times
- Error rates
- Database query performance
- Memory and CPU usage

### Logging

- Centralized logging
- Log aggregation
- Error tracking
- Audit trail

### Health Checks

- `/actuator/health` endpoint
- Database connectivity
- External service status
- Custom health indicators

## Scalability Considerations

### Horizontal Scaling

- Stateless application design
- Load balancer distribution
- Session management (JWT)
- Database connection pooling

### Vertical Scaling

- JVM tuning
- Memory optimization
- CPU allocation
- Database resources

## Technology Choices

### Why Spring Boot?

- Rapid development
- Production-ready features
- Large ecosystem
- Strong community support
- Enterprise-grade

### Why MySQL?

- ACID compliance
- Strong relational support
- Proven reliability
- Good performance
- Wide adoption

### Why AWS Cognito?

- Managed authentication
- Built-in security features
- Scalable
- OAuth 2.0 support
- Integration with AWS ecosystem

### Why Docker?

- Consistent environments
- Easy deployment
- Resource isolation
- Scalability
- CI/CD integration

## Future Enhancements

### Planned Features

1. Real-time notifications (WebSocket)
2. Advanced search with Elasticsearch
3. Document storage (AWS S3)
4. Email/SMS integration
5. Payment gateway integration
6. Analytics dashboard
7. Mobile app API optimization
8. GraphQL API support

### Technical Debt

1. Increase test coverage
2. Implement caching layer
3. Add API rate limiting
4. Improve error messages
5. Add request/response logging
6. Implement circuit breakers
7. Add distributed tracing

## Best Practices

### Code Quality

- Follow SOLID principles
- Write clean, readable code
- Use meaningful names
- Keep methods small and focused
- Avoid code duplication

### Git Workflow

- Feature branches
- Pull request reviews
- Meaningful commit messages
- Conventional commits
- Protected main branch

### Documentation

- Keep README updated
- Document API changes
- Update architecture docs
- Add code comments for complex logic
- Maintain changelog

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [AWS Cognito](https://aws.amazon.com/cognito/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Docker Documentation](https://docs.docker.com/)
