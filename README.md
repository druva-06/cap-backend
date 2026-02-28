# MeritCap (CAP) Backend

A comprehensive backend system for managing educational consultancy services, student applications, and lead generation.

## Project Overview

CAP Backend is a Spring Boot application that provides REST APIs for:

- User authentication and authorization (Admin, Counselor, Student, College)
- Lead management and tracking
- Student profile management
- College and course information
- Application processing
- Communication tracking

## Technology Stack

- **Framework**: Spring Boot 3.3.3
- **Language**: Java 17
- **Database**: MySQL
- **Authentication**: AWS Cognito + JWT
- **Build Tool**: Maven
- **Containerization**: Docker
- **Cloud**: AWS (Secrets Manager, ECS)

## Project Structure

```
meritcap-backend/
├── src/
│   ├── main/
│   │   ├── java/com/meritcap/
│   │   │   ├── api/              # External API integrations
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── DTOs/             # Data Transfer Objects
│   │   │   ├── enums/            # Enumerations
│   │   │   ├── exception/        # Custom exceptions
│   │   │   ├── model/            # JPA entities
│   │   │   ├── repository/       # Data access layer
│   │   │   ├── service/          # Business logic
│   │   │   ├── transformer/      # DTO ↔ Entity converters
│   │   │   └── utils/            # Utility classes
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── application-uat.yaml
│   └── test/                     # Test classes
├── database/
│   ├── README.md
│   └── migrations/               # SQL migration scripts
├── docs/
│   ├── README.md
│   ├── api/                      # API documentation
│   │   └── LEAD_API_DOCUMENTATION.md
│   └── models/                   # Data model documentation
│       └── LEAD_MODEL_DESIGN.md
├── k8s/                          # Kubernetes deployment files
├── docker-compose.yml
├── Dockerfile
├── buildspec.yml                 # AWS CodeBuild spec
└── pom.xml
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Docker (optional, for containerized deployment)

### Configuration

1. **Database Configuration**

   Update database credentials in `src/main/resources/application-{env}.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/cap_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

2. **AWS Cognito Configuration**

   Set up Cognito credentials in application properties or AWS Secrets Manager.

3. **Environment Variables**

   Required environment variables:

   - `AWS_REGION`
   - `COGNITO_USER_POOL_ID`
   - `COGNITO_CLIENT_ID`
   - `COGNITO_CLIENT_SECRET`

### Running the Application

#### Local Development

```bash
# Clone the repository
git clone <repository-url>
cd meritcap-backend

# Run with Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or build and run JAR
mvn clean package
java -jar target/education-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

#### Using Docker

```bash
# Build Docker image
docker build -t meritcap-backend .

# Run container
docker run -p 8080:8080 meritcap-backend
```

#### Using Docker Compose

```bash
docker-compose up
```

## API Documentation

API documentation is available in the `/docs/api` directory.

### Base URL

- **Development**: `http://localhost:8080`
- **UAT**: `http://uat.example.com`
- **Production**: `https://api.example.com`

### Authentication

All API endpoints (except public ones) require JWT authentication:

```bash
Authorization: Bearer <jwt_token>
```

### Available APIs

- **Auth APIs**: `/auth/*` - User authentication and registration
- **Lead APIs**: `/leads/*` - Lead management
- **Student APIs**: `/students/*` - Student profile management
- **Admin APIs**: `/admin/*` - Administrative operations

See [API Documentation](docs/api/) for detailed endpoint information.

## Database

### Migrations

Database migration scripts are located in `database/migrations/`.

Run migrations in order:

```bash
mysql -u username -p database_name < database/migrations/001_create_leads_table.sql
```

See [Database Documentation](database/README.md) for more details.

## Security

- **Authentication**: AWS Cognito with JWT tokens
- **Authorization**: Role-based access control (RBAC)
- **Data Encryption**: Sensitive data encrypted at rest and in transit
- **Secrets Management**: AWS Secrets Manager

### Roles

- `ADMIN` - Full system access
- `COUNSELOR` - Manage leads and students
- `STUDENT` - Access own profile and applications
- `COLLEGE` - Manage college information and applications

## Deployment

### AWS Deployment

The application is deployed on AWS using:

- **ECS**: Container orchestration
- **RDS**: MySQL database
- **Secrets Manager**: Credential management
- **CodeBuild**: CI/CD pipeline

Deployment configuration is in `buildspec.yml`.

### Kubernetes Deployment

Kubernetes manifests are in the `k8s/` directory:

```bash
kubectl apply -f k8s/meritcap-deployment.yaml
kubectl apply -f k8s/meritcap-service.yaml
```

## Development Guidelines

### Code Style

- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Write meaningful commit messages
- Add appropriate logging

### Adding New Features

1. Create entity in `model/`
2. Create repository in `repository/`
3. Create DTOs in `DTOs/`
4. Implement service in `service/impl/`
5. Create controller in `controller/`
6. Add transformer in `transformer/`
7. Write tests
8. Document API in `docs/api/`
9. Document model in `docs/models/`
10. Create migration script in `database/migrations/`

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserAuthServiceTest

# Run with coverage
mvn clean verify
```

## Documentation

Comprehensive documentation is available in the `/docs` directory:

### 📖 Core Documentation

- **[Architecture Guide](docs/ARCHITECTURE.md)** - System architecture, design patterns, and technical decisions
- **[Database Schema](docs/DATABASE_SCHEMA.md)** - Complete database documentation with ERD and optimization tips
- **[Deployment Guide](docs/DEPLOYMENT.md)** - Deploy to Docker, Kubernetes, or AWS ECS
- **[Development Guide](docs/DEVELOPMENT_GUIDE.md)** - Setup, workflow, and best practices

### 🔌 API Documentation

- **[API Reference](docs/api/API_REFERENCE.md)** - Complete API endpoint reference
- **[Lead API](docs/api/LEAD_API_DOCUMENTATION.md)** - Lead management API details

### 📊 Data Models

- **[Lead Model Design](docs/models/LEAD_MODEL_DESIGN.md)** - Lead entity architecture

### 📝 Additional Resources

- **[Database README](database/README.md)** - Database migrations and maintenance
- **[Documentation Index](docs/README.md)** - Complete documentation overview

**Total Documentation**: 3,900+ lines covering architecture, APIs, deployment, and development

## Contributing

1. Create a feature branch
2. Make your changes
3. Write/update tests
4. Update documentation
5. Submit a pull request

## License

[Add License Information]

## Contact

[Add Contact Information]

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [AWS Cognito Documentation](https://docs.aws.amazon.com/cognito/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
